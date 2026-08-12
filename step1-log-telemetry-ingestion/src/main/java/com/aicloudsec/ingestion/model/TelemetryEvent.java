package com.aicloudsec.ingestion.model;

import java.time.Instant;
import java.util.Map;

public record TelemetryEvent(

        String eventId,

        Instant timestamp,

        String sourceType,

        String sourceName,

        String severity,

        String message,

        Map<String, Object> metadata

) {
}
