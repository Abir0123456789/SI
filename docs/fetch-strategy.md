# Stratégie de Fetch et de Cascade — AutoLoc

Règle générale : toutes les associations sont en `FetchType.LAZY`.
- `@ManyToOne` / `@OneToOne` sont EAGER par défaut → forcés en LAZY pour ne pas charger toute la chaîne d'objets liés.
- `@OneToMany` / `@ManyToMany` sont LAZY par défaut → conservés en LAZY (une collection ne se charge que si on l'utilise).

Une cascade n'est appliquée que lorsque l'enfant ne peut pas exister sans son parent (composition) ou lorsqu'il est créé en même temps que lui.

| Association | Côté propriétaire (FK) | Fetch | Cascade | Justification |
|---|---|---|---|---|
| Contrat → Paiement | Paiement (`contrat_id_contrat`) | LAZY | ALL + orphanRemoval | Composition : un paiement n'existe que rattaché à son contrat. Supprimer le contrat supprime ses paiements ; retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | Vehicule (`agence_id_agence`) | LAZY | Aucune | Un véhicule survit à son agence (il peut être réaffecté). Supprimer une agence ne doit pas supprimer la flotte. |
| Agence → Employe | Employe (`agence_id_agence`) | LAZY | Aucune | Un employé a un cycle de vie propre (mutation vers une autre agence). |
| Vehicule ↔ Equipement | Vehicule (table de jointure `vehicule_equipement`) | LAZY | Aucune | Les équipements sont partagés entre plusieurs véhicules : supprimer un véhicule ne doit pas supprimer un équipement utilisé ailleurs. `Set` utilisé pour éviter les doublons et les suppressions/réinsertions massives d'Hibernate sur les `List` en `@ManyToMany`. |
| Client → Reservation | Reservation (`client_id_client`) | LAZY | PERSIST | Une réservation peut être enregistrée en même temps que le client. Pas de REMOVE : l'historique des réservations doit être conservé. |
| Reservation → Vehicule | Reservation (`vehicule_id_vehicule`) | LAZY | Aucune | Association unidirectionnelle : la réservation référence un véhicule existant, elle ne gère pas son cycle de vie. |
| Reservation ↔ Contrat | Contrat (`reservation_id_reservation`) | LAZY | ALL (côté Reservation) | Le contrat découle directement de la réservation : il est créé, mis à jour et supprimé avec elle. |
| Vehicule → Maintenance | Maintenance (`vehicule_id_vehicule`) | LAZY | PERSIST | Une maintenance peut être enregistrée via le véhicule. Pas de REMOVE : l'historique de maintenance est conservé. |

## Remarques
- Le côté inverse (`@OneToMany(mappedBy = ...)`, `@ManyToMany(mappedBy = ...)`, `@OneToOne(mappedBy = ...)`) ne crée aucune colonne ; sans `mappedBy`, Hibernate créerait une table de jointure inutile.
- Lombok ciblé (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`), pas de `@Data` ni `@ToString` : le `toString`/`hashCode` généré parcourrait les deux côtés des associations bidirectionnelles (StackOverflowError) et déclencherait le chargement des relations LAZY.
- Avec LAZY, un accès à une relation hors transaction lève une `LazyInitializationException` ; elle sera traitée par des requêtes dédiées (Atelier 7).
- `Contrat.addPaiement()` / `removePaiement()` maintiennent les deux côtés de l'association synchronisés.
