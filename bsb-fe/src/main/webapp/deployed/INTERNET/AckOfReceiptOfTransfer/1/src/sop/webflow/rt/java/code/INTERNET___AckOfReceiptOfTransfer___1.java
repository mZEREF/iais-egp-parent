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

public class INTERNET___AckOfReceiptOfTransfer___1 extends BaseProcessClass {
	private static final String DELEGATOR = "ackOfTransferReceiptDelegator";


	public void start_OnStepProcess_0() throws Exception {
		// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void saveAndPreConfirm_OnStepProcess_0() throws Exception {
		// 		saveAndPreConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveAndPreConfirm", this);
	}

	public void save_OnStepProcess_0() throws Exception {
		// 		save->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "save", this);
	}

	public void prepareSwitch1_OnStepProcess_0() throws Exception {
		// 		prepareSwitch1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch1", this);
	}

	public void prepareSwitch2_OnStepProcess_0() throws Exception {
		// 		prepareSwitch2->OnStepProcess
	}

	public void preSelfFacSelect_OnStepProcess_0() throws Exception {
		// 		preSelfFacSelect->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preSelfFacSelect", this);
	}

	public void preSwitch_OnStepProcess_0() throws Exception {
		// 		preSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preSwitch", this);
	}

	public void formPageSaveDraft_OnStepProcess_0() throws Exception {
		// 		formPageSaveDraft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveDraft", this);
	}

	public void confirmPageSaveDraft_OnStepProcess_0() throws Exception {
		// 		confirmPageSaveDraft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveConfirmDraft", this);
	}

}
