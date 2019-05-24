package sg.gov.moh.iais.test.validate;


import sg.gov.moh.iais.common.IFormValidator;
import sg.gov.moh.iais.common.helper.IaisFormHelper;
import sg.gov.moh.iais.common.util.StringUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

public class TestValidate  extends IFormValidator {
    private static final String FORM = "ApplicationForm";

    public TestValidate (BaseProcessClass process){
     this.process = process;
     this.formName = FORM;
    }
    @Override
    public void validateProject(IaisFormHelper helper) {
        String age = helper.getFieldValue("iais.test.age");
        int ageint = Integer.parseInt(age);
        if(ageint>80 || ageint<18){
            this.errorCount++;
            helper.setFieldErrorMessage("iais.test.age","age is wrong!!!");
        }

    }

    @Override
    public void addRequired(List required) {
        required.add("iais.test.age");
    }
}
