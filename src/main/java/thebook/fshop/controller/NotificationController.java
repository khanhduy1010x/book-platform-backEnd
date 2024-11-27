package thebook.fshop.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Notification;
import thebook.fshop.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/get-notification")
    public ApiResponse<List<Notification>> getNotification() {
        return ApiResponse.<List<Notification>>builder()
                .result(        notificationService.getAllNotifications()
)
                .build();

    }
}
