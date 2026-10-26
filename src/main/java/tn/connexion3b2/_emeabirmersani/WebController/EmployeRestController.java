package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Employe;
import tn.connexion3b2._emeabirmersani.Domain.RoleEmploye;
import tn.connexion3b2._emeabirmersani.Service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeRestController {

    private final IEmployeService employeService;

    @GetMapping("all")
    public List<Employe> getAll() {
        return employeService.findAll();
    }

    @GetMapping("get/{id}")
    public Employe getById(@PathVariable("id") long id) {
        return employeService.getById(id);
    }

    @PostMapping("add")
    public Employe create(@RequestBody Employe employe) {
        return employeService.add(employe);
    }

    @PutMapping("update")
    public Employe update(@RequestBody Employe employe) {
        return employeService.update(employe);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        employeService.deleteById(id);
    }

    // ---------- métier ----------

    @PutMapping("affecter-agence/{idEmploye}/{idAgence}")
    public Employe affecterAgence(@PathVariable("idEmploye") long idEmploye,
                                  @PathVariable("idAgence") long idAgence) {
        return employeService.affecterAgence(idEmploye, idAgence);
    }

    @GetMapping("agence/{idAgence}")
    public List<Employe> getByAgence(@PathVariable("idAgence") long idAgence) {
        return employeService.findByAgence(idAgence);
    }

    @GetMapping("role/{role}")
    public List<Employe> getByRole(@PathVariable("role") RoleEmploye role) {
        return employeService.findByRole(role);
    }
}
