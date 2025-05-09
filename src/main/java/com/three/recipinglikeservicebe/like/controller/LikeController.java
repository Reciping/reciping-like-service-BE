// com.three.recipinglikeservicebe.like.controller.LikeController
package com.three.recipinglikeservicebe.like.controller;

import com.three.recipinglikeservicebe.like.dto.*;
import com.three.recipinglikeservicebe.like.dto.RecipeLikeStatusResponseDto;
import com.three.recipinglikeservicebe.like.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 1. 좋아요 생성
    @PostMapping
    public LikeResponseDto createLike(@RequestBody LikeRequestDto request) {
        return likeService.createLike(request);
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

    // 4. 좋아요 상태 조회 (총 개수 + 내가 눌렀는지)
    @GetMapping("/recipe/{recipeId}/status")
    public RecipeLikeStatusResponseDto getRecipeLikeStatus(
            @PathVariable Long recipeId,
            @RequestParam Long userId) {
        return likeService.getRecipeLikeStatus(recipeId, userId);
    }

    @PostMapping("/recipe/status-list")
    public List<RecipeLikeStatusResponseDto> getLikeStatusForRecipes(
            @RequestBody RecipeLikeStatusListRequestDto requestDto
    ) {
        return likeService.getLikeStatuses(requestDto);
    }

}
