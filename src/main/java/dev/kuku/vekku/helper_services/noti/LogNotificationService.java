package dev.kuku.vekku.helper_services.noti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")
public class LogNotificationService implements NotificationService {

  @Override
  public void sendNotification(String subject, String body, String target, Object metadata) {
    log.info("--------------------------------------------------");
    log.info("DEVELOPMENT NOTIFICATION LOG");
    log.info("Target:   {}", target);
    log.info("Subject:  {}", subject);
    log.info("Body:     {}", body);
    log.info("Metadata: {}", metadata);
    log.info("--------------------------------------------------");
  }
}
