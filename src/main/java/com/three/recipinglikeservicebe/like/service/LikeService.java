package com.three.recipinglikeservicebe.like.service;

import com.three.recipinglikeservicebe.like.document.Like;
import com.three.recipinglikeservicebe.like.dto.*;
import com.three.recipinglikeservicebe.like.repository.LikeRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class LikeService {
    private final LikeRepository likeRepository;
//    private final AuditLogger auditLogger;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
//        this.auditLogger = auditLogger;
    }

    public LikeResponseDto createLike(LikeRequestDto request) {
        Like like = new Like(request.userId(), request.recipeId());
        Like saved = likeRepository.save(like);

//        auditLogger.info(String.format("사용자 %d가 레시피 %d에 좋아요 생성", request.userId(), request.recipeId()));

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

    public void deleteLikeByUserAndRecipe(Long userId, Long recipeId) {
        likeRepository.deleteByUserIdAndRecipeId(userId, recipeId);

//        auditLogger.info(String.format("사용자 %d가 레시피 %d에 좋아요 취소", userId, recipeId));
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
