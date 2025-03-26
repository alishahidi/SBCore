package net.alishahidi.sbcore.core.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchParams {
    int defaultPage() default 0;
    int defaultSize() default 10;
    String[] defaultSort() default {"id,asc"};
}