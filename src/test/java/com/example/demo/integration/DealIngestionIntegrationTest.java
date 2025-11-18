package com.example.demo.integration;

import com.example.demo.DTO.DealDto;
import com.example.demo.Repositories.DealRepository;
import com.example.demo.Demo2Application;
import com.example.demo.DTO.DealDto;
import com.example.demo.DTO.ImportReport;
import com.example.demo.Repositories.DealRepository;
import com.example.demo.Models.Deal; // Import the Entity to manually save/check
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Loads the entire application and starts the web server on a random port
@SpringBootTest(classes = Demo2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Optional: Use a specific profile if needed (like 'test' to use specific test configs)
@ActiveProfiles("test")
class DealIngestionIntegrationTest {

    // Makes real HTTP calls to the running application
    @Autowired
    private TestRestTemplate restTemplate;

    // Directly accesses the database for setup and verification
    @Autowired
    private DealRepository dealRepository;

    private static final String API_URL = "/api/deals/ingest";

    // --- Setup and Cleanup ---

    @BeforeEach
    void setUp() {
        // Ensure the table is clean before every test run
        dealRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up the table after every test run
        dealRepository.deleteAll();
    }

    // --- Core Integration Test Cases ---

    @Test
    void testIngestDeals_ShouldHandleMixedBatchCorrectlyAndEnforceNoRollback() {

        // ARRANGE: Create a batch with one valid deal, one invalid amount, and one invalid currency
        DealDto validDeal = new DealDto(DealDto("FX-PASS-100", "USD", "EUR", Instant.now(), BigDecimal.valueOf(100.00));
        DealDto invalidAmountDeal = new DealDto("FX-FAIL-200", "GBP", "JPY", Instant.now(), BigDecimal.valueOf(-10.00));
        DealDto invalidCurrencyDeal = new DealDto("FX-FAIL-300", "CHF", "CHF", Instant.now(), BigDecimal.valueOf(500.00));

        List<DealDto> mixedDeals = List.of(validDeal, invalidAmountDeal, invalidCurrencyDeal);

        // ACT: Send the request to the real Controller
        ResponseEntity<ImportReport> response = restTemplate.postForEntity(
                API_URL, mixedDeals, ImportReport.class
        );

        // ASSERT 1: HTTP Status and Report Integrity
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK because at least one deal succeeded.");
        ImportReport report = response.getBody();
        assertNotNull(report);

        // ASSERT 2: Service Layer Reporting
        assertEquals(3, report.getTotalDealsReceived());
        assertEquals(1, report.getSuccessfulImports(), "Only the valid deal should have succeeded.");

        // ASSERT 3: Database Verification (CRITICAL: Proves No Rollback)
        // Check that the valid deal was saved
        assertTrue(dealRepository.existsById("FX-PASS-100"), "Valid deal MUST be committed to the DB.");
        // Check that the failed deals were NOT saved
        assertFalse(dealRepository.existsById("FX-FAIL-200"), "Invalid deal should not be in DB.");
        assertFalse(dealRepository.existsById("FX-FAIL-300"), "Invalid deal should not be in DB.");
    }

    @Test
    void testIngestDeals_ShouldFailOnDuplicateIDAcrossBatches() {
        // ARRANGE 1: Save a deal directly to the DB to simulate it already existing
        String duplicateId = "FX-DUP-999";
        Deal existingDeal = new Deal(
                duplicateId, "AUD", "CAD", Instant.now(), BigDecimal.valueOf(1.00)
        );
        dealRepository.save(existingDeal);

        // ARRANGE 2: Create a batch containing the duplicate deal
        DealDto duplicateDTO = new DealDto(duplicateId, "AUD", "CAD", Instant.now(), BigDecimal.valueOf(1.00));

        // ACT: Send the request
        ResponseEntity<ImportReport> response = restTemplate.postForEntity(
                API_URL, List.of(duplicateDTO), ImportReport.class
        );

        // ASSERT
        // Since 0 deals succeeded, the Controller should return 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Should return 400 Bad Request since zero deals succeeded.");

        ImportReport report = response.getBody();
        assertNotNull(report);
        assertEquals(0, report.getSuccessfulImports());

        // Verify the specific error message
        assertTrue(report.getFailedDeals().stream()
                        .anyMatch(f -> f.getValidationRule().equals("UNIQUE_ID_CHECK")),
                "The failure reason must be the UNIQUE_ID_CHECK."
        );
    }
}