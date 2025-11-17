package com.example.demo.Validator.rules;

import com.example.demo.DTO.DealDto;
import com.example.demo.Validator.DealValidationStrategy;
import com.example.demo.Validator.ValidationResult;

public class NotNullFieldsStrategy implements DealValidationStrategy {
    @Override
    public ValidationResult validate(DealDto deal) {
        if (deal.getDealUniqueId() == null || deal.getDealUniqueId().isBlank()) {
            return ValidationResult.invalid(getRuleName(), "Deal Unique ID is missing.");
        }
        if (deal.getFromCurrencyIsoCode() == null || deal.getFromCurrencyIsoCode().isBlank()) {
            return ValidationResult.invalid(getRuleName(), "From Currency Code is missing.");
        }
        if (deal.getToCurrencyIsoCode() == null || deal.getToCurrencyIsoCode().isBlank()) {
            return ValidationResult.invalid(getRuleName(), "To Currency Code is missing.");
        }
        if (deal.getDealTimestamp() == null) {
            return ValidationResult.invalid(getRuleName(), "Deal Timestamp is missing.");
        }
        if (deal.getDealAmount() == null) {
            return ValidationResult.invalid(getRuleName(), "Deal Amount is missing.");
        }

        return ValidationResult.valid();
    }

    @Override
    public String getRuleName() {
        return "NOT_NULL_FIELDS_CHECK";
    }
}
