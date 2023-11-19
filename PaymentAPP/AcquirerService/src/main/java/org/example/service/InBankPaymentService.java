package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequest;
import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.Agency;
import org.example.model.BankAccount;
import org.example.model.CreditCard;
import org.example.model.Payment;
import org.example.repository.IAgencyRepository;
import org.example.repository.IBankAccountRepository;
import org.example.repository.ICreditCardRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class InBankPaymentService {

    private final IBankAccountRepository bankAccountRepository;

    private final ICreditCardRepository creditCardRepository;

    private final IAgencyRepository agencyRepository;

    private final TransactionDetailsService transactionDetailsService;

    private final PasswordEncoder bcryptEncoder;

    public void doCardPayment(Payment payment, CardPaymentRequest cardPaymentRequestDto) {
        Agency agency = agencyRepository.findAgencyByMerchantId(payment.getMerchantId())
                .orElseThrow(() -> new NotFoundException("No seller account exist!"));;
        CreditCard creditCard = creditCardRepository.findCreditCardByPan(cardPaymentRequestDto.getCardHolderName())
                .orElseThrow(() -> new NotFoundException("No buyer account exist!"));

        if(payment.getAmount() > creditCard.getBankAccount().getBalance())
            transactionDetailsService.failedPayment(payment, "Not enough money!");

        payment.setIssuerCardNumber(creditCard.getPan());
        transferMoney(creditCard.getBankAccount(),agency.getBankAccount(),payment);
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
//        BankAccount bankAccount = bankAccountRepository.findBankAccountByCardHolderName(cardPaymentRequestDto.getCardHolderName())
//                .orElseThrow(() -> new NotFoundException("Account doesn't exist!"));
        CreditCard creditCard = creditCardRepository.findCreditCardByCardHolderName(cardPaymentRequestDto.getCardHolderName())
                .orElseThrow(() -> new NotFoundException("Account doesn't exist!"));

        if(!bcryptEncoder.matches(cardPaymentRequestDto.getPan(), creditCard.getPan()))
            throw new BadRequestException("Invalid parameters!");

        if(!bcryptEncoder.matches(cardPaymentRequestDto.getSecurityCode(), creditCard.getSecurityCode()))
            throw new BadRequestException("Invalid parameters!");

        if(!cardPaymentRequestDto.getCardHolderName().equalsIgnoreCase(creditCard.getCardHolderName()))
            throw new BadRequestException("Invalid parameters!");

        if(!(String.valueOf(cardPaymentRequestDto.getExpirationDate().getMonth()).equals(String.valueOf(creditCard.getExpirationDate().getMonth()))
                && String.valueOf(cardPaymentRequestDto.getExpirationDate().getYear()).equals(String.valueOf(creditCard.getExpirationDate().getYear()))))
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
