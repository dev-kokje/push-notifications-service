package com.backend.services.pushnotificationsservice.api;

import com.backend.services.pushnotificationsservice.entity.Notification;
import com.backend.services.pushnotificationsservice.entity.Topic;
import com.backend.services.pushnotificationsservice.repository.AwsSnsTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicApi {

    private final AwsSnsTopicRepository awsSnsTopicRepository;

    @GetMapping("/")
    public Flux<Topic> listAllTopics() {
        return awsSnsTopicRepository
                .getAll();
    }

    @PostMapping("/create")
    public Mono<String> createNewTopic(@RequestParam(name = "displayName") String displayName) {
        System.out.println("Topic name - " + displayName);
        return awsSnsTopicRepository
                .save(displayName);
    }

    @PostMapping("/subscribe")
    public Mono<String> subscribeToTopic(@RequestBody SubscriptionRequest request){
        return awsSnsTopicRepository
                .subscribe(request.topicArn(), request.platformEndpointArn());
    }

    @PostMapping("/publish")
    public Mono<String> publishNotification(@RequestBody PublishNotificationRequest request) {
        return awsSnsTopicRepository
                .publishMessage(request.topicArn(), request.notification());
    }

    public record SubscriptionRequest (
            String topicArn,
            String platformEndpointArn
    ){}

    public record PublishNotificationRequest (
            String topicArn,
            Notification notification
    ){}
}
