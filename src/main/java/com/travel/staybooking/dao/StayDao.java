package com.travel.staybooking.dao;
import com.travel.staybooking.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.travel.staybooking.entity.User;
import java.util.List;

@Repository
public interface StayDao extends JpaRepository<Stay, Long>{
    List<Stay> findByHost(User user);
    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);
}
