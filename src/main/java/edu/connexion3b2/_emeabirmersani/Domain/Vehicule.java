package edu.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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

    // Vehicule * --- 1 Agence
    @ManyToOne
    private Agence agence;

    // Vehicule * --- * Equipement (bidirectionnelle, Vehicule = côté propriétaire)
    @ManyToMany
    @ToString.Exclude
    private List<Equipement> equipements;

    // Vehicule 1 --- * Reservation (bidirectionnelle)
    @OneToMany(mappedBy = "vehicule")
    @JsonIgnore
    @ToString.Exclude
    private List<Reservation> reservations;

    // Maintenance --> Vehicule : unidirectionnelle, rien de ce côté
}
