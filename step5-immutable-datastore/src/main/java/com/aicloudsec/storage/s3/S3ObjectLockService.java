package com.aicloudsec.storage.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.ObjectLockLegalHoldStatus;
import software.amazon.awssdk.services.s3.model.ObjectLockMode;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Instant;

@Service
public class S3ObjectLockService {

    private final S3Client s3Client;

    @Value("${storage.s3.bucket-name}")
    private String bucketName;

    public S3ObjectLockService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void storeImmutableObject(
            String key,
            String payload,
            Instant retainUntil) {

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .objectLockMode(ObjectLockMode.COMPLIANCE)
                        .objectLockRetainUntilDate(retainUntil)
                        .build();

        s3Client.putObject(
                request,
                RequestBody.fromString(payload)
        );
    }
}
