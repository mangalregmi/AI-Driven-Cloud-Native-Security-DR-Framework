package com.aicloudsec.ingestion.service;

import com.mangal.ingestion.model.TelemetryEvent;
import org.springframework.stereotype.Service;

@Service
public class TelemetryIngestionService {

    public TelemetryEvent process(TelemetryEvent event) {

        System.out.println(
                "Telemetry received from: "
                        + event.sourceType()
                        + " | "
                        + event.sourceName()
        );

        return event;
    }
}
