package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohChecklistConfig___1 extends BaseProcessClass {
	private static final String DELEGATOR ="hcsaChklConfigDelegator";
	public void prepare_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}
	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}
	public void switchAction_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "switchAction", this);
	}
}