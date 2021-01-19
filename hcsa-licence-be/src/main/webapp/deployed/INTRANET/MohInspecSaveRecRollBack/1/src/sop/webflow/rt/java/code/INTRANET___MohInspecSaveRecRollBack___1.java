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

public class INTRANET___MohInspecSaveRecRollBack___1 extends BaseProcessClass {

	private static final long serialVersionUID = -770769921561882946L;
	private static final String DELEGATOR ="inspecSaveRecRollBackDelegator";

	public void inspecSaveRecRollBackStart_OnStepProcess_0() throws Exception {
	// 		InspecSaveRecRollBackStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspecSaveRecRollBackStart", this);
	}

	public void inspecSaveRecRollBackAction_OnStepProcess_0() throws Exception {
	// 		InspecSaveRecRollBackAction->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspecSaveRecRollBackAction", this);
	}

}
