package com.backend.services.pushnotificationsservice.entity;

import lombok.Builder;

@Builder
public record Notification(
        String title,
        String body,
        String imageUrl
) {}
