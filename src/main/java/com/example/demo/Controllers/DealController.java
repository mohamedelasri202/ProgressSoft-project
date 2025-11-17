package com.example.demo.Controllers;

import com.example.demo.DTO.DealDto;
import com.example.demo.DTO.ImportReport;
import com.example.demo.Services.DealIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@Controller
@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealIngestionService dealIngestionService;

    public DealController(DealIngestionService dealIngestionService) {
        this.dealIngestionService = dealIngestionService;
    }
    @PostMapping("/ingest")
    public ResponseEntity<ImportReport> ingestDeals(@RequestBody List<DealDto> deals) {

        if (deals == null || deals.isEmpty()) {
            ImportReport report = new ImportReport(0, 0, 0, "No deals provided in the request body.", List.of());
            return ResponseEntity.badRequest().body(report);
        }

        ImportReport report = dealIngestionService.processBatch(deals);

        if (report.getSuccessfulImports() > 0 && report.getFailedImports() == 0) {
            return ResponseEntity.ok(report);
        } else if (report.getSuccessfulImports() > 0 && report.getFailedImports() > 0) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.badRequest().body(report);
        }
    }
}
