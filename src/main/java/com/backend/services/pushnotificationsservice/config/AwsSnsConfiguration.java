package com.backend.services.pushnotificationsservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class AwsSnsConfiguration {

    @Bean
    public SnsAsyncClient snsAsyncClient() {

        return SnsAsyncClient.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(ProfileCredentialsProvider.create("aws-sns-user-profile"))
                .build();
    }
}
