package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequest;
import org.example.dto.CardPaymentResponse;
import org.example.dto.PaymentRequestFromClient;
import org.example.dto.TransactionDetails;
import org.example.exception.NotFoundException;
import org.example.model.Agency;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IAgencyRepository;
import org.example.repository.IPaymentRepository;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class CardPaymentService {

    private final IAgencyRepository agencyRepository;

    private final IPaymentRepository paymentRepository;

    public CardPaymentRequest createCardPaymentRequest(PaymentRequestFromClient paymentRequestDto) throws Exception {
        Agency agency = agencyRepository.findById(paymentRequestDto.getMerchantOrderId())
                .orElseThrow(() -> new NotFoundException("Agency not found!"));

        return new CardPaymentRequest(
                agency.getMerchantId(),
                agency.getMerchantPassword(),
                paymentRequestDto.getMerchantOrderId(),
                paymentRequestDto.getAmount(),
                paymentRequestDto.getTimeStamp(),
                new URL("http://localhost:4000/success"),
                new URL("http://localhost:4000/failed"),
                new URL("http://localhost:4000/error")
        );
    }

    public void createPayment(PaymentRequestFromClient paymentRequestFromClient, CardPaymentResponse responseDto) {
        Payment payment = new Payment();
        payment.setId(responseDto.getPaymentId());
        payment.setTimestamp(paymentRequestFromClient.getTimeStamp());
        payment.setAmount(paymentRequestFromClient.getAmount());
        payment.setMerchantOrderId(paymentRequestFromClient.getMerchantOrderId());
        payment.setStatus(PaymentStatus.IN_PROGRESS);

        paymentRepository.save(payment);
    }

    public void finishPayment(TransactionDetails transactionDetails) {
        Payment payment = paymentRepository.findById(transactionDetails.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment doesn't exist!"));

        payment.setStatus(transactionDetails.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
