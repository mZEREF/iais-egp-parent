package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FeLoginHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ncs.secureconnect.sim.common.LoginInfo;
import com.ncs.secureconnect.sim.lite.SIMUtil;
import lombok.extern.slf4j.Slf4j;
import ncs.secureconnect.sim.entities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Delegator(value = "singpassLandingDelegator")
@Slf4j
public class FESingpassLandingDelegator {
    @Autowired
    private OrgUserManageService orgUserManageService;


    @Autowired
    private MyInfoAjax myInfoAjax;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SINGPASS_CORPASS);
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
        log.info("singpassCallBack===========>>>Start");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SINGPASS_CORPASS);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, null);
        String identityNo;
        String scp = null;

        String testMode = FeLoginHelper.getTestMode(request);
        if (FELandingDelegator.LOGIN_MODE_REAL.equals(testMode)) {
            String samlArt = ParamUtil.getString(request, Constants.SAML_ART);
            LoginInfo oLoginInfo = SIMUtil.doSingPassArtifactResolution(request, samlArt);
            if (oLoginInfo == null){
                return;
            }

            log.debug(StringUtil.changeForLog("oLoginInfo" + JsonUtil.parseToJson(oLoginInfo)));

            identityNo = oLoginInfo.getLoginID();
        } else {
            identityNo = ParamUtil.getString(request, UserConstants.ENTITY_ID);
            scp = ParamUtil.getString(request, UserConstants.LOGIN_SCP);
        }

        if (StringUtil.isEmpty(identityNo)){
            log.info(StringUtil.changeForLog("identityNo ====>>>>>>>>>" + identityNo));
            return;
        }

        String idType = IaisEGPHelper.checkIdentityNoType(identityNo);

        log.info(StringUtil.changeForLog("singpassCallBack nric " + identityNo));

        ParamUtil.setRequestAttr(request, UserConstants.ENTITY_ID, identityNo);
        ParamUtil.setRequestAttr(request, UserConstants.ID_TYPE, idType);
        ParamUtil.setRequestAttr(request, UserConstants.LOGIN_SCP, scp);
        log.info("singpassCallBack===========>>>End");
    }

    public void validatePwd(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);

        String testMode = FeLoginHelper.getTestMode(bpc.request);
        if (FELandingDelegator.LOGIN_MODE_DUMMY_WITHPASS.equals(testMode)){
            boolean scpCorrect = orgUserManageService.validatePwd(userSession);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "The account or password is incorrect");
                ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, IaisEGPConstant.YES);
                AuditTrailHelper.insertLoginFailureAuditTrail(request, userSession.getIdentityNo());
                return;
            }
        }
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, IaisEGPConstant.NO);
    }

    public void hasMohIssueUen(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String identityNo = ParamUtil.getRequestString(request, UserConstants.ENTITY_ID);
        String idType = ParamUtil.getRequestString(request, UserConstants.ID_TYPE);
        String scp = ParamUtil.getRequestString(request, UserConstants.LOGIN_SCP);
        FeUserDto userSession = new FeUserDto();
        userSession.setScp(scp);
        userSession.setIdentityNo(identityNo);
        userSession.setIdType(idType);

        IaisApiResult iaisApiResult = orgUserManageService.checkIssueUen(identityNo, idType);
        if (iaisApiResult.isHasError()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", MessageUtil.getMessageDesc("GENERAL_ERR0013"));
            ParamUtil.setRequestAttr(bpc.request, "hasMohIssueUen", IaisEGPConstant.YES);
            AuditTrailHelper.insertLoginFailureAuditTrail(request, identityNo);
        }else {
            ParamUtil.setRequestAttr(bpc.request, "hasMohIssueUen", IaisEGPConstant.NO);
        }

        ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, userSession);
    }

    /**
     * StartStep: receiveUserInfo
     *
     * @param bpc
     * @throws
     */
    public void receiveUserInfo(BaseProcessClass bpc){
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
            Optional<MyInfoDto> infoOpt = Optional.ofNullable(myInfoAjax.getMyInfo(identityNo));
            if (infoOpt.isPresent()){
                MyInfoDto myInfoDto = infoOpt.get();
                if(!myInfoDto.isServiceDown()){
                    userSession.setEmail(myInfoDto.getEmail());
                    userSession.setMobileNo(myInfoDto.getMobileNo());
                    userSession.setDisplayName(myInfoDto.getUserName());
                    LicenseeDto licenseeInfo = new LicenseeDto();
                    licenseeInfo.setFloorNo(myInfoDto.getFloor());
                    licenseeInfo.setPostalCode(myInfoDto.getPostalCode());
                    licenseeInfo.setUnitNo(myInfoDto.getUnitNo());
                    licenseeInfo.setBlkNo(myInfoDto.getBlockNo());
                    licenseeInfo.setBuildingName(myInfoDto.getBuildingName());
                    licenseeInfo.setStreetName(myInfoDto.getStreetName());
                    ParamUtil.setSessionAttr(request, UserConstants.SESSION_LICENSEE_INFO_ATTR, licenseeInfo);
                }else {
                    ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, "Y");
                }
            }

            ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request, UserConstants.IS_FIRST_LOGIN, IaisEGPConstant.YES);
        }

        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(userSession)));
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        log.info("receiveUserInfo===========>>>End");
    }

    /**
     * StartStep: initSingpassInfo
     *
     * @param bpc
     * @throws
     */
    public void initSingpassInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
        log.info("initSingpassInfo===========>>>Start");
        String name = ParamUtil.getString(request, UserConstants.NAME);
        String salutation = ParamUtil.getString(request, UserConstants.SALUTATION);
        String designation = ParamUtil.getString(request, UserConstants.DESIGNATION);
        String idNo = ParamUtil.getString(request, UserConstants.ID_NUMBER);
        String mobileNo = ParamUtil.getString(request, UserConstants.MOBILE_NO);
        String officeNo = ParamUtil.getString(request, UserConstants.OFFICE_NO);
        String email = ParamUtil.getString(request, UserConstants.EMAIL);

        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        if (userSession != null){
            userSession.setDisplayName(name);
            userSession.setDesignation(designation);
            userSession.setSalutation(salutation);
            userSession.setIdentityNo(idNo);
            userSession.setMobileNo(mobileNo);
            userSession.setOfficeTelNo(officeNo);
            userSession.setIdType(IaisEGPHelper.checkIdentityNoType(idNo));
            userSession.setEmail(email);
            ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
            ValidationResult validationResult = WebValidationHelper.validateProperty(userSession, "create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            } else {
                OrganizationDto organization = new OrganizationDto();
                organization.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                organization.setOrgType(UserConstants.ORG_TYPE);
                organization.setUenNo(userSession.getUenNo());
                organization.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                organization.setFeUserDto(userSession);
                LicenseeDto licenseeInfo = (LicenseeDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_LICENSEE_INFO_ATTR);
                organization.setLicenseeDto(licenseeInfo);

                FeUserDto createdUser = orgUserManageService.createSingpassAccount(organization);
                User user = new User();
                user.setDisplayName(createdUser.getDisplayName());
                user.setUserDomain(createdUser.getUserDomain());
                user.setId(createdUser.getUserId());
                FeLoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_SING_PASS);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }

            log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(userSession)));
        }

        log.info("initSingpassInfo===========>>>End");
    }


    /**
     * StartStep: redirectToInbox
     *
     * @param bpc
     * @throws
     */
    public void redirectToInbox(BaseProcessClass bpc){
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, FeLoginHelper.INBOX_URL);
    }

    public void initLoginInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);

        log.info("initLoginInfo===========>>>Start");
        //if lack of user information here, sop api (/api/v1/users/userdomain_and_userid/cs_hcsa/privilegenos) will throw exception
        if (userSession != null){
            User user = new User();
            user.setDisplayName(userSession.getDisplayName());
            user.setUserDomain(userSession.getUserDomain());
            user.setId(userSession.getUserId());
            user.setIdentityNo(userSession.getIdentityNo());
            FeLoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_SING_PASS);
        }
        log.info("initLoginInfo===========>>>End");
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
