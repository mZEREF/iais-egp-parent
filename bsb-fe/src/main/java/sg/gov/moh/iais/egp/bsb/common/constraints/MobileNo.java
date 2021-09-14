package sg.gov.moh.iais.egp.bsb.common.constraints;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(checkWith = MobileNoCheck.class)
public @interface MobileNo {

    String message() default "{context} is not a valid mobile number";

    String[] profiles() default {};

    ConstraintTarget[] appliesTo() default ConstraintTarget.VALUES;

    String when() default "";
}
