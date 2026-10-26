package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Agence;
import tn.connexion3b2._emeabirmersani.Domain.Employe;
import tn.connexion3b2._emeabirmersani.Domain.RoleEmploye;
import tn.connexion3b2._emeabirmersani.Repository.IAgenceRepository;
import tn.connexion3b2._emeabirmersani.Repository.IEmployeRepository;
import tn.connexion3b2._emeabirmersani.Service.IEmployeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;

    @Override
    public Employe getById(long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("employe not found"));
    }

    @Override
    public Employe add(Employe employe) {
        return employeRepository.save(employe);
    }

    @Override
    public Employe update(Employe employe) {
        if (employe.getIdEmploye() == null || !employeRepository.existsById(employe.getIdEmploye())) {
            throw new EntityNotFoundException("employe not found");
        }
        return employeRepository.save(employe);
    }

    @Override
    public void deleteById(long id) {
        if (!employeRepository.existsById(id)) {
            throw new EntityNotFoundException("employe not found");
        }
        employeRepository.deleteById(id);
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    @Transactional
    public Employe affecterAgence(long idEmploye, long idAgence) {
        Employe employe = getById(idEmploye);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("agence not found"));
        employe.setAgence(agence); // côté propriétaire : la FK est écrite au commit
        return employe;
    }

    @Override
    public List<Employe> findByAgence(long idAgence) {
        return employeRepository.findByAgenceIdAgence(idAgence);
    }

    @Override
    public List<Employe> findByRole(RoleEmploye role) {
        return employeRepository.findByRole(role);
    }
}
