package io.github.alishahidi.sbcore.util;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDetails {
    FileType type;
    String extension;
    String mimeType;
    Long size;
}
