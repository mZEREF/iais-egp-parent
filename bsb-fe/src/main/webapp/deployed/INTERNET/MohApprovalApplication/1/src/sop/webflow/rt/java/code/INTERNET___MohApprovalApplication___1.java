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

public class INTERNET___MohApprovalApplication___1 extends BaseProcessClass {
	private static final String DELEGATOR ="newApprovalDelegator";
	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void prepare_OnStepProcess_0() throws Exception {
	// 		Prepare->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}

	public void prepareDocuments_OnStepProcess_0() throws Exception {
	// 		PrepareDocuments->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareDocuments", this);
	}

	public void preparePreview_OnStepProcess_0() throws Exception {
	// 		PreparePreview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preparePreview", this);
	}

	public void prepareForms_OnStepProcess_0() throws Exception {
	// 		PrepareForms->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareForms", this);
	}

	public void doDocuments_OnStepProcess_0() throws Exception {
	// 		doDocuments->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doDocuments", this);
	}

	public void doPreview_OnStepProcess_0() throws Exception {
	// 		doPreview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPreview", this);
	}

	public void doForms_OnStepProcess_0() throws Exception {
	// 		doForms->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doForms", this);
	}

	public void controlSwitch_OnStepProcess_0() throws Exception {
	// 		ControlSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "controlSwitch", this);
	}

	public void doSaveDraft_OnStepProcess_0() throws Exception {
	// 		doSaveDraft->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSaveDraft", this);
	}

	public void doSubmit_OnStepProcess_0() throws Exception {
	// 		doSubmit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doSubmit", this);
	}

	public void prepareCompanyInfo_OnStepProcess_0() throws Exception {
	// 		PrepareCompanyInfo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareCompanyInfo", this);
	}
}
