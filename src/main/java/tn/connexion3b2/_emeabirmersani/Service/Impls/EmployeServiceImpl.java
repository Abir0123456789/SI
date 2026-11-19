package tn.connexion3b2._emeabirmersani.Service.Impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.connexion3b2._emeabirmersani.Domain.Employe;
import tn.connexion3b2._emeabirmersani.Repository.IEmployeRepository;
import tn.connexion3b2._emeabirmersani.Service.IEmployeService;
import tn.connexion3b2._emeabirmersani.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant");
        }
        verifier(employe);
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existant = findById(id);
        verifier(employe);
        // recopie des attributs modifiables, sans toucher à l'identifiant ni aux associations
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    // Règle de gestion : appelée à la création et à la modification
    private void verifier(Employe employe) {
        if (estVide(employe.getNom())) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (estVide(employe.getPrenom())) {
            throw new IllegalArgumentException("Le prénom de l'employé est obligatoire");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'employé est obligatoire");
        }
    }

    private static boolean estVide(String valeur) {
        return valeur == null || valeur.isBlank();
    }
}
