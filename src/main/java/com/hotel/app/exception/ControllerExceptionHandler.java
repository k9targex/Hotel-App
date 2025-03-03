package com.hotel.app.exception;


import com.hotel.app.exception.exceptions.HotelNotFoundException;
import com.hotel.app.exception.exceptions.IncorrectParameter;
import com.hotel.app.model.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            HotelNotFoundException.class,
            HttpClientErrorException.NotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleHotelNotFoundException(RuntimeException ex) {
        return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseError handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }


    /** Обработчик исключения MissingServletRequestParameterException. */
    @ExceptionHandler({MissingServletRequestParameterException.class, ResponseStatusException.class,IncorrectParameter.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        return new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }



}
