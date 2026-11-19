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
import tn.connexion3b2._emeabirmersani.Domain.Equipement;
import tn.connexion3b2._emeabirmersani.Service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("equipements")
@RequiredArgsConstructor
public class EquipementRestController {

    private final IEquipementService equipementService;

    @PostMapping("add")
    public Equipement create(@RequestBody Equipement equipement) {
        return equipementService.create(equipement);
    }

    @GetMapping
    public List<Equipement> findAll() {
        return equipementService.findAll();
    }

    @GetMapping("{id}")
    public Equipement findById(@PathVariable("id") Long id) {
        return equipementService.findById(id);
    }

    @PutMapping("{id}")
    public Equipement update(@PathVariable("id") Long id, @RequestBody Equipement equipement) {
        return equipementService.update(id, equipement);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        equipementService.deleteById(id);
    }
}
