package sg.gov.moh.iais.test.action;

import sg.gov.moh.iais.common.IFormValidator;
import sg.gov.moh.iais.test.validate.TestValidate;
import sop.webflow.rt.api.BaseProcessClass;

public class TestController {
    public static void prepareData(BaseProcessClass base){

    }
    public static void validate(BaseProcessClass base){
        IFormValidator testValidate = new TestValidate(base);
        testValidate.validate();
    }
}
