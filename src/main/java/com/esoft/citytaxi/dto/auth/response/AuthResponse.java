package com.esoft.citytaxi.dto.auth.response;

import com.esoft.citytaxi.constant.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String name;
    private UserRoles userRole;
    private String email;
    private String username;
}
