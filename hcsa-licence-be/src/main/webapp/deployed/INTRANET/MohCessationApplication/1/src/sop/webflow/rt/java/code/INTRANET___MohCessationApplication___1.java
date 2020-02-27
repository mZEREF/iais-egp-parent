package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohCessationApplication___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="CessationApplicationBe";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void preapreData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void valiant_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "valiant", this);
	}

	public void action_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "action", this);
	}

	public void saveData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "saveData", this);
	}

}
