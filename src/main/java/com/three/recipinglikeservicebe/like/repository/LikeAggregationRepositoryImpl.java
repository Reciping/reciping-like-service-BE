package com.three.recipinglikeservicebe.like.repository;

import com.three.recipinglikeservicebe.like.document.LikeCountDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeAggregationRepositoryImpl implements LikeAggregationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<LikeCountDocument> aggregateAndSaveLikeCounts() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("recipe_id").count().as("likeCount"),
                Aggregation.project("likeCount").and("_id").as("recipeId")
        );

        List<LikeCountDocument> results = mongoTemplate
                .aggregate(aggregation, "likes", LikeCountDocument.class)
                .getMappedResults();

        results.forEach(doc -> doc.setUpdatedAt(LocalDateTime.now()));
        return results;
    }
}
