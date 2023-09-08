package com.company.youse.security;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * This class handles basic functionality requires for JSON web tokens (JWT)
 */
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // todo jwtSecret and jwtExpiration are application parameters, so they can be included in the application properties file
    // A secret key, that is required to encode and decode the JWT tokens
    private String jwtSecret = "jwtYouseAppSecretKey6%5$";


    /*
     * Default constructor
     */
    public JwtProvider(){

    }

    /**
     * Generate a new JWT token from the give username/email
     * @param username
     * @return
     */
    public String generateJwtTokenWithUsername(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                //one hour equals 3 600 000 millis
                .setExpiration(new Date(System.currentTimeMillis()+ 3600000 ))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * method to generate refreshToken, should not expire
     * @param subject
     * @return
     */
    public String createRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 3600000* 24*20 ))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    /**
     * method validated token
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromJwtToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    /**
     * Parse a JWT token and validate it
     * @param authToken
     * @return
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        } catch (Exception e) {
            logger.error("Might be Invalid JWT signature -> Message: {} ", e);
        }

        return false;
    }

    /**
     * Retrieve the username/email encoded within the JWT
     * @param token
     * @return
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * Retrieve the username/email encoded within the JWT
     * @param token
     * @return
     */
    public String getUserNameFromExpiredJwtToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        }catch (ExpiredJwtException e){
//            System.out.println(e.getClaims().getSubject());
            return e.getClaims().getSubject();
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}
