package org.example.repository;

import org.example.model.PayPalPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPayPalPaymentRepository extends JpaRepository<PayPalPayment, Long> {
}
