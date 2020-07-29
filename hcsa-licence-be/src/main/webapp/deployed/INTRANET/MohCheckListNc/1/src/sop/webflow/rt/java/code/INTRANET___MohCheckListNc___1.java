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

public class INTRANET___MohCheckListNc___1 extends BaseProcessClass {
	private static final String DELEGATOR ="inspectionNcCheckListDelegator";
	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void pre_OnStepProcess_0() throws Exception {
	// 		pre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "pre", this);
	}

	public void doNext_OnStepProcess_0() throws Exception {
	// 		doNext->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doNext", this);
	}

	public void preViewCheckList_OnStepProcess_0() throws Exception {
	// 		preViewCheckList->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preViewCheckList", this);
	}

	public void doCheckList_OnStepProcess_0() throws Exception {
	// 		doCheckList->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doCheckList", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "successViewPre", this);
	// 		Step1->OnStepProcess
	}

	public void step2_OnStepProcess_0() throws Exception {
	// 		Step2->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "successViewBack", this);
	}

	public void doSubmit_OnStepProcess_0() throws Exception {
	// 		doSubmit->OnStepProcess
	}

	public void addAhoc_OnStepProcess_0() throws Exception {
	// 		addAhoc->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "addAhocs", this);
	}

	public void deleteAhoc_OnStepProcess_0() throws Exception {
	// 		saveAhoc->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveAhocs", this);
	}

	public void listAhocs_OnStepProcess_0() throws Exception {
	// 		listAhocs->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "listAhocs", this);
	}

	public void changeTab_OnStepProcess_0() throws Exception {
		// 		changeTab->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "changeTab", this);
	}
}
