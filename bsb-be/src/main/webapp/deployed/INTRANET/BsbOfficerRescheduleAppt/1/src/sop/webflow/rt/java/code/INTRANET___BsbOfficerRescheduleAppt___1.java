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

public class INTRANET___BsbOfficerRescheduleAppt___1 extends BaseProcessClass {
	private static final String DELEGATOR ="bsbRescheduleApptDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		Init->OnStepProcess
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareRescheduleData", this);
	}

	public void preSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preSwitch", this);
	}

	public void specifyDate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "specificDate", this);
	}

	public void validate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "validateFormData", this);
	}

	public void saveRescheduleData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "saveRescheduleDate", this);
	}

}