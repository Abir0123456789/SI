package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Equipement;
import tn.connexion3b2._emeabirmersani.Service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementRestController {

    private final IEquipementService equipementService;

    @GetMapping("all")
    public List<Equipement> getAll() {
        return equipementService.findAll();
    }

    @GetMapping("get/{id}")
    public Equipement getById(@PathVariable("id") long id) {
        return equipementService.getById(id);
    }

    @PostMapping("add")
    public Equipement create(@RequestBody Equipement equipement) {
        return equipementService.add(equipement);
    }

    @PutMapping("update")
    public Equipement update(@RequestBody Equipement equipement) {
        return equipementService.update(equipement);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        equipementService.deleteById(id);
    }

    // ---------- métier ----------

    @GetMapping("vehicule/{idVehicule}")
    public List<Equipement> getByVehicule(@PathVariable("idVehicule") long idVehicule) {
        return equipementService.findByVehicule(idVehicule);
    }
}
