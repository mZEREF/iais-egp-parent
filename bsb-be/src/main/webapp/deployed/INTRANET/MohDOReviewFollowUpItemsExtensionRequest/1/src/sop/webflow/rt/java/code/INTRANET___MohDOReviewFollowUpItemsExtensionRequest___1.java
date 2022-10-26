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

public class INTRANET___MohDOReviewFollowUpItemsExtensionRequest___1 extends BaseProcessClass {
	private static final String DELEGATOR ="mohDOReviewFollowUpItemsExtension";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		Init->OnStepProcess
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}

	public void handleSubmit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "handleSubmit", this);
	}

	public void skip_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "skip", this);
	}

	public void approve_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "acceptResponse", this);
	}

	public void reject_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "rejectAction", this);
	}

	public void rfi_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "rfi", this);
	}

	public void bindAction_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "bindAction", this);
	}
}
