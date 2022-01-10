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

public class INTRANET___DOSubmitSuspension___1 extends BaseProcessClass {
	private static final String DELEGATOR ="bsbSuspensionDelegator";
	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void preData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void doValidate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSuspensionValidate", this);
	}

	public void submit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSuspension", this);
	}

}
