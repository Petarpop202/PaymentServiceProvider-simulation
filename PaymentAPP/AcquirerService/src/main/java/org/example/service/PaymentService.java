package org.example.service;

import org.example.model.Payment;
import org.example.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;

    public void save(Payment payment){
        paymentRepository.save(payment);
    }

    public Payment update(Payment payment){
        Payment oldPayment = paymentRepository.findById(payment.getId()).orElseGet(null);
        if(oldPayment == null)
            return null;
        oldPayment.setStatus(payment.getStatus());
        oldPayment.setTimestamp(payment.getTimestamp());
        paymentRepository.save(oldPayment);
        return oldPayment;
    }
}
