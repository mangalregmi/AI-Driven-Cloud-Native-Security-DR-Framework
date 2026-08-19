package com.aicloudsec.orchestration.client;

import com.aicloudsec.orchestration.model.RecoveryState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImmutableStorageClient {

    private final RestTemplate restTemplate;

    @Value("${storage.service.url}")
    private String storageServiceUrl;

    public ImmutableStorageClient(
            RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public void storeRecoveryState(
            RecoveryState state) {

        String url =
                storageServiceUrl
                        + "/api/v1/backups/immutable";

        restTemplate.postForEntity(
                url,
                state,
                String.class
        );

        System.out.println(
                "Recovery state sent to Step 5: "
                        + state.eventId()
        );
    }
}
