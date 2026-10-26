package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNumPermis(String numPermis);

    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}
