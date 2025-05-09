package com.three.recipinglikeservicebe.like.dto;

import java.time.LocalDateTime;

public record LikeResponseDto(
        String id,
        Long userId,
        Long recipeId,
        LocalDateTime createdAt
) {}