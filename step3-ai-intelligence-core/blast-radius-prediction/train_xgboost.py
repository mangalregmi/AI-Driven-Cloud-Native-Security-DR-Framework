import os
import numpy as np
import xgboost as xgb


X = np.array([
    [2, 35, 45, 120, 1, 250],
    [2, 40, 50, 130, 1, 260],
    [3, 55, 60, 200, 2, 300],
    [3, 65, 70, 400, 5, 450],
    [4, 80, 75, 700, 10, 600],
    [4, 90, 85, 1200, 25, 900],
    [5, 95, 90, 1800, 40, 1200],
    [5, 98, 95, 2200, 50, 1500]
])

y = np.array([
    5,
    8,
    15,
    25,
    40,
    65,
    85,
    95
])

model = xgb.XGBRegressor(
    n_estimators=100,
    max_depth=4,
    learning_rate=0.1,
    random_state=42
)

model.fit(X, y)

model_dir = os.path.join(
    os.path.dirname(__file__),
    "model"
)

os.makedirs(model_dir, exist_ok=True)

model_path = os.path.join(
    model_dir,
    "blast_radius.json"
)

model.save_model(model_path)

print(
    f"XGBoost model saved to {model_path}"
)
