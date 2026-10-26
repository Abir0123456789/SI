package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Paiement;
import tn.connexion3b2._emeabirmersani.Service.IPaiementService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementRestController {

    private final IPaiementService paiementService;

    @GetMapping("all")
    public List<Paiement> getAll() {
        return paiementService.findAll();
    }

    @GetMapping("get/{id}")
    public Paiement getById(@PathVariable("id") long id) {
        return paiementService.getById(id);
    }

    @PostMapping("add")
    public Paiement create(@RequestBody Paiement paiement) {
        return paiementService.add(paiement);
    }

    @PutMapping("update")
    public Paiement update(@RequestBody Paiement paiement) {
        return paiementService.update(paiement);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        paiementService.deleteById(id);
    }

    // ---------- métier ----------

    @PostMapping("payer/{idContrat}")
    public Paiement payer(@PathVariable("idContrat") long idContrat,
                          @RequestBody Paiement paiement) {
        return paiementService.payer(idContrat, paiement);
    }

    @GetMapping("contrat/{idContrat}")
    public List<Paiement> getByContrat(@PathVariable("idContrat") long idContrat) {
        return paiementService.findByContrat(idContrat);
    }

    @GetMapping("reste/{idContrat}")
    public BigDecimal resteAPayer(@PathVariable("idContrat") long idContrat) {
        return paiementService.resteAPayer(idContrat);
    }
}
