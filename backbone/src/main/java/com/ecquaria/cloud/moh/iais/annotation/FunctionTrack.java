package com.ecquaria.cloud.moh.iais.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface FunctionTrack {
    String funcName() default "";
    String moduleName() default "";
    int auditType() default 0;
}
