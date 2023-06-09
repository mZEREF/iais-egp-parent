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

public class INTERNET___EventBusSample___1 extends BaseProcessClass {
	private static final String DELEGATOR ="eventBusSampleDelegate";
	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void callbackStep_OnStepProcess_0() throws Exception {
	// 		CallbackStep->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "callback", this);
	}

	public void finalStep_OnStepProcess_0() throws Exception {
	// 		FinalStep->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "finalStep", this);
	}

}
