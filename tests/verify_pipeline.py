
#!/usr/bin/env python3
"""
AI-CNDR end-to-end event trace verification.

Checks Docker Compose service logs for a supplied event ID.
A log match is evidence of visibility, not proof of successful
processing or durable storage.
"""

import subprocess
import sys

SERVICES = {
    "Step 1 - Ingestion": "step1",
    "Step 2 - Streaming": "step2",
    "Step 3 - AI Intelligence": "step3",
    "Step 4 - Orchestration": "step4",
    "Step 5 - Storage": "step5",
}


def get_logs(service):
    result = subprocess.run(
        [
            "docker", "compose", "logs",
            "--no-color", "--tail=1000", service
        ],
        capture_output=True,
        text=True,
        check=False
    )

    if result.returncode != 0:
        raise RuntimeError(result.stderr.strip())

    return result.stdout


def main():
    if len(sys.argv) != 2:
        print("Usage: python3 tests/verify_pipeline.py EVENT_ID")
        return 2

    event_id = sys.argv[1]

    print(f"\nAI-CNDR Pipeline Verification: {event_id}")
    print("-" * 55)

    matches = 0

    for stage, service in SERVICES.items():
        try:
            logs = get_logs(service)
            matching_lines = [
                line for line in logs.splitlines()
                if event_id in line
            ]

            if matching_lines:
                print(f"[FOUND] {stage}")
                print(f"        {matching_lines[-1][:220]}")
                matches += 1
            else:
                print(f"[NOT VERIFIED] {stage}")

        except RuntimeError as exc:
            print(f"[ERROR] {stage}: {exc}")

    print("-" * 55)
    print(f"Event ID visible in {matches}/5 service logs")

    if matches == 5:
        print("All stages have matching log entries.")
        print("Processing success still requires verification.")
        return 0

    print("Some stages have no matching event ID in logs.")
    print("They may need additional event-level logging.")
    return 1


if __name__ == "__main__":
    sys.exit(main())
