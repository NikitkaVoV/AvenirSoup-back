package ru.nikitavov.soup.web.security.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.nikitavov.soup.database.model.security.RefreshToken;
import ru.nikitavov.soup.database.repository.realisation.RefreshTokenMyRepository;
import ru.nikitavov.soup.general.model.tuple.Tuple2;
import ru.nikitavov.soup.web.security.AppProperties;
import ru.nikitavov.soup.web.security.data.UserPrincipal;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final AppProperties appProperties;
    private final RefreshTokenMyRepository refreshTokenRepository;

    public Tuple2<String, Date> createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        String generateToken = generateToken(userPrincipal.getUuid(), expiryDate, appProperties.getAuth().getTokenSecret());
        return new Tuple2<>(generateToken, expiryDate);
    }

    public String generateToken(Object param, Date expiryDate, String secret) {
        return Jwts.builder()
                .setSubject(param.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserUUIDFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenRefreshExpirationMsec());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String generateToken = generateToken(userPrincipal.getUuid(), expiryDate, appProperties.getAuth().getTokenRefreshSecret());

        RefreshToken token = RefreshToken.builder().uuid(userPrincipal.getUuid()).token(generateToken).expiryDate(expiryDate).build();
        refreshTokenRepository.save(token);

        return generateToken;
    }

}
