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

public class INTERNET___ViewIncidentNotificaiton___1 extends BaseProcessClass {
	private static final String DELEGATOR = "bsbInboxReportableEventViewDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
	}

	public void preView_OnStepProcess_0() throws Exception {
	// 		PreView->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preNotificationData", this);
	}

}
