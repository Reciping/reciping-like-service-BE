package com.three.recipinglikeservicebe.like.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeCount {
    @Field("_id")
    private Long recipeId;

    private Long likeCount;
}
