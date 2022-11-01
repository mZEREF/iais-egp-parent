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

public class INTRANET___MohBsbTaskCommonPool___1 extends BaseProcessClass {
	private static final String DELEGATOR ="bsbTaskCommonPoolDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "init", this); 
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this); 
	}

	public void search_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "search", this); 
	}

	public void page_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "page", this); 
	}

	public void changeRole_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "changeRole", this); 
	}

	public void assign_OnStepProcess_0() throws Exception {
		//click on application No to pick up task
		EngineHelper.delegate(DELEGATOR, "pickUpTask", this);
	}

	public void multiAssign_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "multiAssign", this); 
	}

	public void detail_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "detail", this); 
	}

	public void sort_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "sort", this);
	}

	public void assignTask_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "assign", this);
	}
}