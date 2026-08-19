package com.aicloudsec.orchestration.model;

import java.time.Instant;
import java.util.Map;

public record RecoveryState(

        String eventId,

        String applicationName,

        String recoveryAction,

        String status,

        Instant timestamp,

        Map<String, Object> metadata

) {
}
