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

public class INTERNET___MohRfcPersonnelList___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="MohRequestForChangeMenuDelegator";

	public void preparePersonnelList_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preparePersonnelList", this);
	}

	public void doPersonnelList_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doPersonnelList", this);
	}

	public void preparePersonnelEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preparePersonnelEdit", this);
	}

	public void doPersonnelEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doPersonnelEdit", this);
	}

	public void init_OnStepProcess_0 () throws Exception { 
	
		// 		init->OnStepProcess
			EngineHelper.delegate(DELEGATOR, "preparePersonnel", this);
		} 

	public void controlSwitch_OnStepProcess_0 () throws Exception { 
	
		// 		ControlSwitch->OnStepProcess
			EngineHelper.delegate(DELEGATOR, "controlSwitch", this);
		}

	public void step1_OnStepProcess_0() throws Exception {
	// 		Step1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "personnleListStart", this);
	}

	public void step1_OnStepProcess_1() throws Exception {
	// 		Step1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "personnleListStart", this);
	}

	public void doPaging_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "personnlePaging", this);
	}

	public void doSorting_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "personnleSorting", this);
	}

	public void doSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "personnleSearch", this);
	}

	public void step2_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "personnleAckBack", this);
	}

	
}
