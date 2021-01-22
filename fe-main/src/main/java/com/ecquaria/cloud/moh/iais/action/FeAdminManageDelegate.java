package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.impl.OrgUserManageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    @Autowired
    private MyInfoAjax myInfoAjax;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug("****doStart Process ****");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT, AuditTrailConsts.FUNCTION_USER_MANAGEMENT);
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
            Map<String, FeUserQueryDto> feMap = IaisCommonUtils.genNewHashMap();
            for (FeUserQueryDto item:feAdminQueryDtoSearchResult.getRows()
            ) {
                if(feMap.get(item.getId()) != null){
                    if(RoleConsts.USER_ROLE_ORG_ADMIN.equals(feMap.get(item.getId()).getRole()) && RoleConsts.USER_ROLE_ORG_ADMIN.equals(item.getRole())){
                        feMap.get(item.getId()).setRole(RoleConsts.USER_ROLE_ORG_ADMIN);
                    }
                }else{
                    feMap.put(item.getId(),item);
                }
            }
            List<FeUserQueryDto> feUserQueryDtoList = IaisCommonUtils.genNewArrayList();
            for (Map.Entry<String,FeUserQueryDto> entry:feMap.entrySet()
                 ) {
                feUserQueryDtoList.add(entry.getValue());
            }
            for (FeUserQueryDto item:feUserQueryDtoList
            ) {
                item.setSalutation(MasterCodeUtil.getCodeDesc(item.getSalutation()));
                item.setIdType(MasterCodeUtil.getCodeDesc(item.getIdType()));
                item.setDesignation(MasterCodeUtil.getCodeDesc(item.getDesignation()));
                if(AppConsts.COMMON_STATUS_ACTIVE.equals(item.getIsActive())){
                    item.setIsActive("1");
                }else{
                    item.setIsActive("0");
                }
            }
            CrudHelper.doPaging(searchParam,bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "feAdmin",feUserQueryDtoList);
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
        ParamUtil.setSessionAttr(bpc.request,"inter_user_attr", accountDto);
        ParamUtil.setSessionAttr(bpc.request,"canEditFlag", "Y");
        ParamUtil.setSessionAttr(bpc.request,"title", "Create");
    }

    public void edit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************edit"));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
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
        ParamUtil.setSessionAttr(bpc.request,"inter_user_attr",feUserDto);
        ParamUtil.setSessionAttr(bpc.request,"isAdmin",isAdmin);
        ParamUtil.setSessionAttr(bpc.request,"canEditFlag", "Y");
        ParamUtil.setSessionAttr(bpc.request,"title", "Edit");
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

        }
        else if("save".equalsIgnoreCase(action)){

            log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
            String name = ParamUtil.getString(bpc.request,"name");
            String salutation = ParamUtil.getString(bpc.request,"salutation");
            String idType = ParamUtil.getString(bpc.request,"idType");
            String idNo = StringUtil.toUpperCase(ParamUtil.getString(bpc.request,"idNo"));
            String designation = ParamUtil.getString(bpc.request,"designation");
            String mobileNo = ParamUtil.getString(bpc.request,"mobileNo");
            String officeNo = ParamUtil.getString(bpc.request,"officeNo");
            String email = ParamUtil.getString(bpc.request,"email");
            String active = ParamUtil.getString(bpc.request,"active");
            String role = ParamUtil.getString(bpc.request,"role");

            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_NEED_VALIDATE_FIELD, IaisEGPConstant.YES);
            FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, "inter_user_attr");
            if(feUserDto.isCorpPass()){
                feUserDto.setUserId(feUserDto.getUenNo() + "_" + idNo);
            }else{
                feUserDto.setUserId(idNo);
            }
            feUserDto.setIdType(idType);
            feUserDto.setIdentityNo(idNo);
            feUserDto.setDisplayName(name);
            feUserDto.setSalutation(salutation);
            feUserDto.setDesignation(designation);
            feUserDto.setMobileNo(mobileNo);
            feUserDto.setOfficeTelNo(officeNo);
            feUserDto.setEmail(email);
            feUserDto.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            feUserDto.setAvailable(Boolean.TRUE);

            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)) {
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
            }else{
                feUserDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                feUserDto.setUserRole(RoleConsts.USER_ROLE_ORG_USER);
            }

            ParamUtil.setSessionAttr(bpc.request, "inter_user_attr", feUserDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "edit");

            if (validationResult.isHasErrors()){
                log.debug("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "back");
            }else{
                AuditTrailDto att = IaisEGPHelper.getCurrentAuditTrailDto();
                att.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);
                AuditTrailHelper.callSaveAuditTrail(att);

                Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                successMap.put("save","suceess");
                orgUserManageService.saveMyinfoDataByFeUserDtoAndLicenseeDto(null,feUserDto,null,false);
                if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "success");
                }else{
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "inbox");
                }
            }
        }else {
            repalceFeUserDtoByMyinfo(bpc.request);
        }
    }

    private FeUserDto repalceFeUserDtoByMyinfo( HttpServletRequest request){
        log.info("---- relaod feuesr start --------- ");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(request, "inter_user_attr");
        if(!StringUtil.isEmpty(loginContext.getNricNum()) && loginContext.getNricNum().equalsIgnoreCase(feUserDto.getIdNumber())){
            MyInfoDto myInfoDto = myInfoAjax.getMyInfo(loginContext.getNricNum());
            if(myInfoDto != null){
                if(!myInfoDto.isServiceDown()){
                    feUserDto.setEmail(myInfoDto.getEmail());
                    feUserDto.setMobileNo(myInfoDto.getMobileNo());
                    feUserDto.setDisplayName(myInfoDto.getUserName());
                    ParamUtil.setSessionAttr(request, "inter_user_attr", feUserDto);
                }else {
                    ParamUtil.setRequestAttr(request,"myinfoServiceDown", "Y");
                }
            }
        }else {
            log.info("------- Illegal operation get Myinfo ---------");
        }
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE, "back");
        log.info("---- relaod feuesr end --------- ");
        return feUserDto;
    }

}
