package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.Paiement;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
}
