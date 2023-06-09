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

public class INTERNET___MohInspecUserRecFeToBe___1 extends BaseProcessClass {

	private static final long serialVersionUID = 9160935777710992743L;
	private static final String DELEGATOR ="inspecUserRecFeToBeDelegator";

	public void inspecUserRecFeToBeStatrt_OnStepProcess_0() throws Exception {
	// 		InspecUserRecFeToBeStatrt->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspecUserRecFeToBeStatrt", this);
	}

	public void inspecUserRecFeToBePre_OnStepProcess_0() throws Exception {
	// 		InspecUserRecFeToBePre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "inspecUserRecFeToBePre", this);
	}
}
