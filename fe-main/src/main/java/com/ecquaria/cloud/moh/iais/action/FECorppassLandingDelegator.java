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
import com.ecquaria.cloud.moh.iais.helper.FeLoginHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
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
        IaisEGPHelper.sendRedirect(bpc.request, bpc.response, FeLoginHelper.INBOX_URL);
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

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION,
                AuditTrailConsts.FUNCTION_SINGPASS_CORPASS);

        String uen;
        String identityNo;
        String scp = null;

        String testMode = FeLoginHelper.getTestMode(request);
        if (FELandingDelegator.LOGIN_MODE_REAL.equals(testMode)) {
            String samlArt = ParamUtil.getString(request, Constants.SAML_ART);
            LoginInfo oLoginInfo = SIMUtil4Corpass.doCorpPassArtifactResolution(request, samlArt);

            if (oLoginInfo == null) {
                log.info("<== oLoginInfo is empty ==>");
                return;
            }

            log.debug(StringUtil.changeForLog("oLoginInfo" + JsonUtil.parseToJson(oLoginInfo)));

            UserInfoToken userInfoToken = oLoginInfo.getUserInfo();

            if (userInfoToken == null) {
                log.info("<== userInfoToken is empty ==>");
                return;
            }

            log.debug(StringUtil.changeForLog("userInfoToken" + JsonUtil.parseToJson(userInfoToken)));

            uen = userInfoToken.getEntityId();
            identityNo  = userInfoToken.getUserIdentity();
        } else {
            uen = ParamUtil.getRequestString(request, UserConstants.ENTITY_ID);
            identityNo =  ParamUtil.getRequestString(request, UserConstants.CORPPASS_ID);
            scp =  ParamUtil.getRequestString(request, UserConstants.LOGIN_SCP);
        }

        if (StringUtil.isEmpty(identityNo)){
            log.info(StringUtil.changeForLog("identityNo ====>>>>>>>>>" + identityNo));
            AuditTrailHelper.insertLoginFailureAuditTrail(bpc.request, uen, identityNo);
            return;
        }

        String idType = IaisEGPHelper.checkIdentityNoType(identityNo);
        FeUserDto userSession = new FeUserDto();
        userSession.setUenNo(uen);
        userSession.setIdentityNo(identityNo);
        userSession.setIdType(idType);
        userSession.setScp(scp);

        ParamUtil.setSessionAttr(request, UserConstants.SESSION_CAN_EDIT_USERINFO, "N");

        Optional<OrganizationDto> optional = Optional.ofNullable(orgUserManageService.findOrganizationByUen(uen));
        if (optional.isPresent()) {
            OrganizationDto organization = optional.get();
            userSession.setOrgId(organization.getId());
            //If no account under uen, register according to the current nric
            boolean isNotExistUser = orgUserManageService.isNotExistUserAccount(organization.getId());
            if (isNotExistUser){
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
            }else {
                ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "Y");
            }

        }else {
            ParamUtil.setRequestAttr(request, UserConstants.ACCOUNT_EXISTS_VALIDATE_FLAG, "N");
        }

        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, userSession);
        log.info("corppassCallBack===========>>>End");
    }

    public void validatePwd(BaseProcessClass bpc){
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        String testMode = FeLoginHelper.getTestMode(bpc.request);
        if (FELandingDelegator.LOGIN_MODE_DUMMY_WITHPASS.equals(testMode)) {
            boolean scpCorrect = orgUserManageService.validatePwd(userSession);
            if (!scpCorrect) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG , "The account or password is incorrect");
                ParamUtil.setRequestAttr(bpc.request, UserConstants.SCP_ERROR, "Y");
                AuditTrailHelper.insertLoginFailureAuditTrail(bpc.request, userSession.getUenNo(), userSession.getIdentityNo());
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
        ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "N");
    }

    /**
     * StartStep: loginUser
     *
     * @param bpc
     * @throws
     */
    public void loginUser(BaseProcessClass bpc){
        FeUserDto userSession = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
        String uen = userSession.getUenNo();
        String identityNo =  userSession.getIdentityNo();
        String scp = userSession.getScp();

        log.info("corppassCallBack=====loginUser======>>>Start");

        userSession =  orgUserManageService.getUserByNricAndUen(uen, identityNo);
        if (userSession != null){
            userSession.setScp(scp);
            userSession.setUenNo(uen);
            ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, userSession);

            //normal user also can login  (2020/12)
            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_ADMIN, "Y");
            User user = new User();
            user.setDisplayName(userSession.getDisplayName());
            user.setUserDomain(userSession.getUserDomain());
            user.setId(userSession.getUserId());
            user.setIdentityNo(userSession.getIdentityNo());
            FeLoginHelper.initUserInfo(bpc.request, bpc.response, user, AuditTrailConsts.LOGIN_TYPE_CORP_PASS);
        }else {
            // Add Audit Trail -- Start
            AuditTrailHelper.insertLoginFailureAuditTrail(bpc.request, uen, identityNo);
            // End Audit Trail -- End
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", MessageUtil.getMessageDesc("GENERAL_ERR0012"));
            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_ADMIN, "N");
        }
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
        //String idType = ParamUtil.getString(request, UserConstants.ID_TYPE);
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
            ParamUtil.setRequestAttr(request, UserConstants.IS_NEED_VALIDATE_FIELD, IaisEGPConstant.NO);
            ValidationResult validationResult = WebValidationHelper.validateProperty(userSession, "create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            } else {
                OrganizationDto organizationDto = new OrganizationDto();
                organizationDto.setId(userSession.getOrgId());
                organizationDto.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                organizationDto.setOrgType(UserConstants.ORG_TYPE);
                organizationDto.setUenNo(userSession.getUenNo());
                organizationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

                organizationDto.setFeUserDto(userSession);

                FeUserDto postUpdate = orgUserManageService.createCorpPassUser(organizationDto);

                User user = new User();
                user.setDisplayName(postUpdate.getDisplayName());
                user.setUserDomain(postUpdate.getUserDomain());
                user.setId(postUpdate.getUserId());
                FeLoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_CORP_PASS);

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
