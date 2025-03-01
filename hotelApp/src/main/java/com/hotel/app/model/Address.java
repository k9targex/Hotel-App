package com.hotel.app.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private int houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}