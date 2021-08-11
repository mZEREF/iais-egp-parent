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

public class INTRANET___AORevocationTaskList___1 extends BaseProcessClass {

	private static final String DELEGATOR = "AORevocationDelegator";
	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareTaskListData", this);
	}

	public void prepareSwitch_OnStepProcess_0() throws Exception {
		// 		prepareSwitch->OnStepProcess
	}

	public void doSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSearch", this);
	}

	public void doSorting_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSorting", this);
	}

	public void doPaging_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doPaging", this);
	}

}
