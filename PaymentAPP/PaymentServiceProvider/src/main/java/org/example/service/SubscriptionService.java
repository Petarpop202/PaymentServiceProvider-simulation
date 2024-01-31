package org.example.service;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.PaymentController;
import org.example.dto.CardPaymentResponse;
import org.example.dto.SubscriptionDTO;
import org.example.exception.BadRequestException;
import org.example.model.Agency;
import org.example.model.PaymentMethod;
import org.example.model.Subscription;
import org.example.repository.IAgencyRepository;
import org.example.repository.IPaymentMethodRepository;
import org.example.repository.ISubscriptionRepository;
import org.example.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private IAgencyRepository agencyRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger = LogManager.getLogger(SubscriptionService.class);

    public Subscription createSubscription(SubscriptionDTO subscriptionDTO) throws Exception {
        List<PaymentMethod> paymentMethods = findPaymentMethods(subscriptionDTO.getMethodsForSubscription());
        Subscription subscription = new Subscription();
        subscription.setSubscribedPaymentMethods(paymentMethods);
        ResponseEntity<Agency> responseEntity = restTemplate.postForEntity(
                "https://localhost:9001/api/aquirer/subscription/create",
                null,
                Agency.class );
        Agency agency = responseEntity.getBody();
        if (agency == null) {
            throw new Exception("Problem while subscribing");
        }
        agency = agencyRepository.save(agency);
        subscription.setAgency(agency);
        subscription = subscriptionRepository.save(subscription);
        String token = tokenUtils.generateToken(subscription.getId());
        emailService.sendTokenMail(token);
        logger.info("User subscribed to psp .");
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
