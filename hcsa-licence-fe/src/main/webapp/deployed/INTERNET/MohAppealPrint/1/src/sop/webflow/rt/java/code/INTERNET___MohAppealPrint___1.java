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

public class INTERNET___MohAppealPrint___1 extends BaseProcessClass {
	private static final String DELEGATOR ="mohAppealPrint";
	public void step1_OnStepProcess_0() throws Exception {
	// 		Step1->OnStepProcess
		 EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void start_OnStepProcess_0() throws Exception {
	// 		start->OnStepProcess
		 EngineHelper.delegate(DELEGATOR, "start", this);
	}

}
