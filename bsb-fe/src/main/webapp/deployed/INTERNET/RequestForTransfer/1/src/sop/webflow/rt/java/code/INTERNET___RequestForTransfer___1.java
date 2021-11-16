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



public class INTERNET___RequestForTransfer___1 extends BaseProcessClass {
	private static final String DELEGATOR ="requestTransferDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void prepareSwitch1_OnStepProcess_0() throws Exception {
		// 		prepareSwitch1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch1", this);
	}

	public void saveAndConfirm_OnStepProcess_0() throws Exception {
	// 		saveAndConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveAndConfirm", this);
	}

	public void prepareSwitch2_OnStepProcess_0() throws Exception {
		// 		prepareSwitch2->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch2", this);
	}

	public void save_OnStepProcess_0() throws Exception {
	// 		save->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "save", this);
	}

	public void saveDraft_OnStepProcess_0() throws Exception {
		// 		saveDraft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "save", this);
	}

	public void preFacSelect_OnStepProcess_0() throws Exception {
		// 		preFacSelect->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preFacSelect", this);
	}

	public void preSwitch0_OnStepProcess_0() throws Exception {
		// 		preSwitch0->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preSwitch0", this);
	}

}
