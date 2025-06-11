package com.three.recipinglikeservicebe.like.controller;

import com.three.recipinglikeservicebe.global.config.SecurityUtil;
import com.three.recipinglikeservicebe.global.logger.CustomLogger;
import com.three.recipinglikeservicebe.like.document.LikeCountDocument;
import com.three.recipinglikeservicebe.like.dto.*;
import com.three.recipinglikeservicebe.like.dto.RecipeLikeStatusResponseDto;
import com.three.recipinglikeservicebe.like.service.LikeCountAggregationScheduler;
import com.three.recipinglikeservicebe.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.three.recipinglikeservicebe.global.logger.LogType;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;
    private final LikeCountAggregationScheduler likeCountAggregationScheduler;

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    private final SecurityUtil securityUtil;

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
    public List<LikeResponseDto> getAllLikes(HttpServletRequest httpRequest) {
        CustomLogger.track(
                logger,
                LogType.GET_LIKES,
                "/api/v1/likes",
                "GET",
                securityUtil.getCurrentUserId(), // 관리자 등 특정 역할만 호출 가능할 경우를 대비
                null,
                "-",
                "-",
                httpRequest
        );
        return likeService.getAllLikes();
    }

    // 3. 좋아요 삭제
    @DeleteMapping("/{likeId}")
    public void deleteLike(@PathVariable String likeId, HttpServletRequest httpRequest) {
        CustomLogger.track(
                logger,
                LogType.DELETE_LIKE_BY_ID,
                "/api/v1/likes/" + likeId,
                "DELETE",
                securityUtil.getCurrentUserId(),
                null,
                likeId,
                "-",
                httpRequest
        );
        likeService.deleteLike(likeId);
    }

    @GetMapping("/counts")
    public ResponseEntity<List<LikeCountDocument>> getAllLikeCounts(HttpServletRequest httpRequest) {
        CustomLogger.track(
                logger,
                LogType.GET_LIKE_COUNTS, // LogType에 추가 필요
                "/api/v1/likes/counts",
                "GET",
                securityUtil.getCurrentUserId(),
                null,
                "-",
                "-",
                httpRequest
        );
        List<LikeCountDocument> counts = likeService.getAllLikeCounts();
        return ResponseEntity.ok(counts);
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
            @RequestBody RecipeLikeStatusListRequestDto requestDto,
            HttpServletRequest httpRequest
    ) {
        String recipeIdsPayload = requestDto.recipeIdList().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        CustomLogger.track(
                logger,
                LogType.GET_LIKE_STATUS_LIST, // LogType에 추가 필요
                "/api/v1/likes/recipe/status-list",
                "POST",
                String.valueOf(requestDto.userId()),
                null,
                "multiple", // 대상이 여러 개임을 명시
                "count:" + requestDto.recipeIdList().size(), // payload에 개수 등 요약 정보 기록
                httpRequest
        );
        return new RecipeLikeStatusListResponseDto(likeService.getLikeStatusList(requestDto));
    }

    // 좋아요 수동 트리거
    @PostMapping("/aggregate")
    public ResponseEntity<Void> triggerLikeCountAggregation(HttpServletRequest httpRequest) {
        CustomLogger.track(
                logger,
                LogType.TRIGGER_AGGREGATION, // LogType에 추가 필요
                "/api/v1/likes/aggregate",
                "POST",
                securityUtil.getCurrentUserId(), // 관리자 기능이므로 호출자 기록이 필수
                null,
                "-",
                "manual_trigger",
                httpRequest
        );
        likeCountAggregationScheduler.aggregateAndSaveLikeCountsManually();
        return ResponseEntity.ok().build();
    }
}
