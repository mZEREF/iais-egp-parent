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

public class INTRANET___MohIntranetUser___1 extends BaseProcessClass {

	private static final String DELEGATOR ="IntranetUser";
	
	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this);
	}
	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}
	public void prepareSwitch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareSwitch", this);
	}
	public void prepareCreate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareCreate", this);
	}
	public void doCreate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doCreate", this);
	}
	public void prepareEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareEdit", this);
	}
	public void doEdit_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doEdit", this);
	}
	public void doDelete_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doDelete", this);
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
	public void doStatus_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "changeStatus", this);
	}
	public void doChange_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "saveStatus", this);
	}

}
