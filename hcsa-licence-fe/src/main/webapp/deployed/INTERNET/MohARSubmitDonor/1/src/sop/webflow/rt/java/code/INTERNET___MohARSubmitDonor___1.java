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

public class INTERNET___MohARSubmitDonor___1 extends BaseProcessClass {
	private static final String DELEGATOR ="submitDonorDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		PrepareSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPrepareSwitch", this);
	}

	public void return_OnStepProcess_0() throws Exception {
	// 		Return->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doReturn", this);
	}

	public void preparePage_OnStepProcess_0() throws Exception {
	// 		PreparePage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPreparePage", this);
	}

	public void prepareConfim_OnStepProcess_0() throws Exception {
	// 		PrepareConfim->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPrepareConfim", this);
	}

	public void draft_OnStepProcess_0() throws Exception {
	// 		Draft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doDraft", this);
	}

	public void submission_OnStepProcess_0() throws Exception {
	// 		Submission->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSubmission", this);
	}

	public void pageAction_OnStepProcess_0() throws Exception {
	// 		PageAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPageAction", this);
	}

	public void pageConfirmAction_OnStepProcess_0() throws Exception {
	// 		PageConfirmAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPageConfirmAction", this);
	}

}
