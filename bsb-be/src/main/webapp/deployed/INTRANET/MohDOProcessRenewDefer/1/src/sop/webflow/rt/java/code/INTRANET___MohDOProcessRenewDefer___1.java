package sop.webflow.rt.java.code;
import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;

public class INTRANET___MohDOProcessRenewDefer___1 extends BaseProcessClass {
    private static final String DELEGATOR ="doProcessRenewDeferDelegator";

    public void start_OnStepProcess_0() throws Exception {
        // 		Start->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "start", this);
    }

    public void prepareData_OnStepProcess_0() throws Exception {
        // 		PrepareData->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "prepareData", this);
    }

    public void prepareSwitch_OnStepProcess_0() throws Exception {
        // 		PrepareSwitch->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
    }

    public void process_OnStepProcess_0() throws Exception {
        // 		Process->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "process", this);
    }

}
