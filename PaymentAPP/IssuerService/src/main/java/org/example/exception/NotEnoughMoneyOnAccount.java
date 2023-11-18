package org.example.exception;

public class NotEnoughMoneyOnAccount extends RuntimeException{

    public NotEnoughMoneyOnAccount(String message) {
        super(message);
    }
}
