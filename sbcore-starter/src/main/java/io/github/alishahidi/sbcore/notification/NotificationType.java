package io.github.alishahidi.sbcore.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    INFO("info"),
    SUCCESS("success"),
    ERROR("error"),
    WARNING("warning");

    private final String name;

    public static NotificationType fromFieldName(String name) {
        for (NotificationType field : NotificationType.values()) {
            if (field.getName().equalsIgnoreCase(name)) {
                return field;
            }
        }
        throw new IllegalArgumentException("No enum constant with field name " + name);
    }
}
