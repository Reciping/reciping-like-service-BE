package com.three.recipinglikeservicebe.like.repository;

import com.three.recipinglikeservicebe.like.document.LikeCountDocument;

import java.util.List;

public interface LikeAggregationRepository {
    List<LikeCountDocument> aggregateAndSaveLikeCounts();
}
