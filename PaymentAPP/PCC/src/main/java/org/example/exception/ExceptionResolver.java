package org.example.exception;

import org.example.dto.PaymentStatus;
import org.example.dto.SuccessfulPaymentData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResolver {


    @ExceptionHandler(InvalidCardNumberException.class)
    public ResponseEntity<?> notFoundException(InvalidCardNumberException exception){
        SuccessfulPaymentData successfulPaymentData = new SuccessfulPaymentData();
        successfulPaymentData.setStatus(PaymentStatus.ERROR);
        return new ResponseEntity<>(successfulPaymentData, HttpStatus.OK);
    }

    @ExceptionHandler(RequestExpiredException.class)
    public ResponseEntity<?> requestExpiredException(RequestExpiredException exception){
        SuccessfulPaymentData successfulPaymentData = new SuccessfulPaymentData();
        successfulPaymentData.setStatus(PaymentStatus.ERROR);
        return new ResponseEntity<>(successfulPaymentData, HttpStatus.NOT_FOUND);
    }

}
