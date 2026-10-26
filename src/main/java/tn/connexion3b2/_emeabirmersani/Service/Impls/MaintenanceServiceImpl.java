package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Maintenance;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Repository.IMaintenanceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IReservationRepository;
import tn.connexion3b2._emeabirmersani.Repository.IVehiculeRepository;
import tn.connexion3b2._emeabirmersani.Service.IMaintenanceService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private static final List<StatutReservation> STATUTS_ACTIFS =
            List.of(StatutReservation.EN_ATTENTE, StatutReservation.CONFIRMEE);

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Maintenance getById(long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("maintenance not found"));
    }

    @Override
    public Maintenance add(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance update(Maintenance maintenance) {
        if (maintenance.getIdMaintenance() == null || !maintenanceRepository.existsById(maintenance.getIdMaintenance())) {
            throw new EntityNotFoundException("maintenance not found");
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public void deleteById(long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new EntityNotFoundException("maintenance not found");
        }
        maintenanceRepository.deleteById(id);
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    @Transactional
    public Maintenance planifier(long idVehicule, Maintenance maintenance) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("vehicule not found"));
        LocalDate aujourdhui = LocalDate.now();
        if (maintenance.getDateDebut() == null) {
            maintenance.setDateDebut(aujourdhui);
        }
        LocalDate debut = maintenance.getDateDebut();
        LocalDate fin = maintenance.getDateFin();
        if (fin != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException("dateFin doit être postérieure ou égale à dateDebut");
        }
        // fin inconnue : on vérifie au moins le jour de début
        LocalDate finControle = fin != null ? fin : debut;
        if (reservationRepository.countChevauchements(idVehicule, debut, finControle, STATUTS_ACTIFS) > 0) {
            throw new IllegalStateException("le véhicule a une réservation active sur cette période");
        }

        maintenance.setIdMaintenance(null);
        vehicule.addMaintenance(maintenance); // méthode utilitaire : synchronise les deux côtés
        // maintenance qui commence aujourd'hui (ou avant) : le véhicule est immobilisé
        if (!debut.isAfter(aujourdhui)) {
            vehicule.setStatut(StatutVehicule.MAINTENANCE);
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional
    public Maintenance terminer(long idMaintenance) {
        Maintenance maintenance = getById(idMaintenance);
        LocalDate aujourdhui = LocalDate.now();
        if (maintenance.getDateDebut() != null && maintenance.getDateDebut().isAfter(aujourdhui)) {
            throw new IllegalStateException("la maintenance n'a pas encore commencé");
        }
        if (maintenance.getDateFin() == null || maintenance.getDateFin().isAfter(aujourdhui)) {
            maintenance.setDateFin(aujourdhui);
        }
        Vehicule vehicule = maintenance.getVehicule();
        if (vehicule != null && vehicule.getStatut() == StatutVehicule.MAINTENANCE) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
        return maintenance;
    }

    @Override
    public List<Maintenance> findByVehicule(long idVehicule) {
        return maintenanceRepository.findByVehiculeIdVehiculeOrderByDateDebutDesc(idVehicule);
    }

    @Override
    public List<Maintenance> findEnCours() {
        return maintenanceRepository.findEnCours(LocalDate.now());
    }
}
