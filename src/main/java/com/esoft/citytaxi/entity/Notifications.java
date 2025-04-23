
package com.esoft.citytaxi.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "Notifications")
public class Notifications extends BaseEntity {

    @Column(name = "notification_type")
    private String notificationType;

    private String content;

    private boolean isSent;

    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
