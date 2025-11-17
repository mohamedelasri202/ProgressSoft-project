package com.example.demo.Models;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Deal {
    @Id
    @Column( name = "deal_unique_id", unique = true, nullable = false)
    private int id;
    @Column(name = "from_currency_iso_code", length = 3, nullable = false)
    private String fromCurrencyIsoCode;
    @Column(name = "to_currency_iso_code", length = 3, nullable = false)
    private String toCurrencyIsoCode;
    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;
    @Column(name = "deal_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal dealAmount;
}
