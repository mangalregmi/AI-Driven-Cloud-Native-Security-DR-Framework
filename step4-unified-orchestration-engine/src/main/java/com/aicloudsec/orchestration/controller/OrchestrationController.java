package com.aicloudsec.orchestration.controller;

import com.aicloudsec.orchestration.dto.OrchestrationResponse;
import com.aicloudsec.orchestration.model.AIAnalysisResult;
import com.aicloudsec.orchestration.service.OrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orchestration")
public class OrchestrationController {

    private final OrchestrationService orchestrationService;

    public OrchestrationController(
            OrchestrationService orchestrationService) {

        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/execute")
    public ResponseEntity<OrchestrationResponse> execute(
            @RequestBody AIAnalysisResult result,
            @RequestParam(defaultValue = "default")
            String namespace,
            @RequestParam(defaultValue = "payment-service")
            String applicationName,
            @RequestParam(defaultValue = "HOSTED_ZONE_ID")
            String hostedZoneId,
            @RequestParam(defaultValue = "service.example.com")
            String domainName) {

        String action =
                orchestrationService.orchestrate(
                        result,
                        namespace,
                        applicationName,
                        hostedZoneId,
                        domainName
                );

        return ResponseEntity.accepted()
                .body(
                        new OrchestrationResponse(
                                result.eventId(),
                                action,
                                "EXECUTED"
                        )
                );
    }
}
