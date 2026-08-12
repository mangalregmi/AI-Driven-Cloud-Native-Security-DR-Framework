package com.aicloudsec.streaming.kafka;

import com.aicloudsec.streaming.model.TelemetryEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTelemetryProducer {

    private static final String TOPIC = "security-telemetry";

    private final KafkaTemplate<String, TelemetryEvent> kafkaTemplate;

    public KafkaTelemetryProducer(
            KafkaTemplate<String, TelemetryEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TelemetryEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.eventId(),
                event
        );
    }
}
