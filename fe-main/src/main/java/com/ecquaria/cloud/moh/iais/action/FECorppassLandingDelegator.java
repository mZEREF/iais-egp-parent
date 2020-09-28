package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.LoginHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ncs.secureconnect.sim.common.LoginInfo;
import com.ncs.secureconnect.sim.lite.SIMUtil4Corpass;
import lombok.extern.slf4j.Slf4j;
import ncs.secureconnect.sim.entities.Constants;
import ncs.secureconnect.sim.entities.corpass.UserInfoToken;
import org.springframework.beans.factory.annotation.Autowired;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Delegator(value = "corpassLandingDelegator")
@Slf4j
public class FECorppassLandingDelegator {

    public static final String IS_DECLARE = "isDeclare";
    public static final String IS_KEY_APPOINTMENT = "isKeyAppointment";

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
     * StartStep: redirectToInbox
     *
     * @param bpc
     * @throws
     */
    public void redirectToInbox(BaseProcessClass bpc){
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, LoginHelper.INBOX_URL);
    }

    /**
     * StartStep: croppassCallBack
     *
     * @param bpc
     * @throws
     */
    public void corppassCallBack(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
        log.info("corppassCallBack===========>>>Start");
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, null);

        AuditTrailHelper.auditFunction("FE Landing CorpPass", "login");

        String uen;
        String identityNo;
        String scp = null;

        boolean isTestMode = LoginHelper.isTestMode(request);
        if (isTestMode){
            uen = ParamUtil.getRequestString(request, UserConstants.ENTITY_ID);
            identityNo =  ParamUtil.getRequestString(request, UserConstants.CORPPASS_ID);
            scp =  ParamUtil.getRequestString(request, UserConstants.LOGIN_SCP);
        }else {
            String samlArt = ParamUtil.getString(request, Constants.SAML_ART);
            LoginInfo oLoginInfo = SIMUtil4Corpass.doCorpPassArtifactResolution(request, samlArt);

            if (oLoginInfo == null) {
                log.info("<== oLoginInfo is empty ==>");
                return;
            }

            UserInfoToken userInfoToken = oLoginInfo.getUserInfo();

            if (userInfoToken == null) {
                log.info("<== userInfoToken is empty ==>");
                return;
            }

            uen = userInfoToken.getEntityId();
            identityNo  = userInfoToken.getUserIdentity();
        }

        if (StringUtil.isEmpty(identityNo)){
            log.info(StringUtil.changeForLog("identityNo ====>>>>>>>>>" + identityNo));
            LoginHelper.insertLoginFailureAuditTrail(uen, identityNo);
            return;
        }

        String idType = IaisEGPHelper.checkIdentityNoType(identityNo);
        FeUserDto userDto = new FeUserDto();
        userDto.setUenNo(uen);
        userDto.setIdentityNo(identityNo);
        userDto.setIdType(idType);
        userDto.setScp(scp);

        ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, "N");
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userDto);
        Optional<OrganizationDto> optional = Optional.ofNullable(orgUserManageService.findOrganizationByUen(uen));
        if (optional.isPresent()) {
            ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "Y");
        }else {
            ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
        }
        log.info("corppassCallBack===========>>>End");
    }

    public void validatePwd(BaseProcessClass bpc){
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));
        boolean isTestMode = LoginHelper.isTestMode(bpc.request);
        if (isTestMode) {
            boolean scpCorrect = orgUserManageService.validatePwd(feUserDto);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "The account or password is incorrect");
                ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, "Y");
                LoginHelper.insertLoginFailureAuditTrail(feUserDto.getUenNo(), feUserDto.getIdentityNo());
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
        HttpServletResponse response = bpc.response;
        String uen = ParamUtil.getRequestString(request, UserConstants.ENTITY_ID);
        String nric =  ParamUtil.getRequestString(request, UserConstants.CORPPASS_ID);

        // a key appointment holder
        //boolean isKeyAppointment = orgUserManageService.isKeyappointment(uen, nric);
//        if (isKeyAppointment){
//            ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "Y");
//        }else {
//            ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "N");
//        }
        ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "N");
    }


    /**
     * StartStep: loginUser
     *
     * @param bpc
     * @throws
     */
    public void loginUser(BaseProcessClass bpc){
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        String uen = feUserDto.getUenNo();
        String identityNo =  feUserDto.getIdentityNo();
        String scp = feUserDto.getScp();

        log.info("corppassCallBack=====loginUser======>>>Start");

        feUserDto =  orgUserManageService.getUserByNricAndUen(uen, identityNo);
        if (feUserDto != null){
            feUserDto.setScp(scp);
            feUserDto.setUenNo(uen);
            ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, feUserDto);
            ParamUtil.setRequestAttr(bpc.request, "isAdminRole", "Y");
            User user = new User();
            user.setDisplayName(feUserDto.getDisplayName());
            user.setUserDomain(feUserDto.getUserDomain());
            user.setId(feUserDto.getUserId());
            user.setIdentityNo(feUserDto.getIdentityNo());
            LoginHelper.initUserInfo(bpc.request, bpc.response, user, AuditTrailConsts.LOGIN_TYPE_CORP_PASS);
        }else {
            // Add Audit Trail -- Start
            LoginHelper.insertLoginFailureAuditTrail(uen, identityNo);
            // End Audit Trail -- End
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", MessageUtil.getMessageDesc("GENERAL_ERR0012"));
            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_ADMIN, "N");
        }
        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));
        log.info("corppassCallBack=====loginUser======>>>End");
    }

    /**
     * StartStep: initCorppassUserInfo
     *
     * @param bpc
     * @throws
     */
    public void initCorppassUserInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;

        log.info("initCorppassUserInfo===========>>>Start");
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
            ParamUtil.setRequestAttr(request, UserConstants.IS_NEED_VALIDATE_FIELD, IaisEGPConstant.NO);
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

                FeUserDto postUpdate = orgUserManageService.createCorpPassUser(organizationDto);

                User user = new User();
                user.setDisplayName(postUpdate.getDisplayName());
                user.setUserDomain(postUpdate.getUserDomain());
                user.setId(postUpdate.getUserId());
                LoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_CORP_PASS);

                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }

        log.info(StringUtil.changeForLog("======>> fe user json" + JsonUtil.parseToJson(feUserDto)));
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
}
