package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.egp.core.forms.util.FormRuntimeUtil;
import com.ecquaria.egp.core.forms.util.entity.FormButton;
//import com.ecquaria.moh.iais.test.action.TestDelegator;

public class IAIS___suochengTest___1 extends BaseProcessClass {
    private static final String DELEGATOR ="testDelegator";
    public void prepareData_OnStepProcess_0() throws Exception {
    }
    public void validate_OnStepProcess_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "validate", this);

    }
    public void fillFormPage_OnPageLoad_0() throws Exception {

    }
    public void applicationForm_OnPageLoad_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "prepareData", this);
    }
    public void saveDraft_OnStepProcess_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "saveDraft", this);
    }
}
