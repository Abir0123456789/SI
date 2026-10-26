package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {

    boolean existsByImmatriculation(String immatriculation);

    List<Vehicule> findByStatut(StatutVehicule statut);

    List<Vehicule> findByCategorie(CategorieVehicule categorie);

    List<Vehicule> findByAgenceIdAgence(Long idAgence);

    List<Vehicule> findByTarifJournalierBetween(BigDecimal min, BigDecimal max);

    boolean existsByAgenceIdAgence(Long idAgence);

    // véhicules libres sur [debut, fin] : aucune réservation active ni maintenance qui chevauche
    @Query("SELECT v FROM Vehicule v WHERE " +
           "v.idVehicule NOT IN (SELECT r.vehicule.idVehicule FROM Reservation r " +
           "    WHERE r.statut IN :statuts AND r.dateDebut <= :fin AND r.dateFin >= :debut) " +
           "AND v.idVehicule NOT IN (SELECT m.vehicule.idVehicule FROM Maintenance m " +
           "    WHERE m.dateDebut <= :fin AND (m.dateFin IS NULL OR m.dateFin >= :debut))")
    List<Vehicule> findDisponibles(@Param("debut") LocalDate debut,
                                   @Param("fin") LocalDate fin,
                                   @Param("statuts") List<StatutReservation> statuts);
}
