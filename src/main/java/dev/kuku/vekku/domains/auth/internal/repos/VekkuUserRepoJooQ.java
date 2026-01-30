package dev.kuku.vekku.domains.auth.internal.repos;

import static dev.kuku.vekku.jooq.Tables.USERS;

import dev.kuku.vekku.domains.auth.internal.AuthHelper;
import dev.kuku.vekku.domains.auth.internal.models.AuthUser;
import dev.kuku.vekku.domains.auth.internal.repos.entities.User;
import dev.kuku.vekku.domains.auth.models.VekkuUser;
import dev.kuku.vekku.domains.auth.params.StartSignupParam;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VekkuUserRepoJooQ implements VekkuUserRepo {

  private final DSLContext dsl;

  @Override
  public @Nullable VekkuUser findByUsernameOrEmail(String username, String email) {
    if (!StringUtils.hasText(username) && !StringUtils.hasText(email)) {
      throw new IllegalArgumentException("Both username and email can't be null");
    }

    var user =
        dsl.selectFrom(USERS)
            .where(USERS.USERNAME.eq(username).or(USERS.EMAIL.eq(email)))
            .fetchOneInto(User.class);
    if (user == null) {
      return null;
    }
    return AuthHelper.UserToVekkuUser(user);
  }

  @Override
  public @Nullable AuthUser findAuthUserByUsernameOrEmail(String username, String email) {
    if (!StringUtils.hasText(username) && !StringUtils.hasText(email)) {
      throw new IllegalArgumentException("Both username and email can't be null");
    }

    var user =
        dsl.selectFrom(USERS)
            .where(USERS.USERNAME.eq(username).or(USERS.EMAIL.eq(email)))
            .fetchOneInto(User.class);

    if (user == null) {
      return null;
    }

    return new AuthUser(AuthHelper.UserToVekkuUser(user), user.getHashedPassword());
  }

  @Override
  public @Nullable VekkuUser getUserById(String id) {
    var user = dsl.selectFrom(USERS).where(USERS.ID.eq(id)).fetchOneInto(User.class);

    return user != null ? AuthHelper.UserToVekkuUser(user) : null;
  }

  @Override
  public @Nullable VekkuUser createUser(StartSignupParam input) {
    var id = UUID.randomUUID().toString();
    var now = LocalDateTime.now();

    var record =
        dsl.insertInto(USERS)
            .set(USERS.ID, id)
            .set(USERS.USERNAME, input.username())
            .set(USERS.EMAIL, input.email())
            .set(
                USERS.HASHED_PASSWORD,
                input.password()) // Assuming input.password() is already hashed by AuthService
            .set(USERS.CREATED_AT, now)
            .onConflictDoNothing()
            .returning()
            .fetchOne();

    if (record == null) {
      return null;
    }

    return AuthHelper.UserToVekkuUser(record.into(User.class));
  }
}
