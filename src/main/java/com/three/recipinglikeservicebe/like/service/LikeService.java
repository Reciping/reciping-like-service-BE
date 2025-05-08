package com.three.recipinglikeservicebe.like.service;

import com.three.recipinglikeservicebe.like.document.Like;
import com.three.recipinglikeservicebe.like.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like createLike(Like like) {
        return likeRepository.save(like);
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public void deleteLike(String id) {
        likeRepository.deleteById(id);
    }
}
