package org.example.controller;

import org.example.dto.CardPaymentRequestDto;
import org.example.dto.CardPaymentResponseDto;
import org.example.dto.PaymentRequestFromClientDto;
import org.example.service.CardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CardPaymentService cardPaymentService;

    @PostMapping("/cardPayment")
    public ResponseEntity<?> cardPaymentRequest(@RequestBody PaymentRequestFromClientDto paymentRequestDto) throws Exception {
        final String uri = "http://localhost:9000/api/aquirer/psp-payment-response";
        CardPaymentRequestDto cardPaymentRequestDto = cardPaymentService.createCardPaymentRequest(paymentRequestDto);
        if(cardPaymentRequestDto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found");

        ResponseEntity<CardPaymentResponseDto> responseEntity = restTemplate.postForEntity(
                uri,
                cardPaymentRequestDto,
                CardPaymentResponseDto.class );
        CardPaymentResponseDto responseDto = responseEntity.getBody();

        if(responseDto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account not found");
        return ResponseEntity.ok(responseDto);

    }
}
