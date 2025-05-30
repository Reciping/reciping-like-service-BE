package com.three.recipinglikeservicebe.like.repository;

import com.three.recipinglikeservicebe.like.document.LikeCountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LikeCountRepository extends MongoRepository<LikeCountDocument, Long> {
    Optional<LikeCountDocument> findByRecipeId(Long recipeId);
}
