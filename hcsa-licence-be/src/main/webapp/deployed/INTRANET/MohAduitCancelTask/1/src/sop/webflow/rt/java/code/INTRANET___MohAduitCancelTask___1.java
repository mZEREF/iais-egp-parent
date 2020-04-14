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

public class INTRANET___MohAduitCancelTask___1 extends BaseProcessClass {
	private static final String DELEGATOR ="auditCancelTaskDelegator";
	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void initData_OnStepProcess_0() throws Exception {
	// 		InitData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void pre_OnStepProcess_0() throws Exception {
	// 		Pre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pre", this);
	}



	public void verification_OnStepProcess_0() throws Exception {
	// 		Verification->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "vad", this);
	}

	public void step6_OnStepProcess_0() throws Exception {
	// 		Back->OnStepProcess
	}

	public void cancelTask_OnStepProcess_0() throws Exception {
	// 		CancelTask->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "cancelTask", this);
	}


}
