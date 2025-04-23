package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String roleName);

    Optional<UserRole> findByCode(UserRoles code);

}
