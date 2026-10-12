# Stratégie de chargement et de cascade — AutoLoc (Atelier 2)

Ce document justifie, pour chaque association du modèle AutoLoc, le choix du côté propriétaire, de la stratégie de chargement (`FetchType`) et de la propagation du cycle de vie (`CascadeType`, `orphanRemoval`).

## 1. Règles générales retenues

- **Côté propriétaire** : toujours le côté `@ManyToOne` (ou, pour un `@OneToOne`, l'entité qui porte la FK). Le côté inverse utilise `mappedBy` et ne crée aucune colonne.
- **Fetch** : tout est en `LAZY`. Les `@ManyToOne` et `@OneToOne` (EAGER par défaut) sont forcés en `LAZY` ; les `@OneToMany` / `@ManyToMany` gardent leur `LAZY` par défaut, déclaré explicitement pour la lisibilité. Les besoins de chargement ponctuels seront traités par des requêtes dédiées (`JOIN FETCH`, `@EntityGraph` — Atelier 7).
- **Cascade** : uniquement quand l'enfant ne peut pas exister sans son parent (composition) ou quand on veut créer parent et enfants en une seule opération (`PERSIST`). Jamais de `REMOVE` sur une entité qui a une vie propre ou qui est partagée.
- **Lombok ciblé** : `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`. Pas de `@Data`. Chaque attribut d'association est en `@ToString.Exclude` : un `toString()` ne doit ni boucler entre les deux côtés (`StackOverflowError`), ni déclencher un chargement LAZY (`LazyInitializationException` hors transaction).
- **Synchronisation des deux côtés** : les méthodes utilitaires (`Contrat.addPaiement/removePaiement`, `Client.addReservation`, `Vehicule.addMaintenance/addEquipement/removeEquipement`, `Reservation.attacherContrat`) mettent à jour le côté propriétaire, sinon la cascade enregistrerait des enfants sans FK.

## 2. Tableau de synthèse

| Association | Type | Côté propriétaire (FK générée) | Fetch | Cascade | Justification |
|---|---|---|---|---|---|
| Contrat → Paiement | `@OneToMany` / `@ManyToOne` | `Paiement` (`contrat_id_contrat`) | LAZY | ALL + `orphanRemoval` | Composition : un paiement n'existe que rattaché à son contrat. Enregistrer le contrat enregistre ses paiements ; supprimer le contrat supprime ses paiements ; retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | `@OneToMany` / `@ManyToOne` | `Vehicule` (`agence_id_agence`) | LAZY | Aucune | Un véhicule survit à son agence (il peut être réaffecté). Supprimer une agence ne doit jamais supprimer la flotte. LAZY : lister les agences ne doit pas charger tous leurs véhicules. |
| Agence → Employe | `@OneToMany` / `@ManyToOne` | `Employe` (`agence_id_agence`) | LAZY | Aucune | L'employé a un cycle de vie RH propre (embauche, mutation, départ), indépendant de l'agence. Aucune opération sur l'agence ne doit se propager aux employés. |
| Vehicule ↔ Equipement | `@ManyToMany` | `Vehicule` (`@JoinTable vehicule_equipement`) | LAZY | Aucune | Équipements partagés entre plusieurs véhicules : un `REMOVE` supprimerait un équipement encore utilisé ailleurs. Le catalogue d'équipements est géré séparément, donc pas de `PERSIST`. `Set` : pas de doublon dans la table de jointure, et Hibernate supprime une seule ligne au lieu de vider puis réinsérer toute la collection (comportement d'un `List`). |
| Client → Reservation | `@OneToMany` / `@ManyToOne` | `Reservation` (`client_id_client`) | LAZY | PERSIST | Permet de créer un nouveau client avec sa première réservation en un seul `save`. Pas de `REMOVE` : l'historique des réservations (et des contrats et paiements qui en dépendent) doit être conservé même si un client est supprimé ou désactivé. |
| Reservation → Vehicule | `@ManyToOne` (unidirectionnelle) | `Reservation` (`vehicule_id_vehicule`) | LAZY | Aucune | Une réservation référence un véhicule existant ; elle ne le crée, ne le modifie et ne le supprime jamais. Unidirectionnelle : la liste des réservations d'un véhicule s'obtient par requête, pas par une collection. |
| Reservation ↔ Contrat | `@OneToOne` | `Contrat` (`reservation_id_reservation`, `UNIQUE`) | LAZY | ALL (côté `Reservation`) | Le contrat naît de la réservation et n'a pas de sens sans elle : persister/supprimer la réservation se propage au contrat, puis en chaîne à ses paiements (Contrat → Paiement en ALL). |
| Vehicule → Maintenance | `@OneToMany` / `@ManyToOne` | `Maintenance` (`vehicule_id_vehicule`) | LAZY | PERSIST | Permet de planifier une maintenance via `vehicule.addMaintenance(...)` puis un seul `save`. Pas de `REMOVE` : l'historique des maintenances est une trace à conserver ; supprimer un véhicule qui a des maintenances est bloqué par la FK, ce qui oblige à une décision explicite. |

