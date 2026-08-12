package com.aicloudsec.ingestion.collector;

import com.mangal.ingestion.model.TelemetryEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class KubernetesLogCollector {

    public TelemetryEvent collect(
            String podName,
            String message,
            String severity) {

        return new TelemetryEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                "KUBERNETES_POD",
                podName,
                severity,
                message,
                Map.of(
                        "environment", "kubernetes"
                )
        );
    }
}
