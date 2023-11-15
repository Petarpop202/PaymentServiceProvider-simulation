package org.example.controller;

import org.example.dto.CardPaymentRequestDto;
import org.example.dto.CardPaymentResponseDto;
import org.example.dto.PspPaymentRequestDto;
import org.example.service.AcquirerService;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/aquirer")
public class PaymentController {
    @Autowired
    private RestTemplate restTemplate;

    private final AcquirerService acquirerService;

    public PaymentController(AcquirerService acquirerService){this.acquirerService = acquirerService;}

    @PostMapping(
            value = "/psp-payment-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pspPaymentResponse(@RequestBody PspPaymentRequestDto cardPaymentRequestDto) throws Exception {
        // todo: Da li da pravimo karticu odvojenu od bankovnog racuna ?
        return ResponseEntity.ok(acquirerService.createPspPaymentResponse(cardPaymentRequestDto));
    }

    @PostMapping(
            value = "/card-payment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> cardPayment(@RequestBody CardPaymentRequestDto cardPaymentRequestDto){
        CardPaymentResponseDto cardPaymentResponseDto = acquirerService.cardPayment(cardPaymentRequestDto);
        return null;
    }
}
