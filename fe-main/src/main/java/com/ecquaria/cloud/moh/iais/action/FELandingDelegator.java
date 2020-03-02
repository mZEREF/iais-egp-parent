package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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

	/**
	 * StartStep: croppassLogin
	 *
	 * @param bpc
	 * @throws
	 */
	public void croppassLogin(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;


		String entityId = ParamUtil.getString(request, "entityId");
		String corpPassId = ParamUtil.getString(request, "corpPassId");
		String password = ParamUtil.getString(request, "password");

		log.info("entityId" + entityId);
		log.info("corpPassId" + corpPassId);
		log.info("password" + password);



	}

	/**
	 * StartStep: singpassLogin
	 *
	 * @param bpc
	 * @throws
	 */
	public void singpassLogin(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		String entityId = ParamUtil.getString(request, "entityId");
		String password = ParamUtil.getString(request, "password");

		boolean reuslt = orgUserManageService.validateSingpassId(entityId, password);
		if (!reuslt){

			return;
		}

		IaisApiResult<List<String>> apiResult = orgUserManageService.singPassLoginFe(entityId);
		if (apiResult.isHasError()){
			int errorCode = apiResult.getErrorCode();
			if (IaisApiStatusCode.FIND_UEN_BY_SINGPASS_ID.getStatusCode() == errorCode){
				List<String> uenList = apiResult.getEntity();
				String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl("https://egp.sit.inter.iais.com" + EngineHelper.getContextPath()
						 + "/eservice/INTERNET/FE_Landing/1/croppass", bpc.request);
				try {
					ParamUtil.setSessionAttr(request, "uenList", (Serializable) uenList);
					bpc.response.sendRedirect(tokenUrl);
				} catch (IOException e) {
					log.error(e.getMessage());
				}

			}

		}


		/*StringBuilder url = new StringBuilder();
		url.append("https://").append(bpc.request.getServerName())
				.append("/hcsa-licence-web/eservice/INTERNET/MohLicenceView");
		String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
		try {

			bpc.response.sendRedirect(tokenUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}*/

	}

}
