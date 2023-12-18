package org.example.service;

import com.circle.client.model.CreateWalletResponse;
import org.example.dto.Invoice;
import org.example.dto.PaymentData;
import org.example.exceptions.InvoiceCreationException;
import org.example.model.CryptoPayment;
import org.example.repository.CryptoPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
public class CoinGateService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${coingate.url}")
    private String coinGateUrl;

    @Value("${coingate.api-token}")
    private String coinGateApiToken;

    @Autowired
    private CryptoPaymentRepository cryptoPaymentRepository;

    public Object createPayment(PaymentData paymentData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Token " + coinGateApiToken);

        HttpEntity<PaymentData> entity = new HttpEntity<>(paymentData, headers);
        ResponseEntity<Invoice> response = restTemplate.postForEntity(coinGateUrl + "/orders", entity, Invoice.class);
        if (response.getStatusCode().isError()) {
            throw new InvoiceCreationException("Internal server error", response);
        }
        createCryptoPayment((Invoice) response.getBody());
        return response.getBody();
    }

    private void createCryptoPayment(Invoice invoice) {
        CryptoPayment cryptoPayment = new CryptoPayment(invoice);
        cryptoPaymentRepository.save(cryptoPayment);
    }
}
