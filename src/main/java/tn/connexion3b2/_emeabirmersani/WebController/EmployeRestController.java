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
import tn.connexion3b2._emeabirmersani.Domain.Employe;
import tn.connexion3b2._emeabirmersani.Service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("employes")
@RequiredArgsConstructor
public class EmployeRestController {

    private final IEmployeService employeService;

    @PostMapping("add")
    public Employe create(@RequestBody Employe employe) {
        return employeService.create(employe);
    }

    @GetMapping
    public List<Employe> findAll() {
        return employeService.findAll();
    }

    @GetMapping("{id}")
    public Employe findById(@PathVariable("id") Long id) {
        return employeService.findById(id);
    }

    @PutMapping("{id}")
    public Employe update(@PathVariable("id") Long id, @RequestBody Employe employe) {
        return employeService.update(id, employe);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        employeService.deleteById(id);
    }
}
