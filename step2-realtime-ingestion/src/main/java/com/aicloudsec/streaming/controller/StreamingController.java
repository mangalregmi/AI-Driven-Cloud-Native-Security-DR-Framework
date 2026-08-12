package com.aicloudsec.streaming.controller;

import com.aicloudsec.streaming.dto.StreamingResponse;
import com.aicloudsec.streaming.model.TelemetryEvent;
import com.aicloudsec.streaming.service.StreamingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stream")
public class StreamingController {

    private final StreamingService streamingService;

    public StreamingController(
            StreamingService streamingService) {

        this.streamingService = streamingService;
    }

    @PostMapping("/kafka")
    public ResponseEntity<StreamingResponse>
    publishToKafka(
            @RequestBody TelemetryEvent event) {

        streamingService.streamToKafka(event);

        return ResponseEntity.accepted()
                .body(
                        new StreamingResponse(
                                event.eventId(),
                                "KAFKA",
                                "ACCEPTED"
                        )
                );
    }

    @PostMapping("/kinesis")
    public ResponseEntity<StreamingResponse>
    publishToKinesis(
            @RequestBody TelemetryEvent event) {

        streamingService.streamToKinesis(event);

        return ResponseEntity.accepted()
                .body(
                        new StreamingResponse(
                                event.eventId(),
                                "AWS_KINESIS",
                                "ACCEPTED"
                        )
                );
    }
}
