package com.aicloudsec.orchestration.isolation;

import io.fabric8.kubernetes.api.model.networking.v1.NetworkPolicy;
import io.fabric8.kubernetes.api.model.networking.v1.NetworkPolicyBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NetworkIsolationService {

    private final KubernetesClient kubernetesClient;

    public NetworkIsolationService(
            KubernetesClient kubernetesClient) {

        this.kubernetesClient = kubernetesClient;
    }

    public void isolateWorkload(
            String namespace,
            String applicationName) {

        NetworkPolicy policy =
                new NetworkPolicyBuilder()
                        .withNewMetadata()
                            .withName(
                                "isolate-" + applicationName
                            )
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
