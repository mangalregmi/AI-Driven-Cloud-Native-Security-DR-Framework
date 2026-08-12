package com.aicloudsec.ingestion.controller;

import com.mangal.ingestion.dto.IngestionResponse;
import com.mangal.ingestion.model.TelemetryEvent;
import com.mangal.ingestion.service.TelemetryIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/telemetry")
public class TelemetryController {

    private final TelemetryIngestionService ingestionService;

    public TelemetryController(
            TelemetryIngestionService ingestionService) {

        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<IngestionResponse> ingest(
            @RequestBody TelemetryEvent event) {

        TelemetryEvent processed =
                ingestionService.process(event);

        IngestionResponse response =
                new IngestionResponse(
                        processed.eventId(),
                        "ACCEPTED",
                        "Telemetry event successfully ingested"
                );

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/health")
    public String health() {
        return "Telemetry ingestion service is running";
    }
}
