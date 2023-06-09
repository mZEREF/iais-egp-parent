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
import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;

public class INTRANET___MohLicAppMainOnlineEnquiry___1 extends BaseProcessClass {
	private static final String DELEGATOR ="onlineEnquiryLicAppMainDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this); 	
		// 		start->OnStepProcess
	}

	public void preSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preSearch", this); 	
		// 		preSearch->OnStepProcess
	}

	public void nextStep_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "nextStep", this); 	
		// 		nextStep->OnStepProcess
	}

	public void licInfoJump_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "licInfoJump", this); 	
		// 		licInfoJump->OnStepProcess
	}

	public void appInfoJump_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "appInfoJump", this); 	
		// 		appInfoJump->OnStepProcess
	}

}
