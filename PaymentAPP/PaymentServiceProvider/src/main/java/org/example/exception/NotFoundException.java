package org.example.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.CardPaymentService;

@SuppressWarnings("serial")

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){super(message);}
}
