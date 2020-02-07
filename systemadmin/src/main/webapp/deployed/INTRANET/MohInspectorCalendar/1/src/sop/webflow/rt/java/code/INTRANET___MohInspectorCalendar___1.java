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

public class INTRANET___MohInspectorCalendar___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="inspectorCalendarDelegator";
	
	public void preLoad_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preLoad", this);
	}

	public void doQuery_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doQuery", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "startStep", this);
	}

	public void doPaging_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doPaging", this);
	}

	public void doSorting_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSorting", this);
	}

}
