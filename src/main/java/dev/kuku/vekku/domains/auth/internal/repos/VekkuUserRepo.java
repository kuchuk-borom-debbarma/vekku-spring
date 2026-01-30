package dev.kuku.vekku.domains.auth.internal.repos;

import dev.kuku.vekku.domains.auth.internal.models.AuthUser;
import dev.kuku.vekku.domains.auth.models.VekkuUser;
import dev.kuku.vekku.domains.auth.params.StartSignupParam;
import javax.annotation.Nullable;

public interface VekkuUserRepo {
  /**
   * Find a user with the given username or email. Both can't be empty!
   *
   * @return null or found user
   */
  @Nullable
  VekkuUser findByUsernameOrEmail(String username, String email);

  @Nullable
  AuthUser findAuthUserByUsernameOrEmail(String username, String email);

  /**
   * Get user by ID
   *
   * @return found user or null
   */
  @Nullable
  VekkuUser getUserById(String id);

  /**
   * Persist the user
   *
   * @return created user or null if failed
   */
  @Nullable
  VekkuUser createUser(StartSignupParam input);
}
