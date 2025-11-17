//package com.example.demo.Validator.rules;
//
//import com.example.demo.DTO.DealDto;
//import com.example.demo.Repositories.DealRepository;
//import com.example.demo.Validator.DealValidationStrategy;
//import com.example.demo.Validator.ValidationResult;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UniqueIdStrategy implements DealValidationStrategy {
//    private final DealRepository dealRepository;
//
//    public UniqueIdStrategy(DealRepository dealRepository) {
//        this.dealRepository = dealRepository;
//    }
//
//    @Override
//    public ValidationResult validate(DealDto deal) {
//        if (deal.getDealUniqueId() == null) {
//            return ValidationResult.invalid(getRuleName(), "Deal Unique ID cannot be null for uniqueness check.");
//        }
//
//        // Check 1: Does the ID already exist in the database?
//        if (dealRepository.existsById(deal.getDealUniqueId())) {
//            return ValidationResult.invalid(
//                    getRuleName(),
//                    "Deal ID '" + deal.getDealUniqueId() + "' is a duplicate."
//            );
//        }
//        return ValidationResult.valid();
//    }
//
//    @Override
//    public String getRuleName() {
//        return "UNIQUE_ID_CHECK";
//    }
//}
