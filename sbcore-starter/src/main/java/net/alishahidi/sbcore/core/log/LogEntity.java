package net.alishahidi.sbcore.core.log;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "logs"
)
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String traceId;
    String method;
    String endpoint;
    @Column(columnDefinition = "TEXT")
    String requestBody;
    @Column(columnDefinition = "TEXT")
    String responseBody;

    String ip;
    String serverIp;
    String username;

    @Enumerated(EnumType.STRING)
    LogStatus status;

    Long processTime;
    Long requestPayloadSize;
    Long responsePayloadSize;

    @Column(columnDefinition = "TEXT")
    String errorMessage;

    @Column(columnDefinition = "TEXT")
    String stackTrace;

    @CreationTimestamp
    Date createdAt;

    @UpdateTimestamp
    Date updatedAt;
}