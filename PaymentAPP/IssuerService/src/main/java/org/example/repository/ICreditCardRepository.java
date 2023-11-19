package org.example.repository;

import org.example.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICreditCardRepository extends JpaRepository<CreditCard, Long> {

    Optional<CreditCard> findByPan(String pan);

    Optional<CreditCard> findCreditCardByCardHolderName(String cardHolderName);
}
