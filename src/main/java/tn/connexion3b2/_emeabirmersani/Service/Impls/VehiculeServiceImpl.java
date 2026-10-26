package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Agence;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Equipement;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Repository.IAgenceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IEquipementRepository;
import tn.connexion3b2._emeabirmersani.Repository.IMaintenanceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IReservationRepository;
import tn.connexion3b2._emeabirmersani.Repository.IVehiculeRepository;
import tn.connexion3b2._emeabirmersani.Service.IVehiculeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private static final List<StatutReservation> STATUTS_ACTIFS =
            List.of(StatutReservation.EN_ATTENTE, StatutReservation.CONFIRMEE);

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final IReservationRepository reservationRepository;
    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Vehicule getById(long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("vehicule not found"));
    }

    @Override
    public Vehicule add(Vehicule vehicule) {
        if (vehicule.getImmatriculation() != null
                && vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new IllegalStateException("immatriculation déjà utilisée : " + vehicule.getImmatriculation());
        }
        if (vehicule.getStatut() == null) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule update(Vehicule vehicule) {
        // sans id (ou id inexistant), save() ferait un INSERT : on refuse
        if (vehicule.getIdVehicule() == null || !vehiculeRepository.existsById(vehicule.getIdVehicule())) {
            throw new EntityNotFoundException("vehicule not found");
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public void deleteById(long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new EntityNotFoundException("vehicule not found");
        }
        // historique conservé (pas de cascade REMOVE) : on refuse plutôt que laisser la FK échouer
        if (reservationRepository.existsByVehiculeIdVehicule(id)) {
            throw new IllegalStateException("suppression impossible : le véhicule a des réservations");
        }
        if (maintenanceRepository.existsByVehiculeIdVehicule(id)) {
            throw new IllegalStateException("suppression impossible : le véhicule a des maintenances");
        }
        vehiculeRepository.deleteById(id); // les lignes de vehicule_equipement partent avec (côté propriétaire)
    }

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public List<Vehicule> findByStatut(StatutVehicule statut) {
        return vehiculeRepository.findByStatut(statut);
    }

    @Override
    public List<Vehicule> findByCategorie(CategorieVehicule categorie) {
        return vehiculeRepository.findByCategorie(categorie);
    }

    @Override
    public List<Vehicule> findByAgence(long idAgence) {
        return vehiculeRepository.findByAgenceIdAgence(idAgence);
    }

    @Override
    public List<Vehicule> findByTarif(BigDecimal min, BigDecimal max) {
        if (min == null || max == null || min.compareTo(max) > 0) {
            throw new IllegalArgumentException("intervalle de tarif invalide");
        }
        return vehiculeRepository.findByTarifJournalierBetween(min, max);
    }

    @Override
    public List<Vehicule> findDisponibles(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null || dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("période invalide : dateFin doit être postérieure ou égale à dateDebut");
        }
        return vehiculeRepository.findDisponibles(dateDebut, dateFin, STATUTS_ACTIFS);
    }

    @Override
    @Transactional
    public Vehicule affecterAgence(long idVehicule, long idAgence) {
        Vehicule vehicule = getById(idVehicule);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("agence not found"));
        vehicule.setAgence(agence); // côté propriétaire : la FK est écrite au commit
        return vehicule;
    }

    @Override
    @Transactional
    public Vehicule ajouterEquipement(long idVehicule, long idEquipement) {
        Vehicule vehicule = getById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new EntityNotFoundException("equipement not found"));
        if (vehicule.getEquipements().contains(equipement)) {
            throw new IllegalStateException("équipement déjà présent sur ce véhicule");
        }
        vehicule.addEquipement(equipement); // méthode utilitaire : synchronise les deux côtés
        return vehicule;
    }

    @Override
    @Transactional
    public Vehicule retirerEquipement(long idVehicule, long idEquipement) {
        Vehicule vehicule = getById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new EntityNotFoundException("equipement not found"));
        if (!vehicule.getEquipements().contains(equipement)) {
            throw new IllegalStateException("cet équipement n'est pas sur ce véhicule");
        }
        vehicule.removeEquipement(equipement); // méthode utilitaire : synchronise les deux côtés
        return vehicule;
    }
}
