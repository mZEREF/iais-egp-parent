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

public class INTRANET___HuangKun___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="huangKunDelegator";

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

}
