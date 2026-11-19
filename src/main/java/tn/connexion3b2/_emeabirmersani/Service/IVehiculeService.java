package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    Vehicule create(Vehicule vehicule);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);

    Vehicule assignToAgence (Long idvehicule ,Long idAgence);

    Vehicule assignToEquipement(Long idVehicule, Long idEquipement);

    Vehicule UnassignToAgence(Long idVehicule, Long idAgence);

    Vehicule unassignToEquipement(Long idVehicule, Long idEquipement);
}
