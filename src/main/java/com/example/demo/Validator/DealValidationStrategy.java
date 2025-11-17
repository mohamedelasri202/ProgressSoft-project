package com.example.demo.Validator;

import com.example.demo.DTO.DealDto;

public interface DealValidationStrategy {

    ValidationResult validate(DealDto deal);

    String getRuleName();
}
