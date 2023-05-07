package com.backend.services.pushnotificationsservice.api;

import com.backend.services.pushnotificationsservice.repository.AwsSnsDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceApi {

    private final AwsSnsDeviceRepository awsSnsDeviceRepository;

    @PostMapping("/")
    public Mono<String> registerDevice(@RequestBody DeviceRegistrationRequest request) {
        return awsSnsDeviceRepository
                .registerDevice(request.deviceToken(), request.deviceType());
    }

    public record DeviceRegistrationRequest (
            String deviceToken,
            String deviceType
    ){}
}
