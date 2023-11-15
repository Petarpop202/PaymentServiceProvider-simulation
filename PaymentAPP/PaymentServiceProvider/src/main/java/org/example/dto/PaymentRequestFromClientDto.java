package org.example.dto;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestFromClientDto {
    private Long merchantOrderId;
    private float amount;
    private LocalDateTime timeStamp;
}
