FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt .

RUN pip install \
    --no-cache-dir \
    -r requirements.txt

COPY . .

# Train the prototype AI models during image creation.
# The generated model artifacts are then available
# to the FastAPI inference service at runtime.
RUN python anomaly-detection/train_isolation_forest.py && \
    python blast-radius-prediction/train_xgboost.py

EXPOSE 8083

CMD [
    "uvicorn",
    "api.app:app",
    "--host",
    "0.0.0.0",
    "--port",
    "8083"
]
