package com.backend.services.pushnotificationsservice.entity;

import lombok.Builder;

@Builder
public record Topic (
        String arn,
        String displayName
) {}
