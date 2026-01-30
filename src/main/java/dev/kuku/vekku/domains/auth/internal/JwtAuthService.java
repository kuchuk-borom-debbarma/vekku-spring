package dev.kuku.vekku.domains.auth.internal;

import dev.kuku.vekku.domains.auth.AuthService;
import dev.kuku.vekku.domains.auth.model.SignInData;
import dev.kuku.vekku.domains.auth.model.VekkuUser;
import dev.kuku.vekku.domains.auth.params.SignInParam;
import dev.kuku.vekku.domains.auth.params.StartSignupParam;
import dev.kuku.vekku.domains.auth.params.VerifySignupToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtAuthService implements AuthService {
  @Override
  public String startSignUp(StartSignupParam input) {
    log.info("Start sign up {}", input);
    return "";
  }

  @Override
  public StartSignupParam verifySignupToken(VerifySignupToken input) {
    return null;
  }

  @Override
  public VekkuUser createUser(StartSignupParam input) {
    return null;
  }

  @Override
  public SignInData signIn(SignInParam input) {
    return null;
  }

  @Override
  public VekkuUser getUserById(String id) {
    return null;
  }
}
