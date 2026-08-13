package com.aicloudsec.orchestration.model;

public record AIAnalysisResult(

        String eventId,

        boolean threatDetected,

        double anomalyScore,

        double predictedBlastRadius,

        String recommendation

) {
}
