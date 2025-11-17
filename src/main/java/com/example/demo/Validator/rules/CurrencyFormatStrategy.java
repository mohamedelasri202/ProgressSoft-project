package com.example.demo.Validator.rules;

import com.example.demo.DTO.DealDto;
import com.example.demo.Validator.DealValidationStrategy;
import com.example.demo.Validator.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class CurrencyFormatStrategy implements DealValidationStrategy {

    @Override
    public ValidationResult validate(DealDto deal) {
        String fromCode = deal.getFromCurrencyIsoCode();
        String toCode = deal.getToCurrencyIsoCode();


        if (!isValidIsoCode(fromCode)) {
            return ValidationResult.invalid(getRuleName(), "From Currency Code must be a 3-letter ISO code.");
        }
        if (!isValidIsoCode(toCode)) {
            return ValidationResult.invalid(getRuleName(), "To Currency Code must be a 3-letter ISO code.");
        }


        if (fromCode.equalsIgnoreCase(toCode)) {
            return ValidationResult.invalid(getRuleName(), "From and To Currency Codes cannot be the same.");
        }

        return ValidationResult.valid();
    }


    private boolean isValidIsoCode(String code) {
        return code != null && code.length() == 3 && code.toUpperCase().matches("^[A-Z]{3}$");
    }

    @Override
    public String getRuleName() {
        return "CURRENCY_FORMAT_CHECK";
    }
}
