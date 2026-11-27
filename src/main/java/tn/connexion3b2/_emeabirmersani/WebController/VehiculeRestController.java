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
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Service.IVehiculeService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("vehicules")
@RequiredArgsConstructor
public class VehiculeRestController {

    private final IVehiculeService vehiculeService;

    @PostMapping("add")
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("{id}")
    public Vehicule findById(@PathVariable("id") Long id) {
        return vehiculeService.findById(id);
    }

    @PutMapping("{id}")
    public Vehicule update(@PathVariable("id") Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        vehiculeService.deleteById(id);
    }

    // Affecter : PUT /api/vehicules/{idVehicule}/agences/{idAgence}
    @PutMapping("{idVehicule}/agences/{idAgence}")
    public Vehicule assignToAgence(@PathVariable("idVehicule") Long idVehicule, @PathVariable("idAgence") Long idAgence) {
        return vehiculeService.assignToAgence(idVehicule, idAgence);
    }

    @PutMapping("/{idVehicule}/equipements/{idEquipement}")
    public Vehicule assignToEquipement(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        return vehiculeService.assignToEquipement(idVehicule, idEquipement);
    }

    // Desaffecter : PUT /api/vehicules/unassignToAgence/{idVehicule}/{idAgence}
    @PutMapping("unassignToAgence/{idVehicule}/{idAgence}")
    public Vehicule unassignToAgence(@PathVariable("idVehicule") Long idVehicule, @PathVariable("idAgence") Long idAgence) {
        return vehiculeService.unassignToAgence(idVehicule, idAgence);
    }

    @PutMapping("unassignToEquipement/{idVehicule}/{idEquipement}")
    public Vehicule unassignToEquipement(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        return vehiculeService.unassignToEquipement(idVehicule, idEquipement);
    }

    @GetMapping("listbycategorie/{categorie}")
    public List<Vehicule> afficherVehicules(@PathVariable CategorieVehicule categorie) {
        return vehiculeService.getAllBycategorie(categorie);
    }

    // Corrige par rapport au projet type : le chemin declarait {tarifJournalier} alors que la methode attend min et max
    @GetMapping("listbycategorieAndtarif/{categorie}/{min}/{max}")
    public List<Vehicule> afficherVehiculesPartarif(@PathVariable CategorieVehicule categorie, @PathVariable BigDecimal min, @PathVariable BigDecimal max) {
        return vehiculeService.getAllBycategorieAndtarif(categorie, min, max);
    }
}