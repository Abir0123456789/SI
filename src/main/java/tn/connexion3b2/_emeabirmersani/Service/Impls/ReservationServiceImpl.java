package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Client;
import tn.connexion3b2._emeabirmersani.Domain.Contrat;
import tn.connexion3b2._emeabirmersani.Domain.Reservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Repository.IClientRepository;
import tn.connexion3b2._emeabirmersani.Repository.IMaintenanceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IReservationRepository;
import tn.connexion3b2._emeabirmersani.Repository.IVehiculeRepository;
import tn.connexion3b2._emeabirmersani.Service.IReservationService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private static final List<StatutReservation> STATUTS_ACTIFS =
            List.of(StatutReservation.EN_ATTENTE, StatutReservation.CONFIRMEE);

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Reservation getById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation not found"));
    }

    @Override
    public Reservation add(Reservation reservation) {
        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(Reservation reservation) {
        if (reservation.getIdReservation() == null || !reservationRepository.existsById(reservation.getIdReservation())) {
            throw new EntityNotFoundException("reservation not found");
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteById(long id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("reservation not found");
        }
        reservationRepository.deleteById(id); // cascade ALL : son contrat (et ses paiements) aussi
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional
    public Reservation reserver(long idClient, long idVehicule, LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null || dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("période invalide : dateFin doit être postérieure ou égale à dateDebut");
        }
        if (dateDebut.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("la date de début est déjà passée");
        }
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new EntityNotFoundException("client not found"));
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("vehicule not found"));
        if (reservationRepository.countChevauchements(idVehicule, dateDebut, dateFin, STATUTS_ACTIFS) > 0) {
            throw new IllegalStateException("le véhicule est déjà réservé sur cette période");
        }
        if (maintenanceRepository.countChevauchements(idVehicule, dateDebut, dateFin) > 0) {
            throw new IllegalStateException("le véhicule est en maintenance sur cette période");
        }

        Reservation reservation = Reservation.builder()
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .statut(StatutReservation.EN_ATTENTE)
                .vehicule(vehicule)
                .client(client)
                .build();
        client.addReservation(reservation); // méthode utilitaire : synchronise les deux côtés
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation confirmer(long idReservation) {
        Reservation reservation = getById(idReservation);
        if (reservation.getStatut() != StatutReservation.EN_ATTENTE) {
            throw new IllegalStateException("seule une réservation EN_ATTENTE peut être confirmée");
        }
        reservation.setStatut(StatutReservation.CONFIRMEE);
        return reservation;
    }

    @Override
    @Transactional
    public Reservation annuler(long idReservation) {
        Reservation reservation = getById(idReservation);
        if (reservation.getStatut() != StatutReservation.EN_ATTENTE
                && reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new IllegalStateException("réservation déjà " + reservation.getStatut());
        }
        reservation.setStatut(StatutReservation.ANNULEE);
        Contrat contrat = reservation.getContrat();
        if (contrat != null) {
            contrat.setValide(false);
            liberer(reservation.getVehicule());
        }
        return reservation;
    }

    @Override
    @Transactional
    public Reservation terminer(long idReservation) {
        Reservation reservation = getById(idReservation);
        if (reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new IllegalStateException("seule une réservation CONFIRMEE peut être terminée");
        }
        reservation.setStatut(StatutReservation.TERMINEE);
        liberer(reservation.getVehicule());
        return reservation;
    }

    @Override
    public List<Reservation> findByClient(long idClient) {
        return reservationRepository.findByClientIdClient(idClient);
    }

    @Override
    public List<Reservation> findByVehicule(long idVehicule) {
        return reservationRepository.findByVehiculeIdVehicule(idVehicule);
    }

    @Override
    public List<Reservation> findByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    // le véhicule redevient disponible s'il était en location
    private void liberer(Vehicule vehicule) {
        if (vehicule != null && vehicule.getStatut() == StatutVehicule.LOUE) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
    }
}
