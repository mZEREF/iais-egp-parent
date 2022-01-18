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

public class INTERNET___IncidentFollowUPReport1A___1 extends BaseProcessClass {

	private static final String DELEGATOR ="followUPReportDelegator";

	public void init_OnStepProcess_0() throws Exception {
		// 		Init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "initFollowup1A", this);
	}

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "startFollowup1A", this);
	}

	public void preReferenceNo_OnStepProcess_0() throws Exception {
	// 		PreReferenceNo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preReferenceNo", this);
	}

	public void handleReferenceNo_OnStepProcess_0() throws Exception {
		// 		HandleReferenceNo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleReferenceNo1A", this);
	}

	public void preFollowUPReport1A_OnStepProcess_0() throws Exception {
	// 		PreFollowUPReport1A->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preFollowUPReport1A", this);
	}

	public void handleFollowUPReport1A_OnStepProcess_0() throws Exception {
	// 		HandleFollowUPReport1A->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleFollowUPReport1A", this);
	}

	public void preViewReport1A_OnStepProcess_0() throws Exception {
	// 		PreViewReport1A->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preViewReport1A", this);
	}

	public void handleViewReport1A_OnStepProcess_0() throws Exception {
		// 		HandleViewReport1A->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleViewReport1A", this);
	}

	public void submitReport1A_OnStepProcess_0() throws Exception {
	// 		SubmitReport1A->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submitReport1A", this);
	}

}
