package org.example.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.Invoice;

import java.util.UUID;

@Entity
@Table(name = "crypto_payments")
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "price_amount", nullable = false)
    private double priceAmount;

    @Column(name = "currency", nullable = false)
    private String currency;

    public CryptoPayment(Invoice invoice) {
        orderId = invoice.getUuid();
        status = invoice.getStatus();
        priceAmount = Double.parseDouble(invoice.getPriceAmount());
        currency = invoice.getPriceCurrency();
    }
}
