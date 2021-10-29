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

public class INTERNET___MohFacilityCertifierRegistration___1 extends BaseProcessClass {
	private static final String DELEGATOR ="bsbFacCertifierRegisterDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
	}

	public void init_OnStepProcess_0() throws Exception {
		// 		init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void preCompInfo_OnStepProcess_0() throws Exception {
		// 		PreCompInfo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preCompInfo", this);
	}

	public void doCompInfo_OnStepProcess_0() throws Exception {
		// 		doCompInfo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doCompInfo", this);
	}
	
	public void preOrganisationInfo_OnStepProcess_0() throws Exception {
	// 		PreOrganisationInfo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preOrganisationInfo", this);
	}

	public void preAdministrator_OnStepProcess_0() throws Exception {
	// 		PreAdministrator->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preAdministrator", this);
	}

	public void preCertifyingTeam_OnStepProcess_0() throws Exception {
	// 		PreCertifyingTeam->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preCertifyingTeam", this);
	}

	public void prepareDocuments_OnStepProcess_0() throws Exception {
	// 		PrepareDocuments->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareDocuments", this);
	}

	public void preparePreview_OnStepProcess_0() throws Exception {
	// 		PreparePreview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preparePreview", this);
	}

	public void doAdministrator_OnStepProcess_0() throws Exception {
	// 		doAdministrator->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doAdministrator", this);
	}

	public void doCertifyingTeam_OnStepProcess_0() throws Exception {
	// 		doCertifyingTeam->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doCertifyingTeam", this);
	}

	public void doOriganisationProfile_OnStepProcess_0() throws Exception {
	// 		doOriganisationProfile->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doOrganisationProfile", this);
	}

	public void doDocument_OnStepProcess_0() throws Exception {
	// 		doDocument->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doDocument", this);
	}

	public void doPreview_OnStepProcess_0() throws Exception {
	// 		doPreview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPreview", this);
	}

	public void controlSwitch_OnStepProcess_0() throws Exception {
	// 		ControlSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "controlSwitch", this);
	}

	public void prepare_OnStepProcess_0() throws Exception {
	// 		Prepare->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}

	public void preAcknowledge_OnStepProcess_0() throws Exception {
		// 		PreAcknowledge->OnStepProcess
	}

}
