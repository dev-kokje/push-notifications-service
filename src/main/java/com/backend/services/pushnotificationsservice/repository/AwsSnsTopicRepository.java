package com.backend.services.pushnotificationsservice.repository;

import com.backend.services.pushnotificationsservice.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.*;
import com.backend.services.pushnotificationsservice.entity.Topic;

@Repository
@RequiredArgsConstructor
public class AwsSnsTopicRepository {

    private final SnsAsyncClient snsAsyncClient;

    private static final String SUBSCRIPTION_PROTOCOL = "application";

    public Mono<String> save(String displayName) {
        var request = CreateTopicRequest.builder()
                .name(displayName)
                .build();

        return Mono.fromFuture(snsAsyncClient.createTopic(request))
                .map(CreateTopicResponse::topicArn);
    }

    public Flux<Topic> getAll() {
        return Mono.fromFuture(snsAsyncClient.listTopics())
                .flatMapIterable(ListTopicsResponse::topics)
                .flatMap(topic -> getTopicAttributes(topic.topicArn()));
    }

    private Mono<Topic> getTopicAttributes(String topicArn) {
        var request = GetTopicAttributesRequest.builder()
                                                .topicArn(topicArn)
                                                .build();

        return Mono.fromFuture(snsAsyncClient.getTopicAttributes(request))
                .map(response -> Topic.builder()
                        .displayName(response.attributes().get("DisplayName"))
                        .arn(response.attributes().get("TopicArn"))
                        .build());
    }

    public Mono<String> subscribe(String topicArn, String platformEndpointArn) {
        var request = SubscribeRequest.builder()
                .topicArn(topicArn)
                .endpoint(platformEndpointArn)
                .protocol(SUBSCRIPTION_PROTOCOL)
                .returnSubscriptionArn(true)
                .build();

        return Mono.fromFuture(snsAsyncClient.subscribe(request))
                .map(SubscribeResponse::subscriptionArn);
    }

    public Mono<String> publishMessage(String topicArn, Notification notification) {
        var request = PublishRequest.builder()
                .targetArn(topicArn)
                .message(getFormattedNotification(notification))
                .build();

        return Mono.fromFuture(snsAsyncClient.publish(request))
                .map(PublishResponse::messageId);
    }

    private String getFormattedNotification(Notification notification ) {
        //SNS needs stringified JSON. Push notifications won't work with any changes in this format
        return String.format("{\"GCM\": \"%s\"}", createGcmMessage(notification));
    }

    private String createGcmMessage(Notification notification) {

        try {
            var notificationMessage = new JSONObject();

            notificationMessage.put("title", notification.title());
            notificationMessage.put("body", notification.body());

            if(!notification.imageUrl().isEmpty()) {
                notificationMessage.put("image", notification.imageUrl());
            }

            var gcmMessage = new JSONObject();
            gcmMessage.put("notification", notificationMessage);

            var gcmMessageString = gcmMessage.toString().replace("\"", "\\\"");
            gcmMessageString = gcmMessageString.replace("\\/", "/");

            return gcmMessageString;

        } catch (JSONException e) {
            throw new RuntimeException("ERROR while creating GCM notification message" + e);
        }
    }
}