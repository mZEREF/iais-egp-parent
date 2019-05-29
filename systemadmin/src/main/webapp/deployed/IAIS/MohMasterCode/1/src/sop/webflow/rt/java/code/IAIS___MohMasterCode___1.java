package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class IAIS___MohMasterCode___1 extends BaseProcessClass {

	public void init_OnStepProcess_0() throws Exception {
		EngineHelper.delegate("masterCodeController", "getMasterCodeList", this);
	}
}