package com.example.demo.DTO;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
@Value
public class DealDto {
    String dealUniqueId;


    String fromCurrencyIsoCode;


    String toCurrencyIsoCode;


    Instant dealTimestamp;


    BigDecimal dealAmount;

    public String getDealUniqueId() {
        return dealUniqueId;
    }

    public String getToCurrencyIsoCode() {
        return toCurrencyIsoCode;
    }

    public String getFromCurrencyIsoCode() {
        return fromCurrencyIsoCode;
    }

    public Instant getDealTimestamp() {
        return dealTimestamp;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

}
