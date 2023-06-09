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

public class INTRANET___AOProcessSelfAudit___1 extends BaseProcessClass {
	private static final String DELEGATOR ="selfAuditDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareProcessSelfAuditData", this);
	}

	public void approved_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "aoApproved", this);
	}

	public void routeBack_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "aoInternalClarifications", this);
	}

	public void doValidate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "aoValidate", this);
	}
}
