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

public class INTERNET___MohWithOutRenewal___1 extends BaseProcessClass {
	private static final String DELEGATOR ="withOutRenewalDelegator";
	public void preparatData_OnStepProcess_0() throws Exception {
	// 		preparatData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}
	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}
	public void prepareInstructions_OnStepProcess_0() throws Exception {
	// 		prepareInstructions
		EngineHelper.delegate(DELEGATOR, "prepareInstructions", this);
	}
	public void prepareLicenceReview_OnStepProcess_0() throws Exception {
	// 		prepareLicenceReview
		EngineHelper.delegate(DELEGATOR, "prepareLicenceReview", this);
	}
	public void preparePayment_OnStepProcess_0() throws Exception {
	// 		preparePayment
		EngineHelper.delegate(DELEGATOR, "preparePayment", this);
	}
	public void prepareAcknowledgement_OnStepProcess_0() throws Exception {
	// 		prepareAcknowledgement
		EngineHelper.delegate(DELEGATOR, "prepareAcknowledgement", this);
	}
	public void doInstructions_OnStepProcess_0() throws Exception {
	// 		doInstructions
		EngineHelper.delegate(DELEGATOR, "doInstructions", this);
	}
	public void doLicenceReview_OnStepProcess_0() throws Exception {
	// 		doLicenceReview
		EngineHelper.delegate(DELEGATOR, "doLicenceReview", this);
	}
	public void doPayment_OnStepProcess_0() throws Exception {
	// 		doPayment
		EngineHelper.delegate(DELEGATOR, "doPayment", this);
	}
	public void doAcknowledgement_OnStepProcess_0() throws Exception {
	// 		doAcknowledgement
		EngineHelper.delegate(DELEGATOR, "doAcknowledgement", this);
	}
	public void toPrepareData_OnStepProcess_0() throws Exception {
	// 		toPrepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "toPrepareData", this);
	}
	public void prepareJump_OnStepProcess_0() throws Exception {
	// 		prepareJump->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareJump", this);
	}
	public void step2_OnStepProcess_0() throws Exception {
	// 		controlSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "controlSwitch", this);
	}

}
