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

public class INTERNET___MohBsbFacilityCertifierRegistration___1 extends BaseProcessClass {
	private static final String DELEGATOR ="bsbFacCertifierRegisterDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
	}

	public void init_OnStepProcess_0() throws Exception {
	// 		Init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void preBeginFacilityCertifier_OnStepProcess_0() throws Exception {
	// 		PreBeginFacilityCertifier->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preBeginFacilityCertifier", this);
	}

	public void handleBeginFacilityCertifier_OnStepProcess_0() throws Exception {
	// 		HandleBeginFacilityCertifier->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleBeginFacilityCertifier", this);
	}

	public void preCompanyProfile_OnStepProcess_0() throws Exception {
	// 		PreCompanyProfile->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preCompanyProfile", this);
	}

	public void handleCompanyProfile_OnStepProcess_0() throws Exception {
	// 		HandleCompanyProfile->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleCompanyProfile", this);
	}

	public void preAdministrator_OnStepProcess_0() throws Exception {
	// 		PreAdministrator->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preAdministrator", this);
	}

	public void handleAdministrator_OnStepProcess_0() throws Exception {
	// 		HandleAdministrator->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleAdministrator", this);
	}

	public void preCertifyingTeam_OnStepProcess_0() throws Exception {
	// 		PreCertifyingTeam->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preCertifyingTeam", this);
	}

	public void handleCertifyingTeam_OnStepProcess_0() throws Exception {
	// 		HandleCertifyingTeam->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleCertifyingTeam", this);
	}

	public void preCertifyingTeamPreview_OnStepProcess_0() throws Exception {
	// 		PreCertifyingTeamPreview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preCertifyingTeamPreview", this);
	}

	public void handleCertifyingTeamPreview_OnStepProcess_0() throws Exception {
	// 		HandleCertifyingTeamPreview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleCertifyingTeamPreview", this);
	}

	public void preSupportingDoc_OnStepProcess_0() throws Exception {
	// 		PreSupportingDoc->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preSupportingDoc", this);
	}

	public void handleSupportingDoc_OnStepProcess_0() throws Exception {
	// 		HandleSupportingDoc->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handleSupportingDoc", this);
	}

	public void prePreviewSubmit_OnStepProcess_0() throws Exception {
	// 		PrePreviewSubmit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preparePreviewSubmit", this);
	}

	public void handlePreviewSubmit_OnStepProcess_0() throws Exception {
	// 		HandlePreviewSubmit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "handlePreviewSubmit", this);
	}

	public void jumpFilter_OnStepProcess_0() throws Exception {
	// 		JumpFilter->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "jumpFilter", this);
	}

	public void actionFilter_OnStepProcess_0() throws Exception {
	// 		ActionFilter->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "actionFilter", this);
	}

	public void preAcknowledge_OnStepProcess_0() throws Exception {
	// 		PreAcknowledge->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preAcknowledge", this);
	}

	public void print_OnStepProcess_0() throws Exception {
	// 		Print->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "print", this);
	}

}