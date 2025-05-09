// com.three.recipinglikeservicebe.like.dto.RecipeLikeStatusResponseDto.java
package com.three.recipinglikeservicebe.like.dto;

public record RecipeLikeStatusResponseDto(
        long recipeId,
        long likeCount,
        boolean isLiked
) {}
