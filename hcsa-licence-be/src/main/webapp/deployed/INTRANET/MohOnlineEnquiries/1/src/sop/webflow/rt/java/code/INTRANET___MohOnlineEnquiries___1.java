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

public class INTRANET___MohOnlineEnquiries___1 extends BaseProcessClass {
	private static final String DELEGATOR ="onlineEnquiriesDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "start", this); 
		// 		Start->OnStepProcess
	}

	public void doBasicSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doBasicSearch", this); 
		// 		doBasicSearch->OnStepProcess
	}

	public void preSearchLicence_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preSearchLicence", this); 
		// 		preSearchLicence->OnStepProcess
	}

	public void doSearchLicence_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSearchLicence", this); 
		// 		doSearchLicence->OnStepProcess
	}

	public void doSearchLicenceAfter_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doSearchLicenceAfter", this); 
		// 		doSearchLicenceAfter->OnStepProcess
	}

	public void preLicDetails_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preLicDetails", this); 
		// 		preLicDetails->OnStepProcess
	}

	public void preBasicSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preBasicSearch", this); 
		// 		preBasicSearch->OnStepProcess
	}

	public void preAppDetails_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preAppDetails", this); 
	// 		preAppDetails->OnStepProcess
	}

	public void callCessation_OnStepProcess_0() throws Exception {
			EngineHelper.delegate(DELEGATOR, "callCessation", this); 
		// 		callCessation->OnStepProcess
		}

	public void preInspReport_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "preInspReport", this);
	// 		preInspReport->OnStepProcess
	}



}
