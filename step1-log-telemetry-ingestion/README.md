# Step 1 – Log & Telemetry Ingestion

This module implements the first layer of the AI-Driven Cloud-Native
Security and Disaster Recovery Framework.

## Responsibilities

The ingestion service collects and normalizes telemetry from:

- Kubernetes Pods
- Cloud Microservices
- Security Audit Logs

All incoming telemetry is converted into a common `TelemetryEvent`
representation before being forwarded to the real-time ingestion layer.

## Architecture Position

Kubernetes Pods / Cloud Microservices / Audit Logs

↓

Log & Telemetry Ingestion

↓

Apache Kafka / AWS Kinesis

## API

### Submit Telemetry

POST `/api/v1/telemetry`

### Health Check

GET `/api/v1/telemetry/health`

## Technology

- Java 21
- Spring Boot
- Maven
- Docker
