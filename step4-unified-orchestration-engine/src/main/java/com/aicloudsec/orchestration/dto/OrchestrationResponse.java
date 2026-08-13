package com.aicloudsec.orchestration.dto;

public record OrchestrationResponse(

        String eventId,

        String action,

        String status

) {
}
