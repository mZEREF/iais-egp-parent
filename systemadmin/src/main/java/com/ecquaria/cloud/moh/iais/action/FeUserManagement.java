package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.FeUserConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
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
        ParamUtil.clearSession(bpc.request, FeUserConstants.SESSION_USER_DTO,FeUserConstants.SESSION_USER_UEN_NAME);
        ParamUtil.setSessionAttr(bpc.request,"uenNo",null);
        ParamUtil.setSessionAttr(bpc.request,"AllServicesForHcsaRole",
                (Serializable) HcsaServiceCacheHelper.getAllServiceSelectOptions(true));
    }

    public void prepare(BaseProcessClass bpc){
        log.debug("****preparePage Process ****");
        ParamUtil.clearSession(bpc.request, FeUserConstants.SESSION_USER_DTO);
            SearchParam searchParam = getSearchParam(bpc.request,false);
            QueryHelp.setMainSql("systemAdmin", "userFeAccount",searchParam);
            SearchResult<BeUserQueryDto> feAdminQueryDtoSearchResult = organizationClient.getFeUserList(searchParam).getEntity();
            ParamUtil.setRequestAttr(bpc.request, "feAdmin",feAdminQueryDtoSearchResult);
            ParamUtil.setRequestAttr(bpc.request, "feAdminSearchParam",searchParam);
    }

    public void search(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc.request,false);
        String idNo = ParamUtil.getRequestString(bpc.request, FeUserConstants.ID_NUMBER);
        String designation = ParamUtil.getRequestString(bpc.request,FeUserConstants.DESIGNATION);
        String uenNo = ParamUtil.getRequestString(bpc.request,FeUserConstants.SESSION_USER_UEN_NAME);
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
            searchParam.addFilter(FeUserConstants.ID_NUMBER,idNo,true);
        }else{
            searchParam.removeFilter(FeUserConstants.ID_NUMBER);
            searchParam.removeParam(FeUserConstants.ID_NUMBER);
        }

        if(!StringUtil.isEmpty(designation)){
            searchParam.addFilter(FeUserConstants.DESIGNATION, designation ,true);
        }else{
            searchParam.removeFilter(FeUserConstants.DESIGNATION);
            searchParam.removeParam(FeUserConstants.DESIGNATION);
        }

        if(!StringUtil.isEmpty(uenNo)){
            searchParam.addFilter(FeUserConstants.SESSION_USER_UEN_NAME, uenNo, true);
            //searchParam.addFilter("licenseeName",'%' + licenseeName + '%',true);
        }else{
            searchParam.removeFilter(FeUserConstants.SESSION_USER_UEN_NAME);
            searchParam.removeParam(FeUserConstants.SESSION_USER_UEN_NAME);
        }
        ParamUtil.setSessionAttr(bpc.request,"feUserSearchParam",searchParam);
        ParamUtil.setRequestAttr(bpc.request,FeUserConstants.ID_NUMBER,idNo);
        ParamUtil.setRequestAttr(bpc.request,FeUserConstants.DESIGNATION,designation);
        ParamUtil.setRequestAttr(bpc.request,FeUserConstants.SESSION_USER_UEN_NAME,uenNo);
    }

    public void delete(BaseProcessClass bpc){
        String[] userId =  ParamUtil.getMaskedStrings(bpc.request, "userId");
        log.info(StringUtil.changeForLog("The deleted user: " + Arrays.toString(userId)));
        for (String item : userId) {
            if (!StringUtil.isEmpty(item)) {
                OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(item);
                orgUserDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
                orgUserDto.setIdentityNo(orgUserDto.getIdNumber());
                intranetUserService.updateOrgUser(orgUserDto);
                //sync fe db
                try {
                    List<FeUserDto> userList = intranetUserService.getUserListByNricAndIdTypeWithDel(orgUserDto.getIdNumber(),orgUserDto.getIdType());
                    Optional<FeUserDto> user = userList.stream()
                            .filter(i -> item.equals(i.getId()))
                            .findAny();
                    user.ifPresent(i -> {
                        i.setStatus(AppConsts.COMMON_STATUS_DELETED);
                        syncFeUserWithTrack(i);
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

    public void edit(BaseProcessClass bpc) {
        String userId = ParamUtil.getMaskedString(bpc.request, "userId");
        FeUserDto feUserDto;
        if (StringUtil.isNotEmpty(userId)) {
            feUserDto = organizationClient.getUserAccount(userId).getEntity();
            if (feUserDto.getUserId().indexOf('_') < 0) {
                feUserDto.setSolo(true);
            }
            ParamUtil.setSessionAttr(bpc.request, FeUserConstants.SESSION_USER_DTO, feUserDto);
        } else {
            feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, FeUserConstants.SESSION_USER_DTO);
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.SESSION_NAME_ROLES,
                intranetUserService.getRoleSelection(feUserDto.getLicenseeId()));
        ParamUtil.setSessionAttr(bpc.request, "feusertitle", "Edit");
        ParamUtil.setRequestAttr(bpc.request,FeUserConstants.SESSION_USER_UEN_NAME,feUserDto.getUenNo());
        ParamUtil.setRequestAttr(bpc.request,"organizationId",feUserDto.getOrgId());
    }

    public void create(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, "feusertitle", "Create");
        FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, FeUserConstants.SESSION_USER_DTO);
        String uenNo = null;
        if (feUserDto != null && feUserDto.getId() == null) {
            uenNo = feUserDto.getUenNo();
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.SESSION_NAME_ROLES, checkUenAndRoleSelectOptions(uenNo));
    }

    public void validation(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String title = ParamUtil.getRequestString(request,"feusertitle");
        String action = ParamUtil.getRequestString(request,"action");
        FeUserDto userAttr = (FeUserDto) ParamUtil.getSessionAttr(request,FeUserConstants.SESSION_USER_DTO);
        if("back".equals(action)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE,"suc");
        }else{
            //do validation
            log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
            String name = ParamUtil.getString(request,"name");
            String idNo = StringUtil.toUpperCase(ParamUtil.getString(request,"idNo"));
            //admin role
            String role = ParamUtil.getString(request,"role");
            String roles = ParamUtil.getStringsToString(request, "roles");
            String officeNo = ParamUtil.getString(bpc.request,"officeNo");
            String idType = ParamUtil.getString(bpc.request,"idType");
            String selectServices = ParamUtil.getStringsToString(bpc.request,"service");
            String active = "active".equals(ParamUtil.getString(request,"active")) ? AppConsts.COMMON_STATUS_ACTIVE : AppConsts.COMMON_STATUS_IACTIVE;
            userAttr = Optional.ofNullable(userAttr).orElseGet(() -> new FeUserDto());
            String id = userAttr.getId();
            String prevIdNumber = userAttr.getIdentityNo();
            ControllerHelper.get(request,userAttr);
            userAttr.setId(id);
            if (!userAttr.isSolo() || "Create".equals(title)) {
                userAttr.setIdType(idType);
                userAttr.setIdentityNo(idNo);
                userAttr.setIdNumber(idNo);
            } else {
                idNo = userAttr.getIdentityNo();
            }
            userAttr.setUserRole(role);
            userAttr.setDisplayName(name);
            userAttr.setOfficeTelNo(officeNo);
            userAttr.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            userAttr.setAvailable(Boolean.TRUE);
            userAttr.setStatus(active);
            userAttr.setRoles(roles);
            userAttr.setAccountActivateDatetime(new Date());
            userAttr.setSelectServices(selectServices);

            ValidationResult validationResult;
            ParamUtil.setSessionAttr(request,FeUserConstants.SESSION_USER_DTO,userAttr);
            if ("Edit".equals(title)) {
                validationResult = WebValidationHelper.validateProperty(userAttr, "edit");
                if (StringUtil.isEmpty(userAttr.getId())) {
                    validationResult.addMessage(FeUserConstants.SESSION_USER_UEN_NAME, "GENERAL_ERR0006");
                } else {
                    String organizationId = ParamUtil.getMaskedString(request,"organizationId");
                    if (!userAttr.getOrgId().equalsIgnoreCase(organizationId)) {
                        validationResult.addMessage(FeUserConstants.SESSION_USER_UEN_NAME, "GENERAL_ERR0006");
                    }
                }
            } else {
                String uenNo = ParamUtil.getString(request,FeUserConstants.SESSION_USER_UEN_NAME);
                userAttr.setUenNo(uenNo);
                validationResult = WebValidationHelper.validateProperty(userAttr, "create");
                if (StringUtil.isEmpty(uenNo)){
                    validationResult.addMessage(FeUserConstants.SESSION_USER_UEN_NAME, "GENERAL_ERR0006");
                    validationResult.setHasErrors(true);
                } else {
                    //save orgid when create
                    OrganizationDto organizationDto = intranetUserService.getByUenNoAndStatus(uenNo, AppConsts.COMMON_STATUS_ACTIVE);
                    if (organizationDto == null) {
                        validationResult.addMessage(FeUserConstants.SESSION_USER_UEN_NAME, "USER_ERR020");
                        validationResult.setHasErrors(true);
                    } else {
                        userAttr.setOrgId(organizationDto.getId());
                    }
                }
            }
            // set user id
            if (userAttr.isCorpPass() && !userAttr.isSolo()) {
                userAttr.setUserId(userAttr.getUenNo() + "_" + idNo);
            } else {
                userAttr.setUserId(idNo);
            }
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                if("Edit".equals(title)) {
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "editErr");
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE, "editErr");
                }else{
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"createErr");
                }
            }else{
                if (intranetUserService.canUpdateAccount(userAttr, prevIdNumber)) {
                    log.info(StringUtil.changeForLog("Licensee User Management - " + title + " : " + userAttr.getUserId()));
                    AuditTrailDto att = IaisEGPHelper.getCurrentAuditTrailDto();
                    att.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);
                    AuditTrailHelper.callSaveAuditTrail(att);
                    userAttr.setAuditTrailDto(att);
                    // set roles
                    List<String> roleList = IaisCommonUtils.genNewArrayList();
                    if (RoleConsts.USER_ROLE_ORG_ADMIN.equals(userAttr.getUserRole())) {
                        roleList.add(RoleConsts.USER_ROLE_ORG_ADMIN);
                    }
                    if (StringUtil.isNotEmpty(userAttr.getRoles())) {
                        roleList.addAll(Arrays.asList(userAttr.getRoles().split("#")));
                    }
                    List<OrgUserRoleDto> orgUserRoleDtos = IaisCommonUtils.genNewArrayList();
                    for (String r : roleList) {
                        String selectSvcs = null;
                        if (StringUtil.isIn(r, new String[]{RoleConsts.USER_ROLE_ORG_USER})) {
                            selectSvcs = selectServices;
                            if (StringUtil.isEmpty(selectServices)) {
                                selectSvcs = AppServicesConsts.SERVICE_MATRIX_ALL;
                            }
                        }
                        orgUserRoleDtos.add(IaisEGPHelper.createOrgUserRoleDto(r, selectSvcs));
                    }
                    userAttr.setOrgUserRoleDtos(orgUserRoleDtos);
                    //save be
                    userAttr = intranetUserService.saveIntrenetUser(userAttr);
                    // sync
                    syncFeUserWithTrack(userAttr);
                    ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE,"suc");
                }else {
                    ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr("identityNo", "USER_ERR002"));
                    ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE,"createErr");
                    return;
                }
            }
        }
    }

    public void syncFeUserWithTrack(FeUserDto userAttr) {
        eicGatewayClient.callEicWithTrack(userAttr, this::syncFeUser, this.getClass(), "syncFeUser");
    }

    public void syncFeUser(FeUserDto userAttr) {
        eicGatewayClient.syncFeUser(userAttr);
    }

    private SearchParam getSearchParam(HttpServletRequest request, boolean refresh){
        SearchParam searchParam;
        if(refresh){
            searchParam = new SearchParam(BeUserQueryDto.class.getName());
            searchParam.setSort("ID", SearchParam.ASCENDING);
            searchParam.setPageNo(1);
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            ParamUtil.setSessionAttr(request,"feUserSearchParam",searchParam);
        } else {
            searchParam = (SearchParam) ParamUtil.getSessionAttr(request,"feUserSearchParam");
        }
        return searchParam;
    }

    @RequestMapping(value = "/checkUenAndRoleData", method = RequestMethod.POST)
    @ResponseBody
    public List<SelectOption> checkUenAndRoleData(HttpServletRequest request){
        return checkUenAndRoleSelectOptions(ParamUtil.getString(request,"uenNo"));
    }

    public List<SelectOption> checkUenAndRoleSelectOptions(String uenNo) {
        if (StringUtil.isEmpty(uenNo) || uenNo.length() < 5) {
            return intranetUserService.getRoleSelection(null);
        }
        String licenseeId = null;
        OrganizationDto organizationDto = intranetUserService.getByUenNoAndStatus(uenNo, AppConsts.COMMON_STATUS_ACTIVE);
        if (organizationDto != null) {
            licenseeId = Optional.ofNullable(organizationDto.getLicenseeDto())
                    .map(LicenseeDto::getId)
                    .orElse(null);
        }
        return intranetUserService.getRoleSelection(licenseeId);
    }
}
