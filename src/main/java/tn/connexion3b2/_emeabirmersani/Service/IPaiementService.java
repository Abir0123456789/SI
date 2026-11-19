package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Paiement;

import java.util.List;

/**
 * Lecture seule : un paiement est créé, modifié ou retiré uniquement via son Contrat
 * (composition Contrat <>--- Paiement, cascade ALL + orphanRemoval).
 */
public interface IPaiementService {

    Paiement findById(Long id);

    List<Paiement> findAll();
}
