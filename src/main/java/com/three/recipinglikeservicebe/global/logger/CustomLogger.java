package com.three.recipinglikeservicebe.global.logger;

import com.three.recipinglikeservicebe.global.util.CustomIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class CustomLogger {

    public static void track(
            Logger logger,
            LogType logType,
            String path,
            String method,
            String userId,
            String transactionId, // GET 메서드에는 transactionId가 없으므로 null로 들어감
            String targetId,
            String payload,
            HttpServletRequest request
    ) {
        LogActorType actorType = resolveActorRole();

        logger.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                logType.name(),                               // 로그 타입
                actorType.name(),
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
    private static LogActorType resolveActorRole() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getAuthorities() == null) {
                return LogActorType.GUEST;
            }

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority(); // e.g. ROLE_ADMIN
                if (role.contains("ADMIN")) return LogActorType.ADMIN;
                if (role.contains("USER")) return LogActorType.USER;
            }

            return LogActorType.GUEST;
        } catch (Exception e) {
            return LogActorType.GUEST;
        }
    }
}