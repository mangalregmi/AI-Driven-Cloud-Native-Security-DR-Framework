package com.aicloudsec.storage.dto;

public record BackupResponse(

        String eventId,

        String storageType,

        String status

) {
}
