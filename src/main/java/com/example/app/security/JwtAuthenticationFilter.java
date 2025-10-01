package com.example.app.security;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// kiểm tra token và set user vào Spring Security(tiến hành sau khi đăng nhập)
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

//  kiểm tra khi đăng nhập nếu đăng nhập thành công tiến hành phân quyền, nếu đăng nhập thất bại thì không
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		logger.debug("Processing request for path: {}", path);

		// Bỏ qua các endpoint public
		if (path.startsWith("/api/auth/") || path.startsWith("/actuator/health") || path.startsWith("/actuator/info")) {
			logger.debug("Skipping authentication for public endpoint: {}", path);
			filterChain.doFilter(request, response);
			return;
		}

		String token = getJwtFromRequest(request);

		if (token != null && jwtTokenProvider.validateToken(token)) {
			try {
				String username = jwtTokenProvider.getUsernameFromToken(token);
				String role = jwtTokenProvider.getRoleFromToken(token);

				logger.debug("Valid token found for user: {} with role: {}", username, role);

				if (role != null && !role.isEmpty()) {
					// Thêm ROLE_ prefix để tương thích với hasRole()
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

					UserDetails userDetails = new User(username, "", Collections.singletonList(authority));

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
					logger.debug("Authentication set for user: {}", username);
				}
			} catch (Exception e) {
				logger.error("Error processing JWT token", e);
				SecurityContextHolder.clearContext();
			}
		} else {
			logger.debug("No valid token found for path: {}", path);
		}

		filterChain.doFilter(request, response);
	}

//	lấy JWT từ request
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		logger.debug("Authorization header: {}", bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			String token = bearerToken.substring(7);
			logger.debug("Extracted JWT token: {}...", token.substring(0, Math.min(20, token.length())));
			return token;
		}
		logger.debug("No Bearer token found in Authorization header");
		return null;
	}
}
