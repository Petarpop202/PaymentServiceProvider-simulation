package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "credit_cards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String pan;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "security_code")
    private String securityCode;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
}
