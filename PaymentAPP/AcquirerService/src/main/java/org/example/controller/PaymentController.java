package org.example.controller;

import org.example.dto.*;
import org.example.exception.NotFoundException;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IPaymentRepository;
import org.example.service.AcquirerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/aquirer")
@CrossOrigin
public class PaymentController {

    private final AcquirerService acquirerService;

    private final IPaymentRepository paymentRepository;

    public PaymentController(AcquirerService acquirerService, IPaymentRepository paymentRepository){this.acquirerService = acquirerService;this.paymentRepository = paymentRepository;}

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


    @PostMapping (value = "/payment-status")
    public ResponseEntity<?> getPaymentStatus(@RequestBody IdCardRequest paymentId){
        Payment payment = paymentRepository.findById(paymentId.getPaymentId())
                .orElseThrow(() -> new NotFoundException("No payment!"));
        PaymentStatusResponse paymentStatusResponse = new PaymentStatusResponse(
                paymentId.getPaymentId(),
                payment.getSuccessUrl(),
                payment.getErrorUrl(),
                payment.getFailedUrl(),
                payment.getStatus());
        return ResponseEntity.ok(paymentStatusResponse);
    }
}
