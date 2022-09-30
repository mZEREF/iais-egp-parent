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

public class INTRANET___BaiChuan___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="baiChuanDelegator";

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void doSearch_OnStepProcess_0() throws Exception {
	// 		doSearch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}

	public void sortRecords_OnStepProcess_0() throws Exception {
	// 		sortRecords->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSorting", this);
	}

	public void changePage_OnStepProcess_0() throws Exception {
	// 		changePage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPaging", this);
	}

	public void addCategory_OnStepProcess_0() throws Exception {
	// 		addCategory->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "addCategory", this);
	}

	public void prepareAddCategory_OnStepProcess_0() throws Exception {
	// 		prepareAddCategory->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareAddCategory", this);
	}

	public void addGame_OnStepProcess_0() throws Exception {
	// 		addGame->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "addGame", this);
	}

	public void prepareEdit_OnStepProcess_0() throws Exception {
	// 		prepareEdit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareEdit", this);
	}

	public void prepareAddGame_OnStepProcess_0() throws Exception {
	// 		prepareAddGame->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareAddGame", this);
	}

	public void doEdit_OnStepProcess_0() throws Exception {
	// 		doEdit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doEdit", this);
	}

	public void doBack_OnStepProcess_0() throws Exception {
	// 		doBack->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doBack", this);
	}

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}

}
