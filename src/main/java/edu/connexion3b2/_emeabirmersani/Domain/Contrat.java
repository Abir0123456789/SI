package edu.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;

    // Contrat 1 --- 1 Reservation : côté PROPRIÉTAIRE (porte la FK reservation_id_reservation)
    // LAZY forcé : le @OneToOne est EAGER par défaut.
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Reservation reservation;

    // Étapes 2 à 5 — Contrat 1 <>--- * Paiement : côté INVERSE (mappedBy, aucune colonne créée)
    // Composition : cascade ALL + orphanRemoval ; LAZY (configuration finale retenue).
    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    private List<Paiement> paiements = new ArrayList<>();

    // Méthodes utilitaires : maintiennent les deux côtés de l'association synchronisés
    public void addPaiement(Paiement paiement) {
        paiements.add(paiement);
        paiement.setContrat(this);
    }

    public void removePaiement(Paiement paiement) {
        paiements.remove(paiement);   // orphanRemoval => DELETE du paiement au flush
        paiement.setContrat(null);
    }
}
