package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FeAdminManageDelegate
 *
 * @author guyin
 * @date 2019/10/18 15:07
 */
@Delegator("feAdminManageDelegate")
@Slf4j
public class FeAdminManageDelegate {
    @Autowired
    private OrgUserManageService orgUserManageService;
    @Autowired
    private MyInfoAjax myInfoAjax;

    public static final String FE_NO_ADMIN_ROLES_SHOW                 = "feNoAdminRolesShow";
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug("****doStart Process ****");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT, AuditTrailConsts.FUNCTION_USER_MANAGEMENT);
        ParamUtil.clearSession(bpc.request,IaisEGPConstant.SESSION_NAME_ROLES,FE_NO_ADMIN_ROLES_SHOW);
        myInfoAjax.setVerifyTakenAndAuthoriseApiUrl(bpc.request,"MohFeAdminUserManagement/Edit");
        ParamUtil.setSessionAttr(bpc.request,"AllServicesForHcsaRole",(Serializable) HcsaServiceCacheHelper.getAllServiceSelectOptions());
    }

    /**
     * AutoStep: preparePage
     *
     * @param bpc
     * @throws
     */
    public void preparePage(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ParamUtil.setSessionAttr(bpc.request, MyinfoUtil.MYINFODTO_REFRESH + loginContext.getNricNum(), null);
        if (loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)) {
            String organizationId = loginContext.getOrgId();
            SearchParam searchParam = new SearchParam(FeUserQueryDto.class.getName());
            searchParam.setSort("ID", SearchParam.ASCENDING);
            searchParam.addFilter("orgid",organizationId,true);
            QueryHelp.setMainSql("interInboxQuery", "feUserList",searchParam);
            SearchResult<FeUserQueryDto> feAdminQueryDtoSearchResult = orgUserManageService.getFeUserList(searchParam);
            feAdminQueryDtoSearchResult.getRows().stream().forEach(item -> item.setIsActive(AppConsts.COMMON_STATUS_ACTIVE.equals(item.getIsActive()) ? AppConsts.YES : AppConsts.NO));
            CrudHelper.doPaging(searchParam,bpc.request);
            ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SESSION_NAME_ROLES,
                    (Serializable) orgUserManageService.getRoleSelection(loginContext.getLicenseeId()));
            ParamUtil.setRequestAttr(bpc.request, "feAdmin",feAdminQueryDtoSearchResult.getRows());
            ParamUtil.setRequestAttr(bpc.request, "feAdminSearchParam",searchParam);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
        }else{
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.FALSE);
        }
    }

    public void create(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************create"));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String organizationId = loginContext.getOrgId();
        OrganizationDto organizationDto = orgUserManageService.getOrganizationById(organizationId);
        FeUserDto accountDto = new FeUserDto();
        accountDto.setUenNo(organizationDto.getUenNo());
        accountDto.setOrgId(organizationId);
        String isAdmin ;
        if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
            isAdmin = "1";
        }else{
            isAdmin = "0";
        }
        ParamUtil.setSessionAttr(bpc.request,"isAdmin",isAdmin);
        ParamUtil.setSessionAttr(bpc.request,UserConstants.SESSION_USER_DTO, accountDto);
        ParamUtil.setSessionAttr(bpc.request,"canEditFlag", "Y");
        ParamUtil.setSessionAttr(bpc.request,"title", "Create");
    }

    public void edit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************edit"));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(AppConsts.YES.equalsIgnoreCase( (String) ParamUtil.getSessionAttr(bpc.request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK))){
            repalceFeUserDtoByMyinfo(bpc.request);
            return;
        }
        String userId ;
        String isAdmin ;
        if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
            String userInputName = ParamUtil.getString(bpc.request,"userIndex");
            userId = ParamUtil.getMaskedString(bpc.request,userInputName);
            isAdmin = "1";
        }else{
            userId = loginContext.getUserId();
            isAdmin = "0";
        }
        FeUserDto feUserDto = orgUserManageService.getUserAccount(userId);
        // show role names
        if (IaisCommonUtils.isNotEmpty(feUserDto.getOrgUserRoleDtos())) {
            Map<String, String> roleMap = orgUserManageService.getFeRoleMap();
            String roleNames = feUserDto.getOrgUserRoleDtos().stream()
                    .map(role -> roleMap.get(role.getRoleName()))
                    .filter(Objects::nonNull)
                    .sorted(String::compareTo)
                    .collect(Collectors.joining("<br/>"));
            ParamUtil.setSessionAttr(bpc.request, FE_NO_ADMIN_ROLES_SHOW, roleNames);
        }
        ParamUtil.setRequestAttr(bpc.request,MyinfoUtil.IS_LOAD_MYINFO_DATA,String.valueOf(feUserDto.getFromMyInfo()));
        ParamUtil.setSessionAttr(bpc.request,UserConstants.SESSION_USER_DTO,feUserDto);
        ParamUtil.setSessionAttr(bpc.request,"isAdmin",isAdmin);
        ParamUtil.setSessionAttr(bpc.request,"canEditFlag", "N");
        ParamUtil.setSessionAttr(bpc.request,"title", "Edit");
    }


    /**
     * AutoStep: InsertDatabase
     *
     * @param bpc
     * @throws
     */
    public void editValidation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getString(request, "action");
        if ("cancel".equals(action)) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "success");
            } else {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "inbox");
            }

        } else if ("clearInfo".equalsIgnoreCase(action)) {
            clearInfo(request);
        } else if ("save".equalsIgnoreCase(action)) {
            log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
            String name = ParamUtil.getString(request, "name");
            String idNo = StringUtil.toUpperCase(ParamUtil.getString(request, "idNo"));
            String active = ParamUtil.getString(request, "active");
            //admin role
            String role = ParamUtil.getString(request, "role");
            String roles = ParamUtil.getStringsToString(request, "roles");
            String officeNo = ParamUtil.getString(bpc.request, "officeNo");
            ParamUtil.setRequestAttr(request, UserConstants.IS_NEED_VALIDATE_FIELD, IaisEGPConstant.YES);
            FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
            String id = feUserDto.getId();
            String oldRoles = feUserDto.getRoles();
            ControllerHelper.get(request, feUserDto);
            if (StringUtil.isEmpty(id)) {
                if (feUserDto.isCorpPass()) {
                    feUserDto.setUserId(feUserDto.getUenNo() + "_" + idNo);
                } else {
                    feUserDto.setUserId(idNo);
                }
                feUserDto.setIdType(IaisEGPHelper.checkIdentityNoType(idNo));
                feUserDto.setIdentityNo(idNo);
            }
            feUserDto.setId(id);
            feUserDto.setRoles(roles);
            feUserDto.setOfficeTelNo(officeNo);
            if (AppConsts.YES.equalsIgnoreCase(ParamUtil.getString(request, "loadMyInfoData"))) {
                ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA, AppConsts.YES);
            } else {
                feUserDto.setDisplayName(name);
            }
            feUserDto.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            feUserDto.setAvailable(Boolean.TRUE);

            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)) {
                if ("active".equals(active)) {
                    feUserDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                } else {
                    feUserDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                }
                feUserDto.setUserRole(role);
                feUserDto.setSelectServices(ParamUtil.getStringsToString(bpc.request, "service"));
            } else {
                feUserDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                feUserDto.setUserRole(RoleConsts.USER_ROLE_ORG_USER);
                feUserDto.setRoles(oldRoles);
            }

            ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, feUserDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "edit");

            if (validationResult.isHasErrors()) {
                log.debug("****************Error");
                Map<String, String> errorMap = validationResult.retrieveAll();
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap)));
                ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "back");
            } else {
                LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
                MyInfoDto myInfoDto = (MyInfoDto) ParamUtil.getSessionAttr(request,
                        MyinfoUtil.MYINFODTO_REFRESH + loginContext.getNricNum());
                boolean needRefersh = myInfoDto != null && !myInfoDto.isServiceDown();
                boolean licenseeHave = licenseeDto != null && licenseeDto.getLicenseeIndividualDto() != null;
                if (!needRefersh) {
                    myInfoDto = null;
                }
                AuditTrailDto att = IaisEGPHelper.getCurrentAuditTrailDto();
                att.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);
                AuditTrailHelper.callSaveAuditTrail(att);
                feUserDto.setAuditTrailDto(att);
                if (StringUtil.isEmpty(feUserDto.getSelectServices())) {
                    feUserDto.setSelectServices(AppServicesConsts.SERVICE_MATRIX_ALL);
                }
                orgUserManageService.saveMyinfoDataByFeUserDtoAndLicenseeDto(licenseeDto, feUserDto,
                        reSetMyInfoData(feUserDto, myInfoDto), false);
                if (licenseeHave) {
                    ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,
                            orgUserManageService.getFeUserAccountByNricAndType(licenseeDto.getLicenseeIndividualDto().getIdNo(),
                                    licenseeDto.getLicenseeIndividualDto().getIdType(), feUserDto.getUenNo()));
                }
                if (loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)) {
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "success");
                } else {
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "inbox");
                }
            }
        } else {
            repalceFeUserDtoByMyinfo(request);
        }
    }

    private MyInfoDto reSetMyInfoData(FeUserDto feUserDto,MyInfoDto myInfoDto){
        if(myInfoDto == null){
            return null;
        }
        MyInfoDto myInfoDtoReSet = MiscUtil.transferEntityDto(myInfoDto,MyInfoDto.class);
        if(feUserDto.getDisplayName().equalsIgnoreCase(myInfoDto.getUserName())){
            myInfoDtoReSet.setUserName(feUserDto.getDisplayName());
            myInfoDtoReSet.setEmail(feUserDto.getEmail());
            myInfoDtoReSet.setMobileNo(feUserDto.getMobileNo());
        }
        log.info(StringUtil.changeForLog("-------------reSetMyInfoData data : " + JsonUtil.parseToJson(myInfoDtoReSet) + "---------"));
        return myInfoDtoReSet;
    }

    public void  clearInfo(HttpServletRequest request){
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        feUserDto.setDisplayName(null);
        feUserDto.setSalutation(null);
        feUserDto.setDesignation(null);
        feUserDto.setDesignationOther(null);
        feUserDto.setMobileNo(null);
        feUserDto.setOfficeTelNo(null);
        feUserDto.setEmail(null);
        feUserDto.setFromMyInfo(0);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
            feUserDto.setSelectServices(null);
        }
        ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,feUserDto);
        ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA,AppConsts.NO);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE, "back");
    }
    private FeUserDto repalceFeUserDtoByMyinfo( HttpServletRequest request){
        log.info("---- relaod feuesr start --------- ");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        if(AppConsts.YES.equalsIgnoreCase( (String) ParamUtil.getSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK)) ||("getMyInfo".equalsIgnoreCase(ParamUtil.getString(request,"action")) && !StringUtil.isEmpty(loginContext.getNricNum()) && loginContext.getNricNum().equalsIgnoreCase(feUserDto.getIdNumber()))){
            MyInfoDto myInfoDto = AppConsts.YES.equalsIgnoreCase( (String) ParamUtil.getSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK)) ? myInfoAjax.getMyInfoData(request) : myInfoAjax.getMyInfo(loginContext.getNricNum(),request);
            if(myInfoDto != null){
                if(!myInfoDto.isServiceDown()){
                    feUserDto.setEmail(myInfoDto.getEmail());
                    feUserDto.setMobileNo(myInfoDto.getMobileNo());
                    feUserDto.setDisplayName(myInfoDto.getUserName());
                    feUserDto.setFromMyInfo(1);
                    ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA,AppConsts.YES);
                    ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO, feUserDto);
                    ParamUtil.setSessionAttr(request,MyinfoUtil.MYINFODTO_REFRESH +loginContext.getNricNum(),myInfoDto);
                }else {
                    ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
                }
            }else {
                ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
            }
        }else {
            log.info("------- Illegal operation get Myinfo ---------");
        }
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE, "back");
        log.info("---- relaod feuesr end --------- ");
        return feUserDto;
    }

}
