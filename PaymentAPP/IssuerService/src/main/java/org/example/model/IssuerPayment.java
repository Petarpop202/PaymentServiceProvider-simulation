package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "issuer_payment")
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class IssuerPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Column(name = "issuer_order_id")
    private long issuerOrderId;

    @Getter
    @Column(name = "issuer_order_timestamp")
    private Date issuerOrderTimestamp;

    @Column
    private float price;

    @Column(name = "issuer_pan")
    private String issuerPan;

}
