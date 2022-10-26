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

public class INTRANET___MohDOScreening___1 extends BaseProcessClass {

	private static final String DELEGATOR ="doScreeningDelegator";
	
	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		PrepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void process_OnStepProcess_0() throws Exception {
	// 		Process->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "process", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
	// 		PrepareSwitch->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}

}
