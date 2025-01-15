package com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:15:09
 */

@ConfigurationProperties(prefix = "aws.s3")
public record S3ConfigProperties(String bucketName, String region, String accessKey, String secretKey, int presignedUrlExpiration) {

}
