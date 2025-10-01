package com.example.app.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	@Value("${jwt.secret}") // lấy trong application.properties
	private String jwtSecret;

	@Value("${jwt.expiration-ms}") // lấy trong application.properties
	private long jwtExpirationMs; // 7 ngày mặc định từ cấu hình

	// tạo token UserDetails=CustomUserDetails
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		String role = userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");
		claims.put("role", role);

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

		return Jwts.builder() // tạo
				.setClaims(claims)// thêm role vào token
				.setSubject(userDetails.getUsername()) // username
				.setIssuedAt(new Date(System.currentTimeMillis()))// ngày tạo
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))// ngày hết hạn
				.signWith(key, SignatureAlgorithm.HS512).compact();// chữ kí HS512
	}

	// kiểm tra token
	public boolean validateToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// lấy username từ token
	public String getUsernameFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	// lấy role từ token
	public String getRoleFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return (String) claims.get("role");
	}

	// láy hạn từ token
	public LocalDateTime getExpirationFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		Date expiration = claims.getExpiration();
		return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}