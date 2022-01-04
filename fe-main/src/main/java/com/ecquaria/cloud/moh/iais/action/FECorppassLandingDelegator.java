package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.OidcCpAuthResponDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FeLoginHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.ecquaria.cloudfeign.FeignException;
import com.ncs.secureconnect.sim.common.LoginInfo;
import com.ncs.secureconnect.sim.lite.SIMUtil4Corpass;
import ecq.commons.exception.BaseException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ncs.secureconnect.sim.entities.Constants;
import ncs.secureconnect.sim.entities.corpass.UserInfoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sop.webflow.process5.ProcessCacheHelper;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator(value = "corpassLandingDelegator")
@Slf4j
public class FECorppassLandingDelegator {

    public static final String IS_DECLARE = "isDeclare";
    public static final String IS_KEY_APPOINTMENT = "isKeyAppointment";

    @Value("${moh.halp.login.test.mode}")
    private String openTestMode;

    @Autowired
    private OrgUserManageService orgUserManageService;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
    }

    /**
     * StartStep: croppassCallBack
     *
     * @param bpc
     * @throws
     */
    public void corppassCallBack(BaseProcessClass bpc){
        //Check user login status
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

            return;
        }

        HttpServletRequest request = bpc.request;
        log.info(StringUtil.changeForLog("Corppass Login service [corppassCallBack] START ...."));
        String ssoLoginFlag = (String) request.getAttribute("ssoLoginFlag");
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SESSION_ENTRANCE, AuditTrailConsts.LOGIN_TYPE_CORP_PASS);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, null);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION,
                AuditTrailConsts.FUNCTION_SINGPASS_CORPASS);

        String uen = null;
        String identityNo = null;
        String scp = null;

        if (AppConsts.YES.equals(ssoLoginFlag)) {
            identityNo = (String) request.getAttribute("ssoNric");
            uen = (String) request.getAttribute("ssoUen");
        } else if (FELandingDelegator.LOGIN_MODE_REAL.equals(openTestMode)) {
            String samlArt = ParamUtil.getString(request, Constants.SAML_ART);
            LoginInfo loginInfo = null;
            try {
                loginInfo = SIMUtil4Corpass.doCorpPassArtifactResolution(request, samlArt);
            } catch (Exception e) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "Invalid Login.");
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
                return;
            }

            if (loginInfo == null) {
                log.info("<== oLoginInfo is empty ==>");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "Invalid Login.");
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
                return;
            }

            log.debug(StringUtil.changeForLog("oLoginInfo" + JsonUtil.parseToJson(loginInfo)));

            UserInfoToken userInfoToken = loginInfo.getUserInfo();

            if (userInfoToken == null) {
                log.info("<== userInfoToken is empty ==>");
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "Invalid Login.");
                return;
            }

            log.debug(StringUtil.changeForLog("userInfoToken" + JsonUtil.parseToJson(userInfoToken)));

            uen = userInfoToken.getEntityId();
            identityNo  = userInfoToken.getUserIdentity();
        } else if (FELandingDelegator.LOGIN_MODE_REAL_OIDC.equals(openTestMode)) {
            String userInfoMsg = request.getParameter("userToken");
            String eicCorrelationId = request.getParameter("ecquaria_correlationId");

            //check the state against with the session attribute
            String token = ConfigHelper.getString("corppass.oidc.token");
            String postUrl = ConfigHelper.getString("corppass.oidc.postUrl");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("ecquaria-correlationId", eicCorrelationId);
            headers.set("ecquaria-authToken", token);

            HttpEntity entity = new HttpEntity(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OidcCpAuthResponDto> respon = restTemplate.exchange(postUrl + userInfoMsg, HttpMethod.GET, entity, OidcCpAuthResponDto.class);
            if (HttpStatus.OK == respon.getStatusCode()) {
                OidcCpAuthResponDto oiRepon = respon.getBody();
                if (oiRepon != null && oiRepon.getUserInfo() != null) {
                    identityNo = oiRepon.getUserInfo().getNricFin();
                    uen = oiRepon.getUserInfo().getCpUid();
                }
            }
        } else {
            uen = ParamUtil.getRequestString(request, UserConstants.ENTITY_ID);
            identityNo =  ParamUtil.getRequestString(request, UserConstants.CORPPASS_ID);
            scp =  ParamUtil.getRequestString(request, UserConstants.LOGIN_SCP);
        }

        if (StringUtil.isEmpty(identityNo)){
            log.info(StringUtil.changeForLog("identityNo ====>>>>>>>>>" + identityNo));
            if (!StringUtil.isEmpty(identityNo) && !StringUtil.isEmpty(uen)) {
                AuditTrailHelper.insertLoginFailureAuditTrail(bpc.request, uen, identityNo);
            }
            ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "Invalid Login.");
            return;
        }

        String identityNoUpper = identityNo.toUpperCase();
        String idType = IaisEGPHelper.checkIdentityNoType(identityNoUpper);
        FeUserDto userSession = new FeUserDto();
        userSession.setUenNo(uen);
        userSession.setIdentityNo(identityNoUpper);
        userSession.setIdType(idType);
        userSession.setScp(scp);

        ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, "N");
        Optional<OrganizationDto> optional = Optional.ofNullable(orgUserManageService.findOrganizationByUen(uen));
        if (optional.isPresent()) {
            OrganizationDto orgn = optional.get();
            userSession.setOrgId(orgn.getId());
            //If no account under uen, register according to the current nric
            boolean isNotExistUser = orgUserManageService.isNotExistUserAccount(orgn.getId());
            if (isNotExistUser){
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
            }else {
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "Y");
            }
        }else {
            ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
        }

        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        log.info(StringUtil.changeForLog("Corppass Login service [corppassCallBack] END ...."));
    }

    public void validatePwd(BaseProcessClass bpc){
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        log.info("=======>validatePwd>>>>>>>>>{}", openTestMode);
        if (FELandingDelegator.LOGIN_MODE_DUMMY_WITHPASS.equals(openTestMode)) {
            boolean scpCorrect = orgUserManageService.validatePwd(userSession);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "The account is incorrect");
                ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, "Y");
                AuditTrailHelper.insertLoginFailureAuditTrail(bpc.request, userSession.getUenNo(), userSession.getIdentityNo(), "The account is incorrect");
                return;
            }
        }
        ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, "N");
    }


    /**
     * StartStep: validateKeyAppointment
     *
     * @param bpc
     * @throws
     */
    public void validateKeyAppointment(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        boolean isKeyPerson = userSession.isKeyAppointment();
        if (isKeyPerson){
            ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        }
        ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "Y");
    }

    /**
     * StartStep: loginUser
     *
     * Init login info
     *
     * @param bpc
     * @throws
     */
    public void loginUser(BaseProcessClass bpc) throws FeignException, BaseException {
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        String uen = userSession.getUenNo();
        String identityNo =  userSession.getIdentityNo();
        String scp = userSession.getScp();
        userSession =  orgUserManageService.getUserByNricAndUen(uen, identityNo);
        //get active flag and active role flag
        String userAndRoleFlag = orgUserManageService.getActiveUserAndRoleFlag(userSession);
        if (AppConsts.TRUE.equals(userAndRoleFlag) && Optional.ofNullable(userSession).isPresent()){
            userSession.setScp(scp);
            userSession.setUenNo(uen);
            ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, userSession);
            //normal user also can login  (2020/12)
            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_ADMIN, "Y");
            FeLoginHelper.initUserInfo(bpc.request, userSession);
            //issue 68766
            orgUserManageService.setSingPassAutoCeased(uen, identityNo);
        }else {
            // Add Audit Trail -- Start
            AuditTrailHelper.insertLoginFailureAuditTrail(bpc.request, uen, identityNo, "GENERAL_ERR0012");
            // End Audit Trail -- End
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", MessageUtil.getMessageDesc("GENERAL_ERR0012"));
            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_ADMIN, "N");
        }
    }

    /**
     * StartStep: initCorppassUserInfo
     *
     * Create Corppass User
     *
     * @param bpc
     * @throws
     */
    public void initCorppassUserInfo(BaseProcessClass bpc) throws FeignException, BaseException {
        HttpServletRequest request = bpc.request;
        log.info("initCorppassUserInfo===========>>>Start");
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        if (Optional.ofNullable(userSession).isPresent()){
            FeLoginHelper.writeUserField(request, userSession);
            ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
            ParamUtil.setRequestAttr(request, UserConstants.IS_NEED_VALIDATE_FIELD, IaisEGPConstant.NO);
            ValidationResult validationResult = WebValidationHelper.validateProperty(userSession, "create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            } else {
                OrganizationDto orgn = new OrganizationDto();
                orgn.setId(userSession.getOrgId());
                orgn.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                orgn.setOrgType(UserConstants.ORG_TYPE);
                orgn.setUenNo(userSession.getUenNo());
                orgn.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                orgn.setFeUserDto(userSession);

                FeUserDto createdUser = orgUserManageService.createCorpPassUser(orgn);
                //create egp user
                orgUserManageService.createClientUser(createdUser);
                FeLoginHelper.initUserInfo(request, createdUser);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }

        log.info("initCorppassUserInfo===========>>>End");
    }

    /**
     * StartStep: isDeclare
     *
     * @param bpc
     * @throws
     */
    public void isDeclare(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String[] declarationCheckbox = ParamUtil.getStrings(request, "declarationCheckbox");
        if (declarationCheckbox != null && declarationCheckbox.length > 0){
            String val = declarationCheckbox[0];
            if ("Y".equals(val)){
                FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
                userSession.setDeclareKeyPerson(true);
                ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
                ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_DECLARE, "Y");
            }else {
                ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_DECLARE, "N");
            }
        }
    }


    /**
     * StartStep: ban
     *
     * @param bpc
     * @throws
     */
    public void ban(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    /**
     * StartStep: receiveEntityFormEDH
     *
     * @param bpc
     * @throws
     */
    public void receiveEntityFormEDH(BaseProcessClass bpc){
        log.info("receiveEntityFormEDH Start...........");
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        orgUserManageService.receiveEntityFormEDH(userSession);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        log.info("receiveEntityFormEDH END...........");
    }
}
