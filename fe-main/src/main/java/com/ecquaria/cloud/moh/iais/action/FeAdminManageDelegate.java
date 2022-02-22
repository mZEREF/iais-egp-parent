package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
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
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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
    private OrgUserManageService orgUserManageService;
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
            Map<String, FeUserQueryDto> feMap = IaisCommonUtils.genNewHashMap();
            for (FeUserQueryDto item:feAdminQueryDtoSearchResult.getRows()) {
                if(feMap.get(item.getId()) != null){
                    if(RoleConsts.USER_ROLE_ORG_ADMIN.equals(feMap.get(item.getId()).getRole()) && RoleConsts.USER_ROLE_ORG_ADMIN.equals(item.getRole())){
                        feMap.get(item.getId()).setRole(RoleConsts.USER_ROLE_ORG_ADMIN);
                    }
                }else{
                    feMap.put(item.getId(),item);
                }
            }
            List<FeUserQueryDto> feUserQueryDtoList = IaisCommonUtils.genNewArrayList();
            for (Map.Entry<String,FeUserQueryDto> entry:feMap.entrySet()) {
                feUserQueryDtoList.add(entry.getValue());
            }
            feUserQueryDtoList.forEach(item -> {
                if (AppConsts.COMMON_STATUS_ACTIVE.equals(item.getIsActive())) {
                    item.setIsActive("1");
                } else {
                    item.setIsActive("0");
                }
            });
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
        String action = ParamUtil.getString(bpc.request,"action");
        if("cancel".equals(action)){
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_ORG_ADMIN)){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "success");
            }else{
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "inbox");
            }

        }else if("clearInfo".equalsIgnoreCase(action)) {
          clearInfo(bpc.request);
        }
        else if ("save".equalsIgnoreCase(action)) {
            log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
            String name = ParamUtil.getString(bpc.request,"name");
            String salutation = ParamUtil.getString(bpc.request,"salutation");
            String idNo = StringUtil.toUpperCase(ParamUtil.getString(bpc.request,"idNo"));
            String designation = ParamUtil.getString(bpc.request,"designation");
            String designationOther = ParamUtil.getString(bpc.request,"designationOther");
            String mobileNo = ParamUtil.getString(bpc.request,"mobileNo");
            String officeNo = ParamUtil.getString(bpc.request,"officeNo");
            String email = ParamUtil.getString(bpc.request,"email");
            String active = ParamUtil.getString(bpc.request,"active");
            String role = ParamUtil.getString(bpc.request,"role");
            ParamUtil.setRequestAttr(bpc.request, UserConstants.IS_NEED_VALIDATE_FIELD, IaisEGPConstant.YES);
            FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO);
            if(feUserDto.isCorpPass()){
                feUserDto.setUserId(feUserDto.getUenNo() + "_" + idNo);
            }else{
                feUserDto.setUserId(idNo);
            }
            feUserDto.setIdType(IaisEGPHelper.checkIdentityNoType(idNo));
            feUserDto.setIdentityNo(idNo);
            if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getString(bpc.request,"loadMyInfoData"))){
                ParamUtil.setRequestAttr(bpc.request,MyinfoUtil.IS_LOAD_MYINFO_DATA,AppConsts.YES);
            }else {
                feUserDto.setDisplayName(name);
            }
            feUserDto.setSalutation(salutation);
            feUserDto.setDesignation(designation);
            feUserDto.setDesignationOther(designationOther);
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
                feUserDto.setSelectServices(ParamUtil.getStringsToString(bpc.request,"service"));
            }else{
                feUserDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                feUserDto.setUserRole(RoleConsts.USER_ROLE_ORG_USER);
            }

            ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, feUserDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "edit");

            if (validationResult.isHasErrors()){
                log.debug("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "back");
            }else{
                LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
                MyInfoDto myInfoDto = (MyInfoDto) ParamUtil.getSessionAttr(bpc.request,MyinfoUtil.MYINFODTO_REFRESH +loginContext.getNricNum());
                boolean needRefersh = myInfoDto != null && !myInfoDto.isServiceDown();
                boolean licenseeHave = licenseeDto!=null && licenseeDto.getLicenseeIndividualDto() != null;
                if (!needRefersh) {
                    myInfoDto = null;
                }
                AuditTrailDto att = IaisEGPHelper.getCurrentAuditTrailDto();
                att.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);
                AuditTrailHelper.callSaveAuditTrail(att);

                Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                successMap.put("save","suceess");
                orgUserManageService.saveMyinfoDataByFeUserDtoAndLicenseeDto(licenseeDto,feUserDto,reSetMyInfoData(feUserDto,myInfoDto),false);
                if(licenseeHave){
                    ParamUtil.setSessionAttr(bpc.request, UserConstants.SESSION_USER_DTO, orgUserManageService.getFeUserAccountByNricAndType(licenseeDto.getLicenseeIndividualDto().getIdNo(), licenseeDto.getLicenseeIndividualDto().getIdType(), feUserDto.getUenNo()));
                }
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
