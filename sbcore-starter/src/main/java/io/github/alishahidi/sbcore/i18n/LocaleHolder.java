package io.github.alishahidi.sbcore.i18n;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocaleHolder {
    Locale currentLocale;
}
