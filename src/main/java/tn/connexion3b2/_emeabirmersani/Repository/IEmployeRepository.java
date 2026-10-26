package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.Employe;
import tn.connexion3b2._emeabirmersani.Domain.RoleEmploye;

import java.util.List;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {

    List<Employe> findByAgenceIdAgence(Long idAgence);

    List<Employe> findByRole(RoleEmploye role);

    boolean existsByAgenceIdAgence(Long idAgence);
}
