package org.example.dto;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private Long merchantOrderId;
    private float amount;
    private LocalDateTime timeStamp;
}
