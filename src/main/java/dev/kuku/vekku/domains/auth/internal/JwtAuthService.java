package dev.kuku.vekku.domains.auth.internal;

import dev.kuku.vekku.domains.auth.AuthService;
import dev.kuku.vekku.domains.auth.exceptions.UsernameOrEmailAlreadyUsed;
import dev.kuku.vekku.domains.auth.internal.models.AuthUser;
import dev.kuku.vekku.domains.auth.internal.repos.VekkuUserRepo;
import dev.kuku.vekku.domains.auth.models.SignInData;
import dev.kuku.vekku.domains.auth.models.VekkuUser;
import dev.kuku.vekku.domains.auth.params.SignInParam;
import dev.kuku.vekku.domains.auth.params.StartSignupParam;
import dev.kuku.vekku.domains.auth.params.VerifySignupToken;
import dev.kuku.vekku.helper_services.encrypter.EncryptionService;
import dev.kuku.vekku.helper_services.jwt.JwtService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtAuthService implements AuthService {
  private final VekkuUserRepo userRepo;
  private final EncryptionService encryptionService;
  private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Override
  public String generateSignupToken(StartSignupParam input) {
    log.info("Start sign up {}", input);
    // Validate if username or email is used
    var vu = userRepo.findByUsernameOrEmail(input.username(), input.email());
    if (vu != null) {
      throw new UsernameOrEmailAlreadyUsed(input.username(), input.email());
    }
    // Generate encrypted token with username, email and password. No validation done. We will do it
    // on verify step
    return encryptionService.encryptObject(input);
  }

  @Override
  public StartSignupParam verifySignupToken(VerifySignupToken input) {
    if (input == null || !StringUtils.hasText(input.token())) {
      throw new IllegalArgumentException("input or input.token() is null/empty");
    }

    log.info("Verifying sign up token {}", input);
    return encryptionService.decryptToken(input.token(), StartSignupParam.class);
  }

  @Override
  public VekkuUser createUser(StartSignupParam input) {
    log.info("Creating user for {}", input.username());
    // Hash the password before saving
    String hashedPassword = passwordEncoder.encode(input.password());

    StartSignupParam secureInput =
        new StartSignupParam(input.username(), input.email(), hashedPassword);

    return userRepo.createUser(secureInput);
  }

  @Override
  public SignInData signIn(SignInParam input) {
    AuthUser authUser = userRepo.findAuthUserByUsernameOrEmail(input.key(), input.key());

    if (authUser == null) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    if (!passwordEncoder.matches(input.password(), authUser.hashedPassword())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    // Create simple UserDetails for JWT generation
    UserDetails userDetails =
        new User(authUser.user().username(), authUser.hashedPassword(), Collections.emptyList());

    String token = jwtService.generateToken(userDetails);
    return new SignInData(token);
  }

  @Override
  public VekkuUser getUserById(String id) {
    log.info("Fetching user by id: {}", id);
    return userRepo.getUserById(id);
  }
}
