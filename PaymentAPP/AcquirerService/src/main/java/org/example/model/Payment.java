package org.example.model;

import lombok.*;
import org.example.model.enums.PaymentStatus;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_order_id")
    private Long merchantOrderId;

    @Column
    private float amount;

    @Column
    private LocalDateTime timestamp;

    @Enumerated
    private PaymentStatus status;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @Column
    private String issuerCardNumber;

    @Column
    private URL successUrl;

    @Column
    private URL errorUrl;

    @Column
    private URL failedUrl;

    @Column(name = "issuer_order_id")
    private long issuerOrderId;

    @Column(name = "issuer_timestamp")
    private Date issuerTimestamp;

}
