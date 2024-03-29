package org.example.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.example.dto.*;
import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.Agency;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IAgencyBankCardPrefixesRepository;
import org.example.repository.IAgencyRepository;
import org.example.repository.IBankAccountRepository;
import org.example.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AcquirerService {

    private final IPaymentRepository paymentRepository;

    private final InBankPaymentService inBankPaymentService;

    private final TransactionDetailsService transactionDetailsService;

    private final IAgencyBankCardPrefixesRepository agencyBankCardPrefixesRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CARD_DETAILS_URL = "https://localhost:4000/payment/credit-card/";

    private final IAgencyRepository agencyRepository;

    private static final int PAYMENT_DURATION_MINUTES = 10;

    public PspPaymentResponse createPspPaymentResponse(PspPaymentRequest cardPaymentRequestDto) throws Exception {
        Agency agency = agencyRepository.findAgencyByMerchantId(cardPaymentRequestDto.getMerchantId())
                .orElseThrow(() -> new NotFoundException("Agency not found!"));

        Optional<Payment> optionalPayment = paymentRepository.findById(cardPaymentRequestDto.getPaymentId());
        if(optionalPayment.isPresent())
            throw new BadRequestException("Payment already done done!");

        if (!agency.getMerchantPassword().equals(cardPaymentRequestDto.getMerchantPassword()))
            throw new BadRequestException("Invalid parameters!");

        return generatePaymentIdAndUrl(cardPaymentRequestDto);
    }

    private PspPaymentResponse generatePaymentIdAndUrl(PspPaymentRequest cardPaymentRequestDto)
            throws MalformedURLException {

        Payment payment = new Payment();
        payment.setId(cardPaymentRequestDto.getPaymentId());
        payment.setMerchantId(cardPaymentRequestDto.getMerchantId());
        payment.setStatus(PaymentStatus.IN_PROGRESS);
        payment.setAmount(cardPaymentRequestDto.getAmount());
        payment.setTimestamp(cardPaymentRequestDto.getTimeStamp());
        payment.setValidUntil(cardPaymentRequestDto.getTimeStamp().plusMinutes(PAYMENT_DURATION_MINUTES));
        payment.setMerchantOrderId(cardPaymentRequestDto.getMerchantOrderId());
        payment.setSuccessUrl(cardPaymentRequestDto.getSuccessUrl());
        payment.setErrorUrl(cardPaymentRequestDto.getErrorUrl());
        payment.setFailedUrl(cardPaymentRequestDto.getFailedUrl());

        paymentRepository.save(payment);

        URL paymentUrl = new URL(CARD_DETAILS_URL + payment.getId());
        return new PspPaymentResponse(payment.getId(), paymentUrl);
    }

    public CardPaymentResponse cardPayment(CardPaymentRequest cardPaymentRequestDto) {

        Payment payment = paymentRepository.findById(cardPaymentRequestDto.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment doesn't exist!"));

        validatePayment(payment);
        String checkNumbers = cardPaymentRequestDto.getPan().substring(0,6);
        if(agencyBankCardPrefixesRepository.findAgencyBankCardPrefixesByPrefix(checkNumbers).isEmpty())
            createDifferentBankPayment(cardPaymentRequestDto,payment);
        else {
            inBankPaymentService.validateCreditCard(cardPaymentRequestDto, payment);
            inBankPaymentService.doCardPayment(payment, cardPaymentRequestDto);
        }
        return new CardPaymentResponse(payment.getSuccessUrl().toString());
    }

    private void createDifferentBankPayment(CardPaymentRequest cardPaymentRequest, Payment payment) {
        PccPaymentRequest paymentRequest = new PccPaymentRequest(cardPaymentRequest);
        paymentRequest.setAcquirerId(generateAcquirerPaymentOrderId());
        Date acquirerTimestamp = new Date();
        paymentRequest.setAcquirerTimestamp(acquirerTimestamp);
        paymentRequest.setPaymentPrice(payment.getAmount());

        final String uri = "https://localhost:9006/api/pcc/different-bank";
        ResponseEntity<SuccessfulPaymentData> responseEntity = restTemplate.postForEntity(
                uri,
                paymentRequest,
                SuccessfulPaymentData.class );

        SuccessfulPaymentData responseDto = responseEntity.getBody();
        if(responseDto == null)
            throw new NotFoundException("Not found acc!");
        differentBankPaymentDetails(responseDto, payment);
    }

    private void differentBankPaymentDetails(SuccessfulPaymentData responseDto, Payment payment) {
        payment.setIssuerOrderId(responseDto.getIssuerOrderId());
        payment.setIssuerTimestamp(responseDto.getIssuerOrderTimestamp());
        if(responseDto.getStatus() == PaymentStatus.SUCCESS)
            transactionDetailsService.differentBankSuccessPayment(payment);
        if(responseDto.getStatus() == PaymentStatus.FAILED)
            transactionDetailsService.failedPayment(payment,"");
        if(responseDto.getStatus() == PaymentStatus.ERROR)
            transactionDetailsService.errorPayment(payment,"");
    }

    private void validatePayment(Payment payment) {
        if (payment.getStatus() != PaymentStatus.IN_PROGRESS)
            transactionDetailsService.errorPayment(payment, "Payment is already done!");

        if (payment.getValidUntil().isBefore(LocalDateTime.now()))
            transactionDetailsService.errorPayment(payment, "Payment link is expired!");
    }

    private String generateAcquirerPaymentOrderId() {
            long issuerOrderId = new Random().nextLong(1000000000L, 10000000000L);
            return String.valueOf(issuerOrderId);
    }

    public CardPaymentResponse ipsPayment(IpsPaymentRequest ipsPaymentRequest) {
        Payment payment = paymentRepository.findById(ipsPaymentRequest.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment doesn't exist!"));

        validatePayment(payment);
        inBankPaymentService.doIpsPayment(payment);

        return new CardPaymentResponse(payment.getSuccessUrl().toString());
    }
}
