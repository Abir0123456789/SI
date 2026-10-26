package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Employe;
import tn.connexion3b2._emeabirmersani.Domain.RoleEmploye;

import java.util.List;

public interface IEmployeService {
    Employe getById(long id);
    Employe add(Employe employe);
    Employe update(Employe employe);
    void deleteById(long id);
    List<Employe> findAll();

    // métier
    Employe affecterAgence(long idEmploye, long idAgence);
    List<Employe> findByAgence(long idAgence);
    List<Employe> findByRole(RoleEmploye role);
}
