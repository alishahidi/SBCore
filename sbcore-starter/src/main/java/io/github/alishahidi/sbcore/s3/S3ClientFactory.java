package io.github.alishahidi.sbcore.s3;

import io.github.alishahidi.sbcore.s3.config.S3ClientConfig;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.net.URI;

public class S3ClientFactory {

    public static S3AsyncClient create(S3ClientConfig config) {
        return S3AsyncClient.builder()
                .region(Region.AWS_GLOBAL)
                .endpointOverride(URI.create(config.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(config.getAccess(), config.getSecret())
                ))
                .forcePathStyle(true)
                .build();

    }
}
