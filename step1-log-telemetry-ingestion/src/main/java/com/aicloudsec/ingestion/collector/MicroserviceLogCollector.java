package com.aicloudsec.ingestion.collector;

import com.mangal.ingestion.model.TelemetryEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class MicroserviceLogCollector {

    public TelemetryEvent collect(
            String serviceName,
            String message,
            String severity) {

        return new TelemetryEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                "CLOUD_MICROSERVICE",
                serviceName,
                severity,
                message,
                Map.of(
                        "environment", "cloud"
                )
        );
    }
}
