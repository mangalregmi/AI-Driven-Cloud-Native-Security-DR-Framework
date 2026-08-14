package com.aicloudsec.streaming.client;

import com.aicloudsec.streaming.model.AIAnalysisResult;
import com.aicloudsec.streaming.model.TelemetryEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AIIntelligenceClient {

    private final RestTemplate restTemplate;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    public AIIntelligenceClient(
            RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public AIAnalysisResult analyze(
            TelemetryEvent event) {

        String url =
                aiServiceUrl
                        + "/api/v1/ai/analyze";

        AIAnalysisResult result =
                restTemplate.postForObject(
                        url,
                        event,
                        AIAnalysisResult.class
                );

        if (result == null) {
            throw new IllegalStateException(
                    "Step 3 AI Intelligence Core returned no result"
            );
        }

        return result;
    }
}
