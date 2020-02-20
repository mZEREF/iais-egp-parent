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

public class INTRANET___MohApptInspectionDate___1 extends BaseProcessClass {

	private static final long serialVersionUID = 7068123058164700553L;
	private static final String DELEGATOR ="apptInspectionDateDelegator";

	public void apptInspectionDateStart_OnStepProcess_0() throws Exception {
	// 		apptInspectionDateStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptInspectionDateStart", this);
	}

	public void apptInspectionDateInit_OnStepProcess_0() throws Exception {
	// 		apptInspectionDateInit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptInspectionDateInit", this);
	}

	public void apptInspectionDatePre_OnStepProcess_0() throws Exception {
	// 		apptInspectionDatePre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptInspectionDatePre", this);
	}

	public void apptInspectionDateVali_OnStepProcess_0() throws Exception {
	// 		apptInspectionDateVali->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptInspectionDateVali", this);
	}

	public void apptInspectionDateSuccess_OnStepProcess_0() throws Exception {
	// 		apptInspectionDateSuccess->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptInspectionDateSuccess", this);
	}

}
