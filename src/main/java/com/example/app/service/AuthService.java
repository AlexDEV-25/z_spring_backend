package com.example.app.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.dto.AuthResponse;
import com.example.app.dto.RegisterRequest;
import com.example.app.exception.DuplicateResourceException;
import com.example.app.exception.ResourceNotFoundException;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.security.JwtTokenProvider;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	private final AuthenticationManager authenticationManager; // công cụ của Spring Security để xác thực
																// username/password.(trong SecurityConfig)
	private final UserRepository userRepository;// repository bình thường thôi :))
	private final JwtTokenProvider jwtTokenProvider;// class custom để tạo và kiểm tra JWT token.
	private final UserDetailsService userDetailsService;// load thông tin user từ DB (Spring Security chuẩn), là class
														// CustomUserDetailsService.
	private final PasswordEncoder passwordEncoder;// mã hóa mật khẩu (dùng BCryptPasswordEncoder).

	public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
			JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	public AuthResponse authenticate(String username, String password) {
		try {
			logger.info("Attempting authentication for user: {}", username);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(//
					username, password)); // kiểm tra username & password
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			String token = jwtTokenProvider.generateToken(userDetails);
			String role = jwtTokenProvider.getRoleFromToken(token);
			LocalDateTime expiresAt = jwtTokenProvider.getExpirationFromToken(token);

			logger.info("Authentication successful for user: {}", username);
			return new AuthResponse(token, username, role, expiresAt);
		} catch (BadCredentialsException e) {
			logger.warn("Authentication failed for user: {}", username);
			throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không đúng");
		} catch (Exception e) {
			logger.error("Unexpected error during authentication for user: {}", username, e);
			throw new RuntimeException("Đã xảy ra lỗi trong quá trình xác thực");
		}
	}

//  có thể bỏ
	public User register(RegisterRequest request) {
		logger.info("Attempting to register new user: {}", request.getUsername());

		// Check if username already exists
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			logger.warn("Registration failed: Username already exists: {}", request.getUsername());
			throw new DuplicateResourceException("Tên đăng nhập đã tồn tại");
		}

		// Check if email already exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			logger.warn("Registration failed: Email already exists: {}", request.getEmail());
			throw new DuplicateResourceException("Email đã tồn tại");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setRoleId(request.getRoleId());

		User savedUser = userRepository.save(user);
		logger.info("User registered successfully: {}", savedUser.getUsername());
		return savedUser;
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));
	}
}