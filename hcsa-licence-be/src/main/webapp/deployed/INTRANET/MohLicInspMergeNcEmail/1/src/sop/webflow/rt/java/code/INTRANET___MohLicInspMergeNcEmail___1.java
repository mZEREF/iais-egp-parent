/**
 * Generated by SIT
 *
 * Feel free to add  methods  or comments. The content of this 
 * file will be kept as-is when committed.
 *
 * Extending this  class is not recommended , since the class-
 * name will change together with the version. Calling methods
 * from external code is not recommended as well , for similar
 * reasons.
 */
package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohLicInspMergeNcEmail___1 extends BaseProcessClass {

	private static final String DELEGATOR ="licInspMergeSendNcEmailDelegator";

	public void sendEmail_OnStepProcess_0 () throws Exception { 
	
			EngineHelper.delegate(DELEGATOR, "sendEmail", this); 	}

	public void previewEmail_OnStepProcess_0 () throws Exception { 
	
			EngineHelper.delegate(DELEGATOR, "previewEmail", this); 	}

	public void prepareData_OnStepProcess_0 () throws Exception { 
	
			EngineHelper.delegate(DELEGATOR, "prepareData", this); 	}

	public void emailSubmitStep_OnStepProcess_0 () throws Exception { 
	
			EngineHelper.delegate(DELEGATOR, "emailSubmitStep", this); 	}


	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	// 		Step1->OnStepProcess
	}

}
