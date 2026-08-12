package com.mangal.ingestion.dto;

public record IngestionResponse(

        String eventId,

        String status,

        String message

) {
}
