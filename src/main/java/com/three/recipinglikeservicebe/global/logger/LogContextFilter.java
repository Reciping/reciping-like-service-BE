package com.three.recipinglikeservicebe.global.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LogContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 요청의 메타 데이터를 MDC에 넣음
            LogContextUtil.putRequestContextToMDC(request);
            // 다음 필터나 서블릿으로 전달
            filterChain.doFilter(request, response);
        } finally {
            // 요청이 끝나면 MDC 초기화
            LogContextUtil.clear();
        }
    }
}