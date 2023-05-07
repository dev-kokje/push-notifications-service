package com.backend.services.pushnotificationsservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsAsyncClient;

import java.net.URISyntaxException;

@Configuration
@RequiredArgsConstructor
public class AwsSnsConfiguration {

    @Bean
    public SnsAsyncClient snsAsyncClient() throws URISyntaxException {

        return SnsAsyncClient.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(ProfileCredentialsProvider.create("aws_personal"))
                .build();
    }
}
