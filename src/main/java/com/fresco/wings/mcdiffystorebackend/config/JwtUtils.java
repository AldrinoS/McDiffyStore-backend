package com.fresco.wings.mcdiffystorebackend.config;

import com.fresco.wings.mcdiffystorebackend.model.Role;
import com.fresco.wings.mcdiffystorebackend.model.User;
import com.fresco.wings.mcdiffystorebackend.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Autowired
    private UserRepo userRepo;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.validity}")
    private Integer jwtValidity;

    private String doGenerateToken(List<Role> roles, String username) {

        return Jwts.builder()
                .claim("role", roles.toString())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() * jwtValidity))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateToken(User user) {
        return doGenerateToken(user.getRoles(), user.getUsername());
    }

    private Claims getAllClaim(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken (String token, Function<Claims, T> claimsResolver) {
        Claims allClaim = getAllClaim(token);
        return claimsResolver.apply(allClaim);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);

    }

    public boolean isTokenValid(String token) {
//        String username = getUsernameFromToken(token);
        return isTokenExpired(token);
    }
}
