package com.aicloudsec.streaming.kafka;

import com.aicloudsec.streaming.model.TelemetryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTelemetryConsumer {

    @KafkaListener(
            topics = "security-telemetry",
            groupId = "ai-intelligence-group"
    )
    public void consume(TelemetryEvent event) {

        System.out.println(
                "Telemetry event ready for AI processing: "
                        + event.eventId()
                        + " | "
                        + event.sourceType()
                        + " | "
                        + event.severity()
        );
    }
}
