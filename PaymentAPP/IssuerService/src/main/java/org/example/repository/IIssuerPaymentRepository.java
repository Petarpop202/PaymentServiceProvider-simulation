package org.example.repository;

import org.example.model.IssuerPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIssuerPaymentRepository extends JpaRepository<IssuerPayment, Long> {

    IssuerPayment findIssuerPaymentByIssuerOrderId(long issuerOderId);
}
