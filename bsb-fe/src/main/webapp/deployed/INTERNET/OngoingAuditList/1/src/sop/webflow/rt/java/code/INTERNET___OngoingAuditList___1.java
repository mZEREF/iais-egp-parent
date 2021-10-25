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

public class INTERNET___OngoingAuditList___1 extends BaseProcessClass {
	private static final String DELEGATOR = "auditDateDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareAuditListData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		prepareSwitch->OnStepProcess
	}

	public void sort_OnStepProcess_0() throws Exception {
	// 		sort->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "sort", this);
	}

	public void page_OnStepProcess_0() throws Exception {
	// 		page->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "page", this);
	}

	public void search_OnStepProcess_0() throws Exception {
	// 		search->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}

}
