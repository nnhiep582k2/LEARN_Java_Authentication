package com.auth.user_management.services;

import com.auth.user_management.constants.AppSettingsConfig;
import com.auth.user_management.dtos.AuthenticationResponse;
import com.auth.user_management.dtos.RefreshTokenRequest;
import com.auth.user_management.repositories.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    private AppSettingsConfig appSettingsConfig;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private BlacklistService blacklistService;
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getVerificationKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PrivateKey getSignInKey() {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(appSettingsConfig.getPrivateKey()));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PublicKey getVerificationKey() {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(appSettingsConfig.getPublicKey()));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthenticationResponse generateToken(UserDetails userDetails) {
        String accessToken = generateAccessToken(new HashMap<>(), userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private void saveRefreshToken(String email, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, 7, TimeUnit.DAYS);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        try {
            String refreshToken = UUID.randomUUID().toString();
            saveRefreshToken(userDetails.getUsername(), refreshToken);
            return refreshToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && validateAccessToken(token);
    }

    private boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getVerificationKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateRefreshToken(RefreshTokenRequest request) {
        String key = REFRESH_TOKEN_PREFIX + request.getEmail();
        String storedRefreshToken = (String)redisTemplate.opsForValue().get(key);

        return storedRefreshToken != null && storedRefreshToken.equals(request.getRefreshToken());
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String refreshToken(RefreshTokenRequest request) {
        try {
            if (!validateRefreshToken(request)) return "";
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            return generateAccessToken(new HashMap<>(), user);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void logout(String authHeader, String email) {
        String token = authHeader.substring(7);
        blacklistService.addToBlacklist(token);

        String key = REFRESH_TOKEN_PREFIX + email;
        String storedRefreshToken = (String)redisTemplate.opsForValue().get(key);
        if (storedRefreshToken != null) {
            redisTemplate.delete(token);
        }
    }
}
