# Step 2 – Real-Time Ingestion Layer

This module implements the real-time streaming layer of the
AI-Driven Cloud-Native Security and Disaster Recovery Framework.

## Responsibilities

The service receives normalized telemetry from Step 1 and
streams it using:

- Apache Kafka
- AWS Kinesis

## Architecture Position

Log & Telemetry Ingestion

↓

Apache Kafka / AWS Kinesis

↓

AI Intelligence Core

## Kafka Topic

`security-telemetry`

## Kinesis Stream

`security-telemetry-stream`

## API

### Kafka

POST `/api/v1/stream/kafka`

### AWS Kinesis

POST `/api/v1/stream/kinesis`

## Technology

- Java 21
- Spring Boot
- Apache Kafka
- AWS Kinesis
- Maven
- Docker
