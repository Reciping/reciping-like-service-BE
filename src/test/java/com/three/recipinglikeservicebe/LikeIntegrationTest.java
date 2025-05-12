package com.three.recipinglikeservicebe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.recipinglikeservicebe.like.document.Like;
import com.three.recipinglikeservicebe.like.dto.LikeRequestDto;
import com.three.recipinglikeservicebe.like.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://mongoadmin:mongo123@localhost:27017/reciping_like_service?authSource=admin"
})
public class LikeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        likeRepository.deleteAll();
    }

    @Test
    @DisplayName("좋아요 생성 테스트")
    void createLikeTest() throws Exception {
        LikeRequestDto requestDto = new LikeRequestDto(1L, 100L);

        mockMvc.perform(post("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.recipeId").value(100L));

        assertThat(likeRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("좋아요 삭제 테스트")
    void deleteLikeTest() throws Exception {
        Like like = new Like(1L, 200L);
        like.setCreatedAt(LocalDateTime.now());
        Like saved = likeRepository.save(like);

        mockMvc.perform(delete("/api/v1/likes/{likeId}", saved.getId()))
                .andExpect(status().isOk());

        assertThat(likeRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("좋아요 상태 조회 (개수 + 사용자 상태)")
    void getLikeStatusTest() throws Exception {
        likeRepository.save(new Like(1L, 300L)); // 내가 누른 좋아요
        likeRepository.save(new Like(2L, 300L)); // 다른 사람 좋아요

        mockMvc.perform(get("/api/v1/likes/recipe/{recipeId}/status", 300L)
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLiked").value(true))
                .andExpect(jsonPath("$.likeCount").value(2));
    }

    @Test
    @DisplayName("좋아요 상태 리스트 조회 테스트")
    void getLikeStatusListTest() throws Exception {
        likeRepository.save(new Like(1L, 100L));
        likeRepository.save(new Like(2L, 100L));
        likeRepository.save(new Like(2L, 200L));

        String requestBody = """
    {
      "recipeIdList": [100, 200, 300],
      "userId": 1
    }
    """;

        mockMvc.perform(post("/api/v1/likes/recipe/status-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].recipeId").value(100))
                .andExpect(jsonPath("$.data[0].likeCount").value(2))
                .andExpect(jsonPath("$.data[0].isLiked").value(true))

                .andExpect(jsonPath("$.data[1].recipeId").value(200))
                .andExpect(jsonPath("$.data[1].likeCount").value(1))
                .andExpect(jsonPath("$.data[1].isLiked").value(false))

                .andExpect(jsonPath("$.data[2].recipeId").value(300))
                .andExpect(jsonPath("$.data[2].likeCount").value(0))
                .andExpect(jsonPath("$.data[2].isLiked").value(false));
    }

}
