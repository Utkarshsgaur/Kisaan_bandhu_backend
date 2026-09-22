package com.procurement.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.util.Date;
@Service public class JwtService {
 private final SecretKey key; private final long expiration;
 public JwtService(@Value("${jwt.secret}") String secret,@Value("${jwt.expiration-ms}") long expiration){ if(secret.length()<32) throw new IllegalArgumentException("JWT secret must be at least 32 characters"); this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expiration=expiration; }
 public String generate(Long userId,String phone,String role){ Date now=new Date(); return Jwts.builder().subject(phone).claim("userId",userId).claim("role",role).issuedAt(now).expiration(new Date(now.getTime()+expiration)).signWith(key).compact(); }
 public String extractPhone(String token){return parse(token).getPayload().getSubject();} public String extractRole(String token){return parse(token).getPayload().get("role",String.class);} public Long extractUserId(String token){return parse(token).getPayload().get("userId",Long.class);} public boolean isValid(String token){try{parse(token);return true;}catch(JwtException|IllegalArgumentException e){return false;}}
 private Jws<Claims> parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);}
}
