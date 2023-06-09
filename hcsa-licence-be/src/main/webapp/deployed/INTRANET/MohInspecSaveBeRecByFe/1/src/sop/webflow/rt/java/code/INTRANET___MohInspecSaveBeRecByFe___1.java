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

public class INTRANET___MohInspecSaveBeRecByFe___1 extends BaseProcessClass {

	private static final long serialVersionUID = -4665642663035421671L;
	private static final String DELEGATOR ="inspecSaveBeRecByFeDelegator";

	public void inspecSaveBeRecByFeStart_OnStepProcess_0() throws Exception {
	// 		InspecSaveBeRecByFeStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspecSaveBeRecByFeStart", this);
	}

	public void inspecSaveBeRecByFePre_OnStepProcess_0() throws Exception {
	// 		InspecSaveBeRecByFePre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspecSaveBeRecByFePre", this);
	}

}
