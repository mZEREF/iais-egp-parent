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

public class INTRANET___IntraLogin___1 extends BaseProcessClass {
	private static final String DELEGATOR ="backendLoginDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "Start", this); 
	// 		Start->OnStepProcess
	}

	public void preLogin_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preLogin", this); 
	// 		preLogin->OnStepProcess
	}

	public void doLogin_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doLogin", this); 
	// 		doLogin->OnStepProcess
	}

	public void preRole_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preRole", this); 
	// 		preRole->OnStepProcess
	}

	public void doRole_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doRole", this); 
	// 		doRole->OnStepProcess
	}

	public void validRole_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "validRole", this); 
	// 		validRole->OnStepProcess
	}

}
