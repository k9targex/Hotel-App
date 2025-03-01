package com.hotel.hotelApp.exception;


import com.hotel.hotelApp.exception.exceptions.HotelNotFoundException;
import com.hotel.hotelApp.model.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            HotelNotFoundException.class,
            HttpClientErrorException.NotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError hotelNotFoundException(RuntimeException ex) {
        return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseError handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }
}
