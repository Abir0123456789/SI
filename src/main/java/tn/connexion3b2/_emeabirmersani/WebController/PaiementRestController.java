package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.connexion3b2._emeabirmersani.Domain.Paiement;
import tn.connexion3b2._emeabirmersani.Service.IPaiementService;

import java.util.List;

/**
 * Lecture seule : un paiement est cree, modifie ou retire uniquement via son Contrat
 * (composition Contrat <>--- Paiement, cascade ALL + orphanRemoval).
 */
@RestController
@RequestMapping("paiements")
@RequiredArgsConstructor
public class PaiementRestController {

    private final IPaiementService paiementService;

    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @GetMapping("{id}")
    public Paiement findById(@PathVariable("id") Long id) {
        return paiementService.findById(id);
    }
}
