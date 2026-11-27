package tn.connexion3b2._emeabirmersani.Service;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import java.math.BigDecimal;
import java.util.List;
public interface IVehiculeService {

    Vehicule create(Vehicule vehicule);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);

    // Affecter un vehicule a une agence
    Vehicule assignToAgence(Long idVehicule, Long idAgence);

    Vehicule assignToEquipement(Long idVehicule, Long idEquipement);

    // Retirer un vehicule de son agence
    Vehicule unassignToAgence(Long idVehicule, Long idAgence);

    Vehicule unassignToEquipement(Long idVehicule, Long idEquipement);

    List<Vehicule> getAllBycategorie(CategorieVehicule categorie);

    List<Vehicule> getAllBycategorieAndtarif(CategorieVehicule categorie, BigDecimal min, BigDecimal max);
}



