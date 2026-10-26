package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.Contrat;

import java.util.List;
import java.util.Optional;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    Optional<Contrat> findByReservationIdReservation(Long idReservation);

    List<Contrat> findByValide(boolean valide);
}
