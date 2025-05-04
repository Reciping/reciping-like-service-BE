package com.three.recipinglikeservicebe.like.repository;

import com.three.recipinglikeservicebe.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

}
