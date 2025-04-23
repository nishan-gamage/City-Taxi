package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.entity.Operator;
import com.esoft.citytaxi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {

    Operator findByUser(User user);

    Operator findByUser_Username(String username);

}
