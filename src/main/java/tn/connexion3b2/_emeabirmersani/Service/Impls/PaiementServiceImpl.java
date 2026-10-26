package tn.connexion3b2._emeabirmersani.Service.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.connexion3b2._emeabirmersani.Domain.Contrat;
import tn.connexion3b2._emeabirmersani.Domain.Paiement;
import tn.connexion3b2._emeabirmersani.Repository.IContratRepository;
import tn.connexion3b2._emeabirmersani.Repository.IPaiementRepository;
import tn.connexion3b2._emeabirmersani.Service.IPaiementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final IContratRepository contratRepository;

    @Override
    public Paiement getById(long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("paiement not found"));
    }

    @Override
    public Paiement add(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement update(Paiement paiement) {
        if (paiement.getIdPaiement() == null || !paiementRepository.existsById(paiement.getIdPaiement())) {
            throw new EntityNotFoundException("paiement not found");
        }
        return paiementRepository.save(paiement);
    }

    @Override
    public void deleteById(long id) {
        if (!paiementRepository.existsById(id)) {
            throw new EntityNotFoundException("paiement not found");
        }
        paiementRepository.deleteById(id);
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    @Transactional
    public Paiement payer(long idContrat, Paiement paiement) {
        Contrat contrat = contratRepository.findById(idContrat)
                .orElseThrow(() -> new EntityNotFoundException("contrat not found"));
        if (!contrat.isValide()) {
            throw new IllegalStateException("le contrat n'est pas valide");
        }
        BigDecimal montant = paiement.getMontant();
        if (montant == null || montant.signum() <= 0) {
            throw new IllegalArgumentException("le montant doit être strictement positif");
        }
        if (paiement.getModePaiement() == null) {
            throw new IllegalArgumentException("le mode de paiement est obligatoire");
        }
        BigDecimal reste = resteAPayer(idContrat);
        if (montant.compareTo(reste) > 0) {
            throw new IllegalStateException("montant supérieur au reste à payer (" + reste + ")");
        }

        paiement.setIdPaiement(null);
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }
        contrat.addPaiement(paiement); // synchronise les deux côtés de l'association
        return paiementRepository.save(paiement);
    }

    @Override
    public List<Paiement> findByContrat(long idContrat) {
        return paiementRepository.findByContratIdContrat(idContrat);
    }

    @Override
    public BigDecimal resteAPayer(long idContrat) {
        Contrat contrat = contratRepository.findById(idContrat)
                .orElseThrow(() -> new EntityNotFoundException("contrat not found"));
        BigDecimal total = contrat.getMontantTotal() != null ? contrat.getMontantTotal() : BigDecimal.ZERO;
        BigDecimal paye = paiementRepository.totalPayeParContrat(idContrat);
        if (paye == null) {
            paye = BigDecimal.ZERO;
        }
        return total.subtract(paye);
    }
}
