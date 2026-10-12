package edu.connexion3b2._emeabirmersani.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiement;

    private BigDecimal montant;
    private LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    // Étape 1 — Paiement * ---> 1 Contrat : côté PROPRIÉTAIRE (porte la FK contrat_id_contrat)
    // LAZY forcé : le @ManyToOne est EAGER par défaut.
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Contrat contrat;
}
