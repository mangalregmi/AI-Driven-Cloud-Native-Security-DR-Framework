import os
import sys

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


def test_normal_telemetry_inference():

    event = {
        "eventId": "test-normal-001",
        "sourceType": "KUBERNETES_POD",
        "sourceName": "payment-service",
        "severity": "INFO",
        "message": "Normal application activity",
        "metadata": {
            "cpu_usage": 35,
            "memory_usage": 45,
            "latency_ms": 120,
            "error_rate": 1,
            "request_rate": 250
        }
    }

    features = build_features(event)

    anomaly_engine = IsolationForestEngine()
    blast_predictor = BlastRadiusPredictor()

    anomaly_result = anomaly_engine.predict(features)
    blast_result = blast_predictor.predict(features)

    assert "is_anomaly" in anomaly_result
    assert "anomaly_score" in anomaly_result

    assert isinstance(
        anomaly_result["is_anomaly"],
        bool
    )

    assert isinstance(
        anomaly_result["anomaly_score"],
        float
    )

    assert "predicted_blast_radius" in blast_result

    assert isinstance(
        blast_result["predicted_blast_radius"],
        float
    )


def test_high_risk_telemetry_inference():

    event = {
        "eventId": "test-critical-001",
        "sourceType": "KUBERNETES_POD",
        "sourceName": "payment-service",
        "severity": "CRITICAL",
        "message": (
            "Abnormal CPU usage and elevated "
            "error rate detected"
        ),
        "metadata": {
            "cpu_usage": 98,
            "memory_usage": 95,
            "latency_ms": 1800,
            "error_rate": 40,
            "request_rate": 1200
        }
    }

    features = build_features(event)

    anomaly_engine = IsolationForestEngine()
    blast_predictor = BlastRadiusPredictor()

    anomaly_result = anomaly_engine.predict(features)
    blast_result = blast_predictor.predict(features)

    assert "is_anomaly" in anomaly_result
    assert "anomaly_score" in anomaly_result

    assert "predicted_blast_radius" in blast_result

    blast_radius = blast_result[
        "predicted_blast_radius"
    ]

    assert blast_radius >= 0
    assert blast_radius <= 100
