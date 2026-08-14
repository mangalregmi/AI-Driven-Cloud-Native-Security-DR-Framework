package com.aicloudsec.streaming.kafka;

import com.aicloudsec.streaming.client.AIIntelligenceClient;
import com.aicloudsec.streaming.model.AIAnalysisResult;
import com.aicloudsec.streaming.model.TelemetryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTelemetryConsumer {

    private final AIIntelligenceClient aiIntelligenceClient;

    public KafkaTelemetryConsumer(
            AIIntelligenceClient aiIntelligenceClient) {

        this.aiIntelligenceClient =
                aiIntelligenceClient;
    }

    @KafkaListener(
            topics = "security-telemetry",
            groupId = "ai-intelligence-group"
    )
    public void consume(
            TelemetryEvent event) {

        System.out.println(
                "Telemetry received from Kafka: "
                        + event.eventId()
        );

        AIAnalysisResult result =
                aiIntelligenceClient.analyze(event);

        System.out.println(
                "AI Analysis Completed"
        );

        System.out.println(
                "Event ID: "
                        + result.eventId()
        );

        System.out.println(
                "Threat Detected: "
                        + result.threatDetected()
        );

        System.out.println(
                "Anomaly Score: "
                        + result.anomalyScore()
        );

        System.out.println(
                "Predicted Blast Radius: "
                        + result.predictedBlastRadius()
        );

        System.out.println(
                "Recommendation: "
                        + result.recommendation()
        );
    }
}
