package org.example.controller;

import org.example.model.Agency;
import org.example.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aquirer/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<Agency> createAgency() {
        return new ResponseEntity<>(subscriptionService.createAgency(), HttpStatus.CREATED);
    }
}
