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

public class INTERNET___ReceiptNotification___1 extends BaseProcessClass {
	private static final String DELEGATOR ="dataSubmissionDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareReceiveData", this);
	}

	public void prepareSwitch1_OnStepProcess_0() throws Exception {
		// 		prepareSwitch1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch1", this);
	}

	public void saveAndPrepareConfirm_OnStepProcess_0() throws Exception {
	// 		saveAndPrepareConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareReceiptConfirm", this);
	}

	public void prepareSwitch2_OnStepProcess_0() throws Exception {
		// 		prepareSwitch2->OnStepProcess
	}

	public void save_OnStepProcess_0() throws Exception {
	// 		save->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveReceiptNot", this);
	}

	public void saveDraft_OnStepProcess_0() throws Exception {
		// 		saveDraft->OnStepProcess
	}

	public void preFacSelect_OnStepProcess_0() throws Exception {
		// 		preFacSelect->OnStepProcess
	}

	public void preSwitch0_OnStepProcess_0() throws Exception {
		// 		preSwitch0->OnStepProcess
	}

}
