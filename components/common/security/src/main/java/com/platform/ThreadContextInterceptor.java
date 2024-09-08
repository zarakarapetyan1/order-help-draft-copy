package com.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.enums.AccountType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Configuration


public class ThreadContextInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NoAuthorizationRequired noAuthorizationRequired = handlerMethod.getMethod().getAnnotation(NoAuthorizationRequired.class);

        if (noAuthorizationRequired == null) {
            String header = request.getHeader("Authorization");
            String accountType = getAccountType(header);
            RequiredImporterType requiredImporterType = handlerMethod.getMethod().getAnnotation(RequiredImporterType.class);
            if (requiredImporterType == null) {
                if (!accountType.equals(AccountType.IMPORTER.toString())) {
                    throw new SecurityException();
                }
            }
        }
        return true;
    }

    private String getAccountType(String token) throws IOException {
        String s = token.split("\\.")[1];
        byte[] decode = Base64.getUrlDecoder().decode(s);

        return new ObjectMapper().readTree(decode).get("accountType").asText();

    }
}
