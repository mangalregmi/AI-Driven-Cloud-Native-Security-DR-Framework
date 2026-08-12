def build_features(event: dict):
    metadata = event.get("metadata", {})

    return {
        "severity_score": severity_to_score(event.get("severity")),
        "cpu_usage": float(metadata.get("cpu_usage", 0)),
        "memory_usage": float(metadata.get("memory_usage", 0)),
        "latency_ms": float(metadata.get("latency_ms", 0)),
        "error_rate": float(metadata.get("error_rate", 0)),
        "request_rate": float(metadata.get("request_rate", 0))
    }


def severity_to_score(severity: str):
    severity_map = {
        "DEBUG": 1,
        "INFO": 2,
        "WARN": 3,
        "ERROR": 4,
        "CRITICAL": 5
    }

    return severity_map.get(
        str(severity).upper(),
        0
    )
