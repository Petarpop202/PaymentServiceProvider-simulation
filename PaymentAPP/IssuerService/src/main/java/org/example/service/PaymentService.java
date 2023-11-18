package org.example.service;

import org.example.dto.CardPaymentRequestIssuer;
import org.example.dto.SuccessfulPaymentData;
import org.example.exception.NotEnoughMoneyOnAccount;
import org.example.exception.NotFoundException;
import org.example.model.BankAccount;
import org.example.model.CreditCard;
import org.example.model.IssuerPayment;
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
        // TODO: validate parameters
        String hashedPan = bcryptEncoder.encode(cardPaymentRequestIssuer.getPan());
        CreditCard creditCard = creditCardRepository.findByPan(hashedPan).orElseThrow(() -> new NotFoundException("Account doesn't exist!"));
        BankAccount bankAccount = creditCard.getBankAccount();

        if (bankAccount.getBalance() < cardPaymentRequestIssuer.getPaymentPrice()) {
            // TODO: add message to exception
            throw new NotEnoughMoneyOnAccount("");
        }

        bankAccount.setBalance(bankAccount.getBalance() - cardPaymentRequestIssuer.getPaymentPrice());
        bankAccountRepository.save(bankAccount);
        IssuerPayment issuerPayment = issuerPaymentService.createIssuerPayment(cardPaymentRequestIssuer.getPaymentPrice(), hashedPan);
        return new SuccessfulPaymentData(issuerPayment.getIssuerOrderId(), issuerPayment.getIssuerOrderTimestamp());
    }

}
