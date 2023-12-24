package org.example.service;

import javax.mail.MessagingException;
import org.example.dto.SubscriptionDTO;
import org.example.model.PaymentMethod;
import org.example.model.Subscription;
import org.example.repository.IPaymentMethodRepository;
import org.example.repository.ISubscriptionRepository;
import org.example.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private IPaymentMethodRepository paymentMethodRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private EmailService emailService;

    public Subscription createSubscription(SubscriptionDTO subscriptionDTO) throws MessagingException {
        List<PaymentMethod> paymentMethods = findPaymentMethods(subscriptionDTO.getMethodsForSubscription());
        Subscription subscription = new Subscription();
        subscription.setSubscribedPaymentMethods(paymentMethods);
        subscription = subscriptionRepository.save(subscription);
        String token = tokenUtils.generateToken(subscription.getId());
        emailService.sendTokenMail(token);
        return subscription;
    }

    private List<PaymentMethod> findPaymentMethods(List<org.example.dto.PaymentMethod> paymentMethods) {
        ArrayList<PaymentMethod> realPaymentMethods = new ArrayList<>();
        for (org.example.dto.PaymentMethod pm: paymentMethods) {
            PaymentMethod realPm = paymentMethodRepository.findPaymentMethodByName(pm.getName());
            realPaymentMethods.add(realPm);
        }
        return realPaymentMethods;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public Subscription getSubscriptionById(String id) {
        return subscriptionRepository.findSubscriptionById(Long.valueOf(id));
    }
}
