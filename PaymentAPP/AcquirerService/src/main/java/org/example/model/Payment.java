package org.example.model;

import lombok.*;
import org.example.model.enums.PaymentStatus;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

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

    @Column
    private float amount;

    @Column
    private LocalDateTime timestamp;

    @Column
    private PaymentStatus status;

    private int paymentExpiration;

    public LocalDateTime getValidUntil() {
        if(timestamp != null)
            return timestamp.plusMinutes(paymentExpiration);
        return null;
    }
}
