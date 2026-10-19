package com.leonardo.worldcup_stickers.config;

import java.io.IOException;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpStatus;
import tools.jackson.databind.ObjectMapper;

import com.leonardo.worldcup_stickers.dto.ErrorResponseDto;
import com.leonardo.worldcup_stickers.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String USER_ID_ATTRIBUTE = "user_id";

    private static final List<String> PUBLIC_PATHS = List.of(
            "POST /auth/signin",
            "POST /users");

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String target = request.getMethod() + " " + request.getRequestURI();
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, target));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            unauthorized(response, "Missing token");
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtService.decode(token);
            Long user_id = Long.valueOf(claims.getSubject());
            request.setAttribute(USER_ID_ATTRIBUTE, user_id);
        } catch (JwtException | IllegalArgumentException e) {
            unauthorized(response, "Invalid or expired token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ErrorResponseDto.of(
                HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message));
    }
}
