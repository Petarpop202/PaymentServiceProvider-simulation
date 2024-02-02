package org.example.model;

import lombok.*;
import org.example.model.enums.PaymentStatus;

import javax.persistence.*;
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

    @Column(name = "merchant_order_id")
    private Long merchantOrderId;

    @Column
    private float amount;

    @Column
    private LocalDateTime timestamp;

    @Enumerated
    private PaymentStatus status;
}
