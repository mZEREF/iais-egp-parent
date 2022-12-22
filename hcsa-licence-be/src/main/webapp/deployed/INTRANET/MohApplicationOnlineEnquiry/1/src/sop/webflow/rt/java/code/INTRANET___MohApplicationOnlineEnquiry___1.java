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

public class INTRANET___MohApplicationOnlineEnquiry___1 extends BaseProcessClass {
	private static final String DELEGATOR ="onlineEnquiryApplicationDelegator";


	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this); 	
// 		start->OnStepProcess
	}

	public void preSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preSearch", this); 	
// 		preSearch->OnStepProcess
	}

	public void nextStep_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "nextStep", this); 	
// 		nextStep->OnStepProcess
	}

	public void preAppInfo_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preAppInfo", this); 	
// 		preAppInfo->OnStepProcess
	}

	public void preInsTab_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preInsTab", this); 	
// 		preInsTab->OnStepProcess
	}

	public void insStep_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "insStep", this); 	
// 		insStep->OnStepProcess
	}

	public void preInspectionReport_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preInspectionReport", this); 	
// 		preInspectionReport->OnStepProcess
	}

	public void backInsTab_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "backInsTab", this); 	
// 		backInsTab->OnStepProcess
	}

}