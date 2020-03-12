package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___CessationEffectiveDate___1 extends BaseProcessClass {

	private static final String DELEGATOR ="CessationEffectiveDateBatchjob";
	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}
	public void step2_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doBatchJob", this);
	}

}
