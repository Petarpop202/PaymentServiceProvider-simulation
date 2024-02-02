package org.example.repository;

import org.example.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    PaymentMethod findPaymentMethodByName(String name);
}
