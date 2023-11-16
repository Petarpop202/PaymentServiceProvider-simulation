package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequest;
import org.example.dto.PaymentRequestFromClient;
import org.example.exception.NotFoundException;
import org.example.model.Agency;
import org.example.repository.IAgencyRepository;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class CardPaymentService {

    private final IAgencyRepository agencyRepository;

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
}
