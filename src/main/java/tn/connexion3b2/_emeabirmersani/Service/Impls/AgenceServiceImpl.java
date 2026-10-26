package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.connexion3b2._emeabirmersani.Domain.Agence;
import tn.connexion3b2._emeabirmersani.Repository.IAgenceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IEmployeRepository;
import tn.connexion3b2._emeabirmersani.Repository.IVehiculeRepository;
import tn.connexion3b2._emeabirmersani.Service.IAgenceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;

    @Override
    public Agence getById(long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("agence not found"));
    }

    @Override
    public Agence add(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    public Agence update(Agence agence) {
        if (agence.getIdAgence() == null || !agenceRepository.existsById(agence.getIdAgence())) {
            throw new EntityNotFoundException("agence not found");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public void deleteById(long id) {
        if (!agenceRepository.existsById(id)) {
            throw new EntityNotFoundException("agence not found");
        }
        // aucune cascade Agence -> Vehicule / Employe : on refuse plutôt que laisser la FK échouer
        if (vehiculeRepository.existsByAgenceIdAgence(id)) {
            throw new IllegalStateException("suppression impossible : l'agence possède encore des véhicules");
        }
        if (employeRepository.existsByAgenceIdAgence(id)) {
            throw new IllegalStateException("suppression impossible : l'agence possède encore des employés");
        }
        agenceRepository.deleteById(id);
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public List<Agence> findByVille(String ville) {
        return agenceRepository.findByVilleIgnoreCase(ville);
    }

    @Override
    public List<Agence> rechercherParNom(String nom) {
        return agenceRepository.findByNomContainingIgnoreCase(nom);
    }
}
