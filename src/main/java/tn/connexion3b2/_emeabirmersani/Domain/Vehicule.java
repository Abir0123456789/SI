package tn.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // proxy LAZY : champs techniques Hibernate
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    private String immatriculation;
    private String marque;
    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    // Vehicule * ---> 1 Agence : côté PROPRIÉTAIRE (FK agence_id_agence), LAZY, aucune cascade
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Agence agence;

    // Vehicule * --- * Equipement : côté PROPRIÉTAIRE (table de jointure vehicule_equipement)
    // LAZY, aucune cascade (équipements partagés), Set pour éviter les doublons
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vehicule_equipement")
    @ToString.Exclude
    @Builder.Default
    private Set<Equipement> equipements = new HashSet<>();

    // Vehicule 1 --- * Maintenance : côté INVERSE, LAZY, cascade PERSIST
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    private List<Maintenance> maintenances = new ArrayList<>();

    // Reservation * ---> 1 Vehicule : unidirectionnelle, rien de ce côté

    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
        equipement.getVehicules().add(this);
    }

    public void removeEquipement(Equipement equipement) {
        equipements.remove(equipement);
        equipement.getVehicules().remove(this);
    }

    public void addMaintenance(Maintenance maintenance) {
        maintenances.add(maintenance);
        maintenance.setVehicule(this);
    }
}
