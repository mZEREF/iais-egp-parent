package com.ecquaria.cloud.moh.iais.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface SearchTrack {
    String catalog() default "";
    String key() default "";
}
