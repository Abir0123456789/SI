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
import tn.connexion3b2._emeabirmersani.Domain.Maintenance;
import tn.connexion3b2._emeabirmersani.Service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("maintenances")
@RequiredArgsConstructor
public class MaintenanceRestController {

    private final IMaintenanceService maintenanceService;

    @PostMapping("add")
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return maintenanceService.create(maintenance);
    }

    @GetMapping
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @GetMapping("{id}")
    public Maintenance findById(@PathVariable("id") Long id) {
        return maintenanceService.findById(id);
    }

    @PutMapping("{id}")
    public Maintenance update(@PathVariable("id") Long id, @RequestBody Maintenance maintenance) {
        return maintenanceService.update(id, maintenance);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        maintenanceService.deleteById(id);
    }
}
