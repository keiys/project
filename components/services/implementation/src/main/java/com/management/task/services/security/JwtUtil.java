package com.management.task.services.security;

import com.management.task.management.services.exceptions.userexceptions.UserBadRequestException;
import com.management.task.services.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.*;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "taskmanagementkey";
    private long accessTokenValidity = 300;
    private final JwtParser jwtParser;
    private String TOKEN_HEADER = "Authorization";
    private String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() { this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);}

    public String createToken(UserEntity userEntity) {
        Claims claims = Jwts.claims().setSubject(userEntity.getEmail());
        claims.put("first_name", userEntity.getName());
        claims.put("last_name", userEntity.getSurname());
        claims.put("role", userEntity.getRole());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.SECONDS.toMillis(accessTokenValidity));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims parsJwtClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest request){

        try{
            String token = resolveToken(request);
            if(token != null){
                return parsJwtClaims(token);
            }
            return null;
        }catch (ExpiredJwtException ex){
            throw new UserBadRequestException(ex.getMessage());
        }catch (Exception ex){
            throw new UserBadRequestException("unauthorized");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims){return claims.getExpiration().after(new Date());}
}
