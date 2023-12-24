package org.example.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "paypal_payments")
@NoArgsConstructor
@Setter
public class PayPalPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "payment_id")
    private String paymentId;

    @Column
    private float amount;
}
