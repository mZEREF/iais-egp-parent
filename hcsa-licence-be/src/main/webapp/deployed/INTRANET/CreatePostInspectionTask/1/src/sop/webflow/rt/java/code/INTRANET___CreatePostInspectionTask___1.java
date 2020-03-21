package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___CreatePostInspectionTask___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="postInspectionTask";

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void do_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doBatchJob", this);
	}

}
