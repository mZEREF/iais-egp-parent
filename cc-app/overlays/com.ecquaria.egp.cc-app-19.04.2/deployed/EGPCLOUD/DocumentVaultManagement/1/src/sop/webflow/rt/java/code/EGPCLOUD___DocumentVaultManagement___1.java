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


public class EGPCLOUD___DocumentVaultManagement___1 extends BaseProcessClass {

	private static final long serialVersionUID = 4715976103220995887L;

	public void prepareData_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("documentVaultDelegator", "prepareData", request);
	}

	public void search_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("documentVaultDelegator", "search", request);
	}

	public void step1_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("documentVaultDelegator", "startProcess", request);
	}

	public void paging_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("documentVaultDelegator", "changePage", request);
	}

	public void sorting_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("documentVaultDelegator", "sorting", request);
	}

	public void mainSearch_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("documentVaultDelegator", "mainSearch", request);
	}
	
}
