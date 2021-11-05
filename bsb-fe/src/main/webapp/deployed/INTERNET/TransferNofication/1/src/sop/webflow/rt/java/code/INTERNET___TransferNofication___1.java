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

public class INTERNET___TransferNofication___1 extends BaseProcessClass {
	private static final String DELEGATOR ="transferNotificationDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void saveAndPrepareConfirm_OnStepProcess_0() throws Exception {
	// 		saveAndPrepareConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveAndPrepareConfirm", this);
	}

	public void save_OnStepProcess_0() throws Exception {
	// 		save->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "save", this);
	}

	public void saveDraft_OnStepProcess_0() throws Exception {
		// 		saveDraft->OnStepProcess
	}

}
