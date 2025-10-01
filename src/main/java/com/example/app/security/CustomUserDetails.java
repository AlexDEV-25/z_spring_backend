package com.example.app.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.app.model.User;

// dùng để tạo token trong JwtTokenProvider
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L; // hoặc bất kỳ số nào bạn chọn
	private final User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roleName = mapRoleIdToRoleName(user.getRoleId());
		return Collections.singletonList(new SimpleGrantedAuthority(roleName));
	}

	private String mapRoleIdToRoleName(Long roleId) {
		if (roleId == null)
			return "ANONYMOUS";
		switch (roleId.intValue()) {
		case 1:
			return "HIỆU_TRƯỞNG";
		case 2:
			return "GIẢNG_VIÊN";
		case 3:
			return "SINH_VIÊN";
		case 4:
			return "ADMIN";
		default:
			return "ANONYMOUS";
		}
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
