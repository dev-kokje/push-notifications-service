package com.backend.services.pushnotificationsservice.entity;

import lombok.Builder;

import java.util.Optional;

@Builder
public record Notification(
        String title,
        String body,
        String imageUrl
) {}
