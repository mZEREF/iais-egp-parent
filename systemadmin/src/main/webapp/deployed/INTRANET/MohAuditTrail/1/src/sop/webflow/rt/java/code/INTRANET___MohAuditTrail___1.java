package sop.webflow.rt.java.code;

import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;
public class INTRANET___MohAuditTrail___1 extends BaseProcessClass {
	
  	
	private static final String DELEGATOR ="auditTrailDelegator";
  

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void prepareFullMode_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareFullMode", this);
	}

	public void prepareDataMode_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareDataMode", this);
	}

	public void disableStatus_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "disableStatus", this);
	}

	public void sortRecords_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "sortRecords", this);
	}

	public void changePage_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "changePage", this);
	}

	public void doQuery_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doQuery", this);
	}

	public void doExport_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doExport", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}
}