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

public class INTRANET___MohAduitTcuList___1 extends BaseProcessClass {
	private static final String DELEGATOR ="auditTcuListDelegator";
	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void pre_OnStepProcess_0() throws Exception {
	// 		pre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pre", this);
	}

	public void preconfirm_OnStepProcess_0() throws Exception {
	// 		preconfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preconfirm", this);
	}

	public void cancelTask_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "cancelTask", this);
	// 		cancelTask->OnStepProcess
	}

	public void confirm_OnStepProcess_0() throws Exception {
	// 		confirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "confirm", this);
	}

	public void step7_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "cancel", this);
	// 		Step7->OnStepProcess
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

}
