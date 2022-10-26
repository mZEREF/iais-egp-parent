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

public class INTRANET___MohBsbInspectionNCsDOReview___1 extends BaseProcessClass {
	private static final String DELEGATOR = "bsbInspectionOfficerReviewNCs";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "startDO", this);
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

	public void requestForInformation_OnStepProcess_0() throws Exception {
	// 		RequestForInformation->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "requestForInformationToApplicant", this);
	}

	public void routeToAO_OnStepProcess_0() throws Exception {
		// 		RouteToAO->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "routeToAO", this);
	}

	public void skip_OnStepProcess_0() throws Exception {
		// 		Skip->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "skip", this);
	}


}
