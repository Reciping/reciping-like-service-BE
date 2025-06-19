package com.three.recipinglikeservicebe.global.logger;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.MDC;

@Slf4j
public class CustomLogger {

    public static void track(
            Logger logger,
            LogType logType,
            String targetId,
            String payload,
            HttpServletRequest request
    ) {
        try {
            LogContextUtil.putLogLevel("INFO");
            LogContextUtil.putLogType(logType.name());

            LogContextUtil.putRequestContextToMDC(request);
            LogContextUtil.putCustomContext(targetId, payload);

            logger.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                    MDC.get("log_level"),
                    logType.name(),
                    MDC.get("actor_type"),
                    MDC.get("event_timestamp"),
                    MDC.get("request_path"),
                    MDC.get("http_method"),
                    MDC.get("user_id"),
                    MDC.get("target_id"),
                    MDC.get("payload"),
                    MDC.get("client_ip"),
                    MDC.get("user_agent"),
                    MDC.get("referer")
            ));
        } finally {
            LogContextUtil.clear();
        }
    }
}