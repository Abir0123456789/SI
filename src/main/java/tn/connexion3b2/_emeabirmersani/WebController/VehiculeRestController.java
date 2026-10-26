package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.StatutVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;
import tn.connexion3b2._emeabirmersani.Service.IVehiculeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeRestController {

    private final IVehiculeService vehiculeService;

    @GetMapping("all")
    public List<Vehicule> getAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("get/{id}")
    public Vehicule getById(@PathVariable("id") long id) {
        return vehiculeService.getById(id);
    }

    @PostMapping("add")
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.add(vehicule);
    }

    @PutMapping("update")
    public Vehicule update(@RequestBody Vehicule vehicule) {
        return vehiculeService.update(vehicule);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        vehiculeService.deleteById(id);
    }

    // ---------- métier ----------

    @GetMapping("statut/{statut}")
    public List<Vehicule> getByStatut(@PathVariable("statut") StatutVehicule statut) {
        return vehiculeService.findByStatut(statut);
    }

    @GetMapping("categorie/{categorie}")
    public List<Vehicule> getByCategorie(@PathVariable("categorie") CategorieVehicule categorie) {
        return vehiculeService.findByCategorie(categorie);
    }

    @GetMapping("agence/{idAgence}")
    public List<Vehicule> getByAgence(@PathVariable("idAgence") long idAgence) {
        return vehiculeService.findByAgence(idAgence);
    }

    // ex : GET /vehicules/tarif?min=50&max=120
    @GetMapping("tarif")
    public List<Vehicule> getByTarif(@RequestParam("min") BigDecimal min,
                                     @RequestParam("max") BigDecimal max) {
        return vehiculeService.findByTarif(min, max);
    }

    // ex : GET /vehicules/disponibles?dateDebut=2026-11-01&dateFin=2026-11-05
    @GetMapping("disponibles")
    public List<Vehicule> getDisponibles(
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return vehiculeService.findDisponibles(dateDebut, dateFin);
    }

    @PutMapping("affecter-agence/{idVehicule}/{idAgence}")
    public Vehicule affecterAgence(@PathVariable("idVehicule") long idVehicule,
                                   @PathVariable("idAgence") long idAgence) {
        return vehiculeService.affecterAgence(idVehicule, idAgence);
    }

    @PutMapping("ajouter-equipement/{idVehicule}/{idEquipement}")
    public Vehicule ajouterEquipement(@PathVariable("idVehicule") long idVehicule,
                                      @PathVariable("idEquipement") long idEquipement) {
        return vehiculeService.ajouterEquipement(idVehicule, idEquipement);
    }

    @PutMapping("retirer-equipement/{idVehicule}/{idEquipement}")
    public Vehicule retirerEquipement(@PathVariable("idVehicule") long idVehicule,
                                      @PathVariable("idEquipement") long idEquipement) {
        return vehiculeService.retirerEquipement(idVehicule, idEquipement);
    }
}
