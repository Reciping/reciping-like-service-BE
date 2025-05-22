package com.three.recipinglikeservicebe.global.logger;

import com.three.recipinglikeservicebe.global.util.CustomIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLogger {

    public static void track(
            LogType logType,
            String path,
            String method,
            String userId,
            String transactionId, // GET 메서드에는 transactionId가 없으므로 null로 들어감
            String targetId,
            String payload,
            HttpServletRequest request
    ) {
        log.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                logType.name(),                               // 로그 타입
                LocalDateTime.now(),                          // 시간
                path,                                         // 요청 경로
                method,                                       // GET, POST 등
                userId != null ? userId : "-",
                transactionId != null ? transactionId : "-",
                targetId != null ? targetId : "-",           // 타겟 (recipeId, eventId 등)
                payload != null ? payload : "-",             // 부가정보 (검색어 등)
                CustomIpUtil.getClientIp(request),           // 사용자 IP
                request.getHeader("User-Agent"),          // 브라우저
                request.getHeader("Referer")              // 이전 페이지
        ));
    }
}