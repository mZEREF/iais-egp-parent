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

public class INTERNET___BsbDataSubmission___1 extends BaseProcessClass {
	private static final String DELEGATOR = "dataSubmissionDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
	}

	public void prepareDataSubmissionSelect_OnStepProcess_0() throws Exception {
	// 		prepareDataSubmissionSelect->OnStepProcess
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		prepareSwitch->OnStepProcess
	}

	public void prepareConsume_OnStepProcess_0() throws Exception {
	// 		prepareConsume->OnStepProcess
	}

	public void prepareDisposal_OnStepProcess_0() throws Exception {
	// 		prepareDisposal->OnStepProcess
	}

	public void prepareExport_OnStepProcess_0() throws Exception {
	// 		prepareExport->OnStepProcess
	}

	public void prepareReceipt_OnStepProcess_0() throws Exception {
	// 		prepareReceipt->OnStepProcess
	}

	public void prepareTransfer_OnStepProcess_0() throws Exception {
	// 		prepareTransfer->OnStepProcess
	}

	public void prepareTransferNotification_OnStepProcess_0() throws Exception {
	// 		prepareTransferNotification->OnStepProcess
	}

	public void preAckOfReceiptOfTransfer_OnStepProcess_0() throws Exception {
	// 		preAckOfReceiptOfTransfer->OnStepProcess
	}

	public void prepareFacilitySelect_OnStepProcess_0() throws Exception {
		// 		prepareFacilitySelect->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPrepareFacilitySelect", this);
	}

}
