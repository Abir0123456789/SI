package tn.connexion3b2._emeabirmersani.Service;

import tn.connexion3b2._emeabirmersani.Domain.Client;

import java.util.List;

public interface IClientService {
    Client getById(long id);
    Client add(Client client);
    Client update(Client client);
    void deleteById(long id);
    List<Client> findAll();

    // métier
    Client findByEmail(String email);
    List<Client> rechercher(String motCle);
}
