package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;
public class IAIS___MohSystemParameter___1 extends BaseProcessClass {

private static final String DELEGATOR ="systemParameterDelegator";
	public void loadData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "loadData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void doQuery_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doQuery", this);
	}

	public void sortRecords_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "sortRecords", this);
	}

	public void changePage_OnStepProcess_0() throws Exception {
      EngineHelper.delegate(DELEGATOR, "changePage", this);
	}

	public void disableStatus_OnStepProcess_0() throws Exception {
	  EngineHelper.delegate(DELEGATOR, "disableStatus", this);
	}

	public void doEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doEdit", this);
	}

	public void prepareSwitch_OnStepProcess_1() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}

	public void prepareEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareEdit", this);
	}
}