package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Reservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    Reservation getById(long id);
    Reservation add(Reservation reservation);
    Reservation update(Reservation reservation);
    void deleteById(long id);
    List<Reservation> findAll();

    // métier
    Reservation reserver(long idClient, long idVehicule, LocalDate dateDebut, LocalDate dateFin);
    Reservation confirmer(long idReservation);
    Reservation annuler(long idReservation);
    Reservation terminer(long idReservation);
    List<Reservation> findByClient(long idClient);
    List<Reservation> findByVehicule(long idVehicule);
    List<Reservation> findByStatut(StatutReservation statut);
}
