package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtVerify;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
	public static final String LOGIN_MODE_REAL_OIDC 			= "Prod.OIDC";
	public static final String LOGIN_MODE_DUMMY_NOPASS 			= "Dummy.NoPass";
	public static final String LOGIN_MODE_DUMMY_WITHPASS 	    = "Dummy.WithPass";
	@Value("${jwt.interlogin.base64encodedpub}")
	private String base64encodedPub;

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
	public void initSso(BaseProcessClass bpc) throws InvalidKeySpecException, NoSuchAlgorithmException,
			ParseException {
		LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
		String sessionId = UserSessionUtil.getLoginSessionID(bpc.request.getSession());
		UserSession us = ProcessCacheHelper.getUserSessionFromCache(sessionId);
		if (us != null && lc != null && "Active".equals(us.getStatus())
				&& AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(lc.getUserDomain())){
			StringBuilder url = new StringBuilder();
			url.append("https://").append(bpc.request.getServerName())
					.append("/main-web/eservice/INTERNET/MohInternetInbox");
			IaisEGPHelper.sendRedirect(bpc.request, bpc.response, url.toString());

			return;
		}

		log.info(StringUtil.changeForLog("-------Init SSO-------"));
		HttpServletRequest request = bpc.request;
		JwtVerify verifier = new JwtVerify();
//		String jwtt = (String) request.getHeader("authToken");
		String jwtt = ParamUtil.getString(request, "authToken");
		Jws<Claims> claimsFromToken = verifier.parseVerifyJWT(jwtt, base64encodedPub + "\r\n");
		Claims claims = claimsFromToken.getBody();
		String uenId = (String) claims.get("uen");
		String nric = (String) claims.get("uid");
		String iat = (String) claims.get("iat");
		if (iat == null) {
			throw new IaisRuntimeException("Access Denied");
		}
		Date issueDate = Formatter.parseDateTime(iat, Formatter.DATE_ELIS);
		int seconds = ConfigHelper.getInt("jwt.interlogin.valid.seconds", 30);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 0 - seconds);
		long minTime = cal.getTimeInMillis();
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, seconds);
		long maxTime = cal.getTimeInMillis();
		if (issueDate.getTime() < minTime || issueDate.getTime() > maxTime) {
			throw new IaisRuntimeException("Access Denied");
		}
		if (!StringUtil.isEmpty(uenId)) {
			log.info(StringUtil.changeForLog("Uen Id ==> " + uenId));
			bpc.request.setAttribute("ssoLoginType", "corpass");
			bpc.request.setAttribute("ssoUen", uenId);
		} else {
			bpc.request.setAttribute("ssoLoginType", "singpass");
		}
		log.info(StringUtil.changeForLog("NRIC ==> " + nric));
		log.info(StringUtil.changeForLog("Issue date ==> " + iat));
		bpc.request.setAttribute("ssoNric", nric);
		bpc.request.setAttribute("ssoLoginFlag", AppConsts.YES);
	}

}
