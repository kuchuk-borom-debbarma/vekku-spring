package dev.kuku.vekku.helper_services.noti;

public interface NotificationService {
  void sendNotification(String subject, String body, String target, Object metadata);
}
