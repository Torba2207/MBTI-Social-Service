package com.pg.mbti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom logout success handler to provide a JSON response upon successful logout.
 * This handler is invoked by Spring Security after a user has successfully logged out.
 */
@Component
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper; // Jackson ObjectMapper for JSON serialization

    /**
     * Called when a user has been successfully logged out.
     * Sets the HTTP status to OK, content type to JSON, and writes a success message to the response.
     *
     * @param request The {@link HttpServletRequest}.
     * @param response The {@link HttpServletResponse}.
     * @param authentication The {@link Authentication} object representing the user who logged out (can be null).
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        log.info("User logged out successfully. Authentication: {}", authentication != null ? authentication.getName() : "N/A"); // Log successful logout
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User logged out successfully");

        response.setStatus(HttpServletResponse.SC_OK); // Set HTTP status to 200 OK
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Set content type to application/json
        objectMapper.writeValue(response.getWriter(), responseBody); // Write the JSON response body
        log.debug("Logout success response sent."); // Log that the response has been sent
    }
}