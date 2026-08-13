package com.aicloudsec.orchestration.service;

import com.aicloudsec.orchestration.decision.RecoveryDecisionService;
import com.aicloudsec.orchestration.failover.Route53FailoverService;
import com.aicloudsec.orchestration.isolation.NetworkIsolationService;
import com.aicloudsec.orchestration.model.AIAnalysisResult;
import org.springframework.stereotype.Service;

@Service
public class OrchestrationService {

    private final RecoveryDecisionService decisionService;

    private final NetworkIsolationService isolationService;

    private final Route53FailoverService failoverService;

    public OrchestrationService(
            RecoveryDecisionService decisionService,
            NetworkIsolationService isolationService,
            Route53FailoverService failoverService) {

        this.decisionService = decisionService;
        this.isolationService = isolationService;
        this.failoverService = failoverService;
    }

    public String orchestrate(
            AIAnalysisResult result,
            String namespace,
            String applicationName,
            String hostedZoneId,
            String domainName) {

        String action =
                decisionService.determineAction(result);

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

        return action;
    }
}
