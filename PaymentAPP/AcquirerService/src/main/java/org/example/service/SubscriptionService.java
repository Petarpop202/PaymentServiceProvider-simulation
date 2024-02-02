package org.example.service;

import org.example.model.Agency;
import org.example.model.BankAccount;
import org.example.repository.IAgencyRepository;
import org.example.repository.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class SubscriptionService {

    @Autowired
    private IAgencyRepository agencyRepository;

    @Autowired
    private IBankAccountRepository bankAccountRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public Agency createAgency() {
        BankAccount bankAccount = createBankAccount();
        Date now = new Date();
        String merchantId = "merchant_id-" + String.valueOf(now.getTime());
        String merchantPassword = bcryptEncoder.encode(String.valueOf(now.getTime()));
        Agency agency = new Agency(merchantId, merchantPassword, bankAccount);
        return agencyRepository.save(agency);
    }

    private BankAccount createBankAccount() {
        String bankAccountNumber = generateBankAccountNumber();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankAccountNumber);
        bankAccount.setBalance(0.0f);
        return bankAccountRepository.save(bankAccount);
    }

    private String generateBankAccountNumber() {
        while (true) {
            long randomPartOfBankNumber = new Random().nextLong(1000000000000L, 10000000000000L);
            String bankAccount = "845" + String.valueOf(randomPartOfBankNumber) + "87";
            BankAccount issuerPayment = bankAccountRepository.findBankAccountByAccountNumber(bankAccount).orElse(null);
            if (issuerPayment != null) {
                continue;
            }
            return bankAccount;
        }
    }
}
