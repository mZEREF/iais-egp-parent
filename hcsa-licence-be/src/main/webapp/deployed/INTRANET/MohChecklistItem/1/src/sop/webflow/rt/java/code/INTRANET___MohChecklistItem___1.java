package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;

import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohChecklistItem___1 extends BaseProcessClass {
	private static final String DELEGATOR ="hcsaChklItemDelegator";

	public void prepareItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareChecklistItem", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

	public void addChecklistItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "saveChecklistItem", this);
	}

	public void deleteChecklistItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "deleteChecklistItem", this);
	}

	public void updateChecklistItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "saveChecklistItem", this);
	}

	public void doSort_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "sortRecords", this);
	}

	public void doSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}

	public void editCloneItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "editCloneItem", this);
	}

	public void switchClone_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "switchClone", this);
	}

	public void prepareAddItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareAddItem", this);
	}

	public void prepareEditItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareEditItem", this);
	}

	public void viewCloneData_OnStepProcess_1() throws Exception {
		EngineHelper.delegate(DELEGATOR, "viewCloneData", this);
	}

	public void prepareCloneItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareCloneItem", this);
	}

	public void submitCloneItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "submitCloneItem", this);
		}

	public void cancelClone_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "cancelClone", this);
	}

	public void configToChecklist_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "configToChecklist", this);
	}

	public void preUploadData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preUploadData", this);
	}

	public void submitUploadData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "submitUploadData", this);
	}
	
	
}