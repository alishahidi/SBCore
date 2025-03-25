package com.alishahidi.sbcore.i18n;

import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class I18nUtil {

    MessageSource messageSource;

    @Resource(name = "localeHolder")
    LocaleHolder localeHolder;

    public String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, localeHolder.getCurrentLocale());
    }
}
