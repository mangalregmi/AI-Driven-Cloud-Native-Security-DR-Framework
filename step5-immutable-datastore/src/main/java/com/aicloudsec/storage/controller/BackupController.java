package com.aicloudsec.storage.controller;

import com.aicloudsec.storage.dto.BackupResponse;
import com.aicloudsec.storage.model.RecoveryState;
import com.aicloudsec.storage.service.ImmutableBackupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/backups")
public class BackupController {

    private final ImmutableBackupService backupService;

    public BackupController(
            ImmutableBackupService backupService) {

        this.backupService = backupService;
    }

    @PostMapping("/immutable")
    public ResponseEntity<BackupResponse> createBackup(
            @RequestBody RecoveryState recoveryState) {

        backupService.backup(recoveryState);

        return ResponseEntity.accepted()
                .body(
                        new BackupResponse(
                                recoveryState.eventId(),
                                "AWS_S3_OBJECT_LOCK",
                                "STORED"
                        )
                );
    }
}
