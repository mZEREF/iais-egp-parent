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

public class INTRANET___MohRemindDoPreInspTask___1 extends BaseProcessClass {

	private static final long serialVersionUID = 489301685242797166L;
	private static final String DELEGATOR ="remindInspectorPreInspTask";

	public void remindDoPreInspTaskStart_OnStepProcess_0() throws Exception {
	// 		remindDoPreInspTaskStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "remindDoPreInspTaskStart", this);
	}

	public void remindDoPreInspTaskStep_OnStepProcess_0() throws Exception {
	// 		remindDoPreInspTaskStep->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "remindDoPreInspTaskStep", this);
	}

}
