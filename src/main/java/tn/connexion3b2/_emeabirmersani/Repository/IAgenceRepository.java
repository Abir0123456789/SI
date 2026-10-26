package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.Agence;

import java.util.List;

public interface IAgenceRepository extends JpaRepository<Agence, Long> {

    List<Agence> findByVilleIgnoreCase(String ville);

    List<Agence> findByNomContainingIgnoreCase(String nom);
}
