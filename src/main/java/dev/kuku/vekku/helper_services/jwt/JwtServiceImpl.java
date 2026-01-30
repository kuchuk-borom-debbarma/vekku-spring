package dev.kuku.vekku.helper_services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  @Value("${vekku.security.secret}")
  private String secretKey;

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  @Override
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
        .signWith(getSignInKey(), Jwts.SIG.HS256)
        .compact();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSignInKey() {
    // Since our secret is likely shorter than 256 bits if interpreted as hex/base64 directly
    // without padding,
    // or just a raw string, we'll ensure we use a secure key derivation or just bytes.
    // For simplicity with the existing random string:
    byte[] keyBytes = secretKey.getBytes();
    // If the key is Base64 encoded: keyBytes = Decoders.BASE64.decode(secretKey);
    // Assuming the secret provided earlier is just a raw alphanumeric string.
    // Ideally, use a proper Base64 encoded 256-bit key.
    // For now, let's use the provided secret bytes directly but ensure it's long enough.
    // HMAC-SHA256 requires 256 bits (32 bytes). The generated secret was 32 chars long.
    // So simple getBytes() is fine if treating as raw UTF-8 bytes.

    // HOWEVER, standard practice with JJWT is often Base64.
    // Let's treat the config value as the "seed" material.
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
