package com.alishahidi.sbcore.notification;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationGetDto {
    Long id;
    String message;
    NotificationType type;
    String username;
    LocalDateTime createdAt;
}