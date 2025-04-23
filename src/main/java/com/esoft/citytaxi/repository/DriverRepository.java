package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByUser(User user);

    @Query("SELECT d FROM Driver d WHERE LOWER(d.user.username) IN :usernames")
    List<Driver> findAllByUsernamesIgnoreCase(@Param("usernames") List<String> usernames);

    Driver findByUser_Username(String username);

    @Query("select d from Driver d where d.user.status != ?1")
    List<Driver> findAllNotStatus(UserStatuses status);
}
