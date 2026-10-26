package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.connexion3b2._emeabirmersani.Domain.Reservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByClientIdClient(Long idClient);

    List<Reservation> findByVehiculeIdVehicule(Long idVehicule);

    List<Reservation> findByStatut(StatutReservation statut);

    boolean existsByClientIdClient(Long idClient);

    boolean existsByVehiculeIdVehicule(Long idVehicule);

    // nombre de réservations (aux statuts donnés) d'un véhicule qui chevauchent [debut, fin]
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.vehicule.idVehicule = :idVehicule " +
           "AND r.statut IN :statuts AND r.dateDebut <= :fin AND r.dateFin >= :debut")
    long countChevauchements(@Param("idVehicule") Long idVehicule,
                             @Param("debut") LocalDate debut,
                             @Param("fin") LocalDate fin,
                             @Param("statuts") List<StatutReservation> statuts);
}
