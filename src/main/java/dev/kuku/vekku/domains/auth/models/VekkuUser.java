package dev.kuku.vekku.domains.auth.models;

import java.util.Date;

public record VekkuUser(String id, String username, String email, Date createdAt, Date updatedAt) {}
