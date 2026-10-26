package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Equipement;

import java.util.List;

public interface IEquipementService {
    Equipement getById(long id);
    Equipement add(Equipement equipement);
    Equipement update(Equipement equipement);
    void deleteById(long id);
    List<Equipement> findAll();

    // métier
    List<Equipement> findByVehicule(long idVehicule);
}
