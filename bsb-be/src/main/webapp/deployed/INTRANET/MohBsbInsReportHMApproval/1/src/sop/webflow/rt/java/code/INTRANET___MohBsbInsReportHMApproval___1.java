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

public class INTRANET___MohBsbInsReportHMApproval___1 extends BaseProcessClass {
	private static final String DELEGATOR ="bsbInspectionReportHMApprovalDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		Init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void pre_OnStepProcess_0() throws Exception {
	// 		Pre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pre", this);
	}

	public void bindAction_OnStepProcess_0() throws Exception {
	// 		BindAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "bindAction", this);
	}

	public void handleSubmit_OnStepProcess_0() throws Exception {
	// 		HandleSubmit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleSubmit", this);
	}

	public void approve_OnStepProcess_0() throws Exception {
	// 		Approve->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "approve", this);
	}

	public void reject_OnStepProcess_0() throws Exception {
	// 		Reject->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "reject", this);
	}

	public void handleSaveReport_OnStepProcess_0() throws Exception {
		// 		HandleSaveReport->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleSaveReport", this);
	}

}
