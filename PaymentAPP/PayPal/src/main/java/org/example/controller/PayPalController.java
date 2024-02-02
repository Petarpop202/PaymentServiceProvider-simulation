package org.example.controller;

import org.example.dto.PayPalRequest;
import org.example.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/pay-pal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping(value = "/create-payment")
    public String createPayment(@RequestBody PayPalRequest payPalRequest) throws IOException {
        return payPalService.createPayment(payPalRequest.getAmount());
    }

    @PostMapping(value = "/execute-payment")
    public ResponseEntity<?> executePayment(@RequestParam("token") String token) throws IOException {
        System.out.println(token);
        String message = payPalService.executePayment(token);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
