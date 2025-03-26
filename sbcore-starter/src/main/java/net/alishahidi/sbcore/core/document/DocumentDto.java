package net.alishahidi.sbcore.core.document;

import net.alishahidi.sbcore.core.s3.S3Scope;
import net.alishahidi.sbcore.core.util.FileType;
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
