package org.example.service;

import org.example.dto.CardPaymentRequestIssuer;
import org.example.model.IssuerPayment;
import org.example.repository.IIssuerPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class IssuerPaymentService {

    @Autowired
    private IIssuerPaymentRepository issuerPaymentRepository;

    public IssuerPayment createIssuerPayment(float price, String hashedPan) {
        long issuerOrderId = generateIssuerPaymentOrderId();
        Date issuerOrderTimestamp = new Date();
        IssuerPayment issuerPayment = new IssuerPayment();
        issuerPayment.setIssuerPan(hashedPan);
        issuerPayment.setIssuerOrderId(issuerOrderId);
        issuerPayment.setIssuerOrderTimestamp(issuerOrderTimestamp);
        issuerPayment.setPrice(price);
        return issuerPaymentRepository.save(issuerPayment);
    }

    private long generateIssuerPaymentOrderId() {
        while (true) {
            long issuerOrderId = new Random().nextLong(1000000000L, 10000000000L);
            IssuerPayment issuerPayment = issuerPaymentRepository.findIssuerPaymentByIssuerOrderId(issuerOrderId);
            if (issuerPayment != null) {
                continue;
            }
            return issuerOrderId;
        }
    }
}
