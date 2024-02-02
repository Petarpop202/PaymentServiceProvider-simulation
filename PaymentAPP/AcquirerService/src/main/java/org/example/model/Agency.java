package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "agencies")
@NoArgsConstructor
@AllArgsConstructor
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Column(name = "merchant_id")
    private String merchantId;

    @Getter
    @Column(name = "merchant_password")
    private String merchantPassword;

    @Getter
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    public Agency(String merchantId, String merchantPassword, BankAccount bankAccount) {
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.bankAccount = bankAccount;
    }
}
