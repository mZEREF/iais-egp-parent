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

public class INTERNET___MohDpSIUpload___1 extends BaseProcessClass {
    
    private static final String DELEGATOR = "dpSiUploadDelegate";

    public void start_OnStepProcess_0() throws Exception {
    // 		Start->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "doStart", this);
    }

    public void prepareSwitch_OnStepProcess_0() throws Exception {
    // 		PrepareSwitch->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
    }

    public void preparePage_OnStepProcess_0() throws Exception {
    // 		PreparePage->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "preparePage", this);
    }

    public void pageAction_OnStepProcess_0() throws Exception {
    // 		PageAction->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "doPageAction", this);
    }

    public void submission_OnStepProcess_0() throws Exception {
    // 		Submission->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "doSubmission", this);
    }

    public void prepareReturn_OnStepProcess_0() throws Exception {
    // 		PrepareReturn->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "doSubmission", this);
    }

    public void preparePreview_OnStepProcess_0() throws Exception {
    // 		PreparePreview->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "preparePreview", this);
    }

    public void doPreview_OnStepProcess_0() throws Exception {
    // 		DoPreview->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "doPreview", this);
    }

}
