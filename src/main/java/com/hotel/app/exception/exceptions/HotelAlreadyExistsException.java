package com.hotel.app.exception.exceptions;

public class HotelAlreadyExistsException extends RuntimeException {
    public HotelAlreadyExistsException(String message){
        super(message);
    }
}
