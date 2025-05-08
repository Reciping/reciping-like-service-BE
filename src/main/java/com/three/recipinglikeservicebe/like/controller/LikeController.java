package com.three.recipinglikeservicebe.like.controller;

import com.three.recipinglikeservicebe.like.document.Like;
import com.three.recipinglikeservicebe.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public Like createLike(@RequestBody Like like, HttpServletRequest request) {
        String userIdFromToken = (String) request.getAttribute("userId");
        like.setUserId(Long.parseLong(userIdFromToken));
        return likeService.createLike(like);
    }

    @GetMapping
    public List<Like> getLikes() {
        return likeService.getAllLikes();
    }

    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable String id) {
        likeService.deleteLike(id);
    }
}
