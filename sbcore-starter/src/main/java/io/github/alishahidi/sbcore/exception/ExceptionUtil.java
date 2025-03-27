package io.github.alishahidi.sbcore.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtil {

    public AppException make(ExceptionTemplate template) {
        return new AppException(template);
    }
}
