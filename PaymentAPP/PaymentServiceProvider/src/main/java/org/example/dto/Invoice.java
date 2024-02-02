package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Invoice{

    public int id;

    public String status;

    public String title;

    @JsonProperty(value = "do_not_convert")
    public boolean doNotConvert;

    @JsonProperty(value = "orderable_type")
    public String orderableType;

    @JsonProperty(value = "orderable_id")
    public int orderableId;

    public String uuid;

    @JsonProperty(value = "price_currency")
    public String priceCurrency;

    @JsonProperty(value = "price_amount")
    public String priceAmount;

    @JsonProperty(value = "lightning_network")
    public boolean lightningNetwork;

    @JsonProperty(value = "receive_currency")
    public String receiveCurrency;

    @JsonProperty(value = "receive_amount")
    public String receiveAmount;

    @JsonProperty(value = "created_at")
    public Date createdAt;

    @JsonProperty(value = "order_id")
    public String orderId;

    @JsonProperty(value = "payment_url")
    public String paymentUrl;

    @JsonProperty(value = "underpaid_amount")
    public String underpaidAmount;

    @JsonProperty(value = "overpaid_amount")
    public String overpaidAmount;

    @JsonProperty(value = "is_refundable")
    public boolean isRefundable;

    @JsonProperty(value = "payment_request_uri")
    public Object paymentRequestUri;

    public ArrayList<Object> refunds;

    public ArrayList<Object> voids;

    public ArrayList<Object> fees;

    @JsonProperty(value = "blockchain_transactions")
    public ArrayList<Object> blockchainTransactions;

    public String token;
}
