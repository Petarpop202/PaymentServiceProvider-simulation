package org.example.controller;

import org.example.dto.CardPaymentRequestIssuer;
import org.example.dto.SuccessfulPaymentData;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/issuer")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/card-payment")
    public ResponseEntity<SuccessfulPaymentData> checkPaymentFromIssuerBank(@RequestBody CardPaymentRequestIssuer cardPaymentRequestIssuer) {
        SuccessfulPaymentData paymentData = paymentService.checkPaymentForIssuerBank(cardPaymentRequestIssuer);
        return new ResponseEntity<>(paymentData, HttpStatus.OK);
    }
}
