package com.hotel.app.exception.exceptions;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException (String message){
        super(message);
    }
}
