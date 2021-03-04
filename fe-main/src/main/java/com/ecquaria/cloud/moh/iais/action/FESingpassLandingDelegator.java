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
import com.ecquaria.cloudfeign.FeignException;
import com.ncs.secureconnect.sim.common.LoginInfo;
import com.ncs.secureconnect.sim.lite.SIMUtil;
import ecq.commons.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import ncs.secureconnect.sim.entities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        log.info("singpassCallBack===========>>>Start");
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SESSION_ENTRANCE, AuditTrailConsts.LOGIN_TYPE_SING_PASS);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SINGPASS_CORPASS);
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, null);
        String identityNo;
        String scp = null;

        if (FELandingDelegator.LOGIN_MODE_REAL.equals(openTestMode)) {
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

        String identityNoUpper = identityNo.toUpperCase();
        String idType = IaisEGPHelper.checkIdentityNoType(identityNoUpper);

        log.debug(StringUtil.changeForLog("singpassCallBack nric " + identityNoUpper));
        ParamUtil.setRequestAttr(request, UserConstants.ENTITY_ID, identityNoUpper);
        ParamUtil.setRequestAttr(request, UserConstants.ID_TYPE, idType);
        ParamUtil.setRequestAttr(request, UserConstants.LOGIN_SCP, scp);
        log.info("singpassCallBack===========>>>End");
    }

    public void validatePwd(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);

        String testMode = FeLoginHelper.getTestMode(request);
        if (FELandingDelegator.LOGIN_MODE_DUMMY_WITHPASS.equals(testMode)){
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
    }

    public void hasMohIssueUen(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String identityNo = (String) ParamUtil.getRequestAttr(request, UserConstants.ENTITY_ID);
        String idType = (String) ParamUtil.getRequestAttr(request, UserConstants.ID_TYPE);
        String scp = (String) ParamUtil.getRequestAttr(request, UserConstants.LOGIN_SCP);
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
                    LicenseeDto liceInfo = new LicenseeDto();
                    liceInfo.setFloorNo(myInfoDto.getFloor());
                    liceInfo.setPostalCode(myInfoDto.getPostalCode());
                    liceInfo.setUnitNo(myInfoDto.getUnitNo());
                    liceInfo.setBlkNo(myInfoDto.getBlockNo());
                    liceInfo.setName(myInfoDto.getUserName());
                    liceInfo.setBuildingName(myInfoDto.getBuildingName());
                    liceInfo.setStreetName(myInfoDto.getStreetName());
                    ParamUtil.setSessionAttr(request, UserConstants.SESSION_LICENSEE_INFO_ATTR, liceInfo);
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
    public void initSingpassInfo(BaseProcessClass bpc) throws FeignException, BaseException {
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
                OrganizationDto orgn = new OrganizationDto();
                orgn.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                orgn.setOrgType(UserConstants.ORG_TYPE);
                orgn.setUenNo(userSession.getUenNo());
                orgn.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                orgn.setFeUserDto(userSession);
                LicenseeDto liceInfo = (LicenseeDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_LICENSEE_INFO_ATTR);
                orgn.setLicenseeDto(liceInfo);

                FeUserDto createdUser = orgUserManageService.createSingpassAccount(orgn);
                User user = new User();
                user.setDisplayName(createdUser.getDisplayName());
                user.setUserDomain(createdUser.getUserDomain());
                user.setId(createdUser.getUserId());

                //create egp user
                orgUserManageService.createClientUser(createdUser);
                FeLoginHelper.initUserInfo(request, user);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }

            log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(userSession)));
        }

        log.info("initSingpassInfo===========>>>End");
    }

    public void initLoginInfo(BaseProcessClass bpc) throws FeignException, BaseException {
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
            FeLoginHelper.initUserInfo(request, user);
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
