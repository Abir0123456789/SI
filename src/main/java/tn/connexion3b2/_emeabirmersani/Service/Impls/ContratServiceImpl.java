package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Contrat;
import tn.connexion3b2._emeabirmersani.Domain.Reservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Repository.IContratRepository;
import tn.connexion3b2._emeabirmersani.Repository.IReservationRepository;
import tn.connexion3b2._emeabirmersani.Service.IContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Contrat getById(long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("contrat not found"));
    }

    @Override
    public Contrat add(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional
    public Contrat update(Contrat contrat) {
        if (contrat.getIdContrat() == null) {
            throw new EntityNotFoundException("contrat not found");
        }
        Contrat existant = getById(contrat.getIdContrat());
        // paiements est @JsonIgnore : il arrive vide dans le body. Un save(contrat) direct
        // remplacerait la liste et orphanRemoval supprimerait TOUS les paiements du contrat.
        // On recopie donc uniquement les champs du contrat sur l'entité gérée (dirty checking).
        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());
        existant.setReservation(contrat.getReservation());
        return existant;
    }

    @Override
    public void deleteById(long id) {
        if (!contratRepository.existsById(id)) {
            throw new EntityNotFoundException("contrat not found");
        }
        contratRepository.deleteById(id); // cascade ALL : ses paiements sont supprimés aussi
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    @Transactional
    public Contrat genererContrat(long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("reservation not found"));
        if (reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new IllegalStateException("la réservation doit être CONFIRMEE pour générer un contrat");
        }
        if (reservation.getContrat() != null) {
            throw new IllegalStateException("un contrat existe déjà pour cette réservation");
        }
        Vehicule vehicule = reservation.getVehicule();
        if (vehicule == null || vehicule.getTarifJournalier() == null) {
            throw new IllegalStateException("véhicule ou tarif journalier non défini");
        }

        // nombre de jours facturés = dateFin - dateDebut (minimum 1 jour)
        long nbJours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        if (nbJours < 1) {
            nbJours = 1;
        }
        BigDecimal montant = vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(nbJours));

        Contrat contrat = Contrat.builder()
                .dateSignature(LocalDate.now())
                .montantTotal(montant)
                .valide(true)
                .reservation(reservation)
                .build();
        reservation.attacherContrat(contrat);      // méthode utilitaire : synchronise les deux côtés
        vehicule.setStatut(StatutVehicule.LOUE);   // le véhicule part en location
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findByReservation(long idReservation) {
        return contratRepository.findByReservationIdReservation(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("contrat not found"));
    }

    @Override
    public List<Contrat> findByValide(boolean valide) {
        return contratRepository.findByValide(valide);
    }
}
