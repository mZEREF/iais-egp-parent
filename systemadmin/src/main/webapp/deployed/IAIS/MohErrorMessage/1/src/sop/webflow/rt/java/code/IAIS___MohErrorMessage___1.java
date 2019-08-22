package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;
public class IAIS___MohErrorMessage___1 extends BaseProcessClass {
private static final String DELEGATOR ="messageDelegator";
	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void doCreateOrEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doCreateOrEdit", this);
	}


	public void prepareSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void sortRecords_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSorting", this);
	}

	public void changePage_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doPaging", this);
	}

	public void doSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}

	public void editBefore_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "perpareEdit", this);
	}

	public void doCreate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doCreate", this);
	}

	public void doEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doEdit", this);
	}

	public void disableStatus_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "disableStatus", this);
	}

	

}