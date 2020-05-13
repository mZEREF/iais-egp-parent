package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;

public class INTRANET___MohMasterCode___1 extends BaseProcessClass {
  
  	private static final String DELEGATOR ="masterCodeDelegator";

	public void prepareData_OnStepProcess_0() throws Exception {
      EngineHelper.delegate(DELEGATOR, "prepareData", this);
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

	public void doEdit_OnStepProcess_0() throws Exception {
	// 		doEdit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doEdit", this);
	}

	public void doUpload_OnStepProcess_0() throws Exception {
	// 		doUpload->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doUpload", this);
	}

	public void doCreateCode_OnStepProcess_0() throws Exception {
	// 		doCreateCode->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doCreateCode", this);
	}

	public void prepareCode_OnStepProcess_0() throws Exception {
		// 		prepareCode->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareCode", this);
	}

	public void preateCreateCode_OnStepProcess_0() throws Exception {
	// 		preateCreateCode->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preateCreateCode", this);
	}

	public void createCode_OnStepProcess_0() throws Exception {
	// 		createCode->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "createCode", this);
	}
}
