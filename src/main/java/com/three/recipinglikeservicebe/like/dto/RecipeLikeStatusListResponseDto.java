// RecipeLikeStatusListResponseDto.java
package com.three.recipinglikeservicebe.like.dto;

import java.util.List;

public record RecipeLikeStatusListResponseDto(
        List<RecipeLikeStatusResponseDto> data
) {}
