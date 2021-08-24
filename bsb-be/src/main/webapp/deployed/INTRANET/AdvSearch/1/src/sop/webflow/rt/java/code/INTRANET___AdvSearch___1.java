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

public class INTRANET___AdvSearch___1 extends BaseProcessClass {

	private static final String DELEGATOR ="biosafetyEnquiryDelegator";

	public void preAdvSearch_OnStepProcess_0() throws Exception {
	// 		preAdvSearch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareAdvSearch", this);
	}

	public void preAfterAdvSearch_OnStepProcess_0() throws Exception {
	// 		PreAfterAdvSearch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preAfterAdvSearch", this);
	}

	public void advSearchDoPage_OnStepProcess_0() throws Exception {
	// 		AdvSearchDoPage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "page", this);
	}

	public void doAdvAfterSearch_OnStepProcess_0() throws Exception {
	// 		doAdvAfterSearch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preAfterAdvSearch", this);
	}

	public void prepareDetail_OnStepProcess_0() throws Exception {
	// 		PrepareDetail->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareDetail", this);
	}

	public void advSearchSort_OnStepProcess_0() throws Exception {
	// 		AdvSearchSort->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "sort", this);
	}

	public void step2_OnStepProcess_0() throws Exception {
	// 		Step2->OnStepProcess
	}

}