## 3. Association guidée Contrat ↔ Paiement (étapes 1 à 5)

```java
// Paiement — côté propriétaire
@ManyToOne(fetch = FetchType.LAZY)
private Contrat contrat;

// Contrat — côté inverse
@OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL,
           orphanRemoval = true, fetch = FetchType.LAZY)
private List<Paiement> paiements = new ArrayList<>();
```

| Étape | Constat |
|---|---|
| 1. Unidirectionnelle | Hibernate ajoute la colonne `contrat_id_contrat` dans `paiement` et la contrainte `alter table paiement add constraint FK… foreign key (contrat_id_contrat) references contrat (id_contrat)`. Un paiement connaît son contrat, l'inverse n'est pas encore possible. |
| 2. `mappedBy` | Aucun changement de schéma : ni colonne dans `contrat`, ni table `contrat_paiements`. Sans `mappedBy`, Hibernate aurait créé une table de jointure inutile. |
| 3. `cascade = ALL` | `contratRepository.save(contrat)` enregistre aussi les paiements ajoutés via `addPaiement` ; `delete(contrat)` supprime ses paiements avant le contrat. |
| 4. `orphanRemoval` | `contrat.removePaiement(p)` dans une transaction provoque `delete from paiement where id_paiement=?` au flush, même si le contrat est conservé. |
| 5. LAZY / EAGER | En LAZY, `findById` ne lit que la table `contrat` ; les paiements sont chargés au premier `getPaiements()` (dans une transaction, sinon `LazyInitializationException`). En EAGER, chaque lecture de contrat charge aussi ses paiements, même inutilisés. Configuration finale : LAZY. |

**cascade REMOVE ≠ orphanRemoval** : `REMOVE` agit quand on supprime le parent ; `orphanRemoval` agit quand on détache un enfant de la collection du parent. Les deux sont complémentaires ici.

## 4. Schéma généré (DDL MySQL, extrait)

```sql
create table paiement   (... contrat_id_contrat bigint, ...);
create table contrat    (... reservation_id_reservation bigint, ...);
create table employe    (... agence_id_agence bigint, ...);
create table vehicule   (... agence_id_agence bigint, ...);
create table reservation(... client_id_client bigint, vehicule_id_vehicule bigint, ...);
create table maintenance(... vehicule_id_vehicule bigint, ...);
create table vehicule_equipement (vehicules_id_vehicule bigint not null,
                                  equipements_id_equipement bigint not null,
                                  primary key (vehicules_id_vehicule, equipements_id_equipement));

alter table contrat add constraint UK… unique (reservation_id_reservation);   -- 1..1
alter table paiement add constraint FK… foreign key (contrat_id_contrat) references contrat (id_contrat);
-- + une FK par colonne ci-dessus, et deux FK sur vehicule_equipement
```

Les noms de colonnes sont ceux générés par défaut (`<attribut>_<clé primaire référencée>`), comme demandé dans l'atelier (pas de `@JoinColumn`). Aucune table de jointure parasite n'est générée : seule `vehicule_equipement` existe.

## 5. Points d'attention

- **`@OneToOne` côté inverse** : `Reservation.contrat` est déclaré LAZY, mais sans bytecode enhancement Hibernate ne peut pas créer de proxy pour le côté `mappedBy` (il doit lire `contrat` pour savoir si la valeur est `null`). Ce côté est donc chargé dès que la réservation est lue. Le côté propriétaire `Contrat.reservation` est, lui, réellement LAZY.
- **Chaîne de suppression** : `delete(reservation)` → contrat → paiements. Supprimer un client ou un véhicule ne supprime rien en cascade ; la base refuse la suppression tant que des lignes dépendantes existent.
- **Sérialisation JSON** : les collections inverses sont en `@JsonIgnore` pour éviter les cycles. Exposer des entités avec des `@ManyToOne` LAZY directement dans un contrôleur posera des problèmes de proxy ; on passera par des DTO.
- **`@Builder.Default`** : nécessaire pour que les collections initialisées (`new ArrayList<>()`, `new HashSet<>()`) ne soient pas `null` quand l'objet est construit avec le builder.
