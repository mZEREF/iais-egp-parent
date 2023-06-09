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

public class INTERNET___MohARAndIUIDataSubmission___1 extends BaseProcessClass {
	private static final String DELEGATOR ="arIUIDataSubmissionDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
		// 		Init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void preARIUIDataSubmission_OnStepProcess_0() throws Exception {
	// 		PreARIUIDataSubmission->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preARIUIDataSubmission", this);
	}

	public void doARIUIDataSubmission_OnStepProcess_0() throws Exception {
	// 		DoARIUIDataSubmission->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doARIUIDataSubmission", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		PrepareSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void preAmendPatient_OnStepProcess_0() throws Exception {
	// 		PreAmendPatient->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preAmendPatient", this);
	}

	public void doAmendPatient_OnStepProcess_0() throws Exception {
	// 		DoAmendPatient->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doAmendPatient", this);
	}

	public void prepareConfirm_OnStepProcess_0() throws Exception {
		// 		prepareConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareConfirm", this);
	}

	public void pageConfirmAction_OnStepProcess_0() throws Exception {
	// 		PageConfirmAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pageConfirmAction", this);
	}

	public void submission_OnStepProcess_0() throws Exception {
		// 		Submission->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submission", this);
	}

	public void saveDraft_OnStepProcess_0() throws Exception {
	// 		SaveDraft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveDraft", this);
	}

	public void doBack_OnStepProcess_0() throws Exception {
	// 		DoBack->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doBack", this);
	}

	public void prepareAck_OnStepProcess_0() throws Exception {
		// 		PrepareAck->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareAck", this);
	}
	
	public void prepareStage_OnStepProcess_0() throws Exception {
		// 		prepareStage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareStage", this);
	}

	public void preBatchUpload_OnStepProcess_0() throws Exception {
	// 		PreBatchUpload->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preBatchUpload", this);
	}

	public void submitUpload_OnStepProcess_0() throws Exception {
	// 		SubmitUpload->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submitBatchUpload", this);
	}
}
