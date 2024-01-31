package org.example.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.PaymentController;
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

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class CardPaymentService {

    private final IAgencyRepository agencyRepository;

    private final IPaymentRepository paymentRepository;
    private static final Logger logger = LogManager.getLogger(CardPaymentService.class);

    private static final String PAYMENT_CHOOSE_URL = "https://localhost:4000/choose-payment/";

    public CardPaymentRequest createCardPaymentRequest(Payment payment) throws Exception {
        Agency agency = agencyRepository.findById(payment.getMerchantOrderId())
                .orElseThrow(() -> new NotFoundException("Agency not found!"));

        return new CardPaymentRequest(
                agency.getMerchantId(),
                agency.getMerchantPassword(),
                payment.getMerchantOrderId(),
                payment.getAmount(),
                payment.getTimestamp(),
                new URL("https://localhost:4000/success-payment"),
                new URL("https://localhost:4000/failed-payment"),
                new URL("https://localhost:4000/error-payment"),
                payment.getId()
        );
    }

    public CardPaymentResponse createPayment(PaymentRequestFromClient paymentRequestFromClient) throws MalformedURLException {
        Payment payment = new Payment();
        payment.setTimestamp(paymentRequestFromClient.getTimeStamp());
        payment.setAmount(paymentRequestFromClient.getAmount());
        payment.setMerchantOrderId(paymentRequestFromClient.getMerchantOrderId());
        payment.setStatus(PaymentStatus.IN_PROGRESS);

        paymentRepository.save(payment);
        return new CardPaymentResponse(payment.getId(), new URL(PAYMENT_CHOOSE_URL+payment.getId()));
    }

    public void finishPayment(TransactionDetails transactionDetails) {
        Payment payment = paymentRepository.findById(transactionDetails.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment doesn't exist!"));

        payment.setStatus(transactionDetails.getPaymentStatus());
        paymentRepository.save(payment);
        logger.info("User finished card payment !");
    }
}
