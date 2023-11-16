package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequest;
import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.BankAccount;
import org.example.model.Payment;
import org.example.repository.IBankAccountRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class InBankPaymentService {

    private final IBankAccountRepository bankAccountRepository;

    private final TransactionDetailsService transactionDetailsService;

    public void doCardPayment(Payment payment, CardPaymentRequest cardPaymentRequestDto) {
        BankAccount sellerAccount = bankAccountRepository.findBankAccountByMerchantId(payment.getMerchantId())
                .orElseThrow(() -> new NotFoundException("No seller account exist!"));
        BankAccount buyerAccount = bankAccountRepository.findBankAccountByCardHolderName(cardPaymentRequestDto.getCardHolderName())
                .orElseThrow(() -> new NotFoundException("No buyer account exist!"));

        if(payment.getAmount() > buyerAccount.getBalance())
            transactionDetailsService.failedPayment(payment, "Not enough money!");

        payment.setAcquirerCardNumber(sellerAccount.getPan());
        payment.setIssuerCardNumber(buyerAccount.getPan());
        transferMoney(buyerAccount,sellerAccount,payment);
        transactionDetailsService.successPayment(payment);
    }

    private void transferMoney(BankAccount buyerAccount, BankAccount sellerAccount, Payment payment) {
        buyerAccount.setBalance(buyerAccount.getBalance() - payment.getAmount());
        sellerAccount.setBalance(sellerAccount.getBalance() + payment.getAmount());
        bankAccountRepository.save(buyerAccount);
        bankAccountRepository.save(sellerAccount);
    }

    public void validateCreditCard(CardPaymentRequest cardPaymentRequestDto, Payment payment) {
        checkAllParams(cardPaymentRequestDto);
        checkExpirationDate(cardPaymentRequestDto);
    }

    // todo: Pronalazenje racuna preko pana ne preko card holder-a
    private void checkAllParams(CardPaymentRequest cardPaymentRequestDto) {
        BankAccount bankAccount = bankAccountRepository.findBankAccountByCardHolderName(cardPaymentRequestDto.getCardHolderName())
                .orElseThrow(() -> new NotFoundException("Account doesn't exist!"));

        if(!BCrypt.checkpw(cardPaymentRequestDto.getPan(),bankAccount.getPan()))
            throw new BadRequestException("Invalid parameters!");

        if(!BCrypt.checkpw(cardPaymentRequestDto.getSecurityCode(),bankAccount.getSecurityCode()))
            throw new BadRequestException("Invalid parameters!");

        if(!cardPaymentRequestDto.getCardHolderName().equalsIgnoreCase(bankAccount.getCardHolderName()))
            throw new BadRequestException("Invalid parameters!");

        if(!(String.valueOf(cardPaymentRequestDto.getExpirationDate().getMonth()).equals(String.valueOf(bankAccount.getExpirationDate().getMonth()))
                && String.valueOf(cardPaymentRequestDto.getExpirationDate().getYear()).equals(String.valueOf(bankAccount.getExpirationDate().getYear()))))
            throw new BadRequestException("Invalid parameters!");

    }

    private void checkExpirationDate(CardPaymentRequest cardPaymentRequestDto) {
        LocalDateTime expirationDate = Instant.ofEpochMilli(cardPaymentRequestDto.getExpirationDate().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if(expirationDate.isBefore(LocalDateTime.now()))
            throw new BadRequestException("Card is expired!");
    }

}
