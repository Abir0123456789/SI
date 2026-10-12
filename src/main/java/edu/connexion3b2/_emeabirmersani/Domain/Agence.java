package edu.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;
    private String ville;
    private String adresse;
    private String telephone;

    // Agence 1 --- * Employe (bidirectionnelle)
    @OneToMany(mappedBy = "agence")
    @JsonIgnore
    @ToString.Exclude
    private List<Employe> employes;

    // Agence 1 --- * Vehicule (bidirectionnelle)
    @OneToMany(mappedBy = "agence")
    @JsonIgnore
    @ToString.Exclude
    private List<Vehicule> vehicules;
}
