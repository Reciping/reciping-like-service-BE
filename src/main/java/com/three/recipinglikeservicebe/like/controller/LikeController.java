package com.three.recipinglikeservicebe.like.controller;

import com.three.recipinglikeservicebe.global.logger.CustomLogger;
import com.three.recipinglikeservicebe.like.document.LikeCountDocument;
import com.three.recipinglikeservicebe.like.dto.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;
    private final LikeCountAggregationScheduler likeCountAggregationScheduler;

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @PostMapping
    public LikeResponseDto createLike(@RequestBody LikeRequestDto request, HttpServletRequest httpRequest) {
        try {
            LikeResponseDto response = likeService.createLike(request);

            CustomLogger.track(
                    logger,
                    LogType.CREATE_LIKE,
                    String.valueOf(request.recipeId()),
                    "-",
                    httpRequest
            );

            return response;
        } catch (Exception e) {
            errorLogger.error("Error in createLike(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public List<LikeResponseDto> getAllLikes(HttpServletRequest httpRequest) {
        try {
            CustomLogger.track(
                    logger,
                    LogType.GET_LIKES,
                    "-",
                    "-",
                    httpRequest
            );
            return likeService.getAllLikes();
        } catch (Exception e) {
            errorLogger.error("Error in getAllLikes(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{likeId}")
    public void deleteLike(@PathVariable String likeId, HttpServletRequest httpRequest) {
        try {
            CustomLogger.track(
                    logger,
                    LogType.DELETE_LIKE_BY_ID,
                    likeId,
                    "-",
                    httpRequest
            );
            likeService.deleteLike(likeId);
        } catch (Exception e) {
            errorLogger.error("Error in deleteLike(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/counts")
    public ResponseEntity<List<LikeCountDocument>> getAllLikeCounts(HttpServletRequest httpRequest) {
        try {
            // For "get all like counts", no specific targetId.
            // The actor (userDetails.getUserId()) is logged by LogContextUtil.
            CustomLogger.track(
                    logger,
                    LogType.GET_LIKE_COUNTS,
                    "-", // targetId: No specific target
                    "-", // payload
                    httpRequest
            );
            List<LikeCountDocument> counts = likeService.getAllLikeCounts();
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            errorLogger.error("Error in getAllLikeCounts(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLikeByUserAndRecipe(
            @RequestBody LikeRequestDto likeRequestDto,
            HttpServletRequest httpRequest) {
        try {
            likeService.deleteLikeByUserAndRecipe(likeRequestDto.userId(), likeRequestDto.recipeId());

            CustomLogger.track(
                    logger,
                    LogType.DELETE_LIKE,
                    String.valueOf(likeRequestDto.recipeId()),
                    "forUserId:" + likeRequestDto.userId(),
                    httpRequest
            );

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            errorLogger.error("Error in deleteLikeByUserAndRecipe(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/recipe/{recipeId}/status")
    public RecipeLikeStatusResponseDto getRecipeLikeStatus(
            @PathVariable Long recipeId,
            @RequestParam(value = "userId", required = false) Long userIdParam, // Renamed to avoid confusion with actor
            HttpServletRequest httpRequest) {
        try {
            CustomLogger.track(
                    logger,
                    LogType.GET_LIKE,
                    String.valueOf(recipeId),
                    "forUserId:" + (userIdParam != null ? String.valueOf(userIdParam) : "-"),
                    httpRequest
            );
            return likeService.getRecipeLikeStatus(recipeId, userIdParam);
        } catch (Exception e) {
            errorLogger.error("Error in getRecipeLikeStatus(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/recipe/status-list")
    public RecipeLikeStatusListResponseDto getLikeStatusForRecipes(
            @RequestBody RecipeLikeStatusListRequestDto requestDto,
            HttpServletRequest httpRequest) {
        try {
            String payloadInfo = "forUserId:" + requestDto.userId() +
                    ",recipesCount:" + (requestDto.recipeIdList() != null ? requestDto.recipeIdList().size() : 0);
            CustomLogger.track(
                    logger,
                    LogType.GET_LIKE_STATUS_LIST,
                    "multipleRecipes",
                    payloadInfo,
                    httpRequest
            );
            return new RecipeLikeStatusListResponseDto(likeService.getLikeStatusList(requestDto));
        } catch (Exception e) {
            errorLogger.error("Error in getLikeStatusForRecipes(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/aggregate")
    public ResponseEntity<Void> triggerLikeCountAggregation(HttpServletRequest httpRequest) {
        try {
            CustomLogger.track(
                    logger,
                    LogType.TRIGGER_AGGREGATION,
                    "-",
                    "manual_trigger",
                    httpRequest
            );
            likeCountAggregationScheduler.aggregateAndSaveLikeCountsManually();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            errorLogger.error("Error in triggerLikeCountAggregation(): {}", e.getMessage(), e);
            throw e;
        }
    }
}