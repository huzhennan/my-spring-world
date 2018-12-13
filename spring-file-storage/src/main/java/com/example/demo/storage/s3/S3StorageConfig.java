package com.example.demo.storage.s3;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Profile("s3-storage")
public class S3StorageConfig {

    @Bean
    public MinioClient minioClient() {
        String myEndpoint = "http://localhost:9000";
        String myAccessKey = "AKIAIOSFODNN7EXAMPLE";
        String mySecretKey = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
        String bucketName = "images";

        try {
            MinioClient client = new MinioClient(myEndpoint, myAccessKey, mySecretKey);
            if(!client.bucketExists(bucketName)) {
                client.makeBucket(bucketName);
            }
            return client;
        } catch (NoSuchAlgorithmException | XmlPullParserException | InvalidKeyException
                | IOException | MinioException e) {
            throw new RuntimeException("ERROR: connect to minio server failed.", e.getCause());
        }
    }

    @Bean
    public S3StorageService s3StorageService(MinioClient minioClient) {
        return new S3StorageService(minioClient);
    }
}
