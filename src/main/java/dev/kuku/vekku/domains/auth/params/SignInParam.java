package dev.kuku.vekku.domains.auth.params;

public record SignInParam(String key, SignInType signInType, String password) {
  public enum SignInType {
    EMAIL,
    USERNAME
  }
}
