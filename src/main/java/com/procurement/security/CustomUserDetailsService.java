package com.procurement.security;
import com.procurement.entity.User; import com.procurement.repository.UserRepository; import org.springframework.security.core.userdetails.*; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.stereotype.Service;
@Service public class CustomUserDetailsService implements UserDetailsService {
 private final UserRepository users; public CustomUserDetailsService(UserRepository users){this.users=users;}
 public UserDetails loadUserByUsername(String phone){ User u=users.findByPhoneNumber(phone).orElseThrow(()->new UsernameNotFoundException("User not found")); return new org.springframework.security.core.userdetails.User(u.getPhoneNumber(),"{noop}OTP_LOGIN",u.isActive(),true,true,true,java.util.List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name()))); }
}
