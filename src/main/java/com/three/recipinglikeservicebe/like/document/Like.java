package com.three.recipinglikeservicebe.like.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "likes")
public class Like {

    @Id
    private String id; // MongoDB에서는 String이 기본

    private Long userId;
    private Long recipeId;
    private LocalDateTime createdAt;

    // userId, recipeId를 받는 생성자 + createdAt 자동 설정
    public Like(Long userId, Long recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.createdAt = LocalDateTime.now();
    }
}
