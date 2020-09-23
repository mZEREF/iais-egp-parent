package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
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
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.LoginHelper;
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
        AuditTrailHelper.auditFunction("FE Singpass Landing", "Login");
    }

    public void redirectToCorppass(BaseProcessClass bpc){
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, LoginHelper.MAIN_WEB_URL);
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
        AuditTrailHelper.auditFunction("FE Landing SingPass", "Login");
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, null);
        String identityNo;
        String scp = null;
        if (LoginHelper.isTestMode(request)){
            identityNo = ParamUtil.getString(request, UserConstants.ENTITY_ID);
            scp = ParamUtil.getString(request, UserConstants.LOGIN_PWD);
        }else {
            String samlArt = ParamUtil.getString(request, Constants.SAML_ART);
            LoginInfo oLoginInfo = SIMUtil.doSingPassArtifactResolution(request, samlArt);
            log.info(StringUtil.changeForLog("oLoginInfo" + oLoginInfo));
            if (oLoginInfo == null){
                return;
            }

            identityNo = oLoginInfo.getLoginID();
        }

        if (StringUtil.isEmpty(identityNo)){
            log.info(StringUtil.changeForLog("identityNo ====>>>>>>>>>" + identityNo));
            return;
        }

        String idType = IaisEGPHelper.checkIdentityNoType(identityNo);

        log.info(StringUtil.changeForLog("singpassCallBack nric " + identityNo));

        ParamUtil.setRequestAttr(request, UserConstants.ENTITY_ID, identityNo);
        ParamUtil.setRequestAttr(request, UserConstants.ID_TYPE, idType);
        ParamUtil.setRequestAttr(request, UserConstants.LOGIN_PWD, scp);
        log.info("singpassCallBack===========>>>End");
    }

    public void validatePwd(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);

        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));

        boolean isTestMode = LoginHelper.isTestMode(bpc.request);
        if (isTestMode){
            boolean scpCorrect = orgUserManageService.validatePwd(feUserDto);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "The account or password is incorrect");
                ParamUtil.setRequestAttr(bpc.request, UserConstants.PWD_ERROR, IaisEGPConstant.YES);
                LoginHelper.insertLoginFailureAuditTrail(feUserDto.getIdentityNo());
                return;
            }
        }
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, feUserDto);
        ParamUtil.setRequestAttr(bpc.request, UserConstants.PWD_ERROR, IaisEGPConstant.NO);
    }

    public void hasMohIssueUen(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String identityNo = ParamUtil.getRequestString(request, UserConstants.ENTITY_ID);
        String idType = ParamUtil.getRequestString(request, UserConstants.ID_TYPE);
        String scp = ParamUtil.getRequestString(request, UserConstants.LOGIN_PWD);
        FeUserDto feUserDto = new FeUserDto();
        feUserDto.setScp(scp);
        feUserDto.setIdentityNo(identityNo);
        feUserDto.setIdType(idType);

        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));

        IaisApiResult iaisApiResult = orgUserManageService.checkIssueUen(identityNo, idType);
        if (iaisApiResult.isHasError()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", MessageUtil.getMessageDesc("GENERAL_ERR0013"));
            ParamUtil.setRequestAttr(bpc.request, "hasMohIssueUen", IaisEGPConstant.YES);
            LoginHelper.insertLoginFailureAuditTrail(identityNo);
        }else {
            ParamUtil.setRequestAttr(bpc.request, "hasMohIssueUen", IaisEGPConstant.NO);
        }

        ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, feUserDto);
    }

    /**
     * StartStep: receiveUserInfo
     *
     * @param bpc
     * @throws
     */
    public void receiveUserInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        String identityNo = feUserDto.getIdentityNo();
        String idType = feUserDto.getIdType();
        String scp = feUserDto.getScp();
        Optional<FeUserDto> optional = Optional.ofNullable(orgUserManageService.getFeUserAccountByNricAndType(identityNo, idType));
        if (optional.isPresent()){
            feUserDto = optional.get();
            feUserDto.setScp(scp);
            ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXIST, IaisEGPConstant.NO);
        }else {
            Optional<MyInfoDto> infoOpt = Optional.ofNullable(myInfoAjax.getMyInfo(identityNo));
            if (infoOpt.isPresent()){
                MyInfoDto myInfoDto = infoOpt.get();
                feUserDto.setEmail(myInfoDto.getEmail());
                feUserDto.setMobileNo(myInfoDto.getMobileNo());
            }else {
                feUserDto.setIdentityNo(identityNo);
                feUserDto.setIdType(idType);
                ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXIST, IaisEGPConstant.YES);
            }
        }

        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, feUserDto);
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
        String idType = ParamUtil.getString(request, UserConstants.ID_TYPE);
        String mobileNo = ParamUtil.getString(request, UserConstants.MOBILE_NO);
        String officeNo = ParamUtil.getString(request, UserConstants.OFFICE_NO);
        String email = ParamUtil.getString(request, UserConstants.EMAIL);

        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        if (feUserDto != null){
            feUserDto.setDisplayName(name);
            feUserDto.setDesignation(designation);
            feUserDto.setSalutation(salutation);
            feUserDto.setIdentityNo(idNo);
            feUserDto.setMobileNo(mobileNo);
            feUserDto.setOfficeTelNo(officeNo);
            feUserDto.setIdType(idType);
            feUserDto.setEmail(email);
            ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, feUserDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            } else {
                OrganizationDto organizationDto = new OrganizationDto();
                organizationDto.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                organizationDto.setOrgType(UserConstants.ORG_TYPE);
                organizationDto.setUenNo(feUserDto.getUenNo());
                organizationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                feUserDto.setIdType(IaisEGPHelper.checkIdentityNoType(feUserDto.getIdentityNo()));
                organizationDto.setFeUserDto(feUserDto);

                OrganizationDto postCreateOrg = orgUserManageService.createSingpassAccount(organizationDto);
                FeUserDto postCreateUser = postCreateOrg.getFeUserDto();

                User user = new User();
                user.setDisplayName(postCreateUser.getDisplayName());
                user.setUserDomain(postCreateUser.getUserDomain());
                user.setId(postCreateUser.getUserId());
                LoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_SING_PASS);

                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }

            log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));
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
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, LoginHelper.INBOX_URL);
    }

    public void initLoginInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);

        log.info("initLoginInfo===========>>>Start");
        //if lack of user information here, sop api (/api/v1/users/userdomain_and_userid/cs_hcsa/privilegenos) will throw exception
        if (feUserDto != null){
            User user = new User();
            user.setDisplayName(feUserDto.getDisplayName());
            user.setUserDomain(feUserDto.getUserDomain());
            user.setId(feUserDto.getUserId());
            user.setIdentityNo(feUserDto.getIdentityNo());
            LoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_SING_PASS);
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
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, LoginHelper.MAIN_WEB_URL);
    }
}
