package edu.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // Reservation * --- 1 Vehicule
    @ManyToOne
    private Vehicule vehicule;

    // Reservation * --- 1 Client
    @ManyToOne
    private Client client;

    // Reservation 1 --- 1 Contrat (côté inverse)
    @OneToOne(mappedBy = "reservation")
    @JsonIgnore
    @ToString.Exclude
    private Contrat contrat;
}
