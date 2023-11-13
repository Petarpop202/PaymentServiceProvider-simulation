package org.example.model;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "bank_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BankAccount {
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
}
