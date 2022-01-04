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

public class INTERNET___MohLDTDataSubmission___1 extends BaseProcessClass {
	private static final String DELEGATOR = "ldtDataSubmissionDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void draft_OnStepProcess_0() throws Exception {
	// 		Draft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "draft", this);
	}

	public void submit_OnStepProcess_0() throws Exception {
	// 		Submit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submit", this);
	}

	public void prepareConfirm_OnStepProcess_0() throws Exception {
	// 		PrepareConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareConfirm", this);
	}

	public void pageAction_OnStepProcess_0() throws Exception {
	// 		PageAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pageAction", this);
	}

	public void return_OnStepProcess_0() throws Exception {
	// 		Return->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doReturn", this);
	}

	public void pageConfirmAction_OnStepProcess_0() throws Exception {
	// 		PageConfirmAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pageConfirmAction", this);
	}

	public void prepareStepData_OnStepProcess_0() throws Exception {
	// 		PrepareStepData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareStepData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		PrepareSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

}
