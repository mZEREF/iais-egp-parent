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

public class INTRANET___MohMassEmailBlast___1 extends BaseProcessClass {

	private static final String DELEGATOR ="BlastManagementDelegator";
	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this); 
	}

	public void prepare_OnStepProcess_0() throws Exception {
	// 		Prepare->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepare", this); 
	}

	public void create_OnStepProcess_0() throws Exception {
	// 		Create->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "create", this); 
	}

	public void delete_OnStepProcess_0() throws Exception {
	// 		Delete->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "delete", this); 
	}

	public void edit_OnStepProcess_0() throws Exception {
	// 		Edit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "edit", this); 
	}

	public void search_OnStepProcess_0() throws Exception {
	// 		Search->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "search", this); 
	}

	public void save_OnStepProcess_0() throws Exception {
	// 		Save->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "save", this); 
	}

	public void fillMessage_OnStepProcess_0() throws Exception {
	// 		FillMessage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "fillMessage", this); 
	}

	public void writeMessage_OnStepProcess_0() throws Exception {
	// 		writeMessage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "writeMessage", this); 
	}

	public void selectRecipients_OnStepProcess_0() throws Exception {
	// 		SelectRecipients->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "selectRecipients", this); 
	}

	public void auditTrial_OnStepProcess_0() throws Exception {
	// 		AuditTrial->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "auditTrial", this); 
	}

	public void step2_OnStepProcess_0() throws Exception {
	// 		Step2->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "fillMessageSuccess", this); 
	}

	public void step1_OnStepProcess_0() throws Exception {
	// 		Step1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "writeMessageSuccess", this); 
	}

	public void step3_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "switchBackFill", this); 
	}

	public void sms_OnStepProcess_0() throws Exception {
	// 		Sms->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "writeSms", this); 
	}

	public void step4_OnStepProcess_0() throws Exception {
	// 		Step4->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "saveSms", this); 
	}

	public void createBeforeFill_OnStepProcess_0() throws Exception {
	// 		CreateBeforeFill->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "createBeforeFill", this); 
	}

	public void editBeforeFill_OnStepProcess_0() throws Exception {
	// 		EditBeforeFill->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "editBeforeFill", this); 
	}

	public void preview_OnStepProcess_0() throws Exception {
	// 		Preview->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preview", this); 
	}

	public void auditPage_OnStepProcess_0() throws Exception {
	// 		AuditPage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "auditPage", this); 
	}

	public void doPage_OnStepProcess_0() throws Exception {
	// 		doPage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doPage", this); 
	}

}
