package dev.kuku.vekku.helper_services.encrypter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EncryptionServiceImpl implements EncryptionService {

  private final ObjectMapper objectMapper;

  @Value("${vekku.security.secret}")
  private String secretKey;

  private static final String ALGORITHM = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 128;
  private static final int GCM_IV_LENGTH = 12;

  @Override
  @SneakyThrows
  public String encryptObject(Object payload) {
    if (payload == null) {
      throw new IllegalArgumentException("Payload cannot be null");
    }

    // 1. Serialize to JSON
    String json = objectMapper.writeValueAsString(payload);
    byte[] plaintext = json.getBytes(StandardCharsets.UTF_8);

    // 2. Generate IV
    byte[] iv = new byte[GCM_IV_LENGTH];
    new SecureRandom().nextBytes(iv);

    // 3. Encrypt
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    SecretKey key = getSecretKey();
    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] ciphertext = cipher.doFinal(plaintext);

    // 4. Combine IV + Ciphertext
    byte[] combined = new byte[iv.length + ciphertext.length];
    System.arraycopy(iv, 0, combined, 0, iv.length);
    System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

    // 5. Encode to Base64
    return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
  }

  @Override
  @SneakyThrows
  public <T> T decryptToken(String token, Class<T> clazz) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Token cannot be null or empty");
    }

    // 1. Decode Base64
    byte[] combined = Base64.getUrlDecoder().decode(token);

    // 2. Extract IV
    byte[] iv = new byte[GCM_IV_LENGTH];
    System.arraycopy(combined, 0, iv, 0, iv.length);

    // 3. Extract Ciphertext
    int ciphertextLength = combined.length - GCM_IV_LENGTH;
    byte[] ciphertext = new byte[ciphertextLength];
    System.arraycopy(combined, GCM_IV_LENGTH, ciphertext, 0, ciphertextLength);

    // 4. Decrypt
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    SecretKey key = getSecretKey();
    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    byte[] plaintext = cipher.doFinal(ciphertext);

    // 5. Deserialize
    String json = new String(plaintext, StandardCharsets.UTF_8);
    return objectMapper.readValue(json, clazz);
  }

  private SecretKey getSecretKey() {
    // Ensure key is 32 bytes (256 bits)
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    // In a real app, use a KDF or ensure the config string is exactly 32 chars or a base64 encoded
    // key.
    // For simplicity/robustness here, we'll hash it to ensure correct length if it's a pass-phrase.
    return new SecretKeySpec(sha256(keyBytes), "AES");
  }

  @SneakyThrows
  private byte[] sha256(byte[] input) {
    return java.security.MessageDigest.getInstance("SHA-256").digest(input);
  }
}
