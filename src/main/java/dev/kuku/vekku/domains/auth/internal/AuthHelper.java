package dev.kuku.vekku.domains.auth.internal;

import dev.kuku.vekku.domains.auth.internal.repos.entities.User;
import dev.kuku.vekku.domains.auth.models.VekkuUser;

public class AuthHelper {
  public static VekkuUser UserToVekkuUser(User user) {
    if (user == null) throw new NullPointerException("User is null! cant map");
    return new VekkuUser(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }
}
