package com.aicloudsec.orchestration.isolation;

import io.fabric8.kubernetes.api.model.networking.v1.NetworkPolicy;
import io.fabric8.kubernetes.api.model.networking.v1.NetworkPolicyBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NetworkIsolationService {

    private final KubernetesClient kubernetesClient;

    @Value("${orchestration.mode}")
    private String orchestrationMode;

    public NetworkIsolationService(
            KubernetesClient kubernetesClient) {

        this.kubernetesClient = kubernetesClient;
    }

    public void isolateWorkload(
            String namespace,
            String applicationName) {

        if ("simulation".equalsIgnoreCase(orchestrationMode)) {

            System.out.println(
                    "[SIMULATION] Kubernetes network isolation triggered for: "
                            + applicationName
                            + " in namespace: "
                            + namespace
            );

            return;
        }

        NetworkPolicy policy =
                new NetworkPolicyBuilder()
                        .withNewMetadata()
                            .withName("isolate-" + applicationName)
                            .withNamespace(namespace)
                        .endMetadata()
                        .withNewSpec()
                            .withNewPodSelector()
                                .withMatchLabels(
                                        Map.of(
                                                "app",
                                                applicationName
                                        )
                                )
                            .endPodSelector()
                        .endSpec()
                        .build();

        kubernetesClient
                .network()
                .v1()
                .networkPolicies()
                .inNamespace(namespace)
                .resource(policy)
                .createOrReplace();
    }
}
