package org.example.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "card_prefixes")
@NoArgsConstructor
@AllArgsConstructor
public class AgencyBankCardPrefixes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String prefix;
}
