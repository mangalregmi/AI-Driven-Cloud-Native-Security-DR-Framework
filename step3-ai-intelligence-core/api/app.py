import os
import sys
from typing import Dict, Any

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

from api.orchestration_client import send_to_orchestration


# ---------------------------------------------------------
# Make Step 3 model/feature directories importable.
# Directory names are kept aligned with the architecture.
# ---------------------------------------------------------

BASE_DIR = os.path.dirname(
    os.path.dirname(os.path.abspath(__file__))
)

ANOMALY_DIR = os.path.join(
    BASE_DIR,
    "anomaly-detection"
)

BLAST_RADIUS_DIR = os.path.join(
    BASE_DIR,
    "blast-radius-prediction"
)

FEATURE_DIR = os.path.join(
    BASE_DIR,
    "feature-engineering"
)

for directory in [
    ANOMALY_DIR,
    BLAST_RADIUS_DIR,
    FEATURE_DIR
]:
    if directory not in sys.path:
        sys.path.insert(0, directory)


from isolation_forest import IsolationForestEngine
from xgboost_predictor import BlastRadiusPredictor
from feature_builder import build_features


app = FastAPI(
    title="AI Intelligence Core",
    version="1.1.0",
    description=(
        "AI-driven anomaly detection and predictive "
        "blast-radius analysis for the AI-CNDR framework."
    )
)


# ---------------------------------------------------------
# Request / response models
# ---------------------------------------------------------

class TelemetryRequest(BaseModel):
    eventId: str
    sourceType: str
    sourceName: str
    severity: str
    message: str
    metadata: Dict[str, Any] = Field(default_factory=dict)


class AIResponse(BaseModel):
    eventId: str
    threatDetected: bool
    anomalyScore: float
    predictedBlastRadius: float
    recommendation: str


# ---------------------------------------------------------
# Load trained models once when the service starts.
# ---------------------------------------------------------

try:
    anomaly_engine = IsolationForestEngine()
    blast_radius_predictor = BlastRadiusPredictor()

except Exception as exc:
    raise RuntimeError(
        f"Unable to load AI models: {exc}"
    ) from exc


# ---------------------------------------------------------
# Health endpoint
# ---------------------------------------------------------

@app.get("/health")
def health():
    return {
        "status": "UP",
        "service": "AI Intelligence Core",
        "version": "1.1.0",
        "models": {
            "anomalyDetection": "Isolation Forest",
            "blastRadiusPrediction": "XGBoost"
        }
    }


# ---------------------------------------------------------
# AI inference endpoint
# ---------------------------------------------------------

@app.post(
    "/api/v1/ai/analyze",
    response_model=AIResponse
)
def analyze(event: TelemetryRequest):

    try:
        # Convert incoming telemetry into the feature vector
        # expected by both trained machine-learning models.
        features = build_features(
            event.model_dump()
        )

        # Isolation Forest performs anomaly detection.
        anomaly_result = anomaly_engine.predict(
            features
        )

        # XGBoost predicts potential operational blast radius.
        blast_result = blast_radius_predictor.predict(
            features
        )

        threat_detected = anomaly_result[
            "is_anomaly"
        ]

        anomaly_score = anomaly_result[
            "anomaly_score"
        ]

        # Keep the predicted impact within a 0-100 range.
        blast_radius = max(
            0.0,
            min(
                100.0,
                blast_result[
                    "predicted_blast_radius"
                ]
            )
        )

        # Translate AI output into an orchestration decision.
        if threat_detected and blast_radius >= 70:
            recommendation = "ISOLATE_AND_FAILOVER"

        elif threat_detected or blast_radius >= 40:
            recommendation = "ISOLATE"

        else:
            recommendation = "MONITOR"

        result = AIResponse(
            eventId=event.eventId,
            threatDetected=threat_detected,
            anomalyScore=anomaly_score,
            predictedBlastRadius=blast_radius,
            recommendation=recommendation
        )

        # Forward the AI decision to Step 4.
        try:
            orchestration_response = (
                send_to_orchestration(
                    result.model_dump()
                )
            )

            print(
                "Step 4 orchestration response:",
                orchestration_response
            )

        except Exception as exc:
            # AI analysis remains available even if
            # the orchestration service is temporarily
            # unavailable.
            print(
                "Unable to contact Step 4:",
                str(exc)
            )

        return result

    except Exception as exc:
        raise HTTPException(
            status_code=500,
            detail=f"AI inference failed: {exc}"
        ) from exc
