package com.three.recipinglikeservicebe.like.dto;

import java.util.List;

public record RecipeLikeStatusListRequestDto(
        List<Long> recipeIdList,
        Long userId
) {}
