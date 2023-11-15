package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequestDto;
import org.example.dto.CardPaymentResponseDto;
import org.example.dto.PspPaymentRequestDto;
import org.example.dto.PspPaymentResponseDto;
import org.example.model.BankAccount;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IBankAccountRepository;
import org.example.repository.IPaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AcquirerService {

    private final IBankAccountRepository bankAccountRepository;

    private final IPaymentRepository paymentRepository;

    private final InBankPaymentService inBankPaymentService;

    private static final String CARD_DETAILS_URL = "http://localhost:8000/bank/card-details";

    private static final int PAYMENT_DURATION_MINUTES = 10;

    public PspPaymentResponseDto createPspPaymentResponse(PspPaymentRequestDto cardPaymentRequestDto) throws Exception {
        BankAccount bankAccount = bankAccountRepository.findBankAccountByMerchantId(cardPaymentRequestDto.getMerchantId()).orElseGet(null);
        if(bankAccount == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found!");

        if(!bankAccount.getMerchantPassword().equals(cardPaymentRequestDto.getMerchantPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid password!");

        return generatePaymentIdAndUrl(cardPaymentRequestDto);
    }
    // todo: URL-ovi zakucane gluposti
    private PspPaymentResponseDto generatePaymentIdAndUrl(PspPaymentRequestDto cardPaymentRequestDto) throws MalformedURLException {
        Payment payment = new Payment();
        payment.setMerchantId(cardPaymentRequestDto.getMerchantId());
        payment.setStatus(PaymentStatus.IN_PROGRESS);
        payment.setAmount(cardPaymentRequestDto.getAmount());
        payment.setTimestamp(cardPaymentRequestDto.getTimeStamp());
        payment.setPaymentExpiration(PAYMENT_DURATION_MINUTES);
        payment.setMerchantOrderId(cardPaymentRequestDto.getMerchantOrderId());
        payment.setSuccessUrl(new URL(CARD_DETAILS_URL));
        payment.setErrorUrl(new URL(CARD_DETAILS_URL));
        payment.setFailedUrl(new URL(CARD_DETAILS_URL));
        paymentRepository.save(payment);

        URL paymentUrl = new URL(CARD_DETAILS_URL + payment.getId());
        return new PspPaymentResponseDto(payment.getId(),paymentUrl);
    }

    public CardPaymentResponseDto cardPayment(CardPaymentRequestDto cardPaymentRequestDto) {
        Payment payment = paymentRepository.findById(cardPaymentRequestDto.getPaymentId()).orElseGet(null);

        if(payment == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment doesn't exist!");

        validatePayment(payment);

        // todo: Treba prvo provera da li je kartica iz ove banke

        validateCreditCard(cardPaymentRequestDto,payment);
        inBankPaymentService.doCardPayment(payment, cardPaymentRequestDto);

        paymentRepository.save(payment);
        // todo: Slanje podataka o transakciji na psp
        sendTransactionOnPsp(payment);
        List<URL> urls = List.of(payment.getSuccessUrl(),payment.getFailedUrl(),payment.getErrorUrl());
        return new CardPaymentResponseDto(urls.get(payment.getStatus().ordinal()));
    }


    private void validatePayment(Payment payment) {
        if(payment.getStatus() != PaymentStatus.IN_PROGRESS)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment is already done!");

        if(payment.getValidUntil().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment link is expired!");
    }

    private void validateCreditCard(CardPaymentRequestDto cardPaymentRequestDto,Payment payment) {
        checkAllParams(cardPaymentRequestDto,payment);
        checkExpirationDate(cardPaymentRequestDto);
    }

    // todo: Pronalazenje racuna preko pana ne preko card holder-a
    private void checkAllParams(CardPaymentRequestDto cardPaymentRequestDto, Payment payment) {
        BankAccount bankAccount = bankAccountRepository.findBankAccountByCardHolderName(cardPaymentRequestDto.getCardHolderName()).orElseGet(null);
        if(bankAccount == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account doesn't exist!");

        if(!BCrypt.checkpw(cardPaymentRequestDto.getPan(),bankAccount.getPan()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameters!");

        if(!BCrypt.checkpw(cardPaymentRequestDto.getSecurityCode(),bankAccount.getSecurityCode()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameters!");

        if(!cardPaymentRequestDto.getCardHolderName().equalsIgnoreCase(bankAccount.getCardHolderName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameters!");

        if(!(String.valueOf(cardPaymentRequestDto.getExpirationDate().getMonth()).equals(String.valueOf(bankAccount.getExpirationDate().getMonth()))
                && String.valueOf(cardPaymentRequestDto.getExpirationDate().getYear()).equals(String.valueOf(bankAccount.getExpirationDate().getYear()))))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameters!");

    }

    private void checkExpirationDate(CardPaymentRequestDto cardPaymentRequestDto) {
        LocalDateTime expirationDate = Instant.ofEpochMilli(cardPaymentRequestDto.getExpirationDate().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if(expirationDate.isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is expired!");
    }

    private void sendTransactionOnPsp(Payment payment) {

    }
}
