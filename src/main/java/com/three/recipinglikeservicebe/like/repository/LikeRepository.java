package com.three.recipinglikeservicebe.like.repository;

import com.three.recipinglikeservicebe.like.document.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {
    // 필요하면 findByUserId(Long userId) 같은 쿼리 추가 가능
}
