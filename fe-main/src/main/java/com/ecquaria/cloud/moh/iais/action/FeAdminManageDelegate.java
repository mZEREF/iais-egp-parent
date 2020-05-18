package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.impl.OrgUserManageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Map;

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
    private OrgUserManageServiceImpl orgUserManageService;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug("****doStart Process ****");

        AuditTrailHelper.auditFunction("Front End Admin", "User Management");
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
        if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
            String organizationId = loginContext.getOrgId();
            SearchParam searchParam = new SearchParam(FeUserQueryDto.class.getName());
            searchParam.setSort("ID", SearchParam.ASCENDING);
            searchParam.addFilter("orgid",organizationId,true);
            QueryHelp.setMainSql("interInboxQuery", "feUserList",searchParam);
            SearchResult<FeUserQueryDto> feAdminQueryDtoSearchResult = orgUserManageService.getFeUserList(searchParam);
            for (FeUserQueryDto item:feAdminQueryDtoSearchResult.getRows()
            ) {
                item.setSalutation(MasterCodeUtil.getCodeDesc(item.getSalutation()));
                item.setIdType(MasterCodeUtil.getCodeDesc(item.getIdType()));
                item.setDesignation(MasterCodeUtil.getCodeDesc(item.getDesignation()));
            }
            CrudHelper.doPaging(searchParam,bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "feAdmin",feAdminQueryDtoSearchResult);
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

        ParamUtil.setSessionAttr(bpc.request,"inter_user_attr", accountDto);
        ParamUtil.setSessionAttr(bpc.request,"canEditFlag", "Y");
    }

    public void edit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************edit"));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId ;
        String isAdmin ;
        if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
            userId = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
            isAdmin = "1";
        }else{
            userId = loginContext.getUserId();
            isAdmin = "0";
        }
        FeUserDto feUserDto = orgUserManageService.getUserAccount(userId);
        ParamUtil.setSessionAttr(bpc.request,"inter_user_attr",feUserDto);
        ParamUtil.setSessionAttr(bpc.request,"isAdmin",isAdmin);
        ParamUtil.setSessionAttr(bpc.request,"canEditFlag", "N");
    }


    /**
     * AutoStep: InsertDatabase
     *
     * @param bpc
     * @throws
     */
    public void editValidation(BaseProcessClass bpc) {
        String action = ParamUtil.getString(bpc.request,"action");
        if("cancel".equals(action)){
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "success");
            }else{
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "inbox");
            }

        }else{

            log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
            String name = ParamUtil.getString(bpc.request,"name");
            String salutation = ParamUtil.getString(bpc.request,"salutation");
            String idType = ParamUtil.getString(bpc.request,"idType");
            String idNo = ParamUtil.getString(bpc.request,"idNo");
            String designation = ParamUtil.getString(bpc.request,"designation");
            String mobileNo = ParamUtil.getString(bpc.request,"mobileNo");
            String officeNo = ParamUtil.getString(bpc.request,"officeNo");
            String email = ParamUtil.getString(bpc.request,"email");
            String active = ParamUtil.getString(bpc.request,"active");
            String role = ParamUtil.getString(bpc.request,"role");

            FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, "inter_user_attr");
            if(feUserDto.getIdType() == null){
                feUserDto.setIdType(idType);
            }
            if(feUserDto.getUserId() == null){
                if(feUserDto.getUenNo() != null){
                    feUserDto.setUserId(feUserDto.getUenNo() + "_" + idNo);
                }else{
                    feUserDto.setUserId(idNo);
                }
            }
            if(feUserDto.getIdentityNo() == null){
                feUserDto.setIdentityNo(idNo);
            }
            feUserDto.setDisplayName(name);
            feUserDto.setSalutation(salutation);
            feUserDto.setDesignation(designation);
            feUserDto.setMobileNo(mobileNo);
            feUserDto.setOfficeTelNo(officeNo);
            feUserDto.setEmail(email);
            feUserDto.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            feUserDto.setAvailable(Boolean.TRUE);
            if("active".equals(active)){
                feUserDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            }else{
                feUserDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            }
            if("admin".equals(role)){
                feUserDto.setUserRole(RoleConsts.USER_ROLE_ORG_ADMIN);
            }else{
                feUserDto.setUserRole(RoleConsts.USER_ROLE_ORG_USER);
            }
            ParamUtil.setSessionAttr(bpc.request, "inter_user_attr", feUserDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "edit");

            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));

                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "back");
            }else{
                Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                successMap.put("save","suceess");
                orgUserManageService.editUserAccount(feUserDto);
                orgUserManageService.updateEgpUser(feUserDto);
                //update be user
                OrganizationDto organizationById = orgUserManageService.getOrganizationById(feUserDto.getOrgId());
                OrganizationDto organizationDto = new OrganizationDto();
                organizationDto.setDoMain(feUserDto.getUserDomain());
                organizationDto.setFeUserDto(feUserDto);
                organizationDto.setOrgType(organizationById.getOrgType());
                organizationDto.setStatus(organizationById.getStatus());
                organizationDto.setUenNo(organizationById.getUenNo());
                organizationDto.setId(organizationById.getId());
                String json = JsonUtil.parseToJson(organizationDto);
                orgUserManageService.updateUserBe(organizationDto);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "success");
            }
        }
    }


}
