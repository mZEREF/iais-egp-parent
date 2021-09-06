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

public class INTRANET___AdminSpecifyAuditDt___1 extends BaseProcessClass {
	private static final String DELEGATOR = "auditDateDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSpecifyDtData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		prepareSwitch->OnStepProcess
	}

	public void submit_OnStepProcess_0() throws Exception {
	// 		submit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "specifyDt", this);
	}

}
