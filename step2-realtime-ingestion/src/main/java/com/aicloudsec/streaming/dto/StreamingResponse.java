package com.aicloudsec.streaming.dto;

public record StreamingResponse(

        String eventId,

        String destination,

        String status

) {
}
