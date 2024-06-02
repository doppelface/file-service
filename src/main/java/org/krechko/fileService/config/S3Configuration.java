package org.krechko.fileService.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Value("${aws.s3.access-key}")
    String awsAccessKey;

    @Value("${aws.s3.secret-key}")
    String awsSecretKey;

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_NORTH_1)
                .build();
    }

    @Bean
    public AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(awsAccessKey,awsSecretKey);
    }
}
