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

public class INTRANET___MohIndividualConfig___1 extends BaseProcessClass {

	private static final String DELEGATOR ="hcsaRiskIndividualConfigDelegator";
	public void step1_OnStepProcess_0() throws Exception {
	// 		Step1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void step2_OnStepProcess_0() throws Exception {
	// 		Step2->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}
	
	public void preConfig_OnStepProcess_0() throws Exception {
		// 		PreConfig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}
	
	public void doSubmit_OnStepProcess_0() throws Exception {
	// 		DoSubmit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "submit", this);
	}

	public void doNext_OnStepProcess_0() throws Exception {
	// 		DoNext->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doNext", this);
	}

	public void preConfirm_OnStepProcess_0() throws Exception {
	// 		PreConfirm->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "confirm", this);
	}

	public void backMenu_OnStepProcess_0() throws Exception {
	// 		backMenu->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "backToMenu", this);
	}

	

}
