import os
import joblib
import numpy as np


class IsolationForestEngine:

    def __init__(self, model_path=None):
        if model_path is None:
            model_path = os.path.join(
                os.path.dirname(__file__),
                "model",
                "isolation_forest.pkl"
            )

        self.model = joblib.load(model_path)

    def predict(self, features: dict):
        values = np.array([[
            features["severity_score"],
            features["cpu_usage"],
            features["memory_usage"],
            features["latency_ms"],
            features["error_rate"],
            features["request_rate"]
        ]])

        prediction = self.model.predict(values)[0]

        anomaly_score = self.model.decision_function(values)[0]

        return {
            "is_anomaly": bool(prediction == -1),
            "anomaly_score": float(anomaly_score)
        }
