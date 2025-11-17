package com.example.demo.Services;


import com.example.demo.DTO.FailedDeal;
import com.example.demo.DTO.ImportReport;
import com.example.demo.Repositories.DealRepository;
import com.example.demo.Validator.DealValidator;
import com.example.demo.Validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.DealDto;


import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class DealIngestionService {

    private final DealValidator dealValidator;
    private final DealProcessorService dealProcessorService;


    public DealIngestionService(DealValidator dealValidator, DealProcessorService dealProcessorService) {
        this.dealValidator = dealValidator;
        this.dealProcessorService = dealProcessorService;
    }


    public ImportReport processBatch(List<DealDto> deals) {
        log.info("Starting batch ingestion for {} deals.", deals.size());

        List<FailedDeal> failedDeals = new ArrayList<>();
        int successfulImports = 0;


        for (DealDto deal : deals) {


            List<ValidationResult> validationErrors = dealValidator.validate(deal);

            if (validationErrors.isEmpty()) {


                try {

                    dealProcessorService.saveDeal(deal);
                    successfulImports++;
                    log.debug("Successfully imported deal: {}", deal.getDealUniqueId());

                } catch (Exception e) {

                    log.error("Failed to save deal {} due to transaction error: {}", deal.getDealUniqueId(), e.getMessage());
                    failedDeals.add(new FailedDeal(
                            deal.getDealUniqueId(),
                            "PERSISTENCE_ERROR",
                            "Database transaction failed: " + e.getMessage()
                    ));
                }

            } else {

                log.warn("Deal {} failed validation with {} errors.", deal.getDealUniqueId(), validationErrors.size());


                validationErrors.forEach(error ->
                        failedDeals.add(new FailedDeal(
                                deal.getDealUniqueId(),
                                error.getRuleName(),
                                error.getErrorMessage()
                        ))
                );
            }

        }


        int failedImports = failedDeals.size();
        String overallStatus = String.format("Batch finished. Total: %d, Success: %d, Failed: %d.",
                deals.size(), successfulImports, failedImports);

        log.info(overallStatus);


        return new ImportReport(
                deals.size(),
                successfulImports,
                failedImports,
                overallStatus,
                failedDeals
        );
    }
}
