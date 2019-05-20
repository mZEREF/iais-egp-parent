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

import com.ecquaria.egp.api.AppStatus;
import com.ecquaria.egp.core.bat.BATControllerHelper;


import ecq.commons.helper.StringHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class EGOV___DynamicFlowV2___1 extends
		BaseProcessClass {


	/**
	 *
	 */
	private static final long serialVersionUID = -3990666281548825204L;

	private static final String ATTR_BACK_STEP = "$BACK_STEP";
	private static final String ATTR_STAGE_NAME = "$CURRENT_STAGE_NAME";

	private static final String KEY_REQUIRED = "required";
	private static final String KEY_STATUS = "status";

	private static final String STAGE_VETTING = "vetting";
	private static final String STATUS_VETTING_DEFAULT = "101003000";
	private static final String STATUS_VETTING = "101003001";
	private static final String STATUS_VETTING_REJECT = "101003002";
	private static final String STATUS_VETTING_PENDING_AMENDMENT = "101003003";
	private static final String STATUS_VETTING_PENDING_APPIN_FEE = "101003004";

	private static final String STAGE_PROCESSING = "processing";
	private static final String STATUS_PROCESSING_DEFAULT = "101001010";
	private static final String STATUS_PROCESSING = "101001011";
	private static final String STATUS_PROCESSING_REJECT = "101001012";
	private static final String STATUS_PROCESSING_PENDING_AMENDMENT = "101001013";

	private static final String STATUS_PROCESSING_ENDORSEMENT = "101001014";


	private static final String STAGE_APPROVAL = "approval";
	private static final String STATUS_APPROVAL_DEFAULT = "101002000";
	private static final String STATUS_APPROVAL_APPROVE = "101002001";
	private static final String STATUS_APPROVAL_REJECT = "101002002";
	private static final String STATUS_APPROVAL_PENDING_AMENDMENT = "101002003";
	private static final String STATUS_APPROVAL_ENDORSEMENT = "101002004";

	//	private static final String STATUS_APPROVAL_PENDING_PAYMENT = "101002005";
	private static final String STATUS_APPROVAL_COMPLETED = "101002006";

	private static final String STAGE_COLLECTION = "collection";
	private static final String STATUS_COLLECTION_DEFAULT = "101004000";
	private static final String STATUS_COLLECTION = "101004001";

	private static final String STATUS_PROCESSING_PENDING_APPIN_FEE_DEFAULT = "101005000";
	//private static final String STATUS_PROCESSING_PENDING_APPIN_FEE = "101005001";
	private static final String STATUS_PROCESSING_PAYMENT_DEFAULT = "101006000";
	// AppInFeeCheck->OnStepProcess
	public void appInFeeCheck_OnStepProcess_0() throws Exception {
		String typeValue = BATControllerHelper.getConditionValue(this,
				this.controller.getCurrentComponentName());
		request.setAttribute(KEY_REQUIRED,
				String.valueOf("true".equals(typeValue)));
	}

	// PendingAppInFee->OnStepProcess
	public void pendingAppInFee_OnStepProcess_0() throws Exception {
		BATControllerHelper.updateAppStatus(this,
				STATUS_PROCESSING_PENDING_APPIN_FEE_DEFAULT,
				this.controller.getCurrentComponentName());
		back();
	}

	// ProcessingCondition->OnStepProcess
	public void processingCheck_OnStepProcess_0() throws Exception {
		String typeValue = BATControllerHelper.getConditionValue(this,
				this.controller.getCurrentComponentName());
		request.setAttribute(KEY_REQUIRED,
				String.valueOf("true".equals(typeValue)));
	}

	// PendingProcessing->OnStepProcess
	public void pendingProcessing_OnStepProcess_0() throws Exception {
		BATControllerHelper.updateAppStatus(this, STATUS_PROCESSING_DEFAULT);
		BATControllerHelper.sendTask(this, STAGE_PROCESSING, false);
		back();
	}

	// BindProcessingStage->OnStepProcess
	public void bindProcessingStage_OnStepProcess_0() throws Exception {
		setCurrentStage(STAGE_PROCESSING);
	}

	// ApprovalCondition->OnStepProcess
	public void approvalCheck_OnStepProcess_0() throws Exception {
		String typeValue = BATControllerHelper.getConditionValue(this,
				this.controller.getCurrentComponentName());
		request.setAttribute(KEY_REQUIRED,
				String.valueOf("true".equals(typeValue)));
	}

	// PendingApproval->OnStepProcess
	public void pendingApproval_OnStepProcess_0() throws Exception {
		BATControllerHelper.updateAppStatus(this, STATUS_APPROVAL_DEFAULT);
		BATControllerHelper.sendTask(this, STAGE_APPROVAL, false);
		back();
	}

	// BindApprovalStage->OnStepProcess
	public void bindApprovalStage_OnStepProcess_0() throws Exception {
		setCurrentStage(STAGE_APPROVAL);
	}

	// PaymentCondition->OnStepProcess
	public void paymentCheck_OnStepProcess_0() throws Exception {
		String typeValue = BATControllerHelper.getConditionValue(this,
				this.controller.getCurrentComponentName());
		request.setAttribute(KEY_REQUIRED,
				String.valueOf("true".equals(typeValue)));
	}

	// PendingPayment->OnStepProcess
	public void pendingPayment_OnStepProcess_0() throws Exception {
		BATControllerHelper.updateAppStatus(this,
				STATUS_PROCESSING_PAYMENT_DEFAULT,
				this.controller.getCurrentComponentName());
		back();
	}

	// CollectionCondition->OnStepProcess
	public void collectionCheck_OnStepProcess_0() throws Exception {
		String typeValue = BATControllerHelper.getConditionValue(this,
				this.controller.getCurrentComponentName());
		request.setAttribute(KEY_REQUIRED,
				String.valueOf("true".equals(typeValue)));
	}

	// PendingCollection->OnStepProcess
	public void pendingCollection_OnStepProcess_0() throws Exception {
		BATControllerHelper.updateAppStatus(this, STATUS_COLLECTION_DEFAULT);
		BATControllerHelper.sendTask(this, STAGE_COLLECTION, false);
		back();
	}


	// Completed->OnStepProcess
	public void completed_OnStepProcess_0() throws Exception {
		BATControllerHelper.updateAppStatus(this, STATUS_APPROVAL_COMPLETED);
		back();
	}

	// Prepare->OnStepProcess
	public void prepare_OnStepProcess_0() throws Exception {
		String curStage = getCurrentStage();
		BATControllerHelper.prepareApplicationView(this, curStage);

	}

	// BindStatus->OnStepProcess
	public void bindStatus_OnStepProcess_0() throws Exception {
		BATControllerHelper.completeTask(this);
		AppStatus appStatus = BATControllerHelper.getSubmittedAppStatus(this);
		String status = null;
		// processing
		if (STATUS_PROCESSING.equals(appStatus.getCode())) {
			status = "processing";
			BATControllerHelper.updateAppStatus(this, appStatus.getCode());
		} else if (STATUS_APPROVAL_APPROVE.equals(appStatus.getCode())) {
			status = "approve";
			BATControllerHelper.updateAppStatus(this, appStatus.getCode());
		} else if (STATUS_PROCESSING_PENDING_AMENDMENT.equals(appStatus.getCode())) {
			status = "processingAmendment";

		} else if (STATUS_APPROVAL_PENDING_AMENDMENT.equals(appStatus.getCode())){
			status = "approvalAmendment";

		} else if(STATUS_VETTING.equals(appStatus.getCode())){
			status = "vetting";
			BATControllerHelper.updateAppStatus(this, appStatus.getCode());
		} else if(STATUS_VETTING_PENDING_AMENDMENT.equals(appStatus.getCode())){
			status = "vettingAmendment";
		}else if(STATUS_COLLECTION.equals(appStatus.getCode())){
			status = "collection";
			BATControllerHelper.updateAppStatus(this, appStatus.getCode());
		}else if(STATUS_PROCESSING_ENDORSEMENT.equals(appStatus.getCode())){
			status = "processingEndorsement";
			//BATControllerHelper.updateAppStatus(this, appStatus.getCode());
		}else if (STATUS_APPROVAL_ENDORSEMENT.equals(appStatus.getCode())) {
            status = "approvalEndorsement";
        }else {
			status = "reject";
		}
		request.setAttribute(KEY_STATUS, status);
	}

	// Reject->OnStepProcess
	public void reject_OnStepProcess_0() throws Exception {
		AppStatus appStatus = BATControllerHelper.getSubmittedAppStatus(this);
		if (STATUS_APPROVAL_REJECT.equals(appStatus.getCode())) {
			setBackStep("BindApprovalStage");

		} else if (STATUS_PROCESSING_REJECT.equals(appStatus.getCode())) {
			setBackStep("BindProcessingStage");
		}else if(STATUS_VETTING_REJECT.equals(appStatus.getCode())){
			setBackStep("BindVettingStage");
		}

		// set submit status.
		BATControllerHelper.updateAppStatus(this, appStatus.getCode());
		back();
	}

	// PendingAmendment->OnStepProcess
	public void pendingAmendment_OnStepProcess_0() throws Exception {
		setBackStep("BindProcessingStage");
		// set submit status.
		BATControllerHelper.updateAppStatus(this, BATControllerHelper.getSubmittedAppStatus(this).getCode(), this.controller.getCurrentComponentName());
		back();
	}

	// Processing->OnStepProcess
	public void processing_OnStepProcess_0() throws Exception {
		setBackStep("BindApprovalStage");
	}

	// 		Approve->OnStepProcess
	public void approve_OnStepProcess_0() throws Exception {
		setBackStep("BindCollectionStage");
	}

	// 		PendingApprovalAmendment->OnStepProcess
	public void pendingApprovalAmendment_OnStepProcess_0() throws Exception {
		setBackStep("BindApprovalStage");
		// set submit status.
		BATControllerHelper.updateAppStatus(this, BATControllerHelper.getSubmittedAppStatus(this).getCode(), this.controller.getCurrentComponentName());
		back();
	}

	private void setCurrentStage(String stageName) {
		request.setAttribute(ATTR_STAGE_NAME, stageName);
	}

	private String getCurrentStage() {
		return (String) request.getAttribute(ATTR_STAGE_NAME);
	}

	private void setBackStep(String stepName) {
		request.setAttribute(ATTR_BACK_STEP, stepName);
	}

	private void back() {
		String backStep = (String) request.getAttribute(ATTR_BACK_STEP);
		if (StringHelper.isEmpty(backStep)) {
			BATControllerHelper.invokeReturnStep(this);
			return;
		}

		request.setAttribute(ATTR_BACK_STEP, null);
		String project = this.controller.getCurrentProject();
		String path = this.controller.getCurrentProcessPath();
		String processName = this.controller.getCurrentProcessName();
		int version = this.controller.getCurrentProcessVersion();
		this.controller.invokeProcess(project, path, processName, version,
				backStep);
	}

	public void vettingCond_OnStepProcess_0() throws Exception {
		// 		VettingCond->OnStepProcess
		String typeValue = BATControllerHelper.getConditionValue(this,
				this.controller.getCurrentComponentName());
		request.setAttribute(KEY_REQUIRED,
				String.valueOf("true".equals(typeValue)));
	}

	public void pendingVetting_OnStepProcess_0() throws Exception {
		// 		PendingVetting->OnStepProcess
		BATControllerHelper.updateAppStatus(this, STATUS_VETTING_DEFAULT);
		BATControllerHelper.sendTask(this, STAGE_VETTING, false);
		back();
	}

	public void bindVettingStage_OnStepProcess_0() throws Exception {
		// 		BindVettingStage->OnStepProcess
		setCurrentStage(STAGE_VETTING);
	}

	public void pendingVettingAmendment_OnStepProcess_0() throws Exception {
		// 		PendingVettingAmendment->OnStepProcess
		setBackStep("BindVettingStage");
		// set submit status.
		BATControllerHelper.updateAppStatus(this, BATControllerHelper.getSubmittedAppStatus(this).getCode(), this.controller.getCurrentComponentName());
		back();
	}

	public void vetting_OnStepProcess_0() throws Exception {
		// 		Vetting->OnStepProcess
		setBackStep("BindProcessingStage");
	}

	public void collection_OnStepProcess_0() throws Exception {
		// 		Collection->OnStepProcess
		setBackStep("BindCollectionStage");
	}

	public void bindCollectionStage_OnStepProcess_0() throws Exception {
		// 		BindCollectionStage->OnStepProcess
		setCurrentStage(STAGE_COLLECTION);
	}

	public void approvalEndorsement_OnStepProcess_0() throws Exception {
		// 		ApprovalEndorsement->OnStepProcess
		setBackStep("BindProcessingStage");
		BATControllerHelper.updateAppStatus(this, STATUS_APPROVAL_ENDORSEMENT);
        BATControllerHelper.sendEndorsementTask(this, STAGE_APPROVAL, false);
        back();
	}

	public void processingEndorsement_OnStepProcess_0() throws Exception {
		// 		ProcessingEndorsement->OnStepProcess
		setBackStep("BindProcessingStage");
		BATControllerHelper.updateAppStatus(this, STATUS_PROCESSING_ENDORSEMENT);
		BATControllerHelper.sendEndorsementTask(this, STAGE_PROCESSING, false);
		back();
	}

}
