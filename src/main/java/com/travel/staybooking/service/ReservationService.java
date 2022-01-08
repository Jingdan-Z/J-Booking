package com.travel.staybooking.service;

import com.travel.staybooking.dao.ReservationDao;
import com.travel.staybooking.dao.StayAvailabilityRepository;
import com.travel.staybooking.entity.Reservation;
import com.travel.staybooking.entity.Stay;
import com.travel.staybooking.entity.User;
import com.travel.staybooking.exception.ReservationCollisionException;
import com.travel.staybooking.exception.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private StayAvailabilityRepository stayAvailabilityRepository;
@Autowired
    public ReservationService (ReservationDao reservationDao, StayAvailabilityRepository stayAvailabilityRepository) {
        this.reservationDao = reservationDao;
        this.stayAvailabilityRepository = stayAvailabilityRepository;
    }
    public List<Reservation> listByGuest(String username) {
        return reservationDao.findByGuest(new User.Builder().setUsername(username).build());
    }

    public List<Reservation> listByStay(Long stayId) {
        return reservationDao.findByStay(new Stay.Builder().setId(stayId).build());
    }
    /*
    to keep all processes in this transaction to be unity
    method 1 can use session factory
    method 2 can use this annotaion transactional
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Reservation reservation) throws ReservationCollisionException {
        /*
         * check availability of the given reservation
         * if not =>throw exception
         */
        List<LocalDate> dates = stayAvailabilityRepository.countByDateBetweenAndId(reservation.getStay().getId(), reservation.getCheckinDate(), reservation.getCheckoutDate().minusDays(1));
        int duration = (int) Duration.between(reservation.getCheckinDate().atStartOfDay(), reservation.getCheckoutDate().atStartOfDay()).toDays();
        if (duration > dates.size()) {
            throw new ReservationCollisionException("This is a Duplicate reservation!");
        }
        //update the stay availability
        stayAvailabilityRepository.reserveByDateBetweenAndId(reservation.getStay().getId(), reservation.getCheckinDate(), reservation.getCheckoutDate().minusDays(1));
        reservationDao.save(reservation);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException("Reservation is not available"));
        stayAvailabilityRepository.cancelByDateBetweenAndId(reservation.getStay().getId(), reservation.getCheckinDate(), reservation.getCheckoutDate().minusDays(1));
        reservationDao.deleteById(reservationId);
    }



}
