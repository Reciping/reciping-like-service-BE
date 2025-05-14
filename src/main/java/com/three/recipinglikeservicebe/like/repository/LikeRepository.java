// com.three.recipinglikeservicebe.like.repository.LikeRepository
package com.three.recipinglikeservicebe.like.repository;

import com.three.recipinglikeservicebe.like.document.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
    long countByRecipeId(Long recipeId);
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
}
