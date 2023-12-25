package org.example.controller;

import org.example.dto.InvoiceCallbackData;
import org.example.dto.PaymentData;
import org.example.model.CryptoPayment;
import org.example.service.CoinGateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/coin-gate")
public class CoinGateController {

    @Autowired
    private CoinGateService coinGateService;

    @PostMapping(value = "/create-payment")
    public ResponseEntity<Object> createPayment(@RequestBody PaymentData paymentData) {
        return new ResponseEntity<>(coinGateService.createPayment(paymentData), HttpStatus.OK);
    }

    @PostMapping(value = "/change-status")
    public ResponseEntity<CryptoPayment> changeOrderStatus(@RequestBody InvoiceCallbackData invoiceCallbackData) {
        return new ResponseEntity<>(coinGateService.changePaymentStatus(invoiceCallbackData), HttpStatus.OK);
    }
}
