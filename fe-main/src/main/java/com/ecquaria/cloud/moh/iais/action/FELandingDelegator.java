package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @author: yichen
 * @date time:2/28/2020 2:07 PM
 * @description:
 */

@Delegator(value = "feLandingDelegator")
@Slf4j
public class FELandingDelegator {

	/**
	 * StartStep: startStep
	 *
	 * @param bpc
	 * @throws
	 */
	public void startStep(BaseProcessClass bpc){
	}

	/**
	 * StartStep: preload
	 *
	 * @param bpc
	 * @throws
	 */
	public void preload(BaseProcessClass bpc){
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
		if (loginContext != null && AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(loginContext.getUserDomain())){
			StringBuilder url = new StringBuilder();
			url.append("https://").append(bpc.request.getServerName())
					.append("/main-web/eservice/INTERNET/MohInternetInbox");
			IaisEGPHelper.sendRedirect(bpc.request, bpc.response, url.toString());
		}

		List<String> mohIssueUenList = (List<String>) ParamUtil.getSessionAttr(bpc.request, "uenList");
		ParamUtil.setSessionAttr(bpc.request, "uenList", null);
		if (!IaisCommonUtils.isEmpty(mohIssueUenList)){
			ParamUtil.setRequestAttr(bpc.request, "uenList", mohIssueUenList);
		}
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
