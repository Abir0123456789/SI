package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.connexion3b2._emeabirmersani.Domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    // JPQL : reservations actives a une date donnee (dateDebut <= date <= dateFin)
    @Query("SELECT r FROM Reservation r WHERE r.dateDebut <= :date AND r.dateFin >= :date")
    List<Reservation> findReservationsByDate(@Param("date") LocalDate date);
}

