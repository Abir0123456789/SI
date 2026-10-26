package tn.connexion3b2._emeabirmersani.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmploye;

    private String nom;
    private String prenom;

    @Enumerated(EnumType.STRING)
    private RoleEmploye role;

    // Employe * ---> 1 Agence : côté PROPRIÉTAIRE (FK agence_id_agence), LAZY, aucune cascade
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Agence agence;
}
