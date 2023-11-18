package org.example.repository;

import org.example.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IBankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findBankAccountByCardHolderName(String cardHolderName);

    Optional<BankAccount> findBankAccountByPan(String pan);
}
