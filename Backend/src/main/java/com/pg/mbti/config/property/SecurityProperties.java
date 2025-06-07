package com.pg.mbti.config.property;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import java.util.List;

/**
 * Application-wide constants for configuration.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityProperties {

    public static final class Security {
        public static final String SESSION_COOKIE_NAME = "MY_SESSION_COOKIE";
        public static final ClearSiteDataHeaderWriter.Directive COOKIE_DIRECTIVE =
                ClearSiteDataHeaderWriter.Directive.COOKIES;
        public static final int MAX_SESSIONS = 1;
    }

    public static final class Endpoints {
        public static final String REGISTER = "/api/auth/register";
        public static final String LOGIN = "/api/auth/login";
        public static final String CONFIRM_EMAIL = "/api/auth/confirm-email";
        public static final String FORGOT_PASSWORD = "/api/auth/forgot-password";
        public static final String MBTI_ENUMS = "/api/enums/mbti";
        public static final String ANSWERS = "/api/answers";
        public static final String QUESTIONS = "/api/questions";
        public static final String MBTI_TEST_TRAIN = "/api/mbti/train";
        public static final String MBTI_TEST_NEXT_STEP = "/api/mbti/step";
        public static final String API_DOCS = "/v3/api-docs";
        public static final String SWAGGER_UI = "/swagger-ui/**";
        public static final String SWAGGER_RESOURCES = "/swagger-resources/**";

        public static final String LOGOUT = "/api/auth/logout";

        public static final String[] PUBLIC_ENDPOINTS = {
                REGISTER, LOGIN, CONFIRM_EMAIL, FORGOT_PASSWORD,
                MBTI_ENUMS, ANSWERS, QUESTIONS, MBTI_TEST_TRAIN,
                API_DOCS, SWAGGER_RESOURCES, SWAGGER_UI, MBTI_TEST_NEXT_STEP
        };
    }

    public static final class Cors {
        public static final List<String> ALLOWED_ORIGIN_PATTERNS = List.of("*");
        public static final List<String> ALLOWED_METHODS =
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        public static final List<String> ALLOWED_HEADERS =
                List.of("Authorization", "Content-Type");
        public static final boolean ALLOW_CREDENTIALS = true;
    }
}