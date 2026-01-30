package dev.kuku.vekku.domains.auth.internal.models;

import dev.kuku.vekku.domains.auth.models.VekkuUser;

public record AuthUser(VekkuUser user, String hashedPassword) {}
