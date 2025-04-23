package com.esoft.citytaxi.dto.user;

import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String username;

    private String contactNumber;
    private String email;
    private UserRoles role;
    private UserStatuses status;

    public UserResponseDto(String username, String contactNumber) {
        this.username = username;
        this.contactNumber = contactNumber;
    }

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.contactNumber = user.getContactNumber();
        this.email = user.getEmail();
        this.role = user.getRole().getCode();
        this.status = user.getStatus();
    }

}
