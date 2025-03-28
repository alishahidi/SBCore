package io.github.alishahidi.sbcore.s3.strategy;

import io.github.alishahidi.sbcore.s3.exception.BucketPutException;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface BucketStrategy {

    default CompletableFuture<String> put(S3AsyncClient client, String bucketName, String key, Path path) throws BucketPutException {
        return null;
    }

    default CompletableFuture<String> put(S3AsyncClient client, String bucketName, String key, Path path, String folderName) throws BucketPutException {
        return null;
    }
}
