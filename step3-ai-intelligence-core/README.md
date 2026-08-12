# Step 3 – AI Intelligence Core

This module implements the AI intelligence layer of the
AI-Driven Cloud-Native Security and Disaster Recovery Framework.

## Architecture Responsibilities

The AI Intelligence Core performs:

- Anomaly Detection using Isolation Forest
- Predictive Blast Radius Analysis using XGBoost
- Threat and failure classification
- Recovery recommendations for the orchestration layer

## Architecture Position

Apache Kafka / AWS Kinesis

↓

AI Intelligence Core

- Isolation Forest
- XGBoost

↓

Threat / Failure Detected

↓

Unified Orchestration Engine

## AI Models

### Isolation Forest

Used to identify anomalous telemetry patterns without requiring
pre-labeled attack data.

### XGBoost

Used to estimate the potential blast radius of a detected
failure or security event.

## API

### Health Check

GET `/health`

### Analyze Telemetry

POST `/api/v1/ai/analyze`

## Example Output

```json
{
  "eventId": "evt-1001",
  "threatDetected": true,
  "anomalyScore": 142.5,
  "predictedBlastRadius": 100.0,
  "recommendation": "ISOLATE_AND_FAILOVER"
}
