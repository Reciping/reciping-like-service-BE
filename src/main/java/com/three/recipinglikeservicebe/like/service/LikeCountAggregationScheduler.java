package com.three.recipinglikeservicebe.like.service;

import com.three.recipinglikeservicebe.like.document.LikeCountDocument;
import com.three.recipinglikeservicebe.like.repository.LikeAggregationRepository;
import com.three.recipinglikeservicebe.like.repository.LikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeCountAggregationScheduler {

    private final LikeAggregationRepository likeAggregationRepository;
    private final LikeCountRepository likeCountRepository;

    // [1] 실제 로직을 별도 메서드로 분리
    public void aggregateAndSaveLikeCountsManually() {
        List<LikeCountDocument> counts = likeAggregationRepository.aggregateAndSaveLikeCounts();
        likeCountRepository.saveAll(counts);
    }

    // [2] 스케줄러는 해당 메서드만 호출
    @Scheduled(cron = "0 0 * * * *") // 매시간 정각
    public void aggregateAndSaveBySchedule() {
        aggregateAndSaveLikeCountsManually();
    }
}
