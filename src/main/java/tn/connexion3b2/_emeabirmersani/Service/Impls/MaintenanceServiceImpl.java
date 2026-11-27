package tn.connexion3b2._emeabirmersani.Service.Impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.connexion3b2._emeabirmersani.Domain.Maintenance;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Repository.IMaintenanceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IVehiculeRepository;
import tn.connexion3b2._emeabirmersani.Service.IMaintenanceService;
import tn.connexion3b2._emeabirmersani.WebController.MaintenanceDTO;
import tn.connexion3b2._emeabirmersani.WebController.MaintenanceMapper;
import tn.connexion3b2._emeabirmersani.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final MaintenanceMapper maintenanceMapper;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        if (maintenance.getIdMaintenance() != null) {
            throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        verifier(maintenance);
        // La maintenance porte la FK (vehicule_id_vehicule) sans cascade : on la rattache a un vehicule EXISTANT, recharge depuis la base
        if (maintenance.getVehicule() != null) {
            Long idVehicule = maintenance.getVehicule().getIdVehicule();
            if (idVehicule == null) {
                throw new IllegalArgumentException("Le véhicule doit déjà exister : envoyer seulement son idVehicule");
            }
            Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", idVehicule));
            maintenance.setVehicule(vehicule);
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public MaintenanceDTO findById(Long id) {
        return maintenanceMapper.toDto(getMaintenance(id));
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        Maintenance existant = getMaintenance(id);
        verifier(maintenance);
        // recopie des attributs modifiables, sans toucher à l'identifiant ni aux associations
        existant.setDateDebut(maintenance.getDateDebut());
        existant.setDateFin(maintenance.getDateFin());
        existant.setDescription(maintenance.getDescription());
        return maintenanceRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }

    // Charge l'entite (utilisee par findById et update) : ResourceNotFoundException si l'id n'existe pas
    private Maintenance getMaintenance(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    // Règle de gestion : appelée à la création et à la modification
    private void verifier(Maintenance maintenance) {
        if (maintenance.getDateDebut() != null && maintenance.getDateFin() != null
                && maintenance.getDateFin().isBefore(maintenance.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }
}