package org.example.repository;

import org.example.model.CryptoPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoPaymentRepository extends JpaRepository<CryptoPayment, Long> {

    CryptoPayment findCryptoPaymentByOrderId(String orderId);
}
