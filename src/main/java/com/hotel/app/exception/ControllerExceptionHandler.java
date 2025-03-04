package com.hotel.app.exception;


import com.hotel.app.exception.exceptions.HotelAlreadyExistsException;
import com.hotel.app.exception.exceptions.HotelNotFoundException;
import com.hotel.app.exception.exceptions.IncorrectParameter;
import com.hotel.app.model.dto.ResponseError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    private static final String ERROR_400 = "Error 400: Bad request - ";
    private static final String ERROR_404 = "Error 404: Not Found - ";
    private static final String ERROR_405 = "Error 405: Method not supported - ";
    private static final String ERROR_409 = "Error 409: Conflict - ";
    private static final String ERROR_500 = "Error 500: Internal server error - ";


    /** Обработчик исключения HotelNotFoundException. */
    @ExceptionHandler({
            HotelNotFoundException.class,
            HttpClientErrorException.NotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Hotel not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    public ResponseError handleHotelNotFoundException(RuntimeException ex) {
        String errorMessage = ERROR_404 + ex.getMessage();
        log.error(errorMessage);
        return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Обработчик исключения HttpRequestMethodNotSupportedException. */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ApiResponse(responseCode = "405", description = "Method not supported",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    public ResponseError handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        String errorMessage = ERROR_405 + ex.getMessage();
        log.error(errorMessage);
        return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }


    /** Обработчик исключения MissingServletRequestParameterException. */
    @ExceptionHandler({MissingServletRequestParameterException.class, ResponseStatusException.class,IncorrectParameter.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    public ResponseError handleIllegalArgumentException(RuntimeException ex) {
        String errorMessage = ERROR_400 + ex.getMessage();
        log.error(errorMessage);
        return new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Обработчик исключения HotelAlreadyExistsException. */
    @ExceptionHandler({HotelAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ApiResponse(responseCode = "409", description = "Hotel already exists",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    public ResponseError handleResponseException(HotelAlreadyExistsException ex) {
        String errorMessage = ERROR_409 + ex.getMessage();
        log.error(errorMessage);
        return new ResponseError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Обработчик исключения RuntimeException. */
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    public ResponseError handleAllExceptions(RuntimeException ex) {
        String errorMessage = ERROR_500 + ex.getMessage();
        log.error(errorMessage);
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
