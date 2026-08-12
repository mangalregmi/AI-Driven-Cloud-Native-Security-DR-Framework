package com.aicloudsec.ingestion.collector;

import com.mangal.ingestion.model.TelemetryEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class AuditLogCollector {

    public TelemetryEvent collect(
            String actor,
            String action,
            String severity) {

        return new TelemetryEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                "AUDIT_LOG",
                actor,
                severity,
                action,
                Map.of(
                        "category", "security-audit"
                )
        );
    }
}
