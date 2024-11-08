package tn.esprit.tpfoyer.service;


import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private static final Logger logger = LogManager.getLogger(ReservationServiceImpl.class);

    ReservationRepository reservationRepository;

    @Override
    public List<Reservation> retrieveAllReservations() {
        logger.info("Retrieving all reservations");
        return reservationRepository.findAll();
    }

    @Override
    public Reservation retrieveReservation(String reservationId) {
        logger.info("Retrieving reservation with ID: {}", reservationId);
        return reservationRepository.findById(reservationId).orElse(null);
    }

    @Override
    public Reservation addReservation(Reservation r) {
        logger.info("Adding new reservation: {}", r);
        Reservation savedReservation = reservationRepository.save(r);
        logger.debug("Added reservation with ID: {}", savedReservation.getIdReservation());
        return savedReservation;
    }

    @Override
    public Reservation modifyReservation(Reservation reservation) {
        logger.info("Modifying reservation with ID: {}", reservation.getIdReservation());
        Reservation updatedReservation = reservationRepository.save(reservation);
        logger.debug("Modified reservation: {}", updatedReservation);
        return updatedReservation;
    }

    @Override
    public void removeReservation(String reservationId) {
        logger.warn("Removing reservation with ID: {}", reservationId);
        reservationRepository.deleteById(reservationId);
        logger.debug("Removed reservation with ID: {}", reservationId);
    }

    @Override
    public List<Reservation> trouverResSelonDateEtStatus(Date d, boolean b) {
        logger.info("Finding reservations before date {} with status {}", d, b);
        return reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(d, b);
    }
}
