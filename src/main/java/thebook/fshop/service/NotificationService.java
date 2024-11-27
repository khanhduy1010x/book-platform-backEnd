package thebook.fshop.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.entity.Notification;
import thebook.fshop.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {
    NotificationRepository notificationRepository;
    SecurityService securityService;
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
    public List<Notification> getAllNotifications() {
        var account = securityService.getAccountByJWT();
        return notificationRepository.findByAccount_AccID(account.getAccID());
    }

}
