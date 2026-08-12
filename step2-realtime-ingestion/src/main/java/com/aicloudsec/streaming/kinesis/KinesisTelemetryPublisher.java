package com.aicloudsec.streaming.kinesis;

import com.aicloudsec.streaming.model.TelemetryEvent;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@Component
public class KinesisTelemetryPublisher {

    private static final String STREAM_NAME =
            "security-telemetry-stream";

    private final KinesisClient kinesisClient;

    public KinesisTelemetryPublisher() {

        this.kinesisClient = KinesisClient.create();
    }

    public void publish(TelemetryEvent event) {

        String payload =
                event.eventId()
                        + "|"
                        + event.sourceType()
                        + "|"
                        + event.severity()
                        + "|"
                        + event.message();

        PutRecordRequest request =
                PutRecordRequest.builder()
                        .streamName(STREAM_NAME)
                        .partitionKey(event.eventId())
                        .data(
                                SdkBytes.fromUtf8String(payload)
                        )
                        .build();

        kinesisClient.putRecord(request);
    }
}
