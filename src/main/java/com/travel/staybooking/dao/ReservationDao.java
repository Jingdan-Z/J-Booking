package com.travel.staybooking.dao;

import com.travel.staybooking.entity.Reservation;
import com.travel.staybooking.entity.Stay;
import com.travel.staybooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Long> {
    /***
     * delete and add supported by jp
    below ones are the ones cannot use JPARepo
    ***/
    // why not use guest_id here
    // in reservation class the property is guest should be the same
    List<Reservation> findByGuest(User guest);
    List<Reservation> findByStay(Stay stay);
    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);
}
