package org.example.exception;

public class RequestExpiredException extends RuntimeException{

    public RequestExpiredException(String message) {
        super(message);
    }
}
