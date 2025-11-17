package com.example.demo.Validator;

import com.example.demo.DTO.DealDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class DealValidator {
    private final List<DealValidationStrategy> validators;


    public DealValidator(List<DealValidationStrategy> validators) {
        this.validators = validators;
        log.info("Initialized DealValidator with {} validation strategies.", validators.size());
    }


    public List<ValidationResult> validate(DealDto deal) {
        List<ValidationResult> failures = new ArrayList<>();

        for (DealValidationStrategy validator : validators) {


            ValidationResult result = validator.validate(deal);

            if (result.isInvalid()) {
                failures.add(result);

                log.debug("Validation failed for Deal ID {}: Rule {} failed with message: {}",
                        deal.getDealUniqueId(), result.getRuleName(), result.getErrorMessage());

            }
        }

        return failures;
    }
}
