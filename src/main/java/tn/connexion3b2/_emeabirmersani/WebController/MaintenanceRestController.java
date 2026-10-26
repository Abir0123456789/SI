package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Maintenance;
import tn.connexion3b2._emeabirmersani.Service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceRestController {

    private final IMaintenanceService maintenanceService;

    @GetMapping("all")
    public List<Maintenance> getAll() {
        return maintenanceService.findAll();
    }

    @GetMapping("get/{id}")
    public Maintenance getById(@PathVariable("id") long id) {
        return maintenanceService.getById(id);
    }

    @PostMapping("add")
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return maintenanceService.add(maintenance);
    }

    @PutMapping("update")
    public Maintenance update(@RequestBody Maintenance maintenance) {
        return maintenanceService.update(maintenance);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        maintenanceService.deleteById(id);
    }

    // ---------- métier ----------

    @PostMapping("planifier/{idVehicule}")
    public Maintenance planifier(@PathVariable("idVehicule") long idVehicule,
                                 @RequestBody Maintenance maintenance) {
        return maintenanceService.planifier(idVehicule, maintenance);
    }

    @PutMapping("terminer/{idMaintenance}")
    public Maintenance terminer(@PathVariable("idMaintenance") long idMaintenance) {
        return maintenanceService.terminer(idMaintenance);
    }

    @GetMapping("vehicule/{idVehicule}")
    public List<Maintenance> getByVehicule(@PathVariable("idVehicule") long idVehicule) {
        return maintenanceService.findByVehicule(idVehicule);
    }

    @GetMapping("en-cours")
    public List<Maintenance> getEnCours() {
        return maintenanceService.findEnCours();
    }
}
