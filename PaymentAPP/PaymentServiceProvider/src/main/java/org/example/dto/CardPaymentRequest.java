package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentRequest {
    private String merchantId;
    private String merchantPassword;
    private Long merchantOrderId;
    private float amount;
    private LocalDateTime timeStamp;
    private URL successUrl;
    private URL failedUrl;
    private URL errorUrl;
}
