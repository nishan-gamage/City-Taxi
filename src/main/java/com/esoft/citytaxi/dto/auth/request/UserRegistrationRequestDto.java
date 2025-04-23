package com.esoft.citytaxi.dto.auth.request;

import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserRegistrationRequestDto {
    private String email;
    private String username;
    private String password;
    private String phoneNumber;

    @JsonIgnore
    public User getUser() {
        return User.builder()
                .contactNumber(this.phoneNumber)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .role(UserRole.builder().code(UserRoles.PASSENGER).build())
                .build();
    }

    @JsonIgnore
    public User getUser(UserRoles role) {
        return User.builder()
                .contactNumber(this.phoneNumber)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .role(UserRole.builder().code(role).build())
                .build();
    }
}
