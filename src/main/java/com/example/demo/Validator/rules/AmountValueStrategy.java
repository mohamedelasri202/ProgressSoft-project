//package com.example.demo.Validator.rules;
//
//import com.example.demo.DTO.DealDto;
//import com.example.demo.Repositories.DealRepository;
//import com.example.demo.Validator.DealValidationStrategy;
//import com.example.demo.Validator.DealValidator;
//import com.example.demo.Validator.ValidationResult;
//import org.apache.tomcat.util.digester.Rules;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//@Component
//public class AmountValueStrategy implements DealValidationStrategy {
//    // Constant for checking if the amount is greater than zero
//    private static final BigDecimal ZERO = BigDecimal.ZERO;
//
//    @Override
//    public ValidationResult validate(DealDto deal) {
//        BigDecimal amount = deal.getDealAmount();
//
//        if (amount == null) {
//            // Null check is handled by NotNullFieldsStrategy, but included for robustness
//            return ValidationResult.invalid(getRuleName(), "Deal Amount is missing.");
//        }
//
//        // Check: Amount must be a positive value (greater than zero)
//        if (amount.compareTo(ZERO) <= 0) {
//            return ValidationResult.invalid(
//                    getRuleName(),
//                    "Deal Amount must be a positive value greater than zero."
//            );
//        }
//
//        return ValidationResult.valid();
//    }
//
//    @Override
//    public String getRuleName() {
//        return "AMOUNT_VALUE_CHECK";
//    }
//}
