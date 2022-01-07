package com.travel.staybooking.service;
import com.travel.staybooking.dao.LocationDao;
import com.travel.staybooking.dao.StayAvailabilityRepository;
import com.travel.staybooking.dao.StayDao;
import com.travel.staybooking.entity.Stay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    private StayDao stayRepository;
    private StayAvailabilityRepository stayAvailabilityRepository;
    private LocationDao locationRepository;

    @Autowired
    public SearchService(StayDao stayRepository, StayAvailabilityRepository stayAvailabilityRepository, LocationDao locationRepository) {
        this.stayRepository = stayRepository;
        this.stayAvailabilityRepository = stayAvailabilityRepository;
        this.locationRepository = locationRepository;
    }
    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {
        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        long duration = Duration.between(checkinDate.atStartOfDay(), checkoutDate.atStartOfDay()).toDays();

        List<Long> filteredStayIds = stayAvailabilityRepository.findByDateBetweenAndStateIsAvailable(stayIds, checkinDate, checkoutDate.minusDays(1), duration);
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }

}

