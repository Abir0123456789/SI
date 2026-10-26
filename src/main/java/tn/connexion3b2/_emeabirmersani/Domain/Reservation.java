package tn.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // proxy LAZY : champs techniques Hibernate
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // Reservation * ---> 1 Vehicule : unidirectionnelle, côté PROPRIÉTAIRE (FK vehicule_id_vehicule), LAZY, aucune cascade
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Vehicule vehicule;

    // Reservation * ---> 1 Client : côté PROPRIÉTAIRE (FK client_id_client), LAZY, aucune cascade
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Client client;

    // Reservation 1 --- 1 Contrat : côté INVERSE (mappedBy), cascade ALL côté Reservation
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Contrat contrat;

    // Rattache un contrat à la réservation en synchronisant les deux côtés
    public void attacherContrat(Contrat contrat) {
        this.contrat = contrat;
        if (contrat != null) {
            contrat.setReservation(this);
        }
    }
}
