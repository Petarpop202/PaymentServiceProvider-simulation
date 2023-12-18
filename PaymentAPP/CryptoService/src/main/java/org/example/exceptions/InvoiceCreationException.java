package org.example.exceptions;

import lombok.Getter;
import org.example.dto.Invoice;
import org.springframework.http.ResponseEntity;

@Getter
public class InvoiceCreationException extends RuntimeException{

    private ResponseEntity<Invoice> response;

    public InvoiceCreationException(String message, ResponseEntity<Invoice> response) {
        super(message);
    }
}
