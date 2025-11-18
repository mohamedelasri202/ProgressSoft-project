package com.example.demo.service;



import com.example.demo.DTO.DealDto;
import com.example.demo.DTO.ImportReport;
import com.example.demo.Services.DealIngestionService;
import com.example.demo.Services.DealProcessorService;
import com.example.demo.Validator.DealValidator;
import com.example.demo.Validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealIngestionServiceTest {
    @Mock
    private DealValidator dealValidator;
    @Mock
    private DealProcessorService dealProcessorService;

    // Injecting mocks into the class under test
    @InjectMocks
    private DealIngestionService dealIngestionService;

    // Sample data for testing
    private DealDto validDeal;
    private DealDto invalidDeal;
    private List<ValidationResult> failureList;

    @BeforeEach
    void setUp() {
        // A deal that should pass validation
        validDeal = new DealDto("FX-PASS-001", "USD", "EUR", Instant.now(), BigDecimal.valueOf(100.00));

        // A deal that should fail validation (e.g., negative amount)
        invalidDeal = new DealDto("FX-FAIL-002", "BRL", "JPY", Instant.now(), BigDecimal.valueOf(-50.00));

        // A list simulating validation failure (for mocking the validator)
        failureList = List.of(
                ValidationResult.invalid("AMOUNT_VALUE_CHECK", "Amount is negative.")
        );
    }

    // --- Test Cases ---

    @Test
    void processBatch_shouldSuccessfullyImportAllValidDeals() {
        // ARRANGE
        List<DealDto> deals = List.of(validDeal, validDeal);
        // Mock the validator to always return an empty error list (SUCCESS)
        when(dealValidator.validate(any(DealDto.class))).thenReturn(Collections.emptyList());

        // ACT
        ImportReport report = dealIngestionService.processBatch(deals);

        // ASSERT
        // Verify the transactional save was called twice
        verify(dealProcessorService, times(2)).saveDeal(any(DealDto.class));

        // Verify the report is accurate
        assertEquals(2, report.getTotalDealsReceived());
        assertEquals(2, report.getSuccessfulImports());
        assertEquals(0, report.getFailedImports());
        assertTrue(report.getFailedDeals().isEmpty());
    }

    @Test
    void processBatch_shouldFailAllInvalidDealsAndSkipSave() {
        // ARRANGE
        List<DealDto> deals = List.of(invalidDeal, invalidDeal);
        // Mock the validator to always return a failure list
        when(dealValidator.validate(any(DealDto.class))).thenReturn(failureList);

        // ACT
        ImportReport report = dealIngestionService.processBatch(deals);

        // ASSERT
        // Verify the save method was NEVER called
        verify(dealProcessorService, never()).saveDeal(any(DealDto.class));

        // Verify the report is accurate
        assertEquals(2, report.getTotalDealsReceived());
        assertEquals(0, report.getSuccessfulImports());
        assertEquals(2, report.getFailedImports());
        assertFalse(report.getFailedDeals().isEmpty());
    }

    @Test
    void processBatch_shouldHandleMixedResultsAndEnforceNoRollback() throws Exception {
        // ARRANGE
        List<DealDto> deals = List.of(validDeal, invalidDeal, validDeal); // Expect 2 success, 1 fail

        // Define specific behavior for each deal ID (mocking the validator's input)
        when(dealValidator.validate(validDeal)).thenReturn(Collections.emptyList());
        when(dealValidator.validate(invalidDeal)).thenReturn(failureList);

        // ACT
        ImportReport report = dealIngestionService.processBatch(deals);

        // ASSERT
        // The save method should have been called twice (once for each validDeal)
        verify(dealProcessorService, times(2)).saveDeal(validDeal);

        // Verify the report confirms the mix
        assertEquals(3, report.getTotalDealsReceived());
        assertEquals(2, report.getSuccessfulImports());
        assertEquals(1, report.getFailedImports());

        // Verify the failed deal details were captured
        assertEquals(1, report.getFailedDeals().size());
        assertEquals("FX-FAIL-002", report.getFailedDeals().get(0).getDealUniqueId());
        assertEquals("AMOUNT_VALUE_CHECK", report.getFailedDeals().get(0).getValidationRule());
    }

    @Test
    void processBatch_shouldCatchPersistenceExceptionAndContinueLoop() throws Exception {
        // ARRANGE
        List<DealDto> deals = List.of(validDeal, validDeal);

        when(dealValidator.validate(any(DealDto.class))).thenReturn(Collections.emptyList());

        // Mock the saveDeal method to succeed the first time, but throw a persistence error the second time
        // This simulates a database failure or a concurrent unique constraint violation.
        doNothing().when(dealProcessorService).saveDeal(validDeal); // First call success
        doThrow(new RuntimeException("DB Connection Timeout")).when(dealProcessorService).saveDeal(validDeal); // Second call fails

        // ACT
        ImportReport report = dealIngestionService.processBatch(deals);

        // ASSERT
        // Verify the save method was called twice (once succeeds, one fails, but the loop continues)
        verify(dealProcessorService, times(2)).saveDeal(validDeal);

        // Verify the report reflects the outcome: 1 success, 1 failure due to persistence error
        assertEquals(2, report.getSuccessfulImports()); // The success counter is incremented inside the try block
        assertEquals(1, report.getFailedImports()); // One failure caught by the catch block
        assertEquals("PERSISTENCE_ERROR", report.getFailedDeals().get(0).getValidationRule());
    }
}
