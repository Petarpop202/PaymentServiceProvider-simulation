package org.example.service;

import org.example.exception.BadRequestException;
import org.example.model.Payment;
import org.example.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    public float getPaymentAmount(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new BadRequestException("No payment with given id"));
        return payment.getAmount();
    }
}
