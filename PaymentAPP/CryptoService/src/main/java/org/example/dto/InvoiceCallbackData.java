package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceCallbackData {

    private int id;

    @JsonProperty(value = "order_id")
    private String orderId;

    private String status;

    @JsonProperty(value = "price_currency")
    private String priceCurrency;

    @JsonProperty(value = "price_amount")
    private double priceAmount;

    @JsonProperty(value = "receive_currency")
    private String receiveCurrency;

    @JsonProperty(value = "receive_amount")
    private double receiveAmount;

    @JsonProperty(value = "pay_currency")
    private String payCurrency;

    @JsonProperty(value = "pay_amount")
    private double payAmount;

    @JsonProperty(value = "underpaid_amount")
    private String underpaidAmount;

    @JsonProperty(value = "overpaid_amount")
    private String overpaidAmount;

    @JsonProperty(value = "is_refundable")
    private boolean isRefundable;

    @JsonProperty(value = "created_at")
    private Date createdAt;

    private ArrayList<Object> fees;

    private String token;

}
