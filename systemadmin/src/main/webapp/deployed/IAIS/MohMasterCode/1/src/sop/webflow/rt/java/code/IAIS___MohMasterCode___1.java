package sop.webflow.rt.java.code;

import sg.gov.moh.iais.controller.MasterCodeController;
import sop.webflow.rt.api.BaseProcessClass;
import com.ecquaria.egp.core.forms.util.FormRuntimeUtil;
import com.ecquaria.egp.core.forms.util.entity.FormButton;

public class IAIS___MohMasterCode___1 extends BaseProcessClass {

	public void init_OnStepProcess_0() throws Exception {
		MasterCodeController.getMasterCodeList(this);
	}
}