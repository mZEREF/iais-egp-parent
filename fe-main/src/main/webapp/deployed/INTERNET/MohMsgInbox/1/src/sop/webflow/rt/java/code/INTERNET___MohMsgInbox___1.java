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

public class INTERNET___MohMsgInbox___1 extends BaseProcessClass {
    
    private static final String DELEGATOR ="mohMsgInboxDelegator";

    public void start_OnStepProcess_0() throws Exception {
    // 		Start->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "start", this);
    }

    public void prepareMsg_OnStepProcess_0() throws Exception {
    // 		PrepareMsg->OnStepProcess
        EngineHelper.delegate(DELEGATOR, "prepare", this);
    }

}
