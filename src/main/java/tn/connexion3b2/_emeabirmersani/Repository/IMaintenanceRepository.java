package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.connexion3b2._emeabirmersani.Domain.Maintenance;

import java.time.LocalDate;
import java.util.List;

public interface IMaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVehiculeIdVehiculeOrderByDateDebutDesc(Long idVehicule);

    boolean existsByVehiculeIdVehicule(Long idVehicule);

    // maintenances en cours à une date donnée (dateFin null = durée indéterminée)
    @Query("SELECT m FROM Maintenance m WHERE m.dateDebut <= :jour " +
           "AND (m.dateFin IS NULL OR m.dateFin >= :jour)")
    List<Maintenance> findEnCours(@Param("jour") LocalDate jour);

    // nombre de maintenances d'un véhicule qui chevauchent la période [debut, fin]
    @Query("SELECT COUNT(m) FROM Maintenance m WHERE m.vehicule.idVehicule = :idVehicule " +
           "AND m.dateDebut <= :fin AND (m.dateFin IS NULL OR m.dateFin >= :debut)")
    long countChevauchements(@Param("idVehicule") Long idVehicule,
                             @Param("debut") LocalDate debut,
                             @Param("fin") LocalDate fin);
}
