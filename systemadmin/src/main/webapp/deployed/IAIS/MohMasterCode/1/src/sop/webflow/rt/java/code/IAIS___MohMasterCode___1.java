package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.exception.IaisRuntimeException;
import sop.webflow.rt.api.BaseProcessClass;

public class IAIS___MohMasterCode___1 extends BaseProcessClass {

	public void init_OnStepProcess_0() {
		throw new IaisRuntimeException(ON_PREPARE_DATA);
		EngineHelper.delegate("masterCodeController", "getMasterCodeList", this);
	}
}