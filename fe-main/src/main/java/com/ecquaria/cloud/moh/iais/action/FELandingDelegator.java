package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author: yichen
 * @date time:2/28/2020 2:07 PM
 * @description:
 */

@Delegator(value = "feLandingDelegator")
@Slf4j
public class FELandingDelegator {

	@Autowired
	private OrgUserManageService orgUserManageService;

	/**
	 * StartStep: startStep
	 *
	 * @param bpc
	 * @throws
	 */
	public void startStep(BaseProcessClass bpc){
		AuditTrailHelper.auditFunction("FE Landing", "Login");
	}

	/**
	 * StartStep: preload
	 *
	 * @param bpc
	 * @throws
	 */
	public void preload(BaseProcessClass bpc){

	}

	/**
	 * StartStep: switchAction
	 *
	 * @param bpc
	 * @throws
	 */
	public void switchAction(BaseProcessClass bpc){
		ParamUtil.setSessionAttr(bpc.request, "uenList", null);
	}


}
