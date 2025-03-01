package com.hotel.hotelApp.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class ArrivalTime {
    private String checkIn;
    private String checkOut;
}
