package org.example.controller;


import javax.mail.MessagingException;
import org.example.dto.PaymentMethod;
import org.example.dto.SubscriptionDTO;
import org.example.model.Subscription;
import org.example.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/create-subscription")
    public ResponseEntity<Subscription> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) throws MessagingException {
        Subscription subscription = subscriptionService.createSubscription(subscriptionDTO);
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @GetMapping("/get-all-methods")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        List<org.example.model.PaymentMethod> methods = subscriptionService.getAllPaymentMethods();
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        for (org.example.model.PaymentMethod method: methods) {
            paymentMethods.add(new PaymentMethod(method));
        }
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }

    @GetMapping("/get-methods-for-subscription")
    @PreAuthorize("hasAnyAuthority('CREDIT CARD', 'QR CODE', 'PAY PAL', 'CRYPTO')")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethodsForSubscription(Principal principal) {
        Subscription subscription = subscriptionService.getSubscriptionById(principal.getName());
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        for (org.example.model.PaymentMethod method: subscription.getSubscribedPaymentMethods()) {
            paymentMethods.add(new PaymentMethod(method));
        }
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }
}
