package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.connexion3b2._emeabirmersani.Domain.Agence;
import tn.connexion3b2._emeabirmersani.Service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("agences")
@RequiredArgsConstructor
public class AgenceRestController {

    private final IAgenceService agenceService;

    @PostMapping("add")
    public Agence create(@RequestBody Agence agence) {
        return agenceService.create(agence);
    }

    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @GetMapping("{id}")
    public Agence findById(@PathVariable("id") Long id) {
        return agenceService.findById(id);
    }

    @PutMapping("{id}")
    public Agence update(@PathVariable("id") Long id, @RequestBody Agence agence) {
        return agenceService.update(id, agence);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        agenceService.deleteById(id);
    }
}
