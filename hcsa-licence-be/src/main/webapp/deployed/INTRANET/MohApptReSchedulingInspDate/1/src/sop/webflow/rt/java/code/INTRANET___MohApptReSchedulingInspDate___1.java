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

public class INTRANET___MohApptReSchedulingInspDate___1 extends BaseProcessClass {

	private static final long serialVersionUID = -7278735588392412705L;
	private static final String DELEGATOR ="apptReSchedulingInspDateDelegator";

	public void apptReSchInspDateStart_OnStepProcess_0() throws Exception {
	// 		apptReSchInspDateStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptReSchInspDateStart", this);
	}

	public void apptReSchInspDateInit_OnStepProcess_0() throws Exception {
	// 		apptReSchInspDateInit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptReSchInspDateInit", this);
	}

	public void apptReSchInspDatePre_OnStepProcess_0() throws Exception {
	// 		apptReSchInspDatePre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptReSchInspDatePre", this);
	}

	public void apptReSchInspDateVali_OnStepProcess_0() throws Exception {
	// 		apptReSchInspDateVali->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptReSchInspDateVali", this);
	}

	public void apptReSchInspDateSuccess_OnStepProcess_0() throws Exception {
	// 		apptReSchInspDateSuccess->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptReSchInspDateSuccess", this);
	}

}
