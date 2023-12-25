package org.example.controller;

import org.example.dto.*;
import org.example.exception.NotFoundException;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IPaymentRepository;
import org.example.service.CardPaymentService;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final IPaymentRepository paymentRepository;
    private final CardPaymentService cardPaymentService;

    @Autowired
    private PaymentService paymentService;

    public PaymentController(CardPaymentService cardPaymentService, IPaymentRepository paymentRepository){this.cardPaymentService = cardPaymentService;this.paymentRepository = paymentRepository;}


    @PostMapping(
            value= "/payment-request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('CREDIT CARD', 'QR CODE', 'PAY PAL', 'CRYPTO')")
    public ResponseEntity<?> paymentRequest(@RequestBody PaymentRequestFromClient paymentRequestDto) throws Exception {
        CardPaymentResponse responseDto = cardPaymentService.createPayment(paymentRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(
            value="/card-payment-request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('CREDIT CARD', 'QR CODE')")
    public ResponseEntity<?> cardPaymentRequest(@RequestBody IdCardRequest paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId.getPaymentId())
                .orElseThrow(()-> new NotFoundException("Not found"));
        CardPaymentRequest cardPaymentRequestDto = cardPaymentService.createCardPaymentRequest(payment);
        final String uri = "http://localhost:9010/api/aquirer/psp-payment-response";

        ResponseEntity<CardPaymentResponse> responseEntity = restTemplate.postForEntity(
                uri,
                cardPaymentRequestDto,
                CardPaymentResponse.class );
        CardPaymentResponse responseDto = responseEntity.getBody();

        if(responseDto == null)
            throw new NotFoundException("Bank account not found!");
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(
            value = "/acquirer-bank-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('CREDIT CARD')")
    public void acquirerBankResponse(@RequestBody TransactionDetails transactionDetails){
        cardPaymentService.finishPayment(transactionDetails);
    }

    @PostMapping(value = "/pay-pal-create")
    @PreAuthorize("hasAuthority('PAY PAL')")
    public String createPayPalOrder(@RequestBody PayPalRequest payPalRequest) {
        float paymentAmount = paymentService.getPaymentAmount(payPalRequest.getPaymentId());
        PayPalAmount payPalAmount = new PayPalAmount(paymentAmount);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:9010/api/pay-pal/create-payment", payPalAmount, String.class);
        return responseEntity.getBody();
    }

    @PostMapping(value = "/pay-pal-execute")
    @PreAuthorize("hasAuthority('PAY PAL')")
    public ResponseEntity<?> executePayPalOrder(@RequestParam("token") String token) {
        System.out.println(token);
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("token", token);
        return restTemplate.postForEntity("http://localhost:9010/api/pay-pal/execute-payment?token=" + token, null, String.class);
    }

    @PostMapping(value = "/crypto-payment")
    @PreAuthorize("hasAuthority('CRYPTO')")
    public ResponseEntity<Invoice> createCryptoPayment(@RequestBody PayPalRequest payPalRequest) {
        float paymentAmount = paymentService.getPaymentAmount(payPalRequest.getPaymentId());
        CryptoPaymentData cryptoPaymentData = new CryptoPaymentData(paymentAmount, "USD", "USD", "Title from payment", "http://localhost:4000/success-payment", "http://localhost:9003/api/payment/callback");
        return restTemplate.postForEntity("http://localhost:9010/coin-gate/create-payment", cryptoPaymentData, Invoice.class);
    }

    @PostMapping(value = "/callback")
    public ResponseEntity<?> tryCallback(@RequestBody InvoiceCallbackData invoiceCallbackData) {
        System.out.println("Pogodio sam callback");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
