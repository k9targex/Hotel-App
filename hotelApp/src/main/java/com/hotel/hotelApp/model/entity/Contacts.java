package com.hotel.hotelApp.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Contacts {
    private String phone;
    private String email;
}
