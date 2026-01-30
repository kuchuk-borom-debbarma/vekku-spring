package dev.kuku.vekku.domains.auth.model;

import java.util.Date;

public record VekkuUser(String id, String username, String email, Date createdAt, Date updatedAt) {}
