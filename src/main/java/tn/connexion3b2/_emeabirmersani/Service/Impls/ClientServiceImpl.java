package tn.connexion3b2._emeabirmersani.Service.Impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.connexion3b2._emeabirmersani.Domain.Client;
import tn.connexion3b2._emeabirmersani.Repository.IClientRepository;
import tn.connexion3b2._emeabirmersani.Service.IClientService;
import tn.connexion3b2._emeabirmersani.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        verifier(client);
        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        Client existant = findById(id);
        verifier(client);
        // recopie des attributs modifiables, sans toucher à l'identifiant ni aux associations
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());
        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    // Règle de gestion : appelée à la création et à la modification
    private void verifier(Client client) {
        if (estVide(client.getNom())) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        if (estVide(client.getEmail())) {
            throw new IllegalArgumentException("L'e-mail du client est obligatoire");
        }
    }

    private static boolean estVide(String valeur) {
        return valeur == null || valeur.isBlank();
    }
}
