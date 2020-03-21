package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohAlertNotificationTemplate___1 extends BaseProcessClass {

	private static final String DELEGATOR ="templatesDelegator";
	
	public void prepareDate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void editTemplate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "editTemplate", this);
	}

	public void searchTemplate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "searchTemplate", this);
	}

	public void preViewTemplate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preView", this);
	}

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void doEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doEdit", this);
	}

	public void doPage_OnStepProcess_0() throws Exception {
	// 		doPage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPage", this);
	}

	public void doSort_OnStepProcess_0() throws Exception {
	// 		doSort->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSort", this);
	}

}
