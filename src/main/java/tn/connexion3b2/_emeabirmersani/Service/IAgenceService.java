package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Agence;

import java.util.List;

public interface IAgenceService {
    Agence getById(long id);
    Agence add(Agence agence);
    Agence update(Agence agence);
    void deleteById(long id);
    List<Agence> findAll();

    // métier
    List<Agence> findByVille(String ville);
    List<Agence> rechercherParNom(String nom);
}
