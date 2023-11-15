package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CardPaymentRequestDto;
import org.example.dto.CardPaymentResponseDto;
import org.example.model.BankAccount;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IBankAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InBankPaymentService {

    private final IBankAccountRepository bankAccountRepository;

    public void doCardPayment(Payment payment, CardPaymentRequestDto cardPaymentRequestDto) {
        BankAccount sellerAccount = bankAccountRepository.findBankAccountByMerchantId(payment.getMerchantId()).orElseGet(null);
        BankAccount buyerAccount = bankAccountRepository.findBankAccountByCardHolderName(cardPaymentRequestDto.getCardHolderName()).orElseGet(null);

        if(payment.getAmount() > buyerAccount.getAmount())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money!");

        transferMoney(buyerAccount,sellerAccount,payment);
        payment.setStatus(PaymentStatus.SUCCESS);
    }

    private void transferMoney(BankAccount buyerAccount, BankAccount sellerAccount, Payment payment) {
        buyerAccount.setAmount(buyerAccount.getAmount() - payment.getAmount());
        sellerAccount.setAmount(sellerAccount.getAmount() + payment.getAmount());
        bankAccountRepository.save(buyerAccount);
        bankAccountRepository.save(sellerAccount);
    }


}
