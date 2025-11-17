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

    /**
     * Spring automatically injects ALL beans that implement the DealValidationStrategy interface.
     * This makes the system open for extension (OCP) - just add a new rule class!
     * @param validators A list of all available validation strategies.
     */
    public DealValidator(List<DealValidationStrategy> validators) {
        this.validators = validators;
        log.info("Initialized DealValidator with {} validation strategies.", validators.size());
    }

    /**
     * Executes all available validation strategies on a single deal DTO.
     * * @param deal The DealDTO to be validated.
     * @return A list of ValidationResult objects for all failed rules (empty list if valid).
     */
    public List<ValidationResult> validate(DealDto deal) {
        List<ValidationResult> failures = new ArrayList<>();

        for (DealValidationStrategy validator : validators) {

            // Execute the specific rule defined by the strategy
            ValidationResult result = validator.validate(deal);

            if (result.isInvalid()) {
                failures.add(result);
                // Logging the specific failure for clarity
                log.debug("Validation failed for Deal ID {}: Rule {} failed with message: {}",
                        deal.getDealUniqueId(), result.getRuleName(), result.getErrorMessage());

                // OPTIONAL: You can add logic here to "fail fast" (break the loop)
                // if you don't need to report all possible errors. For this project,
                // collecting all errors is more informative.
            }
        }

        return failures;
    }
}
