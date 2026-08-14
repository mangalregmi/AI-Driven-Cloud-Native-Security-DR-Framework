package com.aicloudsec.ingestion.service;

import com.aicloudsec.ingestion.client.StreamingClient;
import com.aicloudsec.ingestion.model.TelemetryEvent;
import org.springframework.stereotype.Service;

@Service
public class TelemetryIngestionService {

    private final StreamingClient streamingClient;

    public TelemetryIngestionService(
            StreamingClient streamingClient) {

        this.streamingClient = streamingClient;
    }

    public TelemetryEvent process(TelemetryEvent event) {

        System.out.println(
                "Telemetry received from: "
                        + event.sourceType()
                        + " | "
                        + event.sourceName()
        );

        streamingClient.forwardToKafka(event);

        return event;
    }
}
