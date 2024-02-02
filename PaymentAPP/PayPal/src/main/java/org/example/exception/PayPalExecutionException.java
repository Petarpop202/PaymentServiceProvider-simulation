package org.example.exception;

public class PayPalExecutionException extends RuntimeException{

    public PayPalExecutionException(String message) {
        super(message);
    }
}
