package com.aicloudsec.orchestration.failover;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.route53.Route53Client;

@Service
public class Route53FailoverService {

    private final Route53Client route53Client;

    @Value("${orchestration.mode}")
    private String orchestrationMode;

    public Route53FailoverService(
            Route53Client route53Client) {

        this.route53Client = route53Client;
    }

    public void triggerFailover(
            String hostedZoneId,
            String domainName) {

        if ("simulation".equalsIgnoreCase(orchestrationMode)) {

            System.out.println(
                    "[SIMULATION] Route53 hot-standby failover triggered for: "
                            + domainName
            );

            return;
        }

        System.out.println(
                "Executing Route53 failover for domain: "
                        + domainName
        );

        /*
         * Actual Route53 ChangeResourceRecordSets
         * implementation can be added for AWS mode.
         */
    }
}
