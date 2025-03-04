package com.hotel.app.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {
    @NotBlank(message = "Chek in time must be entered!")
    private String checkIn;

    private String checkOut;
}
