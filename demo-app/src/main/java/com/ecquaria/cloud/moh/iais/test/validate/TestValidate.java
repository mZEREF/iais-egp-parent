package com.ecquaria.cloud.moh.iais.test.validate;


import com.ecquaria.cloud.moh.iais.common.helper.IaisFormHelper;
import com.ecquaria.cloud.moh.iais.common.IFormValidator;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

public class TestValidate  extends IFormValidator {
    private static final String FORM = "ApplicationForm";

    public TestValidate (BaseProcessClass process){
     this.process = process;
     this.formName = FORM;
    }
    @Override
    public void validateProject() {
        String age = IaisFormHelper.getFormFieldData(process,formName,"iais.test.age");
        int ageint = Integer.parseInt(age);
        if(ageint>80 || ageint<18){
            this.errorCount++;
            IaisFormHelper.addFieldErrorMessage(process.request,"iais.test.age","age is wrong!!!");
        }

    }

    @Override
    public void addRequired(List required) {
        required.add("iais.test.age");
    }
}
