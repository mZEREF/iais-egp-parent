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

public class INTRANET___MohAdhocChecklist___1 extends BaseProcessClass {
	private static final String DELEGATOR ="adhocChecklistDelegator";
	
	public void initialize_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "initialize", this); 	
	}

	public void getNextStep_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "getNextStep", this); 	
	}

	public void receiveItemPool_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "receiveItemPool", this); 
	}

	public void customItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "customItem", this); 
	}

	public void appendToTail_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "appendToTail", this); 
	}

	public void removeAdhocItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "removeAdhocItem", this); 
	}

	public void doBack_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doBack", this); 
	}

	public void saveAdhocItem_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "saveAdhocItem", this); 
	}

	public void validateAdhocData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "validateAdhocData", this); 
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this); 
	}

	public void doCancel_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doCancel", this); 
	}

}
