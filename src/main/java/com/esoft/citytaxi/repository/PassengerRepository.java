package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.entity.Passenger;
import com.esoft.citytaxi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository  extends JpaRepository<Passenger, Long> {

    Passenger findByUser(User user);

    @Query("select p from Passenger p where p.user.status != ?1")
    List<Passenger> findAllNotStatus(UserStatuses status);

    Optional<Passenger> findByUserUsername(String username);
}
