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

public class INTRANET___MohServiceRelatedInformation___1 extends BaseProcessClass {
    
    private static final String DELEGATOR = "serviceInfoDelegator";
    private static final String APPLICATIONDELEGATOR = "applicationDelegator";

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void prepareJumpPage_OnStepProcess_0() throws Exception {
	// 		PrepareJumpPage->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareJumpPage", this);
	}

	public void prepareJump_OnStepProcess_0() throws Exception {
	// 		prepareJump->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareJump", this);
	}
	
	public void prepareView_OnStepProcess_0() throws Exception {
		// 		PrepareView->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareView", this);
	}
	
	public void prepareResult_OnStepProcess_0() throws Exception {
	// 		PrepareResult->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareResult", this);
	}

	public void doSaveDraft_OnStepProcess_0() throws Exception {
	// 		doSaveDraft->OnStepProcess
		EngineHelper.delegate(APPLICATIONDELEGATOR, "doSaveDraft", this);
	}

	public void doSubmit_OnStepProcess_0() throws Exception {
	// 		doSubmit->OnStepProcess
		EngineHelper.delegate(APPLICATIONDELEGATOR, "doSubmit", this);
	}

	public void prepareStepData_OnStepProcess_0() throws Exception {
    // 		PrepareStepData->OnStepProcess prepareStepData
        EngineHelper.delegate(DELEGATOR, "prepareStepData", this);
    }

    public void doStep_OnStepProcess_0() throws Exception {
    // 		DoStep->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "doStep", this);
    }

	

}
