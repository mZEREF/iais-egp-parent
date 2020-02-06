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

public class INTRANET___MohInspectionFillupChecklist___1 extends BaseProcessClass {
	private static final String DELEGATOR ="fillupChklistDelegator";


    public void start_OnStepProcess_0() throws Exception {
        // 		Start->OnStepProcess
    	EngineHelper.delegate(DELEGATOR, "start", this);
    }

    public void assignedInspectionTask_OnStepProcess_0() throws Exception {
        // 		AssignedInspectionTask->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "assignedInspectionTask", this);

    }

    public void inspectionChecklist_OnStepProcess_0() throws Exception {
        // 		InspectionChecklist->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "inspectionChecklist", this);
    }

    public void submitInspection_OnStepProcess_0() throws Exception {
        // 		SubmitInspection->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "submitInspection", this);
    }

	public void init_OnStepProcess_0() throws Exception {
	// 		init->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void preViewCheckList_OnStepProcess_0() throws Exception {
	// 		preViewCheckList->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preViewCheckList", this);
	}

	public void doCheckList_OnStepProcess_0() throws Exception {
	// 		doCheckList->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doCheckList", this);
	}
}
