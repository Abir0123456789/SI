package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.Equipement;

import java.util.List;

public interface IEquipementRepository extends JpaRepository<Equipement, Long> {

    // jointure sur la table vehicule_equipement via le côté inverse "vehicules"
    List<Equipement> findByVehiculesIdVehicule(Long idVehicule);
}
