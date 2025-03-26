package net.alishahidi.sbcore.core.notification;

import net.alishahidi.sbcore.core.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationManager notificationManager;

    @GetMapping
    public CompletableFuture<ApiResponse<List<NotificationGetDto>>> getUserNotifications() {
        return notificationManager.getAllNotifications()
                .thenApply(ApiResponse::success);
    }

    @PostMapping("/read")
    public CompletableFuture<ApiResponse<List<Long>>> markNotificationsAsRead(@RequestBody List<Long> ids) {
        return notificationManager.readNotifications(ids)
                .thenApply(result -> ApiResponse.success(ids));
    }
}
