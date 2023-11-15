package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentRequestDto {
    private String merchantId;
    private String merchantPassword;
    private Long merchantOrderId;
    private float amount;
    private LocalDateTime timeStamp;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;
}
