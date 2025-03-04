package com.hotel.app.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HotelDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;

}
