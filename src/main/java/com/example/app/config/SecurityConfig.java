package com.example.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.app.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("Configuring security filter chain");

		http.cors().and().csrf().disable() // tắt CSRF (phù hợp cho API REST).
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // không dùng session để lưu
																							// user → chỉ dùng JWT.
				.and().authorizeHttpRequests(authorize -> authorize
						// Public endpoints
						.requestMatchers("/api/auth/**").permitAll().requestMatchers("/actuator/health").permitAll()
						.requestMatchers("/actuator/info").permitAll().requestMatchers("/api/gemini/**").permitAll()

						// Student endpoints - accessible by all authenticated users
						.requestMatchers("/api/student/**").hasAuthority("ROLE_SINH_VIÊN")

						// Lecturer endpoints - accessible by lecturers and principals
						.requestMatchers("/api/teacher/**").hasAuthority("ROLE_GIẢNG_VIÊN")

						// Principal endpoints - only principals
						.requestMatchers("/api/**").hasAuthority("ROLE_HIỆU_TRƯỞNG")

						// All other requests need authentication
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		/*
		 * Đưa JwtAuthenticationFilter chạy trước UsernamePasswordAuthenticationFilter.
		 * 
		 * Nhờ đó, mỗi request sẽ được kiểm tra JWT trước khi đi vào Spring Security.
		 */

		logger.info("Security filter chain configured successfully");
		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {
		logger.info("Configuring CORS filter");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedOrigin("http://127.0.0.1:4200");
		config.addAllowedOrigin("http://127.0.0.1:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	// dùng để mã hoá mật khẩu trước khi lưu vào DB
	@Bean
	public PasswordEncoder passwordEncoder() {
		logger.info("Configuring BCrypt password encoder");
		return new BCryptPasswordEncoder();
	}

	// Là object trung tâm của Spring Security để xử lý authentication, dùng trong
	// AuthService
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		logger.info("Configuring authentication manager");
		return authConfig.getAuthenticationManager();
	}
}
