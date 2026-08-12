import os
import xgboost as xgb
import numpy as np


class BlastRadiusPredictor:

    def __init__(self, model_path=None):
        if model_path is None:
            model_path = os.path.join(
                os.path.dirname(__file__),
                "model",
                "blast_radius.json"
            )

        self.model = xgb.XGBRegressor()

        self.model.load_model(model_path)

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

        return {
            "predicted_blast_radius": float(prediction)
        }
