package sop.webflow.rt.java.code;
import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.cloud.helper.EngineHelper;
public class INTRANET___MohRiskConigMenu___1 extends BaseProcessClass {
	
	private static final String DELEGATOR ="hcsaRiskConfigDelegator";
	public void initConfig_OnStepProcess_0() throws Exception {
	// 		initConfig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "init", this);
	}

	public void start_OnStepProcess_0() throws Exception {
	// 		Start->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "start", this);
	}

	public void preConfig_OnStepProcess_0() throws Exception {
	// 		preConfig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "prepare", this);
	}

	public void next_OnStepProcess_0() throws Exception {
	// 		next->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "donext", this);
	}

	public void preGolbalRiskConig_OnStepProcess_0() throws Exception {
	// 		preGolbalRiskConig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preGolbalRiskConig", this);
	}

	public void preIndividualConfig_OnStepProcess_0() throws Exception {
	// 		PreIndividualConfig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preIndividualRiskConig", this);
	}

	public void preFinancialRiskConfig_OnStepProcess_0() throws Exception {
	// 		PreFinancialRiskConfig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preFinancialRiskConig", this);
	}

	public void preLeaderShipRiskConig_OnStepProcess_0() throws Exception {
	// 		PreLeaderShipRiskConig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preLeadershipRiskConig", this);
	}

	public void preLegislativeRiskConifg_OnStepProcess_0() throws Exception {
	// 		PreLegislativeRiskConifg->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preLegislativeRiskConig", this);
	}

	public void preWeightageRiskConifg_OnStepProcess_0() throws Exception {
	// 		PreWeightageRiskConifg->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preWeightageRiskConig", this);
	}

	public void licTenureRiskConfig_OnStepProcess_0() throws Exception {
	// 		LicTenureRiskConfig->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "preTenureRiskConig", this);
	}

}
