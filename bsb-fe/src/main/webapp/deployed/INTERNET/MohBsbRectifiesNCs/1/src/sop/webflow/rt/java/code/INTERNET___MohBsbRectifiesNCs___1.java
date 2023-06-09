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

public class INTERNET___MohBsbRectifiesNCs___1 extends BaseProcessClass {
	private static final String DELEGATOR ="rectifiesNCsDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);

	}

	public void init_OnStepProcess_0() throws Exception {
	// 		Init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void prepareNCsData_OnStepProcess_0() throws Exception {
	// 		PrepareNCsData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareNCsData", this);
	}

	public void handleNCs_OnStepProcess_0() throws Exception {
	// 		HandleNCs->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleNCs", this);
	}

	public void submitNCs_OnStepProcess_0() throws Exception {
	// 		SubmitNCs->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submitNCs", this);
	}

	public void prepareRectifyData_OnStepProcess_0() throws Exception {
	// 		PrepareRectifyData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareRectifyData", this);
	}

	public void handleRectifyPage_OnStepProcess_0() throws Exception {
	// 		HandleRectifyPage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleRectifyPage", this);
	}

}
