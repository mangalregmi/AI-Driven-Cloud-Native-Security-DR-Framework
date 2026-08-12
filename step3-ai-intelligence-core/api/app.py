from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict, Any

app = FastAPI(
    title="AI Intelligence Core",
    version="1.0.0"
)


class TelemetryRequest(BaseModel):
    eventId: str
    sourceType: str
    sourceName: str
    severity: str
    message: str
    metadata: Dict[str, Any] = {}


class AIResponse(BaseModel):
    eventId: str
    threatDetected: bool
    anomalyScore: float
    predictedBlastRadius: float
    recommendation: str


def severity_score(value: str):
    mapping = {
        "DEBUG": 1,
        "INFO": 2,
        "WARN": 3,
        "ERROR": 4,
        "CRITICAL": 5
    }

    return mapping.get(value.upper(), 0)


@app.get("/health")
def health():
    return {
        "status": "UP",
        "service": "AI Intelligence Core"
    }


@app.post(
    "/api/v1/ai/analyze",
    response_model=AIResponse
)
def analyze(event: TelemetryRequest):

    metadata = event.metadata

    severity = severity_score(event.severity)

    cpu = float(metadata.get("cpu_usage", 0))
    memory = float(metadata.get("memory_usage", 0))
    latency = float(metadata.get("latency_ms", 0))
    error_rate = float(metadata.get("error_rate", 0))

    risk_score = (
        severity * 10
        + cpu * 0.20
        + memory * 0.10
        + latency * 0.01
        + error_rate * 1.5
    )

    threat_detected = risk_score >= 50

    blast_radius = min(
        100.0,
        risk_score
    )

    if blast_radius >= 70:
        recommendation = "ISOLATE_AND_FAILOVER"

    elif blast_radius >= 40:
        recommendation = "ISOLATE"

    else:
        recommendation = "MONITOR"

    return AIResponse(
        eventId=event.eventId,
        threatDetected=threat_detected,
        anomalyScore=risk_score,
        predictedBlastRadius=blast_radius,
        recommendation=recommendation
    )
