package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Agence;
import tn.connexion3b2._emeabirmersani.Service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
public class AgenceRestController {

    private final IAgenceService agenceService;

    @GetMapping("all")
    public List<Agence> getAll() {
        return agenceService.findAll();
    }

    @GetMapping("get/{id}")
    public Agence getById(@PathVariable("id") long id) {
        return agenceService.getById(id);
    }

    @PostMapping("add")
    public Agence create(@RequestBody Agence agence) {
        return agenceService.add(agence);
    }

    @PutMapping("update")
    public Agence update(@RequestBody Agence agence) {
        return agenceService.update(agence);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        agenceService.deleteById(id);
    }

    // ---------- métier ----------

    @GetMapping("ville/{ville}")
    public List<Agence> getByVille(@PathVariable("ville") String ville) {
        return agenceService.findByVille(ville);
    }

    @GetMapping("recherche")
    public List<Agence> rechercher(@RequestParam("nom") String nom) {
        return agenceService.rechercherParNom(nom);
    }
}
