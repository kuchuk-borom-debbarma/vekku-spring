package dev.kuku.vekku.helper_services.encrypter;

public interface EncryptionService {
  String encryptObject(Object payload);

  <T> T decryptToken(String token, Class<T> clazz);
}
