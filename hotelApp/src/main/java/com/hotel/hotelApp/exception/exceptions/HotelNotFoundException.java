package com.hotel.hotelApp.exception.exceptions;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException (String message){
        super(message);
    }
}
