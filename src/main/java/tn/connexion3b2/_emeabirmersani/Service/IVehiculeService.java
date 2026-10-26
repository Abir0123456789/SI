package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IVehiculeService {
    Vehicule getById(long id);
    Vehicule add(Vehicule vehicule);
    Vehicule update(Vehicule vehicule);
    void deleteById(long id);
    List<Vehicule> findAll();

    // métier
    List<Vehicule> findByStatut(StatutVehicule statut);
    List<Vehicule> findByCategorie(CategorieVehicule categorie);
    List<Vehicule> findByAgence(long idAgence);
    List<Vehicule> findByTarif(BigDecimal min, BigDecimal max);
    List<Vehicule> findDisponibles(LocalDate dateDebut, LocalDate dateFin);
    Vehicule affecterAgence(long idVehicule, long idAgence);
    Vehicule ajouterEquipement(long idVehicule, long idEquipement);
    Vehicule retirerEquipement(long idVehicule, long idEquipement);
}
