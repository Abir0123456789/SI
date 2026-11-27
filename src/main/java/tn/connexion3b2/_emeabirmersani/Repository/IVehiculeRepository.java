package tn.connexion3b2._emeabirmersani.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.connexion3b2._emeabirmersani.Domain.CategorieVehicule;
import tn.connexion3b2._emeabirmersani.Domain.Vehicule;

import java.math.BigDecimal;
import java.util.List;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {

    List<Vehicule> findBycategorie(CategorieVehicule categorie);

    List<Vehicule> findAllBycategorieAndTarifJournalierBetween(CategorieVehicule categorie, BigDecimal min, BigDecimal max);
}