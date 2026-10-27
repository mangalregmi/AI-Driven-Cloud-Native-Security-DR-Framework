
#!/usr/bin/env python3
"""
AI-CNDR telemetry ingestion smoke test.

Sends a synthetic telemetry event to Step 1 and verifies
that the API acknowledges it with the expected event ID.

This does not by itself verify delivery to Steps 2–5.
"""

import json
import os
import sys
import urllib.error
import urllib.request
import uuid
from datetime import datetime, timezone


BASE_URL = os.getenv(
    "AI_CNDR_INGESTION_URL",
    "http://localhost:8081"
).rstrip("/")


def main():
    event_id = f"AI-CNDR-SMOKE-{uuid.uuid4().hex[:12]}"

    event = {
        "eventId": event_id,
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "sourceType": "KUBERNETES_POD",
        "sourceName": "payment-service",
        "severity": "CRITICAL",
        "message": "Synthetic high-risk telemetry for integration testing",
        "metadata": {
            "cpu_usage": 98,
            "memory_usage": 95,
            "latency_ms": 1800,
            "error_rate": 40,
            "request_rate": 1200
        }
    }

    request = urllib.request.Request(
        f"{BASE_URL}/api/v1/telemetry",
        data=json.dumps(event).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST"
    )

    print(f"Submitting event: {event_id}")

    try:
        with urllib.request.urlopen(request, timeout=15) as response:
            status = response.status
            body = json.loads(response.read().decode("utf-8"))

    except urllib.error.HTTPError as exc:
        print(f"FAILED: HTTP {exc.code}")
        print(exc.read().decode("utf-8", errors="replace"))
        return 1

    except (urllib.error.URLError, TimeoutError) as exc:
        print(f"FAILED: Cannot reach ingestion service: {exc}")
        return 1

    except (ValueError, UnicodeDecodeError) as exc:
        print(f"FAILED: Invalid JSON response: {exc}")
        return 1

    if status != 202:
        print(f"FAILED: Expected HTTP 202, received {status}")
        return 1

    if body.get("eventId") != event_id:
        print("FAILED: Response event ID does not match")
        return 1

    if body.get("status") != "ACCEPTED":
        print("FAILED: Event was not acknowledged as ACCEPTED")
        return 1

    print("PASSED: Telemetry ingestion acknowledged")
    print(f"Event ID: {event_id}")
    print(f"HTTP status: {status}")
    print("Note: Downstream delivery is not yet verified.")

    return 0


if __name__ == "__main__":
    sys.exit(main())
