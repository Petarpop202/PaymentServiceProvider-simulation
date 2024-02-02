package org.example.controller;

import org.example.dto.*;
import org.example.exception.NotFoundException;
import org.example.model.Payment;
import org.example.repository.IPaymentRepository;
import org.example.service.AcquirerService;
import org.example.service.QrCodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestController
@RequestMapping("/api/aquirer")
@CrossOrigin
public class PaymentController {

    private final AcquirerService acquirerService;

    private final QrCodeService qrCodeService;

    private final IPaymentRepository paymentRepository;

    public PaymentController(AcquirerService acquirerService, IPaymentRepository paymentRepository, QrCodeService qrCodeService){this.acquirerService = acquirerService;this.paymentRepository = paymentRepository;this.qrCodeService = qrCodeService;}

    @PostMapping(
            value = "/psp-payment-response",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pspPaymentResponse(@RequestBody PspPaymentRequest cardPaymentRequestDto) throws Exception {
        return ResponseEntity.ok(acquirerService.createPspPaymentResponse(cardPaymentRequestDto));
    }

    @PostMapping(
            value = "/card-payment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> cardPayment(@RequestBody CardPaymentRequest cardPaymentRequestDto) {
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

    @PostMapping (
            value = "/qr-code-generator",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> generateQrCode(@RequestBody QrCodePaymentRequest qrCodeGenerator) {
        byte[] qrCode = qrCodeService.generateQrCode(qrCodeGenerator);
        String qrCodeEncoded = Base64.getEncoder().encodeToString(qrCode);

        return ResponseEntity.ok(qrCodeEncoded);
    }

    @PostMapping (
            value = "/qr-code-validator",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> validateQrCode(@RequestBody IpsPaymentRequest ipsPaymentRequest) {
        CardPaymentResponse cardPaymentResponse = acquirerService.ipsPayment(ipsPaymentRequest);
        return ResponseEntity.ok(cardPaymentResponse);
    }
}
