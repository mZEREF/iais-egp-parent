package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Delegator(value = "feUserManagement")
@Slf4j
public class FeUserManagement {

    @Autowired
    EicGatewayClient eicGatewayClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    IntranetUserService intranetUserService;
    public void start(BaseProcessClass bpc){
        log.debug("****doStart Process ****");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT, AuditTrailConsts.FUNCTION_USER_MANAGEMENT);
        getSearchParam(bpc.request,true);
    }



    public void prepare(BaseProcessClass bpc){
        log.debug("****preparePage Process ****");
        ParamUtil.setSessionAttr(bpc.request,"inter_user_attr",null);
            SearchParam searchParam = getSearchParam(bpc.request,false);
            QueryHelp.setMainSql("systemAdmin", "userFeAccount",searchParam);
            SearchResult<BeUserQueryDto> feAdminQueryDtoSearchResult = organizationClient.getFeUserList(searchParam).getEntity();
            ParamUtil.setRequestAttr(bpc.request, "feAdmin",feAdminQueryDtoSearchResult);
            ParamUtil.setRequestAttr(bpc.request, "feAdminSearchParam",searchParam);
    }

    private void organizationSelection(BaseProcessClass bpc, String orgId){
        List<OrganizationDto> organList = intranetUserService.getUenList();
        Map<String,String> uenMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(orgId)){
            for (OrganizationDto i :organList
            ) {
                if(i.getId().equals(orgId)){
                    ParamUtil.setRequestAttr(bpc.request,"uenNo",  i.getUenNo());
                    ParamUtil.setRequestAttr(bpc.request,"organizationId",orgId);
                }
            }
        }else{
            for (OrganizationDto i :organList) {
                uenMap.put(i.getUenNo(), i.getId());
                selectOptions.add(new SelectOption(i.getId(), i.getUenNo()));
            }
            ParamUtil.setRequestAttr(bpc.request,"uenSelection", selectOptions);
        }
        ParamUtil.setSessionAttr(bpc.request,"existedUenMap", (Serializable) uenMap);
    }

    public void search(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc.request,false);
        String idNo = ParamUtil.getRequestString(bpc.request,"idNo");
        String designation = ParamUtil.getRequestString(bpc.request,"designation");
        String uenNo = ParamUtil.getRequestString(bpc.request,"uenNo");
        String fieldName = ParamUtil.getRequestString(bpc.request,"fieldName");
        String sortType = ParamUtil.getRequestString(bpc.request,"sortType");

        if(!StringUtil.isEmpty(fieldName) && !StringUtil.isEmpty(sortType)){
            searchParam.setSort(fieldName,sortType);
        }else{
            if(IaisCommonUtils.isEmpty(searchParam.getSortMap())){
                searchParam.setSort("ID", SearchParam.ASCENDING);
            }
        }
        if(!StringUtil.isEmpty(idNo)){
            searchParam.addFilter("idNo",idNo,true);
        }else{
            searchParam.removeFilter("idNo");
            searchParam.removeParam("idNo");
        }

        if(!StringUtil.isEmpty(designation)){
            searchParam.addFilter("designation", designation ,true);
        }else{
            searchParam.removeFilter("designation");
            searchParam.removeParam("designation");
        }

        if(!StringUtil.isEmpty(uenNo)){
            searchParam.addFilter("uenNo", uenNo, true);
            //searchParam.addFilter("licenseeName",'%' + licenseeName + '%',true);
        }else{
            searchParam.removeFilter("uenNo");
            searchParam.removeParam("uenNo");
        }
        ParamUtil.setSessionAttr(bpc.request,"feUserSearchParam",searchParam);
        ParamUtil.setRequestAttr(bpc.request,"idNo",idNo);
        ParamUtil.setRequestAttr(bpc.request,"designation",designation);
        ParamUtil.setRequestAttr(bpc.request,"uenNo",uenNo);
    }

    public void delete(BaseProcessClass bpc){
        String[] userId =  ParamUtil.getMaskedStrings(bpc.request, "userId");
        for (String item:userId
             ) {
            if (!StringUtil.isEmpty(item)){
                OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(item);
                orgUserDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
                orgUserDto.setIdentityNo(orgUserDto.getIdNumber());
                intranetUserService.updateOrgUser(orgUserDto);
                //sync fe db
                try {
                    String orgId = orgUserDto.getOrgId();
                    List<FeUserDto> userList = intranetUserService.getUserListByNricAndIdType(orgUserDto.getIdNumber(),orgUserDto.getIdType());
                    Optional<FeUserDto> user = userList.stream().filter(i -> i.getOrgId().equals(orgId)).findFirst();
                    user.ifPresent(i -> {
                        i.setStatus(AppConsts.COMMON_STATUS_DELETED);
                        eicGatewayClient.syncFeUser(i);
                        intranetUserService.deleteEgpUser(orgUserDto.getUserDomain(),orgUserDto.getUserId());
                    });

                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

    public void searchPage(BaseProcessClass bpc){
        SearchParam searchParamGroup = getSearchParam(bpc.request,false);
        CrudHelper.doPaging(searchParamGroup,bpc.request);
    }

    public void edit(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"inter_user_attr_is_corppass",null);
        String userId = ParamUtil.getMaskedString(bpc.request,"userId");
        FeUserDto feUserDto;

        if(!StringUtil.isEmpty(userId)){
            feUserDto = organizationClient.getUserAccount(userId).getEntity();
            ParamUtil.setSessionAttr(bpc.request,"inter_user_attr",feUserDto);
        }else{
            feUserDto = (FeUserDto)ParamUtil.getSessionAttr(bpc.request,"inter_user_attr");
        }
        ParamUtil.setSessionAttr(bpc.request,"feusertitle", "Edit");
        organizationSelection(bpc,feUserDto.getOrgId());
    }

    public void create(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"feusertitle", "Create");
        organizationSelection(bpc,null);
    }

    public void validation(BaseProcessClass bpc){
        String title = ParamUtil.getRequestString(bpc.request,"feusertitle");
        String action = ParamUtil.getRequestString(bpc.request,"action");
        FeUserDto userAttr = (FeUserDto) ParamUtil.getSessionAttr(bpc.request,"inter_user_attr");
        if("back".equals(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"suc");
        }else{
            //do validation
            log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
            String uenNo = ParamUtil.getString(bpc.request,"uenNo");
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
            active = "active".equals(active) ? AppConsts.COMMON_STATUS_ACTIVE : AppConsts.COMMON_STATUS_IACTIVE;
            userAttr = Optional.ofNullable(userAttr).orElseGet(() -> new FeUserDto());
            String prevIdNumber = userAttr.getIdentityNo();
            userAttr.setUenNo(uenNo);
            userAttr.setIdType(idType);
            userAttr.setIdentityNo(idNo);
            userAttr.setIdNumber(idNo);
            userAttr.setDisplayName(name);
            userAttr.setSalutation(salutation);
            userAttr.setDesignation(designation);
            userAttr.setMobileNo(mobileNo);
            userAttr.setOfficeTelNo(officeNo);
            userAttr.setEmail(email);
            userAttr.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            userAttr.setAvailable(Boolean.TRUE);
            userAttr.setStatus(active);
            userAttr.setAccountActivateDatetime(new Date());
            List<OrgUserRoleDto> orgUserRoleDtoList = IaisCommonUtils.genNewArrayList();
            OrgUserRoleDto orgUserRoleDtoAdmin = new OrgUserRoleDto();
            OrgUserRoleDto orgUserRoleDtoUser = new OrgUserRoleDto();
            orgUserRoleDtoAdmin.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            orgUserRoleDtoUser.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

            if("admin".equals(role)){
                //TODO i don't know why this need two role
                orgUserRoleDtoAdmin.setRoleName(RoleConsts.USER_ROLE_ORG_ADMIN);
                orgUserRoleDtoUser.setRoleName(RoleConsts.USER_ROLE_ORG_USER);
                orgUserRoleDtoList.add(orgUserRoleDtoAdmin);
                orgUserRoleDtoList.add(orgUserRoleDtoUser);
                userAttr.setUserRole(RoleConsts.USER_ROLE_ORG_ADMIN);
            }else{
                orgUserRoleDtoUser.setRoleName(RoleConsts.USER_ROLE_ORG_USER);
                orgUserRoleDtoList.add(orgUserRoleDtoUser);
                userAttr.setUserRole(RoleConsts.USER_ROLE_ORG_USER);
            }

            OrgUserDto userDto = MiscUtil.transferEntityDto(userAttr, OrgUserDto.class);
            ValidationResult validationResult;
            if(userAttr.isCorpPass()){
                userAttr.setUserId(userAttr.getUenNo() + "_" + idNo);
                userDto.setUserId(userAttr.getUenNo() + "_" + idNo);
            }else{
                userAttr.setUserId(idNo);
                userDto.setUserId(idNo);
            }
            ParamUtil.setSessionAttr(bpc.request,"inter_user_attr",userAttr);
            if("Edit".equals(title)){
                validationResult = WebValidationHelper.validateProperty(userAttr, "edit");
            }else{
                validationResult = WebValidationHelper.validateProperty(userAttr, "create");
                if (StringUtil.isEmpty(uenNo)){
                    validationResult.addMessage("uenNo", "GENERAL_ERR0006");
                    validationResult.setHasErrors(true);
                }else {
                    Map<String, String> existedUenMap = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request, "existedUenMap");
                    if (IaisCommonUtils.isNotEmpty(existedUenMap) ){
                        //save orgid when create
                        String orgId = existedUenMap.get(uenNo);
                        if(orgId!=null){
                            userDto.setOrgId(orgId);
                            userAttr.setOrgId(orgId);
                        }
                    }else {
                        validationResult.addMessage("uenNo", "USER_ERR020");
                        validationResult.setHasErrors(true);
                    }
                }
            }
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                if("Edit".equals(title)) {
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "editErr");
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "editErr");
                }else{
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"createErr");
                }
            }else{
                if (intranetUserService.canUpdateAccount(userAttr, prevIdNumber)){
                    AuditTrailDto att = IaisEGPHelper.getCurrentAuditTrailDto();
                    att.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);
                    AuditTrailHelper.callSaveAuditTrail(att);

                    Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                    successMap.put("save","suceess");
                    //save be
                    //save client
                    if("Edit".equals(title)) {
                        intranetUserService.updateOrgUser(userDto);
                        orgUserRoleDtoAdmin.setUserAccId(userAttr.getId());
                        orgUserRoleDtoUser.setUserAccId(userAttr.getId());
                        intranetUserService.removeRoleByAccount(userAttr.getId());
                        intranetUserService.assignRole(orgUserRoleDtoList);
                        editEgpUser(userDto);
                    }else{
                        OrgUserDto orgUserDto = intranetUserService.createIntrenetUser(userDto);
                        orgUserRoleDtoAdmin.setUserAccId(orgUserDto.getId());
                        orgUserRoleDtoUser.setUserAccId(orgUserDto.getId());
                        userAttr.setId(orgUserDto.getId());
                        intranetUserService.assignRole(orgUserRoleDtoList);
                        saveEgpUser(userDto);
                    }
                    //sync fe db
                    try {
                        eicGatewayClient.syncFeUser(userAttr);
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }

                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"suc");
                }else {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr("identityNo", "USER_ERR002"));
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"createErr");
                    return;
                }
            }
        }
    }

    private SearchParam getSearchParam(HttpServletRequest request, boolean refresh){
        SearchParam searchParam;
        if(refresh){
            searchParam = new SearchParam(BeUserQueryDto.class.getName());
            searchParam.setSort("ID", SearchParam.ASCENDING);
            searchParam.setPageNo(1);
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            ParamUtil.setSessionAttr(request,"feUserSearchParam",searchParam);
        }else
        {
            searchParam = (SearchParam) ParamUtil.getSessionAttr(request,"feUserSearchParam");
        }
        return searchParam;
    }

    private void saveEgpUser(OrgUserDto orgUserDto) {
        ClientUser clientUser = MiscUtil.transferEntityDto(orgUserDto, ClientUser.class);
        clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        clientUser.setId(orgUserDto.getUserId());
        clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
        String email = orgUserDto.getEmail();
        String salutation = orgUserDto.getSalutation();
        clientUser.setSalutation(salutation);
        clientUser.setEmail(email);
        String randomStr = IaisEGPHelper.generateRandomString(6);
        String pwd = PasswordUtil.encryptPassword(clientUser.getUserDomain(), randomStr, null);
        clientUser.setPassword(pwd);
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");
        intranetUserService.saveEgpUser(clientUser);
    }

    private void editEgpUser(OrgUserDto orgUserDto) {
        String userId = orgUserDto.getUserId();
        ClientUser clientUser = intranetUserService.getUserByIdentifier(userId, AppConsts.HALP_EGP_DOMAIN);
        if (clientUser != null) {
            Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
            Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
            String email = orgUserDto.getEmail();
            String displayName = orgUserDto.getDisplayName();
            String firstName = orgUserDto.getFirstName();
            String lastName = orgUserDto.getLastName();
            String salutation = orgUserDto.getSalutation();
            String mobileNo = orgUserDto.getMobileNo();

            clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
            clientUser.setId(userId);
            clientUser.setDisplayName(displayName);
            clientUser.setAccountStatus(ClientUser.STATUS_TERMINATED);
            clientUser.setPasswordChallengeQuestion("A");
            clientUser.setPasswordChallengeAnswer("A");
            clientUser.setAccountActivateDatetime(accountActivateDatetime);
            clientUser.setAccountDeactivateDatetime(accountDeactivateDatetime);
            clientUser.setFirstName(firstName);
            clientUser.setLastName(lastName);
            clientUser.setEmail(email);
            clientUser.setMobileNo(mobileNo);
            clientUser.setSalutation(salutation);
            intranetUserService.updateEgpUser(clientUser);
        } else {
            log.debug(StringUtil.changeForLog("===========egpUser can not found============"));
        }
    }
}
