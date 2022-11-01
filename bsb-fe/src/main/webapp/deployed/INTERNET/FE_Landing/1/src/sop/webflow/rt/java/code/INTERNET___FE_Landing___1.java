/**
 * Generated by SIT
 *
 * Feel free to add  methods  or comments. The content of this 
 * file will be kept as-is when committed.
 *
 * Extending this  class is not recommended , since the class-
 * name will change together with the version. Calling methods
 * from external code is not recommended as well , for similar
 * reasons.
 */
package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class INTERNET___FE_Landing___1 extends BaseProcessClass {

	private static final String DELEGATOR ="feLandingDelegator";
	
	public void preload_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preload", this);
	}

	public void switchAction_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "switchAction", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}

	public void singpassSendRedirect_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "singpassSendRedirect", this);
	}

	public void corppassSendRedirect_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "corppassSendRedirect", this);
	}

    public void initSso_OnStepProcess_0() throws Exception {
    //      InitSso->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "initSso", this);
    }

}