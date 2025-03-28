package io.github.alishahidi.sbcore.s3;

import io.github.alishahidi.sbcore.document.Document;
import io.github.alishahidi.sbcore.s3.config.S3ClientConfig;
import io.github.alishahidi.sbcore.s3.strategy.BucketStrategy;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Bucket {

    S3AsyncClient client;
    BucketStrategy strategy;
    String name;

    private Bucket(S3AsyncClient client, BucketStrategy strategy, String name) {
        this.client = client;
        this.strategy = strategy;
        this.name = name;
    }

    public static BucketBuilder builder() {
        return new BucketBuilder();
    }

    public static class BucketBuilder {
        private S3ClientConfig config;
        private BucketStrategy strategy;
        private String name;

        public BucketBuilder config(S3ClientConfig config) {
            this.config = config;
            return this;
        }

        public BucketBuilder strategy(BucketStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public BucketBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Bucket build() {
            if (config == null || strategy == null || name == null) {
                throw new IllegalStateException("Config, Strategy, and Name must be provided");
            }
            S3AsyncClient client = S3ClientFactory.create(config);
            return new Bucket(client, strategy, name);
        }
    }

    public CompletableFuture<String> put(String key, Path path, String folderName) {
        return strategy.put(client, name, key, path, folderName)
                .thenApply(s3Key -> {
                    return s3Key;
                })
                .exceptionally(ex -> {
                    return null;
                });
    }

    public CompletableFuture<String> put(String key, Path path) {
        return strategy.put(client, name, key, path)
                .thenApply(s3Key -> {
                    return s3Key;
                })
                .exceptionally(ex -> {
                    return null;
                });
    }

    public CompletableFuture<Void> remove(String key) {
        return client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(name)
                        .key(key)
                        .build())
                .thenRun(() -> {
                    System.out.println("Deleted: " + key);
                })
                .exceptionally(ex -> {
                    System.err.println("Failed to delete: " + key + ", reason: " + ex.getMessage());
                    return null;
                });
    }

    public CompletableFuture<Void> removeFiles(List<String> keys) {
        List<CompletableFuture<Void>> futures = keys.stream()
                .map(this::remove)
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public Mono<InputStream> get(Document document) {
        return Mono.fromFuture(() -> client.getObject(
                GetObjectRequest.builder()
                        .bucket(document.getScope().getBucket())
                        .key(document.getPath())
                        .build(),
                AsyncResponseTransformer.toBytes()
        )).map(response -> new ByteArrayInputStream(response.asByteArray()));
    }

    public CompletableFuture<Path> getAsTempPath(Document document) {
        return client.getObject(
                GetObjectRequest.builder()
                        .bucket(document.getScope().getBucket())
                        .key(document.getPath()) // Use the document key to fetch from S3
                        .build(),
                AsyncResponseTransformer.toBytes()
        ).thenApply(response -> {
            try {
                // Create a temporary file, using the document's key as part of the file name
                Path tempFile = Files.createTempFile("s3-", "-" + document.getPath().replaceAll("/", "_"));

                // Write the response content to the temporary file
                Files.write(tempFile, response.asByteArray());

                return tempFile; // Return the Path to the temporary file
            } catch (Exception e) {
                throw new RuntimeException("Failed to write temporary file", e);
            }
        }).exceptionally(ex -> {
            System.err.println("Failed to download: " + document.getPath() + ", reason: " + ex.getMessage());
            return null;
        });
    }
}
