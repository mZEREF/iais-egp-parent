package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;

public class IAIS___MohMasterCode___1 extends BaseProcessClass {

	private static final String DELEGATOR ="masterCodeDelegator";

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void prepareCreate_OnStepProcess_0() throws Exception {
		// 		PrepareCreate->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareCreate", this);
	}

	public void prepareEdit_OnStepProcess_0() throws Exception {
		// 		PrepareEdit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareEdit", this);
	}

	public void doSearch_OnStepProcess_0() throws Exception {
		// 		doSearch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}

	public void doSorting_OnStepProcess_0() throws Exception {
		// 		doSorting->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSorting", this);
	}

	public void doPaging_OnStepProcess_0() throws Exception {
		// 		doPaging->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPaging", this);
	}

	public void doDelete_OnStepProcess_0() throws Exception {
		// 		doDelete->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doDelete", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
		// 		PrepareSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void doCreate_OnStepProcess_0() throws Exception {
		// 		doCreate->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doCreate", this);

	}

	public void doEdit_OnStepProcess_0() throws Exception {
		// 		doEdit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doEdit", this);

	}
}
