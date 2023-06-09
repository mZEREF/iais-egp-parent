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

public class INTRANET___DOScreeningIncidentNotification___1 extends BaseProcessClass {
	private static final String DELEGATOR ="incidentMOHProcessDelegator";

	public void start_OnStepProcess_0() throws Exception {
		// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "startDO", this);
	}

	public void preScreeningData_OnStepProcess_0() throws Exception {
	// 		PreScreeningData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preIncidentProcessingData", this);
	}

	public void handleScreening_OnStepProcess_0() throws Exception {
	// 		HandleScreening->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleProcessing", this);
	}

	public void doProcessing_OnStepProcess_0() throws Exception {
	// 		DoProcessing->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doDOProcessing", this);
	}

}
