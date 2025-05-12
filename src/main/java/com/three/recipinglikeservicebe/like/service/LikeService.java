// com.three.recipinglikeservicebe.like.service.LikeService
package com.three.recipinglikeservicebe.like.service;

import com.three.recipinglikeservicebe.like.document.Like;
import com.three.recipinglikeservicebe.like.dto.*;
import com.three.recipinglikeservicebe.like.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    // 1. 좋아요 생성
    public LikeResponseDto createLike(LikeRequestDto request) {
        Like like = new Like(request.userId(), request.recipeId());
        Like saved = likeRepository.save(like);
        return toResponse(saved);
    }

    // 2. 좋아요 전체 조회
    public List<LikeResponseDto> getAllLikes() {
        return likeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 3. 좋아요 삭제
    public void deleteLike(String likeId) {
        likeRepository.deleteById(likeId);
    }

    // 4. 특정 게시글 좋아요 상태 조회
    public RecipeLikeStatusResponseDto getRecipeLikeStatus(Long recipeId, Long userId) {
        long likeCount = likeRepository.countByRecipeId(recipeId);
        boolean isLiked = likeRepository.existsByUserIdAndRecipeId(userId, recipeId);
        return new RecipeLikeStatusResponseDto(recipeId, likeCount, isLiked);
    }

    private LikeResponseDto toResponse(Like like) {
        return new LikeResponseDto(
                like.getId(),
                like.getUserId(),
                like.getRecipeId(),
                like.getCreatedAt()
        );
    }

    public List<RecipeLikeStatusResponseDto> getLikeStatusList(RecipeLikeStatusListRequestDto requestDto) {
        return requestDto.recipeIdList().stream()
                .map(recipeId -> {
                    boolean liked = likeRepository.existsByUserIdAndRecipeId(requestDto.userId(), recipeId);
                    long count = likeRepository.countByRecipeId(recipeId);
                    return new RecipeLikeStatusResponseDto(recipeId, count, liked);
                })
                .toList();
    }
}
