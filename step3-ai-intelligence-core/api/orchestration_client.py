import os
import httpx

ORCHESTRATION_URL = os.getenv(
    "ORCHESTRATION_SERVICE_URL",
    "http://localhost:8084"
)


def send_to_orchestration(result: dict):
    url = (
        ORCHESTRATION_URL
        + "/api/v1/orchestration/execute"
    )

    params = {
        "namespace": "default",
        "applicationName": "payment-service",
        "hostedZoneId": "HOSTED_ZONE_ID",
        "domainName": "service.example.com"
    }

    response = httpx.post(
        url,
        json=result,
        params=params,
        timeout=10.0
    )

    response.raise_for_status()

    return response.json()
