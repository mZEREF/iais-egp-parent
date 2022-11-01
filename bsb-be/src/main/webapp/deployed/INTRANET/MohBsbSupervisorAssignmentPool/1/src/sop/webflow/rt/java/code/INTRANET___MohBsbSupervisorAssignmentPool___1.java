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

public class INTRANET___MohBsbSupervisorAssignmentPool___1 extends BaseProcessClass {
	private static final String DELEGATOR ="supervisorAssignmentPoolDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void bingAction_OnStepProcess_0() throws Exception {

	}

	public void search_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "search", this);
	}

	public void page_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "page", this);
	}

	public void sort_OnStepProcess_0() throws Exception {
		// 		Sort->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "sort", this);
	}


	public void changeRole_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "changeRole", this);
	}

}