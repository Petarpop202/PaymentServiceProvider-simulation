package org.example.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.example.dto.CardPaymentRequest;
import org.example.dto.CardPaymentResponse;
import org.example.dto.PspPaymentRequest;
import org.example.dto.PspPaymentResponse;
import org.example.exception.BadRequestException;
import org.example.exception.ErrorException;
import org.example.exception.NotFoundException;
import org.example.model.Agency;
import org.example.model.BankAccount;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IAgencyRepository;
import org.example.repository.IBankAccountRepository;
import org.example.repository.IPaymentRepository;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcquirerService {

    private final IBankAccountRepository bankAccountRepository;

    private final IPaymentRepository paymentRepository;

    private final InBankPaymentService inBankPaymentService;

    private final TransactionDetailsService transactionDetailsService;

    private final IAgencyRepository agencyRepository;

    private static final String CARD_DETAILS_URL = "http://localhost:8000/bank/card-details/";

    private static final int PAYMENT_DURATION_MINUTES = 10;

    public PspPaymentResponse createPspPaymentResponse(PspPaymentRequest cardPaymentRequestDto) throws Exception {
        Agency agency = agencyRepository.findAgencyByMerchantId(cardPaymentRequestDto.getMerchantId())
                .orElseThrow(()-> new NotFoundException("Agency not found!"));

        // todo: Da li moze provera sa equals jer se porede hashovane lozinke
        if(!agency.getMerchantPassword().equals(cardPaymentRequestDto.getMerchantPassword()))
            throw new BadRequestException("Invalid parameters!");

        return generatePaymentIdAndUrl(cardPaymentRequestDto);
    }
    // todo: URL-ovi zakucane gluposti
    private PspPaymentResponse generatePaymentIdAndUrl(PspPaymentRequest cardPaymentRequestDto) throws MalformedURLException {

        Payment payment = new Payment();
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
        return new PspPaymentResponse(payment.getId(),paymentUrl);
    }

    public CardPaymentResponse cardPayment(CardPaymentRequest cardPaymentRequestDto){

        Payment payment = paymentRepository.findById(cardPaymentRequestDto.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment doesn't exist!"));

        validatePayment(payment);

        // todo: Treba prvo provera da li je kartica iz ove banke

        inBankPaymentService.validateCreditCard(cardPaymentRequestDto,payment);
        inBankPaymentService.doCardPayment(payment, cardPaymentRequestDto);

        return new CardPaymentResponse(payment.getSuccessUrl().toString());
    }

    private void validatePayment(Payment payment) {
        if(payment.getStatus() != PaymentStatus.IN_PROGRESS)
            transactionDetailsService.errorPayment(payment, "Payment is already done!");

        if(payment.getValidUntil().isBefore(LocalDateTime.now()))
            transactionDetailsService.errorPayment(payment, "Payment link is expired!");
    }
}
