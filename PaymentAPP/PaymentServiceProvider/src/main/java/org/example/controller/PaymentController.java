package org.example.controller;

import org.example.dto.*;
import org.example.exception.NotFoundException;
import org.example.service.CardPaymentService;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {
    @Autowired
    private RestTemplate restTemplate;

    private final CardPaymentService cardPaymentService;

    @Autowired
    private PaymentService paymentService;

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

    @PostMapping(value = "/pay-pal-create")
    public String createPayPalOrder(@RequestBody PayPalRequest payPalRequest) {
        float paymentAmount = paymentService.getPaymentAmount(payPalRequest.getPaymentId());
        PayPalAmount payPalAmount = new PayPalAmount(paymentAmount);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:9004/api/pay-pal/create-payment", payPalAmount, String.class);
        return responseEntity.getBody();
    }

    @PostMapping(value = "/pay-pal-execute")
    public ResponseEntity<?> executePayPalOrder(@RequestParam("token") String token) {
        System.out.println(token);
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("token", token);
        return restTemplate.postForEntity("http://localhost:9004/api/pay-pal/execute-payment?token=" + token, null, String.class);
    }
}
