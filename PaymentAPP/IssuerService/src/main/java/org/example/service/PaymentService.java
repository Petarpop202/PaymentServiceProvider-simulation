package org.example.service;

import org.example.dto.CardPaymentRequestIssuer;
import org.example.dto.SuccessfulPaymentData;
import org.example.exception.BadRequestException;
import org.example.exception.NotEnoughMoneyOnAccount;
import org.example.exception.NotFoundException;
import org.example.model.BankAccount;
import org.example.model.CreditCard;
import org.example.model.IssuerPayment;
import org.example.model.PaymentStatus;
import org.example.repository.IBankAccountRepository;
import org.example.repository.ICreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class PaymentService {

    @Autowired
    private IBankAccountRepository bankAccountRepository;

    @Autowired
    private ICreditCardRepository creditCardRepository;

    @Autowired
    private IssuerPaymentService issuerPaymentService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public SuccessfulPaymentData checkPaymentForIssuerBank(CardPaymentRequestIssuer cardPaymentRequestIssuer) {
        this.validateIssuerCardParameters(cardPaymentRequestIssuer);
        CreditCard creditCard = creditCardRepository.findCreditCardByCardHolderName(cardPaymentRequestIssuer.getCardHolderName()).orElseThrow(() -> new NotFoundException("Account doesn't exist!"));
        BankAccount bankAccount = creditCard.getBankAccount();

        if (bankAccount.getBalance() < cardPaymentRequestIssuer.getPaymentPrice()) {
            throw new NotEnoughMoneyOnAccount("Not enough money on account");
        }

        bankAccount.setBalance(bankAccount.getBalance() - cardPaymentRequestIssuer.getPaymentPrice());
        bankAccountRepository.save(bankAccount);
        IssuerPayment issuerPayment = issuerPaymentService.createIssuerPayment(cardPaymentRequestIssuer.getPaymentPrice(), creditCard.getPan());
        return new SuccessfulPaymentData(issuerPayment.getIssuerOrderId(), issuerPayment.getIssuerOrderTimestamp(), PaymentStatus.SUCCESS);
    }

    private void validateIssuerCardParameters(CardPaymentRequestIssuer cardPaymentRequestIssuer) {
        CreditCard creditCard = creditCardRepository.findCreditCardByCardHolderName(cardPaymentRequestIssuer.getCardHolderName())
                .orElseThrow(() -> new NotFoundException("Account doesn't exist!"));

        if(!bcryptEncoder.matches(cardPaymentRequestIssuer.getPan(), creditCard.getPan()))
            throw new BadRequestException("Invalid parameters!");

        if(!bcryptEncoder.matches(cardPaymentRequestIssuer.getSecurityCode(), creditCard.getSecurityCode()))
            throw new BadRequestException("Invalid parameters!");

        if(!(String.valueOf(cardPaymentRequestIssuer.getExpirationDate().getMonth()).equals(String.valueOf(creditCard.getExpirationDate().getMonth()))
                && String.valueOf(cardPaymentRequestIssuer.getExpirationDate().getYear()).equals(String.valueOf(creditCard.getExpirationDate().getYear()))))
            throw new BadRequestException("Invalid parameters!");
    }

}
