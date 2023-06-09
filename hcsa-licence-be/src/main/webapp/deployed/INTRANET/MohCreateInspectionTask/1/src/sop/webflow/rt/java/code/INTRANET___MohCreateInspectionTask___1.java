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

public class INTRANET___MohCreateInspectionTask___1 extends BaseProcessClass {
	
	private static final long serialVersionUID = 5292419497082584698L;
	private static final String DELEGATOR ="createDoInspTaskBatchJob";

	public void mohCreateInspectionTask_OnStepProcess_0() throws Exception {
	// 		mohCreateInspectionTask->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "mohCreateInspectionTask", this);
	}

	public void step1_OnStepProcess_0() throws Exception {
	// 		Step1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "mohCreateInspectionTaskStart", this);
	}

}
