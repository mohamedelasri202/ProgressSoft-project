package com.example.demo.Validator;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ValidationResult {

    boolean valid;
    String ruleName;
    String errorMessage;

    public static ValidationResult valid() {

        return new ValidationResult(true, "N/A", "N/A");
    }


    public static ValidationResult invalid(String ruleName, String errorMessage) {
        return new ValidationResult(false, ruleName, errorMessage);
    }

    public boolean isInvalid() {
        return !valid;
    }
}
