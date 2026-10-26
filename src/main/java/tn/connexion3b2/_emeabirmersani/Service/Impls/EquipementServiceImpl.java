package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Equipement;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Repository.IEquipementRepository;
import tn.connexion3b2._emeabirmersani.Service.IEquipementService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement getById(long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("equipement not found"));
    }

    @Override
    public Equipement add(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement update(Equipement equipement) {
        if (equipement.getIdEquipement() == null || !equipementRepository.existsById(equipement.getIdEquipement())) {
            throw new EntityNotFoundException("equipement not found");
        }
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Equipement equipement = getById(id);
        // Equipement est le côté inverse du @ManyToMany : on retire d'abord l'équipement
        // de chaque véhicule (côté propriétaire) pour vider la table vehicule_equipement
        for (Vehicule vehicule : equipement.getVehicules()) {
            vehicule.getEquipements().remove(equipement);
        }
        equipementRepository.delete(equipement);
    }

    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public List<Equipement> findByVehicule(long idVehicule) {
        return equipementRepository.findByVehiculesIdVehicule(idVehicule);
    }
}
