package com.aicloudsec.streaming.service;

import com.aicloudsec.streaming.kafka.KafkaTelemetryProducer;
import com.aicloudsec.streaming.kinesis.KinesisTelemetryPublisher;
import com.aicloudsec.streaming.model.TelemetryEvent;
import org.springframework.stereotype.Service;

@Service
public class StreamingService {

    private final KafkaTelemetryProducer kafkaProducer;

    private final KinesisTelemetryPublisher kinesisPublisher;

    public StreamingService(
            KafkaTelemetryProducer kafkaProducer,
            KinesisTelemetryPublisher kinesisPublisher) {

        this.kafkaProducer = kafkaProducer;
        this.kinesisPublisher = kinesisPublisher;
    }

    public void streamToKafka(TelemetryEvent event) {

        kafkaProducer.publish(event);
    }

    public void streamToKinesis(TelemetryEvent event) {

        kinesisPublisher.publish(event);
    }
}
