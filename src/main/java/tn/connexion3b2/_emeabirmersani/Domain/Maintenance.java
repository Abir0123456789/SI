package tn.connexion3b2._emeabirmersani.Domain;

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
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaintenance;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;

    // Maintenance * ---> 1 Vehicule : côté PROPRIÉTAIRE (FK vehicule_id_vehicule), LAZY
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Vehicule vehicule;
}
