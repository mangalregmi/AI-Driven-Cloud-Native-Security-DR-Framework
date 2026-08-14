package com.aicloudsec.ingestion.client;

import com.aicloudsec.ingestion.model.TelemetryEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StreamingClient {

    private final RestTemplate restTemplate;

    @Value("${streaming.service.url}")
    private String streamingServiceUrl;

    public StreamingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void forwardToKafka(TelemetryEvent event) {

        String url =
                streamingServiceUrl
                        + "/api/v1/stream/kafka";

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        url,
                        event,
                        String.class
                );

        System.out.println(
                "Forwarded telemetry to Step 2. Status: "
                        + response.getStatusCode()
        );
    }
}
