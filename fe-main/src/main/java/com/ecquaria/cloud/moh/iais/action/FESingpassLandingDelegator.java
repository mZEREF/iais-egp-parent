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
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.OidcSpAuthResponDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FeLoginHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.validation.SoloEditValidator;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.ecquaria.cloudfeign.FeignException;
import com.ncs.secureconnect.sim.common.LoginInfo;
import com.ncs.secureconnect.sim.lite.SIMUtil;
import ecq.commons.exception.BaseException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ncs.secureconnect.sim.entities.Constants;
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

@Delegator(value = "singpassLandingDelegator")
@Slf4j
public class FESingpassLandingDelegator {
    @Autowired
    private OrgUserManageService orgUserManageService;

    @Value("${moh.halp.login.test.mode}")
    private String openTestMode;

    @Autowired
    private MyInfoAjax myInfoAjax;

    @Autowired
    private SoloEditValidator soloEditValidator;

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
        }

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
            String userInfoMsg = request.getParameter("userToken");
            String eicCorrelationId = request.getParameter("ecquaria_correlationId");

            //check the state against with the session attribute
            String token = ConfigHelper.getString("singpass.oidc.token");
            String postUrl = ConfigHelper.getString("singpass.oidc.postUrl");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("ecquaria-correlationId", eicCorrelationId);
            headers.set("ecquaria-authToken", token);

            HttpEntity entity = new HttpEntity(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OidcSpAuthResponDto> respon = restTemplate.exchange(postUrl + userInfoMsg, HttpMethod.GET, entity, OidcSpAuthResponDto.class);
            if (HttpStatus.OK == respon.getStatusCode()) {
                OidcSpAuthResponDto oiRepon = respon.getBody();
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
        //get active flag and active role flag
        String userAndRoleFlag = orgUserManageService.getActiveUserAndRoleFlag(userSession);
        if(AppConsts.FALSE.equals(userAndRoleFlag)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG , "The account is incorrect");
            ParamUtil.setRequestAttr(request, UserConstants.SCP_ERROR, IaisEGPConstant.YES);
            AuditTrailHelper.insertLoginFailureAuditTrail(request, userSession.getIdentityNo(), "The account is incorrect");
            return;
        }
        if (FELandingDelegator.LOGIN_MODE_DUMMY_WITHPASS.equals(openTestMode)){
            boolean scpCorrect = orgUserManageService.validatePwd(userSession);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG , "The account is incorrect");
                ParamUtil.setRequestAttr(request, UserConstants.SCP_ERROR, IaisEGPConstant.YES);
                AuditTrailHelper.insertLoginFailureAuditTrail(request, userSession.getIdentityNo(), "The account is incorrect");
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
        boolean loginFlag = orgUserManageService.validateSingpassAccount(identityNo, idType);
        if (loginFlag){
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
        FeUserDto feUserDto = orgUserManageService.getFeUserAccountByNricAndType(identityNo, idType, null);
        //Unavailable accounts have been filtered
        if (feUserDto != null){
            if(AppConsts.COMMON_STATUS_ACTIVE.equals(feUserDto.getStatus())) {
                userSession = feUserDto;
                userSession.setScp(scp);
                ParamUtil.setRequestAttr(request, UserConstants.IS_FIRST_LOGIN, IaisEGPConstant.NO);
            } else {
                ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request, UserConstants.IS_FIRST_LOGIN, IaisEGPConstant.YES);
            }
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
        if(MyinfoUtil.CLEAR_MYINFO_ACTION.equalsIgnoreCase(ParamUtil.getRequestString(request,"refreshMyInfoData"))){
            log.info(StringUtil.changeForLog("-------CLEAR MYINO ACTCION -------"));
            clearInfo(request);
            return;
        }
        if( !reLoadMyInfoData(request)){
            FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
            if (Optional.ofNullable(userSession).isPresent()){
                setRequestDto(request,userSession);
                Map<String,String> errorMsg = soloEditValidator.validate(request);
                if ( !errorMsg.isEmpty()) {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMsg));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                } else {
                    LicenseeDto liceInfo = (LicenseeDto) ParamUtil.getSessionAttr(request, MyinfoUtil.SOLO_DTO_SEESION);
                    OrganizationDto orgn = new OrganizationDto();
                    orgn.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                    orgn.setOrgType(UserConstants.ORG_TYPE);
                    orgn.setUenNo(userSession.getUenNo());
                    orgn.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    orgn.setFeUserDto(userSession);
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
    private void clearInfo(HttpServletRequest request){
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        LicenseeDto licenseeDto = (LicenseeDto) ParamUtil.getSessionAttr(request, MyinfoUtil.SOLO_DTO_SEESION);
        userSession.setMobileNo(null);
        userSession.setEmail(null);
        userSession.setDisplayName(null);
        if(licenseeDto == null){
            licenseeDto = new LicenseeDto();
        }else {
            licenseeDto.setPostalCode(null);
            licenseeDto.setAddrType(null);
            licenseeDto.setBlkNo(null);
            licenseeDto.setFloorNo(null);
            licenseeDto.setUnitNo(null);
            licenseeDto.setBuildingName(null);
            licenseeDto.setStreetName(null);
            licenseeDto.setMobileNo(null);
            licenseeDto.setEmilAddr(null);
            licenseeDto.setName(null);
        }
        userSession.setFromMyInfo(0);
        ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA, AppConsts.NO);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        ParamUtil.setRequestAttr(request, MyinfoUtil.SINGPASS_LOGIN, IaisEGPConstant.YES);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,userSession);
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
    }

    private void setRequestDto(HttpServletRequest request,FeUserDto userSession){
        LicenseeDto licenseeDto =  (LicenseeDto) ParamUtil.getSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION);
        if(licenseeDto == null){
            licenseeDto = new LicenseeDto();
        }
        licenseeDto.setAddrType(ParamUtil.getString(request,"addrType"));
        userSession.setMobileNo(ParamUtil.getString(request,"telephoneNo"));
        userSession.setEmail(ParamUtil.getString(request,"emailAddr"));
        licenseeDto.setMobileNo(userSession.getMobileNo());
        licenseeDto.setEmilAddr(userSession.getEmail());
        if(StringUtil.isEmpty(userSession.getDesignation())){
            userSession.setDesignation(MyinfoUtil.NO_GET_NAME_SHOW_NAME);
            userSession.setDesignationOther(MyinfoUtil.NO_GET_NAME_SHOW_NAME);
            userSession.setSalutation(MyinfoUtil.NO_GET_NAME_SHOW_NAME);
            userSession.setIdType(IaisEGPHelper.checkIdentityNoType(userSession.getIdentityNo()));
        }
        if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getString(request,"loadMyInfoData"))){
            userSession.setFromMyInfo(1);
            ParamUtil.setRequestAttr(request,MyinfoUtil.IS_LOAD_MYINFO_DATA,AppConsts.YES);
            ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
            ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,userSession);
            return;
        }else {
            userSession.setFromMyInfo(0);
        }
        userSession.setDisplayName(ParamUtil.getString(request,"name"));
        licenseeDto.setPostalCode(ParamUtil.getString(request,"postalCode"));
        licenseeDto.setBlkNo(ParamUtil.getString(request,"blkNo"));
        licenseeDto.setFloorNo(ParamUtil.getString(request,"floorNo"));
        licenseeDto.setUnitNo(ParamUtil.getString(request,"unitNo"));
        licenseeDto.setBuildingName(ParamUtil.getString(request,"buildingName"));
        licenseeDto.setStreetName(ParamUtil.getString(request,"streetName"));
        licenseeDto.setName(userSession.getDisplayName());
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,userSession);
    }

    public boolean reLoadMyInfoData( HttpServletRequest request){
        log.info(StringUtil.changeForLog("------------------reLoadMyInfoData start -----------"));
        ParamUtil.setRequestAttr(request, MyinfoUtil.SINGPASS_LOGIN, IaisEGPConstant.YES);
        if(AppConsts.YES .equalsIgnoreCase( (String) ParamUtil.getSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK)) || AppConsts.YES.equalsIgnoreCase(ParamUtil.getRequestString(request,"refreshMyInfoData"))){
            FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
            String identityNo = userSession.getIdentityNo();
            Optional<MyInfoDto> infoOpt = Optional.ofNullable(AppConsts.YES.equalsIgnoreCase( (String) ParamUtil.getSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK)) ? myInfoAjax.getMyInfoData(request) :myInfoAjax.getMyInfo(identityNo,request));
            if (infoOpt.isPresent()){
                MyInfoDto myInfo = infoOpt.get();
                if(!myInfo.isServiceDown()){
                    log.info(StringUtil.changeForLog("SingPass Login service [receiveUserInfo] MyInfo ...." + JsonUtil.parseToJson(myInfo)));
                    userSession.setFromMyInfo(1);
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
                    liceInfo.setMobileNo(myInfo.getMobileNo());
                    liceInfo.setEmilAddr(myInfo.getEmail());
                    liceInfo.setAddrType(myInfo.getAddrType());
                    String addrType = ParamUtil.getString(request,"addrType");
                    if(StringUtil.isNotEmpty(addrType)){
                        liceInfo.setAddrType(addrType);
                    }
                    ParamUtil.setSessionAttr(request, MyinfoUtil.SOLO_DTO_SEESION, liceInfo);
                    ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA, AppConsts.YES);
                    ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,userSession);
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
