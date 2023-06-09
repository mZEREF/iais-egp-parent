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

public class INTRANET___IntrantUserRoleExport___1 extends BaseProcessClass {
	
	private static final long serialVersionUID = 5916476168198239621L;
	private static final String DELEGATOR ="intrantUserRoleExport";

	public void intrantUserRoleExportStart_OnStepProcess_0() throws Exception {
	// 		IntrantUserRoleExportStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "intrantUserRoleExportStart", this);
	}

	public void intrantUserRoleExportDo_OnStepProcess_0() throws Exception {
	// 		IntrantUserRoleExportDo->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "intrantUserRoleExportDo", this);
	}

}
