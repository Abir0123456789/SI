package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Client;
import tn.connexion3b2._emeabirmersani.Service.IClientService;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientRestController {

    private final IClientService clientService;

    @GetMapping("all")
    public List<Client> getAll() {
        return clientService.findAll();
    }

    @GetMapping("get/{id}")
    public Client getById(@PathVariable("id") long id) {
        return clientService.getById(id);
    }

    @PostMapping("add")
    public Client create(@RequestBody Client client) {
        return clientService.add(client);
    }

    @PutMapping("update")
    public Client update(@RequestBody Client client) {
        return clientService.update(client);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        clientService.deleteById(id);
    }

    // ---------- métier ----------

    @GetMapping("email")
    public Client getByEmail(@RequestParam("email") String email) {
        return clientService.findByEmail(email);
    }

    @GetMapping("recherche")
    public List<Client> rechercher(@RequestParam("motCle") String motCle) {
        return clientService.rechercher(motCle);
    }
}
