package com.three.recipinglikeservicebe.like.controller;

import com.three.recipinglikeservicebe.global.logger.CustomLogger;
import com.three.recipinglikeservicebe.like.dto.*;
import com.three.recipinglikeservicebe.like.dto.RecipeLikeStatusResponseDto;
import com.three.recipinglikeservicebe.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.three.recipinglikeservicebe.global.logger.LogType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 1. 좋아요 생성
    @PostMapping
    public LikeResponseDto createLike(@RequestBody LikeRequestDto request, HttpServletRequest httpRequest) {
        LikeResponseDto response = likeService.createLike(request);

        CustomLogger.track(
                logger,
                LogType.CREATE_LIKE,
                "/api/v1/likes",
                "POST",
                String.valueOf(request.userId()),
                null,
                String.valueOf(request.recipeId()),
                "-",
                httpRequest
        );

        return response;
    }

    // 2. 좋아요 전체 조회
    @GetMapping
    public List<LikeResponseDto> getAllLikes() {
        return likeService.getAllLikes();
    }

    // 3. 좋아요 삭제
    @DeleteMapping("/{likeId}")
    public void deleteLike(@PathVariable String likeId) {
        likeService.deleteLike(likeId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLikeByUserAndRecipe(
            @RequestBody LikeRequestDto likeRequestDto,
            HttpServletRequest httpRequest
    ) {
        likeService.deleteLikeByUserAndRecipe(likeRequestDto.userId(), likeRequestDto.recipeId());

        CustomLogger.track(
                logger,
                LogType.DELETE_LIKE,
                "/api/v1/likes",
                "DELETE",
                String.valueOf(likeRequestDto.userId()),
                null,
                String.valueOf(likeRequestDto.recipeId()),
                "-",
                httpRequest
        );

        return ResponseEntity.noContent().build();
    }

    // 4. 좋아요 상태 조회 (총 개수 + 내가 눌렀는지)
    @GetMapping("/recipe/{recipeId}/status")
    public RecipeLikeStatusResponseDto getRecipeLikeStatus(
            @PathVariable Long recipeId,
            @RequestParam(value = "userId", required = false) Long userId,
            HttpServletRequest httpRequest) {

        CustomLogger.track(
                logger,
                LogType.GET_LIKE,
                "/api/v1/likes/recipe/" + recipeId + "/status",
                "GET",
                String.valueOf(userId),
                null,
                String.valueOf(recipeId),
                null,
                httpRequest
        );

        return likeService.getRecipeLikeStatus(recipeId, userId);
    }

    @PostMapping("/recipe/status-list")
    public RecipeLikeStatusListResponseDto getLikeStatusForRecipes(
            @RequestBody RecipeLikeStatusListRequestDto requestDto
    ) {
        return new RecipeLikeStatusListResponseDto(likeService.getLikeStatusList(requestDto));
    }

}
