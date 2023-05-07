package com.backend.services.pushnotificationsservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.CreatePlatformEndpointRequest;
import software.amazon.awssdk.services.sns.model.CreatePlatformEndpointResponse;

@Repository
@RequiredArgsConstructor
public class AwsSnsDeviceRepository {

    private final SnsAsyncClient snsAsyncClient;

    @Value("${aws.sns.platform_application.gcm}")
    private final String GCM_PLATFORM_APPLICATION_ARN;

    @Value("${aws.sns.platform_application.apns}")
    private final String APNS_PLATFORM_APPLICATION_ARN;

    public Mono<String> registerDevice(String deviceToken, String deviceType) {
        var request = CreatePlatformEndpointRequest.builder()
                .token(deviceToken)
                .platformApplicationArn(deviceType.equals("APPLE") ? APNS_PLATFORM_APPLICATION_ARN : GCM_PLATFORM_APPLICATION_ARN)
                .build();

        return Mono.fromFuture(snsAsyncClient.createPlatformEndpoint(request))
                .map(CreatePlatformEndpointResponse::endpointArn);
    }
}