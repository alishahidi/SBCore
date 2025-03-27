package io.github.alishahidi.sbcore.document;

import io.github.alishahidi.sbcore.s3.S3Scope;
import io.github.alishahidi.sbcore.util.FileType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentDto {
    Long id;

    String path;

    Long size;
    FileType type;
    String mimeType;
    String extension;
    String hash;
    S3Scope scope;

    Date createdAt;
    Date updatedAt;
}
