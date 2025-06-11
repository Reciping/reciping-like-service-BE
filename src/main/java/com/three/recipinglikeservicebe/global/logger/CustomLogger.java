package com.three.recipinglikeservicebe.global.logger;

import com.three.recipinglikeservicebe.global.util.CustomIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.MDC;
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
        LocalDateTime eventTime = LocalDateTime.now();
        String clientIp = CustomIpUtil.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");

        try {
            // === MDC 추가 시작 ===
            // JSON 로그를 위해 모든 정보를 key-value 형태로 MDC에 추가합니다.
            MDC.put("log_type", logType.name());
            MDC.put("actor_type", actorType.name());
            MDC.put("event_timestamp", eventTime.toString());
            MDC.put("request_path", path);
            MDC.put("http_method", method);
            MDC.put("user_id", userId != null ? userId : "-");
            MDC.put("transaction_id", transactionId != null ? transactionId : "-");
            MDC.put("target_id", targetId != null ? targetId : "-");
            MDC.put("payload", payload != null ? payload : "-");
            MDC.put("client_ip", clientIp);
            MDC.put("user_agent", userAgent != null ? userAgent : "-");
            MDC.put("referer", referer != null ? referer : "-");
            // === MDC 추가 끝 ===


            // logger.info()는 한번만 호출합니다. 이 한 번의 호출이 두 Appender 모두에게 전달됩니다.
            // CSV 로거는 아래 포맷팅된 문자열을 사용합니다.
            // JSON 로거는 MDC 데이터를 사용하여 구조화된 로그를 만들고, 아래 문자열을 'message' 필드에 넣습니다.
            logger.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                    logType.name(),
                    actorType.name(),
                    eventTime,
                    path,
                    method,
                    userId != null ? userId : "-",
                    transactionId != null ? transactionId : "-",
                    targetId != null ? targetId : "-",
                    payload != null ? payload : "-",
                    clientIp,
                    userAgent,
                    referer
            ));
        } finally {
            // === MDC 정리 시작 ===
            // 현재 스레드의 MDC에서 사용한 키들을 반드시 제거하여 다른 로그에 영향을 주지 않도록 합니다.
            MDC.remove("log_type");
            MDC.remove("actor_type");
            MDC.remove("event_timestamp");
            MDC.remove("request_path");
            MDC.remove("http_method");
            MDC.remove("user_id");
            MDC.remove("transaction_id");
            MDC.remove("target_id");
            MDC.remove("payload");
            MDC.remove("client_ip");
            MDC.remove("user_agent");
            MDC.remove("referer");
            // === MDC 정리 끝 ===
        }
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