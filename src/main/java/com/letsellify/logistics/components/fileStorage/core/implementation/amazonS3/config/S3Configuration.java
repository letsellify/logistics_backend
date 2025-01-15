package com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:15:12
 */

@Configuration
@EnableConfigurationProperties(S3ConfigProperties.class)
public class S3Configuration {
    private final AwsBasicCredentials awsBasicCredentials;
    private final Region awsRegion;

    public S3Configuration(final S3ConfigProperties configProperties) {
        this.awsBasicCredentials = AwsBasicCredentials.create(configProperties.accessKey(), configProperties.secretKey());
        this.awsRegion = Region.of(configProperties.region());
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                       .region(this.awsRegion)
                       .credentialsProvider(
                         StaticCredentialsProvider.create(this.awsBasicCredentials)
                       )
                       .build();
    }



    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                          .region(this.awsRegion)
                          .credentialsProvider(
                            StaticCredentialsProvider.create(this.awsBasicCredentials)
                          )
                          .build();
    }
}

