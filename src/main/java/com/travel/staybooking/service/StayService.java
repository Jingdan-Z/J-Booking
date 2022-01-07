package com.travel.staybooking.service;

import com.travel.staybooking.dao.LocationDao;
import com.travel.staybooking.exception.GeoEncodingException;
import org.springframework.stereotype.Service;
import com.travel.staybooking.dao.StayDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.travel.staybooking.entity.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.travel.staybooking.entity.User;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StayService {
    private StayDao stayRepository;
    private ImageStorageService imageStorageService;
    private LocationDao locationRepository;
    private GeoService geoService;
    @Autowired
    public StayService(StayDao stayRepository, ImageStorageService imageStorageService,LocationDao locationRepository, GeoService geoService ) {
        this.stayRepository = stayRepository;
        this.imageStorageService = imageStorageService;
        this.geoService = geoService;
        this.locationRepository = locationRepository;

    }
    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public Stay findByIdAndHost(Long stayId) {
        return stayRepository.findById(stayId).orElse(null);
    }

    public void add(Stay stay, MultipartFile[] images) {
        LocalDate date = LocalDate.now().plusDays(1);
        //set the stay as available for 30 days
        List<StayAvailability> availabilities = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            availabilities.add(new StayAvailability.Builder().setId(new StayAvailabilityKey(stay.getId(), date)).setStay(stay).setState(StayAvailabilityState.AVAILABLE).build());
            date = date.plusDays(1);
        }
        stay.setAvailabilities(availabilities);
        List<String> mediaLinks = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);

        stayRepository.save(stay);
        //save location to elastic search
        Location location = geoService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);

    }




    public void delete(Long stayId) {
        stayRepository.deleteById(stayId);
    }

}
