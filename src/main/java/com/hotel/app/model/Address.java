package com.hotel.app.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotNull(message = "House number must be entered!")
    private Integer houseNumber;
    @NotBlank(message = "Street must be entered!")
    private String street;
    @NotBlank(message = "City must be entered!")
    private String city;
    @NotBlank(message = "Country must be entered!")
    private String country;
    @NotBlank(message = "Post code must be entered!")
    private String postCode;
}