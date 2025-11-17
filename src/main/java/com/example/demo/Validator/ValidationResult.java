//package com.example.demo.Validator;
//
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.Value;
//
//@Value
//@AllArgsConstructor
//@NoArgsConstructor(force = true)
//public class ValidationResult {
//    // Core attributes
//    boolean valid;
//    String ruleName;
//    String errorMessage;
//
//    // --- Static Factory Methods for Clean Construction ---
//
//    /**
//     * Factory method for creating a successful validation result.
//     */
//    public static ValidationResult valid() {
//
//        return new ValidationResult(true, "N/A", "N/A");
//    }
//
//    /**
//     * Factory method for creating a failed validation result.
//     * @param ruleName The name of the specific strategy that failed (e.g., "UNIQUE_ID_CHECK").
//     * @param errorMessage A descriptive message about why the rule failed.
//     */
//    public static ValidationResult invalid(String ruleName, String errorMessage) {
//        return new ValidationResult(false, ruleName, errorMessage);
//    }
//
//    /**
//     * Helper method to easily check if the result indicates failure.
//     */
//    public boolean isInvalid() {
//        return !valid;
//    }
//}
