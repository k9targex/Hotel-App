package com.hotel.app.model.dto;

import com.hotel.app.model.Address;
import com.hotel.app.model.ArrivalTime;
import com.hotel.app.model.Contacts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelCreateDto {
    @NotBlank(message = "Name can not be null!")
    private String name;

    private String description;

    @NotBlank(message = "Brand can not be null!")
    private String brand;

    @Valid
    @NotNull(message = "Address must be entered!")
    private Address address;

    @Valid
    @NotNull(message = "Contacts must be entered!")
    private Contacts contacts;

    @Valid
    @NotNull(message = "Check in time must be entered")
    private ArrivalTime arrivalTime;
}
