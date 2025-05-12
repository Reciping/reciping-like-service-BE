package com.three.recipinglikeservicebe.like.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "likes")
@CompoundIndex(def = "{'user_id': 1, 'recipe_id': 1}", name = "user_recipe_idx")
public class Like {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("recipe_id")
    private Long recipeId;

    @Field("created_at")
    private LocalDateTime createdAt;

    public Like(Long userId, Long recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.createdAt = LocalDateTime.now();
    }
}
