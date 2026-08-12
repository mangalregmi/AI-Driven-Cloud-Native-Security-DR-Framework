import os
import joblib
import numpy as np

from sklearn.ensemble import IsolationForest


training_data = np.array([
    [2, 35, 45, 120, 1, 250],
    [2, 40, 50, 130, 1, 260],
    [3, 55, 60, 200, 2, 300],
    [2, 30, 40, 100, 1, 220],
    [3, 60, 65, 250, 3, 350],

    [5, 98, 95, 1500, 30, 1000],
    [5, 95, 90, 1800, 40, 1200],
    [4, 90, 85, 1200, 25, 900]
])

model = IsolationForest(
    contamination=0.25,
    random_state=42
)

model.fit(training_data)

model_dir = os.path.join(
    os.path.dirname(__file__),
    "model"
)

os.makedirs(model_dir, exist_ok=True)

model_path = os.path.join(
    model_dir,
    "isolation_forest.pkl"
)

joblib.dump(model, model_path)

print(
    f"Isolation Forest model saved to {model_path}"
)
