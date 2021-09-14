package sg.gov.moh.iais.egp.bsb.common.constraints;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import java.util.regex.Pattern;


public class MobileNoCheck extends AbstractAnnotationCheck<MobileNo> {
    private static final String MOBILE_PATTERN_STRING = "^[8|9][0-9]{7}$";
    private static final Pattern MOBILE_PATTERN = Pattern.compile(MOBILE_PATTERN_STRING);

    @Override
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator) throws OValException {
        if (valueToValidate == null) {
            return true;
        }
        return MOBILE_PATTERN.matcher(valueToValidate.toString()).matches();
    }
}
