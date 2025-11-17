package com.example.demo.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fx_deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {

    // 1. Deal Unique Id (Primary Key MUST be String to match your DTO/Repository)
    @Id
    @Column(name = "deal_unique_id", nullable = false)
    private String dealUniqueId;

    // 2. From Currency ISO Code
    @Column(name = "from_currency_iso_code", length = 3, nullable = false)
    private String fromCurrencyIsoCode;

    // 3. To Currency ISO Code
    @Column(name = "to_currency_iso_code", length = 3, nullable = false)
    private String toCurrencyIsoCode;

    // 4. Deal timestamp (Using Instant for UTC time precision)
    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;

    // 5. Deal Amount (Using BigDecimal for financial accuracy)
    @Column(name = "deal_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal dealAmount;


}