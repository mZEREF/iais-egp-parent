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

public class INTRANET___MohCreTaskByInspecDate___1 extends BaseProcessClass {

	private static final long serialVersionUID = 4087180374623934619L;
	private static final String DELEGATOR ="inspectionCreTaskByInspDateDelegator";

	public void mohCreTaskByInspecDateStart_OnStepProcess_0() throws Exception {
	// 		MohCreTaskByInspecDateStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "mohCreTaskByInspecDateStart", this);
	}

	public void mohCreTaskByInspecDatePre_OnStepProcess_0() throws Exception {
	// 		MohCreTaskByInspecDatePre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "mohCreTaskByInspecDatePre", this);
	}
}
