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
import tn.connexion3b2._emeabirmersani.Domain.Contrat;
import tn.connexion3b2._emeabirmersani.Service.IContratService;

import java.util.List;

@RestController
@RequestMapping("contrats")
@RequiredArgsConstructor
public class ContratRestController {

    private final IContratService contratService;

    @PostMapping("add")
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.create(contrat);
    }

    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @GetMapping("{id}")
    public Contrat findById(@PathVariable("id") Long id) {
        return contratService.findById(id);
    }

    @PutMapping("{id}")
    public Contrat update(@PathVariable("id") Long id, @RequestBody Contrat contrat) {
        return contratService.update(id, contrat);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        contratService.deleteById(id);
    }
}
