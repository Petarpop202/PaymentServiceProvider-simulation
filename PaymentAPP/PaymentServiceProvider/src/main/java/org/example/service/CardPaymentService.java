package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequestDto;
import org.example.dto.PaymentRequestFromClientDto;
import org.example.model.Agency;
import org.example.repository.IAgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardPaymentService {

    private final IAgencyRepository agencyRepository;

    public CardPaymentRequestDto createCardPaymentRequest(PaymentRequestFromClientDto paymentRequestDto) throws Exception {
        Agency agency = agencyRepository.findById(paymentRequestDto.getMerchantOrderId()).orElseGet(null);
        if(agency == null)
            throw new Exception("Agency not founded!");
        return new CardPaymentRequestDto(
                agency.getMerchantId(),
                agency.getMerchantPassword(),
                paymentRequestDto.getMerchantOrderId(),
                paymentRequestDto.getAmount(),
                paymentRequestDto.getTimeStamp(),
                "Success!",
                "Failed!",
                "Error!"
        );
    }
}
