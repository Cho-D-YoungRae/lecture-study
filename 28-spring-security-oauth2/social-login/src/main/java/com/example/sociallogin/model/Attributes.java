package com.example.sociallogin.model;

import lombok.Builder;

import java.util.Map;

@Builder
public record Attributes(
        Map<String, Object> mainAttributes,
        Map<String, Object> subAttributes,
        Map<String, Object> otherAttributes
) {
}
