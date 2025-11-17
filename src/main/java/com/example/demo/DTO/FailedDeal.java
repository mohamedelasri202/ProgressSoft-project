package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class FailedDeal {
    String dealUniqueId;
    String validationRule;
    String errorMessage;
}
