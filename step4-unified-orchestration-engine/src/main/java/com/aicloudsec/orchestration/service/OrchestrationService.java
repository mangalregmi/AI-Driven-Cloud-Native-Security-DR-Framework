package com.aicloudsec.orchestration.service;

import com.aicloudsec.orchestration.client.ImmutableStorageClient;
import com.aicloudsec.orchestration.decision.RecoveryDecisionService;
import com.aicloudsec.orchestration.failover.Route53FailoverService;
import com.aicloudsec.orchestration.isolation.NetworkIsolationService;
import com.aicloudsec.orchestration.model.AIAnalysisResult;
import com.aicloudsec.orchestration.model.RecoveryState;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class OrchestrationService {

    private final RecoveryDecisionService decisionService;
    private final NetworkIsolationService isolationService;
    private final Route53FailoverService failoverService;
    private final ImmutableStorageClient storageClient;

    public OrchestrationService(
            RecoveryDecisionService decisionService,
            NetworkIsolationService isolationService,
            Route53FailoverService failoverService,
            ImmutableStorageClient storageClient) {

        this.decisionService = decisionService;
        this.isolationService = isolationService;
        this.failoverService = failoverService;
        this.storageClient = storageClient;
    }

    public String orchestrate(
            AIAnalysisResult result,
            String namespace,
            String applicationName,
            String hostedZoneId,
            String domainName) {

        String action =
                decisionService.determineAction(result);

        String status = "COMPLETED";

        try {

            switch (action) {

                case "ISOLATE" ->

                    isolationService.isolateWorkload(
                            namespace,
                            applicationName
                    );

                case "ISOLATE_AND_FAILOVER" -> {

                    isolationService.isolateWorkload(
                            namespace,
                            applicationName
                    );

                    failoverService.triggerFailover(
                            hostedZoneId,
                            domainName
                    );
                }

                default ->
                    System.out.println(
                            "Monitoring event: "
                                    + result.eventId()
                    );
            }

        } catch (Exception ex) {

            status = "FAILED";

            System.out.println(
                    "Orchestration action failed: "
                            + ex.getMessage()
            );
        }

        RecoveryState recoveryState =
                new RecoveryState(
                        result.eventId(),
                        applicationName,
                        action,
                        status,
                        Instant.now(),
                        Map.of(
                                "anomalyScore",
                                result.anomalyScore(),
                                "predictedBlastRadius",
                                result.predictedBlastRadius(),
                                "recommendation",
                                result.recommendation()
                        )
                );

        try {

            storageClient.storeRecoveryState(
                    recoveryState
            );

        } catch (Exception ex) {

            System.out.println(
                    "Unable to contact Step 5: "
                            + ex.getMessage()
            );
        }

        return action;
    }
}
