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
import sop.webflow.rt.api.BaseProcessClass;

import com.ecquaria.cloud.helper.EngineHelper;
public class INTRANET___MohAuditManualList___1 extends BaseProcessClass {
	private static final String DELEGATOR ="auditManualListDelegator";
	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void pre_OnStepProcess_0() throws Exception {
	// 		pre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pre", this);
	}

	public void remove_OnStepProcess_0() throws Exception {
	// 		remove->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "remove", this);
	}

	public void preconfirm_OnStepProcess_0() throws Exception {
	// 		preconfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preconfirm", this);
	}

	public void confirm_OnStepProcess_0() throws Exception {
		// 		confirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "confirm", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
	// 		Step1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "precancel", this);
	}

	public void step2_OnStepProcess_0() throws Exception {
	// 		Step2->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "cancel", this);
	}

	public void vad_OnStepProcess_0() throws Exception {
	// 		vad->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "vad", this);
	}

	public void next_OnStepProcess_0() throws Exception {
	// 		next->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "next", this);
	}

	public void step3_OnStepProcess_0() throws Exception {
	// 		Step3->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "nextToViewTaskList", this);
	}

}
