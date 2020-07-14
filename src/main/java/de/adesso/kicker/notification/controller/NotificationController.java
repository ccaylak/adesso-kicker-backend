package de.adesso.kicker.notification.controller;

import de.adesso.kicker.notification.persistence.Notification;
import de.adesso.kicker.notification.service.NotificationService;
import de.adesso.kicker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final UserService userService;

    @GetMapping("/get")
    public List<Notification> getNotifications() {
        return notificationService.getNotificationsByReceiver(userService.getLoggedInUser());
    }

    @GetMapping("/accept/{notificationId}")
    public void acceptNotification(@PathVariable long notificationId) {
        notificationService.acceptNotification(notificationId);
    }

    @GetMapping("/decline/{notificationId}")
    public void declineNotification(@PathVariable long notificationId) {
        notificationService.declineNotification(notificationId);
    }
}
