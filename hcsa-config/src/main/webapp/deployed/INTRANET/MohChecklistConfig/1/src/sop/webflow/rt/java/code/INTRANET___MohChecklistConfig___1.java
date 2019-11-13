package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohChecklistConfig___1 extends BaseProcessClass {
	private static final String DELEGATOR ="hcsaChklConfigDelegator";
	public void prepare_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}
	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}
	public void switchAction_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "switchAction", this);
	}
	public void addConfigNextAction_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "addConfigNextAction", this);
	}
	public void prepareConfigSectionInfo_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareConfigSectionInfo", this);
	}
	public void preViewConfig_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preViewConfig", this);
	}
	public void prepareAddConfig_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareAddConfig", this);
	}
	public void submitConfig_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "submitConfig", this);
	}
	public void doSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}
	public void prepareChecklistItemInfo_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareChecklistItemInfo", this);
	}
	public void addChecklistItemNextAction_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "addChecklistItemNextAction", this);
	}
	public void addSectionItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "addSectionItem", this);
	}
	
	
	
}