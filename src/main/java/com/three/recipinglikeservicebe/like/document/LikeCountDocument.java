package com.three.recipinglikeservicebe.like.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "recipe_like_counts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeCountDocument {

    @Id
    private Long recipeId;

    private long likeCount;

    private LocalDateTime updatedAt;

}
