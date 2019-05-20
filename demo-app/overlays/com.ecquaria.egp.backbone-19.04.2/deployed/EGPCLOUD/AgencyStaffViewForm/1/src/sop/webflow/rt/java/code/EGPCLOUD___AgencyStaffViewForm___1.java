package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class EGPCLOUD___AgencyStaffViewForm___1 extends BaseProcessClass {

	public void automaticStep0_OnStepProcess_0() throws Exception {
        /*AppViewFormLoadController.onPrepareEditFormData(this, "FormLink1");*/
		EngineHelper.delegate("appViewFormLoadDelegator", "onPrepareEditFormData", this);
	}

	public void automaticStep1_OnStepProcess_0() throws Exception {
      	/*AppViewFormLoadController.saveFormData(this);*/
		EngineHelper.delegate("appViewFormLoadDelegator", "saveFormData", this);
	}
}