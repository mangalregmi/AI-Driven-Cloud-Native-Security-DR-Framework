package com.aicloudsec.storage.service;

import com.aicloudsec.storage.model.RecoveryState;
import com.aicloudsec.storage.s3.S3ObjectLockService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ImmutableBackupService {

    private final S3ObjectLockService objectLockService;

    public ImmutableBackupService(
            S3ObjectLockService objectLockService) {

        this.objectLockService = objectLockService;
    }

    public void backup(RecoveryState state) {

        String objectKey =
                "recovery-state/"
                        + state.eventId()
                        + ".json";

        String payload = """
                {
                  "eventId": "%s",
                  "applicationName": "%s",
                  "recoveryAction": "%s",
                  "status": "%s",
                  "timestamp": "%s"
                }
                """.formatted(
                state.eventId(),
                state.applicationName(),
                state.recoveryAction(),
                state.status(),
                state.timestamp()
        );

        Instant retainUntil =
                Instant.now()
                        .plus(30, ChronoUnit.DAYS);

        objectLockService.storeImmutableObject(
                objectKey,
                payload,
                retainUntil
        );
    }
}
