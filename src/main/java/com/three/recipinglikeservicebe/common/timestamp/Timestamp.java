package com.three.recipinglikeservicebe.common.timestamp;
//
////import jakarta.persistence.Column;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.MappedSuperclass;
//import jakarta.persistence.PrePersist;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import lombok.Getter;
//import org.springframework.data.annotation.CreatedDate;
//
//@Getter
//@MappedSuperclass
////@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamp {
//
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column
//    private LocalDateTime deletedAt;
//
//    @PrePersist
//    public void prePersist() {
//        ZoneId zoneId = ZoneId.of("Asia/Seoul");
//        ZonedDateTime now = ZonedDateTime.now(zoneId);
//        this.createdAt = now.toLocalDateTime();
//    }
//
}
