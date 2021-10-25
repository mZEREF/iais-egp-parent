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
import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;

public class INTRANET___MohInventoryManagement___1 extends BaseProcessClass {

	private static final String DELEGATOR ="inventoryDelegator";
	
	public void preBasicData_OnStepProcess_0() throws Exception {
		// 		PreBasicData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preBasicData", this);
		}

	public void prepareData_OnStepProcess_0() throws Exception {
	// 		prepareData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepareData", this);
	}

	public void preHistoryData_OnStepProcess_0() throws Exception {
	// 		PreHistoryData->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preHistoryData", this);
	}

	public void doAdjustment_OnStepProcess_0 () throws Exception {
		// 		doAdjustment->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doAdjustment", this);
		}

	public void doBasicAdjustment_OnStepProcess_0() throws Exception {
	// 		doBasicAdjustment->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "doBasicAdjustment", this);
	}

	public void preBasicList_OnStepProcess_0() throws Exception {
	// 		PreBasicList->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preBasicList", this);
	}

}
