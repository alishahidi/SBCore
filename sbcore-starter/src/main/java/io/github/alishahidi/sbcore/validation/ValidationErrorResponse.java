package io.github.alishahidi.sbcore.validation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationErrorResponse {
    List<Violation> errors = new ArrayList<>();
}
