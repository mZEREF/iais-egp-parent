package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class EGOV___RepresentCitizenProcess___1 extends BaseProcessClass {

	public void step3_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("representCitizenDelegator", "checkAgencyStaffOTP", request);
	}

	public void prepare_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("representCitizenDelegator", "prepare", request);
	}

	public void checkagentOTP_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("representCitizenDelegator", "checkAgentOTP", request);
	}

	public void sendOTP_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("representCitizenDelegator", "sendOTP", request);
	}

	public void step4_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("representCitizenDelegator", "redirectContinueUrl", request);
	}

}
