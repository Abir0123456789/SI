package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.connexion3b2._emeabirmersani.Domain.Client;
import tn.connexion3b2._emeabirmersani.Repository.IClientRepository;
import tn.connexion3b2._emeabirmersani.Repository.IReservationRepository;
import tn.connexion3b2._emeabirmersani.Service.IClientService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Client getById(long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("client not found"));
    }

    @Override
    public Client add(Client client) {
        if (client.getEmail() != null && clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalStateException("email déjà utilisé : " + client.getEmail());
        }
        if (client.getNumPermis() != null && clientRepository.existsByNumPermis(client.getNumPermis())) {
            throw new IllegalStateException("numéro de permis déjà utilisé : " + client.getNumPermis());
        }
        if (client.getDateInscription() == null) {
            client.setDateInscription(LocalDate.now());
        }
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        if (client.getIdClient() == null || !clientRepository.existsById(client.getIdClient())) {
            throw new EntityNotFoundException("client not found");
        }
        return clientRepository.save(client);
    }

    @Override
    public void deleteById(long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("client not found");
        }
        // pas de cascade REMOVE : l'historique des réservations est conservé
        if (reservationRepository.existsByClientIdClient(id)) {
            throw new IllegalStateException("suppression impossible : le client a des réservations");
        }
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("client not found"));
    }

    @Override
    public List<Client> rechercher(String motCle) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(motCle, motCle);
    }
}
