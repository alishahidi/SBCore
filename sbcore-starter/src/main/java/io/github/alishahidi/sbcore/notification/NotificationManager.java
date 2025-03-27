package io.github.alishahidi.sbcore.notification;

import io.github.alishahidi.sbcore.util.AuthUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationManager {

    NotificationRepository notificationRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(NotificationSendDto notificationSendDto) {
        String username = notificationSendDto.getUsername() != null ?
                notificationSendDto.getUsername() :
                AuthUtils.getAuthenticatedUsername();

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .username(username)
                .message(notificationSendDto.getMessage())
                .type(notificationSendDto.getType())
                .isRead(false)
                .build();

        notificationRepository.save(notificationEntity);
        notificationRepository.flush();
        CompletableFuture.completedFuture(null);
    }

    @Transactional
    @Async
    public CompletableFuture<List<Long>> readNotifications(List<Long> ids) {
        notificationRepository.markNotificationsAsRead(ids);
        return CompletableFuture.completedFuture(ids);
    }

    @Async
    public CompletableFuture<List<NotificationGetDto>> getAllNotifications() {
        String username = AuthUtils.getAuthenticatedUsername();

        List<NotificationGetDto> notifications = notificationRepository.findByUsername(username)
                .parallelStream()
                .map(this::mapNotificationToDto)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(notifications);
    }

    private NotificationGetDto mapNotificationToDto(NotificationEntity entity) {
        return NotificationGetDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .message(entity.getMessage())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
