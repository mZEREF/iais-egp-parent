package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;


public class IAIS___suochengTest___1 extends BaseProcessClass {
    private static final String DELEGATOR ="testDelegator";
    public void prepareData_OnStepProcess_0() throws Exception {
    }
    public void validate_OnStepProcess_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "validate", this);

    }
    public void fillFormPage_OnPageLoad_0() throws Exception {

    }
    public void applicationForm_OnPageLoad_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "prepareData", this);
    }
    public void saveDraft_OnStepProcess_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "saveDraft", this);
    }
    public void initSaveDraftData_OnStepProcess_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "initSaveDraftData", this);
    }

    public void bATStep0_OnApplicationCreate_0() throws Exception {
        EngineHelper.delegate(DELEGATOR, "bATStep0_OnApplicationCreate", this);
    }

    public static void bATStep0_OnPostStatusChange_1(IPostAppContext context) throws Exception {

    }

    public static void bATStep0_OnPreTransfer_0(IPreTransferContext context) throws Exception {

    }
}
