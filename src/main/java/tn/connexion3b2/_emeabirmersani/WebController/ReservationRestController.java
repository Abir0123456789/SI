package tn.connexion3b2._emeabirmersani.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.connexion3b2._emeabirmersani.Domain.Reservation;
import tn.connexion3b2._emeabirmersani.Domain.StatutReservation;
import tn.connexion3b2._emeabirmersani.Service.IReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationRestController {

    private final IReservationService reservationService;

    @GetMapping("all")
    public List<Reservation> getAll() {
        return reservationService.findAll();
    }

    @GetMapping("get/{id}")
    public Reservation getById(@PathVariable("id") long id) {
        return reservationService.getById(id);
    }

    @PostMapping("add")
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.add(reservation);
    }

    @PutMapping("update")
    public Reservation update(@RequestBody Reservation reservation) {
        return reservationService.update(reservation);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") long id) {
        reservationService.deleteById(id);
    }

    // ---------- métier ----------

    // ex : POST /reservations/reserver/1/2?dateDebut=2026-11-01&dateFin=2026-11-05
    @PostMapping("reserver/{idClient}/{idVehicule}")
    public Reservation reserver(@PathVariable("idClient") long idClient,
                                @PathVariable("idVehicule") long idVehicule,
                                @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                                @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return reservationService.reserver(idClient, idVehicule, dateDebut, dateFin);
    }

    @PutMapping("confirmer/{id}")
    public Reservation confirmer(@PathVariable("id") long id) {
        return reservationService.confirmer(id);
    }

    @PutMapping("annuler/{id}")
    public Reservation annuler(@PathVariable("id") long id) {
        return reservationService.annuler(id);
    }

    @PutMapping("terminer/{id}")
    public Reservation terminer(@PathVariable("id") long id) {
        return reservationService.terminer(id);
    }

    @GetMapping("client/{idClient}")
    public List<Reservation> getByClient(@PathVariable("idClient") long idClient) {
        return reservationService.findByClient(idClient);
    }

    @GetMapping("vehicule/{idVehicule}")
    public List<Reservation> getByVehicule(@PathVariable("idVehicule") long idVehicule) {
        return reservationService.findByVehicule(idVehicule);
    }

    @GetMapping("statut/{statut}")
    public List<Reservation> getByStatut(@PathVariable("statut") StatutReservation statut) {
        return reservationService.findByStatut(statut);
    }
}
