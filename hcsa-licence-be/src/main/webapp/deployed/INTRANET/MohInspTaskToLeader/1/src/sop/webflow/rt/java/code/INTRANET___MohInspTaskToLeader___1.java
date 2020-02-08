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

public class INTRANET___MohInspTaskToLeader___1 extends BaseProcessClass {

	private static final long serialVersionUID = 221140796843598389L;
	private static final String DELEGATOR ="inspecTaskToLeaderBatchJob";

	public void inspTaskToLeaderStart_OnStepProcess_0() throws Exception {
	// 		inspTaskToLeaderStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspTaskToLeaderStart", this);
	}

	public void inspTaskToLeaderJob_OnStepProcess_0() throws Exception {
	// 		inspTaskToLeaderJob->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspTaskToLeaderJob", this);
	}

}
