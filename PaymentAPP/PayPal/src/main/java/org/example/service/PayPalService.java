package org.example.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.example.exception.PayPalExecutionException;
import org.example.model.PayPalPayment;
import org.example.repository.IPayPalPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private PayPalHttpClient payPalHttpClient;

    @Autowired
    private IPayPalPaymentRepository payPalPaymentRepository;

    public String createPayment(float amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown().currencyCode("USD").value(String.valueOf(amount));
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountBreakdown);
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("http://localhost:4000/success-payment")
                .cancelUrl("http://localhost:4000/error-payment");
        orderRequest.applicationContext(applicationContext);
        OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);


        HttpResponse<Order> orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
        Order order = orderHttpResponse.result();
//        String redirectUrl = order.links().stream()
//                .filter(link -> "approve".equals(link.rel()))
//                .findFirst()
//                .orElseThrow(NoSuchElementException::new)
//                .href();
        PayPalPayment payPalPayment = new PayPalPayment();
        payPalPayment.setPaymentId(order.id());
        payPalPayment.setAmount(amount);
        payPalPaymentRepository.save(payPalPayment);

        return order.id();
    }

    public String executePayment(String token) throws IOException {
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(token);
        HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
        if (httpResponse.result().status() != null) {
            return "http://localhost:4000/success-payment";
        }
        throw new PayPalExecutionException(("Error completing payment"));
    }
}
