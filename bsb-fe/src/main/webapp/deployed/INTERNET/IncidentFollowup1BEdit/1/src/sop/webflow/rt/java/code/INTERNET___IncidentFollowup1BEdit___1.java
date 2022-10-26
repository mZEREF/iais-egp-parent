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

public class INTERNET___IncidentFollowup1BEdit___1 extends BaseProcessClass {

	private static final String DELEGATOR ="followUPReportDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "startFollowup1B", this);
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		Init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "initFollowup1BEdit", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		PrepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preFollowUPReport1B", this);
	}

	public void handleFollowUPReport1B_OnStepProcess_0() throws Exception {
	// 		HandleFollowUPReport1B->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleFollowUPReport1B", this);
	}

	public void preViewReport1B_OnStepProcess_0() throws Exception {
	// 		PreViewReport1B->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preViewReport1B", this);
	}

	public void handleViewReport1B_OnStepProcess_0() throws Exception {
	// 		HandleViewReport1B->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleViewReport1B", this);
	}

	public void submitReport1B_OnStepProcess_0() throws Exception {
	// 		SubmitReport1B->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submitReport1B", this);
	}

}
