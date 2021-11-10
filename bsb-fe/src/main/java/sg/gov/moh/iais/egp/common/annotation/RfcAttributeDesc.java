package sg.gov.moh.iais.egp.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : LiRan
 * @date : 2021/11/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RfcAttributeDesc {
    //this is the configuration name read from the database(must mapping one to one)
    String aliasName() default "";
}
