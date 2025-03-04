package com.hotel.app.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import jakarta.validation.constraints.Pattern;
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contacts {
    @NotBlank(message = "Phone must be entered!")
    private String phone;

    @NotBlank(message = "Email must be entered!")
    private String email;
}
