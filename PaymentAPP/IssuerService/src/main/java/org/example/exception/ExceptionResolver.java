package org.example.exception;

import org.example.dto.SuccessfulPaymentData;
import org.example.model.PaymentStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResolver extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(BadRequestException exception){
        SuccessfulPaymentData successfulPaymentData = new SuccessfulPaymentData();
        successfulPaymentData.setStatus(PaymentStatus.ERROR);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException exception){
        SuccessfulPaymentData successfulPaymentData = new SuccessfulPaymentData();
        successfulPaymentData.setStatus(PaymentStatus.ERROR);
        return new ResponseEntity<>(successfulPaymentData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughMoneyOnAccount.class)
    public ResponseEntity<?> notEnoughMoneyException(NotEnoughMoneyOnAccount exception) {
        SuccessfulPaymentData successfulPaymentData = new SuccessfulPaymentData();
        successfulPaymentData.setStatus(PaymentStatus.FAILED);
        return new ResponseEntity<>(successfulPaymentData, HttpStatus.NOT_FOUND);
    }
}
