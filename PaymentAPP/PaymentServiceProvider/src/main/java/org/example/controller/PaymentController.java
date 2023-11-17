package org.example.controller;

import org.example.dto.CardPaymentRequest;
import org.example.dto.CardPaymentResponse;
import org.example.dto.PaymentRequestFromClient;
import org.example.dto.TransactionDetails;
import org.example.exception.NotFoundException;
import org.example.service.CardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {
    @Autowired
    private RestTemplate restTemplate;

    private final CardPaymentService cardPaymentService;

    public PaymentController(CardPaymentService cardPaymentService){this.cardPaymentService = cardPaymentService;}


    @PostMapping(
            value= "/card-payment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> cardPaymentRequest(@RequestBody PaymentRequestFromClient paymentRequestDto) throws Exception {
        final String uri = "http://localhost:9000/api/aquirer/psp-payment-response";
        CardPaymentRequest cardPaymentRequestDto = cardPaymentService.createCardPaymentRequest(paymentRequestDto);

        ResponseEntity<CardPaymentResponse> responseEntity = restTemplate.postForEntity(
                uri,
                cardPaymentRequestDto,
                CardPaymentResponse.class );
        CardPaymentResponse responseDto = responseEntity.getBody();

        if(responseDto == null)
            throw new NotFoundException("Bank account not found!");
        cardPaymentService.createPayment(paymentRequestDto,responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(
            value = "/acquirer-bank-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void acquirerBankResponse(@RequestBody TransactionDetails transactionDetails){
        cardPaymentService.finishPayment(transactionDetails);
    }
}
