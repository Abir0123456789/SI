package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Contrat;
import tn.connexion3b2._emeabirmersani.Service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
public class ContratRestController {

    private final IContratService contratService;

    @GetMapping("all")
    public List<Contrat> getAll() {
        return contratService.findAll();
    }

    @GetMapping("get/{id}")
    public Contrat getById(@PathVariable("id") long id) {
        return contratService.getById(id);
    }

    @PostMapping("add")
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.add(contrat);
    }

    @PutMapping("update")
    public Contrat update(@RequestBody Contrat contrat) {
        return contratService.update(contrat);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        contratService.deleteById(id);
    }

    // ---------- métier ----------

    @PostMapping("generer/{idReservation}")
    public Contrat generer(@PathVariable("idReservation") long idReservation) {
        return contratService.genererContrat(idReservation);
    }

    @GetMapping("reservation/{idReservation}")
    public Contrat getByReservation(@PathVariable("idReservation") long idReservation) {
        return contratService.findByReservation(idReservation);
    }

    @GetMapping("valide/{valide}")
    public List<Contrat> getByValide(@PathVariable("valide") boolean valide) {
        return contratService.findByValide(valide);
    }
}
