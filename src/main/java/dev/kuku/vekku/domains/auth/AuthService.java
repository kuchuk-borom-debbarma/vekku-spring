package dev.kuku.vekku.domains.auth;

import dev.kuku.vekku.domains.auth.models.SignInData;
import dev.kuku.vekku.domains.auth.models.VekkuUser;
import dev.kuku.vekku.domains.auth.params.SignInParam;
import dev.kuku.vekku.domains.auth.params.StartSignupParam;
import dev.kuku.vekku.domains.auth.params.VerifySignupToken;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
  /**
   * Generate a token containing the user information during sign up
   *
   * @return sign up token containing encrypted data about the user signing up
   */
  String generateSignupToken(StartSignupParam input);

  /**
   * Verify the token
   *
   * @return user data extracted from the token
   */
  @Nullable
  StartSignupParam verifySignupToken(VerifySignupToken input);

  /**
   * Create a user based on input
   *
   * @return created user DTO
   */
  VekkuUser createUser(StartSignupParam input);

  /**
   * Sign in
   *
   * @param input
   * @return
   */
  SignInData signIn(SignInParam input);

  VekkuUser getUserById(String id);
}
