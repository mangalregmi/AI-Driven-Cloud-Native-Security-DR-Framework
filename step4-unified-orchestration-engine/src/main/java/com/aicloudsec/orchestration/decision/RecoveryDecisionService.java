package com.aicloudsec.orchestration.decision;

import com.aicloudsec.orchestration.model.AIAnalysisResult;
import org.springframework.stereotype.Service;

@Service
public class RecoveryDecisionService {

    public String determineAction(AIAnalysisResult result) {

        if (!result.threatDetected()) {
            return "MONITOR";
        }

        if (result.predictedBlastRadius() >= 70) {
            return "ISOLATE_AND_FAILOVER";
        }

        if (result.predictedBlastRadius() >= 40) {
            return "ISOLATE";
        }

        return "MONITOR";
    }
}
