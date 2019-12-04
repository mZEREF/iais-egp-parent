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

public class INTRANET___ApplicationView___1 extends BaseProcessClass {

	private static final String DELEGATOR ="hcsaApplicationDelegator";
	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}





	public void aSO_OnStepProcess_0() throws Exception {
	// 		ASO->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "rontingTaskToASO", this);
	}


	public void iNS_OnStepProcess_0() throws Exception {
	// 		INS->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "rontingTaskToINS", this);
	}



	
	public void aO2_OnStepProcess_0() throws Exception {
	// 		AO2->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "rontingTaskToAO2", this);
	}


	public void aO3_OnStepProcess_0() throws Exception {
	// 		AO3->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "rontingTaskToAO3", this);
	}


	public void aO1_OnStepProcess_0() throws Exception {
	// 		AO1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "rontingTaskToAO1", this);
	}


	public void pSO_OnStepProcess_1() throws Exception {
	// 		PSO->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "rontingTaskToPSO", this);
	}


	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}





	public void chooseStage_OnStepProcess_0() throws Exception {
	// 		chooseStage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "chooseStage", this);
	}





	public void approve_OnStepProcess_0() throws Exception {
	// 		Approve->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "approve", this);
	}





	public void dMS_OnStepProcess_0() throws Exception {
	// 		DMS->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "routeToDMS", this);
	}





	public void routeBack_OnStepProcess_0() throws Exception {
	// 		RouteBack->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "routeBack", this);
	}





	public void internalEnquiry_OnStepProcess_0() throws Exception {
	// 		InternalEnquiry->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "internalEnquiry", this);
	}


















}
