package dev.kuku.vekku.domains.auth;

import dev.kuku.vekku.domains.auth.model.SignInData;
import dev.kuku.vekku.domains.auth.model.VekkuUser;
import dev.kuku.vekku.domains.auth.params.SignInParam;
import dev.kuku.vekku.domains.auth.params.StartSignupParam;
import dev.kuku.vekku.domains.auth.params.VerifySignupToken;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
  /**
   * Start a signup process that will return a magic link. User will need to click it to complete
   * the verification
   *
   * @return sign up token containing encrypted data about the user signing up
   */
  String startSignUp(StartSignupParam input);

  /**
   * Verify the token
   *
   * @return user data extracted from the token
   */
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
