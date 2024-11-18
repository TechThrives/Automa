package com.automa.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.automa.dto.MessageResponse;
import com.automa.services.implementation.CustomUserDetailsService;
import com.automa.services.implementation.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = new UrlPathHelper().getPathWithinApplication(request);

        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtService.extractJwtTokenFromCookies(request);

        if (jwtToken != null) {
            try {
                String username = jwtService.extractUsername(jwtToken);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                MessageResponse messageResponse = new MessageResponse(
                        "Token Expired",
                        Collections.singletonList("The token has expired. Please sign in again."));
                response.getWriter().write(new ObjectMapper().writeValueAsString(messageResponse));
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                MessageResponse messageResponse = new MessageResponse(
                        "Authentication Error",
                        Collections.singletonList("An error occurred during authentication: " + e.getMessage()));
                response.getWriter().write(new ObjectMapper().writeValueAsString(messageResponse));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
