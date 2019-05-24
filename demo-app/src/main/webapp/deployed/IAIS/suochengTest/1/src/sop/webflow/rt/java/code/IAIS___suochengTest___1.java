package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.egp.core.forms.util.FormRuntimeUtil;
import com.ecquaria.egp.core.forms.util.entity.FormButton;

import sg.gov.moh.iais.test.action.TestController;

public class IAIS___suochengTest___1 extends BaseProcessClass {
    public void prepareData_OnStepProcess_0() throws Exception {
        TestController.prepareData(this);
    }

    public void validate_OnStepProcess_0() throws Exception {
        TestController.validate(this);
    }
}
