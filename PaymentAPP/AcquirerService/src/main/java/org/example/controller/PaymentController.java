package org.example.controller;

import org.example.dto.CardPaymentRequest;
import org.example.dto.CardPaymentResponse;
import org.example.dto.PspPaymentRequest;
import org.example.service.AcquirerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/aquirer")
public class PaymentController {

    private final AcquirerService acquirerService;

    public PaymentController(AcquirerService acquirerService){this.acquirerService = acquirerService;}

    @PostMapping(
            value = "/psp-payment-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pspPaymentResponse(@RequestBody PspPaymentRequest cardPaymentRequestDto) throws Exception {
        // todo: Da li da pravimo karticu odvojenu od bankovnog racuna ?
        return ResponseEntity.ok(acquirerService.createPspPaymentResponse(cardPaymentRequestDto));
    }

    @PostMapping(
            value = "/card-payment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> cardPayment(@RequestBody CardPaymentRequest cardPaymentRequestDto) throws MalformedURLException {
        CardPaymentResponse cardPaymentResponseDto = acquirerService.cardPayment(cardPaymentRequestDto);
        return ResponseEntity.ok(cardPaymentResponseDto);
    }
}
