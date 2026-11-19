package tn.connexion3b2._emeabirmersani.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    // Vehicule * --- 1 Agence (cote proprietaire : FK agence_id_agence)
    @ManyToOne(fetch = FetchType.LAZY)
    private Agence agence;

    // Vehicule * --- * Equipement (cote proprietaire : table de jointure vehicule_equipement)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vehicule_equipement")
    private Set<Equipement> equipements = new HashSet<>();

    // Vehicule 1 --- * Maintenance (cote inverse) : PERSIST
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Maintenance> maintenances = new ArrayList<>();
}
