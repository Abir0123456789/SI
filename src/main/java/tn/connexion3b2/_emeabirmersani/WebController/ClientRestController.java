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
import tn.connexion3b2._emeabirmersani.Domain.Client;
import tn.connexion3b2._emeabirmersani.Service.IClientService;

import java.util.List;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientRestController {

    private final IClientService clientService;

    @PostMapping("add")
    public Client create(@RequestBody Client client) {
        return clientService.create(client);
    }

    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("{id}")
    public Client findById(@PathVariable("id") Long id) {
        return clientService.findById(id);
    }

    @PutMapping("{id}")
    public Client update(@PathVariable("id") Long id, @RequestBody Client client) {
        return clientService.update(id, client);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        clientService.deleteById(id);
    }
}
