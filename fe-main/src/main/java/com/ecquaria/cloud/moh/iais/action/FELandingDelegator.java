package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FeLoginHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.ncs.secureconnect.sim.lite.SIMUtil;
import com.ncs.secureconnect.sim.lite.SIMUtil4Corpass;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.process5.ProcessCacheHelper;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author: yichen
 * @date time:2/28/2020 2:07 PM
 * @description:
 */

@Delegator(value = "feLandingDelegator")
@Slf4j
public class FELandingDelegator {
	public static final String LOGIN_MODE_REAL 					= "Prod";
	public static final String LOGIN_MODE_DUMMY_NOPASS 			= "Dummy.NoPass";
	public static final String LOGIN_MODE_DUMMY_WITHPASS 	    = "Dummy.WithPass";


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
		ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SESSION_ENTRANCE, null);
		LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
		String sessionId = UserSessionUtil.getLoginSessionID(bpc.request.getSession());
		UserSession us = ProcessCacheHelper.getUserSessionFromCache(sessionId);
		if (us != null && lc != null && "Active".equals(us.getStatus())
				&& AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(lc.getUserDomain())){
			StringBuilder url = new StringBuilder();
			url.append("https://").append(bpc.request.getServerName())
					.append("/main-web/eservice/INTERNET/MohInternetInbox");
			IaisEGPHelper.sendRedirect(bpc.request, bpc.response, url.toString());
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


	/**
	 * StartStep: singpassSendRedirect
	 *
	 * @param bpc
	 * @throws
	 */
	public void singpassSendRedirect(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		HttpServletResponse response = bpc.response;
		try {
			SIMUtil.doSingPassLogin(request, response);
		}catch (Exception e){
			log.error("Error initializing Login ");
			log.error(e.getMessage(), e);
			IaisEGPHelper.sendRedirect(bpc.request, bpc.response, FeLoginHelper.MAIN_WEB_URL);
		}
	}

	/**
	 * StartStep: corppassSendRedirect
	 *
	 * @param bpc
	 * @throws
	 */
	public void corppassSendRedirect(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		HttpServletResponse response = bpc.response;
		try {
			SIMUtil4Corpass.doCorpPassLogin(request, response);
		} catch (Exception e) {
			log.error("Error initializing Login ");
			log.error(e.getMessage(), e);
			IaisEGPHelper.sendRedirect(bpc.request, bpc.response, FeLoginHelper.MAIN_WEB_URL);
		}
	}

	/**
	 * Process: FE_Landing
	 * Step: InitSso
	 * @param bpc
	 */
	public void initSso(BaseProcessClass bpc) {
		log.info(StringUtil.changeForLog("-------Init SSO-------"));
	}

	/**
	 * Process: FE_Landing
	 * Step: PrepareSsoData
	 * @param bpc
	 */
	public void prepareSsoData(BaseProcessClass bpc) {
		log.info(StringUtil.changeForLog("-------Prepare SSO Data-------"));

		bpc.request.setAttribute("nextUri", FeLoginHelper.INBOX_URL);
	}

	/**
	 * Process: FE_Landing
	 * Step: SendRedirect
	 * @param bpc
	 */
	public void sendRedirect(BaseProcessClass bpc) throws IOException {
		String url = (String) bpc.request.getAttribute("nextUrl");
		if (StringUtil.isEmpty(url)) {
			String uri = (String) bpc.request.getAttribute("nextUri");
			if (!StringUtil.isEmpty(uri)) {
				url = uri;
			} else {
				String currentProcessName = bpc.runtime.getCurrentProcessName();
				uri = bpc.request.getRequestURI();
				uri = uri.substring(0, uri.indexOf(currentProcessName));
				String processName = (String) bpc.request.getAttribute("processName");
				if (StringUtil.isEmpty(processName)) {
					processName = currentProcessName;
				}
				url = uri + processName;
			}
		}
		String baseUrl = InboxConst.URL_HTTPS + bpc.request.getServerName();
		if (url != null && !url.toLowerCase(AppConsts.DFT_LOCALE).startsWith("http")) {
			url = baseUrl + url;
		}
		log.info(StringUtil.changeForLog("The next URL: " + url));
		String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
		bpc.response.sendRedirect(tokenUrl);
	}
}
