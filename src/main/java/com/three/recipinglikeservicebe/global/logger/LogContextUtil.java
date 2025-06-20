package com.three.recipinglikeservicebe.global.logger;

import com.three.recipinglikeservicebe.global.security.UserDetailsImpl;
import com.three.recipinglikeservicebe.global.util.CustomIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class LogContextUtil {

    public static void putRequestContextToMDC(HttpServletRequest request) {
        put("event_timestamp", LocalDateTime.now().toString());
        put("request_path", request.getRequestURI());
        put("http_method", request.getMethod());
        put("client_ip", CustomIpUtil.getClientIp(request));
        put("user_agent", request.getHeader("User-Agent"));
        put("referer", request.getHeader("Referer"));
        put("user_id", resolveUserId());
        put("actor_type", resolveActorRole().name());
    }

    public static void putCustomContext(String targetId, String payload) {
        put("target_id", targetId);
        put("payload", payload);
    }

    public static void putLogLevel(String level) {
        put("log_level", level);
    }

    public static void putLogType(String type) {
        put("log_type", type);
    }

    public static void clear() {
        MDC.clear();
    }

    private static void put(String key, String value) {
        MDC.put(key, value != null ? value : "-");
    }

    private static String resolveUserId() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) return null;

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                return String.valueOf(userDetails.getUserId()); // ← 여기서 명확히 String 변환
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static LogActorType resolveActorRole() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getAuthorities() == null) {
                return LogActorType.GUEST;
            }

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();
                if (role.contains("ADMIN")) return LogActorType.ADMIN;
                if (role.contains("USER")) return LogActorType.USER;
            }
            return LogActorType.GUEST;
        } catch (Exception e) {
            return LogActorType.GUEST;
        }
    }
}