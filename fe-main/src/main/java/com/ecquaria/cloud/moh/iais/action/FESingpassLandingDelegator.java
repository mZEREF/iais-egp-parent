package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.OidcAuthDto;
import com.ecquaria.cloud.moh.iais.dto.OidcAuthResponDto;
import com.ecquaria.cloud.moh.iais.dto.OidcAuthZErrorDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FeLoginHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloudfeign.FeignException;
import com.ncs.secureconnect.sim.common.LoginInfo;
import com.ncs.secureconnect.sim.lite.SIMUtil;
import ecq.commons.exception.BaseException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ncs.secureconnect.sim.entities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator(value = "singpassLandingDelegator")
@Slf4j
public class FESingpassLandingDelegator {
    @Autowired
    private OrgUserManageService orgUserManageService;

    @Value("${moh.halp.login.test.mode}")
    private String openTestMode;

    @Autowired
    private MyInfoAjax myInfoAjax;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
    }

    public void redirectToCorppass(BaseProcessClass bpc){
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, FeLoginHelper.MAIN_WEB_URL);
    }

    /**
     * StartStep: singpassCallBack
     *
     * @param bpc
     * @throws
     */
    public void singpassCallBack(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String ssoLoginFlag = (String) request.getAttribute("ssoLoginFlag");
        log.info(StringUtil.changeForLog("SingPass Login service [singpassCallBack] START ...."));
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SESSION_ENTRANCE, AuditTrailConsts.LOGIN_TYPE_SING_PASS);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SINGPASS_CORPASS);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, null);
        String identityNo = null;
        String scp = null;

        if (AppConsts.YES.equals(ssoLoginFlag)) {
            identityNo = (String) request.getAttribute("ssoNric");
        } else if (FELandingDelegator.LOGIN_MODE_REAL.equals(openTestMode)) {
            String samlArt = ParamUtil.getString(request, Constants.SAML_ART);
            LoginInfo oLoginInfo = SIMUtil.doSingPassArtifactResolution(request, samlArt);
            if (oLoginInfo == null){
                return;
            }

            log.info(StringUtil.changeForLog("oLoginInfo" + JsonUtil.parseToJson(oLoginInfo)));
            identityNo = oLoginInfo.getLoginID();
        } else if (FELandingDelegator.LOGIN_MODE_REAL_OIDC.equals(openTestMode)) {
            String code = ParamUtil.getString(request, "code");
            String state = ParamUtil.getString(request, "state");
            String error = ParamUtil.getString(request, "error");
            String errorDescription = ParamUtil.getString(request, "error_description");
            //check the state against with the session attribute
            String sessionState = (String) ParamUtil.getSessionAttr(request, "qrcode_state");
            String nonce = (String) ParamUtil.getSessionAttr(request, "qrcode_nonce");
            if (sessionState == null || !sessionState.equals(state)) {
                //validate fail, not continue
                log.info(StringUtil.changeForLog("session state[" + sessionState + "]"));
                log.info(StringUtil.changeForLog("request parameter state[" + state + "]"));
                return;
            }
            String token = ConfigHelper.getString("singpass.oidc.token");
            String postUrl = ConfigHelper.getString("singpass.oidc.postUrl");
            OidcAuthDto oad = new OidcAuthDto();
            oad.setCode(code);
            oad.setNonce(nonce);
            oad.setClientId(ConfigHelper.getString("singpass.oidc.clientId"));
            oad.setRedirectUri(ConfigHelper.getString("singpass.oidc.redirectUrl"));
            OidcAuthZErrorDto oAze = new OidcAuthZErrorDto();
            oAze.setError(error);
            oAze.setErrorDescription(errorDescription);
            oad.setAuthZError(oAze);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            header.setCacheControl(CacheControl.noCache());
            header.set("ecquaria-correlationId", UUID.randomUUID().toString());
            header.set("ecquaria-authToken", token);
            HttpEntity entity = new HttpEntity(oad, header);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OidcAuthResponDto> respon = restTemplate.exchange(postUrl, HttpMethod.POST, entity, OidcAuthResponDto.class);
            if (HttpStatus.OK == respon.getStatusCode()) {
                OidcAuthResponDto oiRepon = respon.getBody();
                if (oiRepon != null && oiRepon.getUserInfo() != null) {
                    identityNo = oiRepon.getUserInfo().getNricFin();
                }
            }
        } else {
            identityNo = ParamUtil.getString(request, UserConstants.ENTITY_ID);
            scp = ParamUtil.getString(request, UserConstants.LOGIN_SCP);
        }

        String identityNoUpper = "";
        if(identityNo != null) {
            identityNoUpper = identityNo.toUpperCase();
        }
        String idType = IaisEGPHelper.checkIdentityNoType(identityNoUpper);

        ParamUtil.setRequestAttr(request, UserConstants.ENTITY_ID, identityNoUpper);
        ParamUtil.setRequestAttr(request, UserConstants.ID_TYPE, idType);
        ParamUtil.setRequestAttr(request, UserConstants.LOGIN_SCP, scp);
        myInfoAjax.setVerifyTakenAndAuthoriseApiUrl(request,"FE_Singpass_Landing/receiveUserInfo",identityNoUpper);
        log.info(StringUtil.changeForLog("SingPass Login service [singpassCallBack] END ...." + "nric : " + identityNoUpper));
    }

    public void validatePwd(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("SingPass Login service [validatePwd] START ...."));
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        log.info("=======>validatePwd>>>>>>>>>{}", openTestMode);
        if (FELandingDelegator.LOGIN_MODE_DUMMY_WITHPASS.equals(openTestMode)){
            boolean scpCorrect = orgUserManageService.validatePwd(userSession);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG , "The account or password is incorrect");
                ParamUtil.setRequestAttr(request, UserConstants.SCP_ERROR, IaisEGPConstant.YES);
                AuditTrailHelper.insertLoginFailureAuditTrail(request, userSession.getIdentityNo(), "The account or password is incorrect");
                return;
            }
        }
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, IaisEGPConstant.NO);
        log.info(StringUtil.changeForLog("SingPass Login service [validatePwd] END ...."));
    }

    public void hasMohIssueUen(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("SingPass Login service [hasMohIssueUen] START ...."));
        HttpServletRequest request = bpc.request;
        String identityNo = (String) ParamUtil.getRequestAttr(request, UserConstants.ENTITY_ID);
        String idType = (String) ParamUtil.getRequestAttr(request, UserConstants.ID_TYPE);
        String scp = (String) ParamUtil.getRequestAttr(request, UserConstants.LOGIN_SCP);
        FeUserDto userSession = new FeUserDto();
        userSession.setScp(scp);
        userSession.setIdentityNo(identityNo);
        userSession.setIdType(idType);
        if (orgUserManageService.validateSingpassAccount(identityNo, idType)){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", MessageUtil.getMessageDesc("GENERAL_ERR0013"));
            ParamUtil.setRequestAttr(bpc.request, "hasMohIssueUen", IaisEGPConstant.YES);
            AuditTrailHelper.insertLoginFailureAuditTrail(request, identityNo);
        }else {
            ParamUtil.setRequestAttr(bpc.request, "hasMohIssueUen", IaisEGPConstant.NO);
        }

        ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, userSession);
        log.info(StringUtil.changeForLog("SingPass Login service [hasMohIssueUen] END ...."));
    }

    /**
     * StartStep: receiveUserInfo
     *
     * @param bpc
     * @throws
     */
    public void receiveUserInfo(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("SingPass Login service [receiveUserInfo] START ...."));
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        String identityNo = userSession.getIdentityNo();
        String idType = userSession.getIdType();
        String scp = userSession.getScp();
        Optional<FeUserDto> optional = Optional.ofNullable(orgUserManageService.getFeUserAccountByNricAndType(identityNo, idType));
        if (optional.isPresent()){
            userSession = optional.get();
            userSession.setScp(scp);
            ParamUtil.setRequestAttr(request, UserConstants.IS_FIRST_LOGIN, IaisEGPConstant.NO);
        }else {
            ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request, UserConstants.IS_FIRST_LOGIN, IaisEGPConstant.YES);
        }

        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(userSession)));
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        reLoadMyInfoData(request);
        log.info(StringUtil.changeForLog("SingPass Login service [receiveUserInfo] END ...."));
    }

    /**
     * StartStep: initSingpassInfo
     *
     * @param bpc
     * @throws
     */
    public void initSingpassInfo(BaseProcessClass bpc) throws FeignException, BaseException {
        log.info(StringUtil.changeForLog("SingPass Login service [initSingpassInfo] START ...."));
        HttpServletRequest request = bpc.request;
        if( !reLoadMyInfoData(request)){
            FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
            if (Optional.ofNullable(userSession).isPresent()){
                FeLoginHelper.writeUserField(request, userSession);
                ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
                ValidationResult validationResult = WebValidationHelper.validateProperty(userSession, "create");
                if (validationResult.isHasErrors()) {
                    Map<String, String> errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                } else {
                    OrganizationDto orgn = new OrganizationDto();
                    orgn.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                    orgn.setOrgType(UserConstants.ORG_TYPE);
                    orgn.setUenNo(userSession.getUenNo());
                    orgn.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    orgn.setFeUserDto(userSession);
                    LicenseeDto liceInfo = (LicenseeDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_LICENSEE_INFO_ATTR);
                    orgn.setLicenseeDto(liceInfo);

                    FeUserDto createdUser = orgUserManageService.createSingpassAccount(orgn);
                    //create egp user
                    orgUserManageService.createClientUser(createdUser);
                    FeLoginHelper.initUserInfo(request, createdUser);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                }

                log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(userSession)));
            }

            log.info(StringUtil.changeForLog("SingPass Login service [initSingpassInfo] END ...."));
        }
    }

    public boolean reLoadMyInfoData( HttpServletRequest request){
        log.info(StringUtil.changeForLog("------------------reLoadMyInfoData start -----------"));
        if(AppConsts.YES .equalsIgnoreCase( (String) ParamUtil.getSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK)) || AppConsts.YES.equalsIgnoreCase(ParamUtil.getRequestString(request,"refreshMyInfoData"))){
            FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
            String identityNo = userSession.getIdentityNo();
            Optional<MyInfoDto> infoOpt = Optional.ofNullable(AppConsts.YES.equalsIgnoreCase( (String) ParamUtil.getSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK)) ? myInfoAjax.getMyInfoData(request) :myInfoAjax.getMyInfo(identityNo,request));
            if (infoOpt.isPresent()){
                MyInfoDto myInfo = infoOpt.get();
                if(!myInfo.isServiceDown()){
                    log.info(StringUtil.changeForLog("SingPass Login service [receiveUserInfo] MyInfo ...." + JsonUtil.parseToJson(myInfo)));
                    userSession.setEmail(myInfo.getEmail());
                    userSession.setMobileNo(myInfo.getMobileNo());
                    userSession.setDisplayName(myInfo.getUserName());
                    LicenseeDto liceInfo = new LicenseeDto();
                    liceInfo.setFloorNo(myInfo.getFloor());
                    liceInfo.setPostalCode(myInfo.getPostalCode());
                    liceInfo.setUnitNo(myInfo.getUnitNo());
                    liceInfo.setBlkNo(myInfo.getBlockNo());
                    liceInfo.setName(myInfo.getUserName());
                    liceInfo.setBuildingName(myInfo.getBuildingName());
                    liceInfo.setStreetName(myInfo.getStreetName());
                    ParamUtil.setSessionAttr(request, UserConstants.SESSION_LICENSEE_INFO_ATTR, liceInfo);
                }else {
                    ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
                }
            }else {
                ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
            }

            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return true;
        }
        return false;
    }

    public void initLoginInfo(BaseProcessClass bpc) throws FeignException, BaseException {
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        log.info(StringUtil.changeForLog("SingPass Login service [initLoginInfo] START ...."));
        //if lack of user information here, sop api (/api/v1/users/userdomain_and_userid/cs_hcsa/privilegenos) will throw exception
        if (userSession != null){
            FeLoginHelper.initUserInfo(request, userSession);
        }
        log.info(StringUtil.changeForLog("SingPass Login service [initLoginInfo] END ...." + JsonUtil.parseToJson(userSession)));
    }

    /**
     * StartStep: step1
     *
     * @param bpc
     * @throws
     */
    public void step1(BaseProcessClass bpc){
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, FeLoginHelper.MAIN_WEB_URL);
    }
}
