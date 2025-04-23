
package com.esoft.citytaxi.entity;

import com.esoft.citytaxi.constant.UserStatuses;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User extends BaseEntity{

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatuses status;

    @Column(name = "contact_number")
    private String contactNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UserRole role;

}
