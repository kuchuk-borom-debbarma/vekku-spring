package dev.kuku.vekku.domains.auth.exceptions;

public class UsernameOrEmailAlreadyUsed extends AuthDomainException {
  public UsernameOrEmailAlreadyUsed(String username, String email) {}
}
