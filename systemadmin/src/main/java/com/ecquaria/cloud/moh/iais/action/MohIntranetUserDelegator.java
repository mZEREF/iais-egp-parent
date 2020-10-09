package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserUpLoadDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import com.ecquaria.cloud.role.Role;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author weilu
 * @date 2019/12/24 15:15
 */
@Delegator(value = "IntranetUser")
@Slf4j
public class MohIntranetUserDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(OrgUserQueryDto.class)
            .searchAttr(IntranetUserConstant.SEARCH_PARAM)
            .resultAttr(IntranetUserConstant.SEARCH_RESULT)
            .sortField(IntranetUserConstant.INTRANET_USER_SORT_COLUMN).sortType(SearchParam.ASCENDING).build();

    @Autowired
    private IntranetUserService intranetUserService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT, AuditTrailConsts.FUNCTION_USER_MANAGEMENT);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, "roleMap", null);
        ParamUtil.setSessionAttr(bpc.request, "orgUserDtos1", null);
        ParamUtil.setSessionAttr(bpc.request, "userFileSize", null);
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter, true);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery", searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT, searchResult);
        }
        //set system max file size
        int userFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "userFileSize", userFileSize);
    }

    public void prepareData(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        prepareOption(bpc);
        String deleteMod = (String) ParamUtil.getRequestAttr(request, "deleteMod");
        if (!StringUtil.isEmpty(deleteMod)) {
            ParamUtil.setRequestAttr(request, "deleteMod", "no");
        }
        List<SelectOption> statusOption = getStatusOption();
        List<SelectOption> roleOption = getRoleOption();
        List<SelectOption> privilegeOption = getprivilegeOption();
        ParamUtil.setSessionAttr(bpc.request, "statusOption", (Serializable) statusOption);
        ParamUtil.setSessionAttr(bpc.request, "roleOption", (Serializable) roleOption);
        ParamUtil.setSessionAttr(bpc.request, "privilegeOption", (Serializable) privilegeOption);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, null);
        Object requestAttr = ParamUtil.getRequestAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT);
        if (requestAttr != null) {
            return;
        }
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter, true);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery", searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT, searchResult);
        }
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_type = request.getParameter("crud_action_type");
        ParamUtil.setSessionAttr(bpc.request, "crud_action_type", crud_action_type);
        return;
    }

    public void preUpdateSelfInfo(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_PERSONAL_PROFILE);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(loginContext.getUserId());
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, intranetUserById);
    }

    public void prepareCreate(BaseProcessClass bpc) {
        List<SelectOption> salutation = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Mr", "Mr");
        SelectOption so2 = new SelectOption("Ms", "Ms");
        SelectOption so3 = new SelectOption("Mrs", "Mrs");
        SelectOption so4 = new SelectOption("Mdm", "Mdm");
        SelectOption so5 = new SelectOption("Dr", "Dr");
        salutation.add(so1);
        salutation.add(so2);
        salutation.add(so3);
        salutation.add(so4);
        salutation.add(so5);
        ParamUtil.setSessionAttr(bpc.request, "salutation", (Serializable) salutation);
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
    }

    public void doCreate(BaseProcessClass bpc) {
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if (!IntranetUserConstant.SAVE_ACTION.equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc.request);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
            return;
        }
        orgUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        intranetUserService.createIntranetUser(orgUserDto);
        saveEgpUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public void prepareEdit(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        if (request == null) {
            return;
        }
        String id = "";
        try{
            id = ParamUtil.getMaskedString(bpc.request, "maskUserId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                bpc.response.sendRedirect("https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        if (id != null && orgUserDto == null) {
            OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, intranetUserById);
        }
    }

    public void doEdit(BaseProcessClass bpc) {
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if (!IntranetUserConstant.SAVE_ACTION.equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        Map<String, String> errorMap = new HashMap<>(34);
        OrgUserDto orgUserDto = prepareEditOrgUserDto(bpc);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto, "save");
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            Map<String, String> validationResultMap = validationResult.retrieveAll();
            errorMap.putAll(validationResultMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
            return;
        }
        orgUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        intranetUserService.updateOrgUser(orgUserDto);
        editEgpUser(orgUserDto);
        List<AuditTrailDto> trailDtoList = IaisCommonUtils.genNewArrayList(1);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);
        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
        trailDtoList.add(auditTrailDto);
        SubmissionClient client = SpringContextHelper.getContext().getBean(SubmissionClient.class);
        try {
            AuditLogUtil.callWithEventDriven(trailDtoList, client);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public void doDelete(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String id = ParamUtil.getMaskedString(bpc.request, "maskUserId");
        OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(id);
        String userId = orgUserDto.getUserId();
        ClientUser clientUser = intranetUserService.getUserByIdentifier(userId, AppConsts.HALP_EGP_DOMAIN);
        if (clientUser != null && clientUser.isFirstTimeLoginNo()) {
            orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_DELETED);
            orgUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            intranetUserService.updateOrgUser(orgUserDto);
            deleteEgpUser(userId);
        } else {
            ParamUtil.setRequestAttr(request, "deleteMod", "no");
        }
        return;
    }

    public void prepareAddRole(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String userAccId = ParamUtil.getRequestString(bpc.request, "userAccId");
        if (request != null) {
            try{
                userAccId = ParamUtil.getMaskedString(bpc.request, "maskUserId");
            }catch (MaskAttackException e){
                log.error(e.getMessage(), e);
                try{
                    bpc.response.sendRedirect("https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
                } catch (IOException ioe){
                    log.error(ioe.getMessage(), ioe);
                    return;
                }
            }
        }
        List<Role> rolesByDomain = intranetUserService.getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
        List<String> assignRoleOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> roleMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> roleMap1 = IaisCommonUtils.genNewHashMap();
        Map<String, String> roleNameGroupId = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(rolesByDomain)) {
            for (Role role : rolesByDomain) {
                String roleName = role.getName();
                String assignRoleId = role.getId();
                assignRoleOption.add(roleName);
                roleMap.put(roleName, assignRoleId);
                roleMap1.put(assignRoleId, roleName);
            }
        }
        // already assign role
        List<OrgUserRoleDto> orgUserRoleDtos = intranetUserService.retrieveRolesByuserAccId(userAccId);
        Set<String> roleNames = IaisCommonUtils.genNewHashSet();
        List<String> assignRoleIds = IaisCommonUtils.genNewArrayList();
        if (orgUserRoleDtos != null && !userAccId.isEmpty()) {
            for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                String roleName = orgUserRoleDto.getRoleName();
                String assignRoleId = orgUserRoleDto.getId();
                roleNames.add(roleName);
                assignRoleIds.add(assignRoleId);
            }
        }
        List<String> roleIds = IaisCommonUtils.genNewArrayList();
        for (String roleName : roleNames) {
            String s = roleMap1.get(roleName);
            roleIds.add(s);
        }

        List<WorkingGroupDto> workingGroups = intranetUserService.getWorkingGroups();
        List<SelectOption> insGroupOptions = IaisCommonUtils.genNewArrayList();
        List<SelectOption> psoGroupOptions = IaisCommonUtils.genNewArrayList();
        List<SelectOption> ao1GroupOptions = IaisCommonUtils.genNewArrayList();
        for (WorkingGroupDto workingGroupDto : workingGroups) {
            String groupId = workingGroupDto.getId();
            String groupName = workingGroupDto.getGroupName();
            roleNameGroupId.put(groupName, groupId);
            if (groupName.contains("Inspection")) {
                SelectOption so = new SelectOption(groupId, groupName);
                insGroupOptions.add(so);
            } else if (groupName.contains("Professional")) {
                SelectOption so = new SelectOption(groupId, groupName);
                psoGroupOptions.add(so);
            } else if (groupName.contains("Level 1")) {
                SelectOption so = new SelectOption(groupId, groupName);
                ao1GroupOptions.add(so);
            }
        }
        assignRoleOption.removeAll(roleIds);
        //remove
//        List<UserGroupCorrelationDto> userGroupsByUserId = intranetUserService.getUserGroupsByUserId(userAccId);
//        List<SelectOption> insGroupRemoveOptions = IaisCommonUtils.genNewArrayList();
//        List<SelectOption> psoGroupRemoveOptions = IaisCommonUtils.genNewArrayList();
//        List<SelectOption> ao1GroupRemoveOptions = IaisCommonUtils.genNewArrayList();
//        if(!IaisCommonUtils.isEmpty(userGroupsByUserId)){
//            for(UserGroupCorrelationDto userGroupCorrelationDto : userGroupsByUserId){
//                String groupId = userGroupCorrelationDto.getGroupId();
//                String groupName = intranetUserService.getWrkGrpById(groupId);
//                if (groupName.contains("Inspection")) {
//                    SelectOption so = new SelectOption(groupId, groupName);
//                    insGroupRemoveOptions.add(so);
//                } else if (groupName.contains("Professional")) {
//                    SelectOption so = new SelectOption(groupId, groupName);
//                    psoGroupRemoveOptions.add(so);
//                } else if (groupName.contains("Level 1")) {
//                    SelectOption so = new SelectOption(groupId, groupName);
//                    ao1GroupRemoveOptions.add(so);
//                }
//            }
//        }
        ParamUtil.setRequestAttr(bpc.request, "assignRoleOption", assignRoleOption);//Professional Screening  - Nursing Home
        ParamUtil.setRequestAttr(bpc.request, "alreadyAssignRoles", roleNames);
        ParamUtil.setRequestAttr(bpc.request, "alreadyAssignRoleIds", assignRoleIds);
        ParamUtil.setSessionAttr(bpc.request, "psoGroupOptions", (Serializable)psoGroupOptions);
        ParamUtil.setSessionAttr(bpc.request, "ao1GroupOptions", (Serializable) ao1GroupOptions);
        ParamUtil.setSessionAttr(bpc.request, "insGroupOptions", (Serializable)insGroupOptions);
//        ParamUtil.setSessionAttr(bpc.request, "psoGroupRemoveOptions", (Serializable)psoGroupRemoveOptions);
//        ParamUtil.setSessionAttr(bpc.request, "ao1GroupRemoveOptions", (Serializable) ao1GroupRemoveOptions);
//        ParamUtil.setSessionAttr(bpc.request, "insGroupRemoveOptions", (Serializable)insGroupRemoveOptions);
        //key = Professional Screening  - Nursing Home  value = AO1
        ParamUtil.setSessionAttr(bpc.request, "roleMap", (Serializable) roleMap);
        //key = Professional Screening  - Nursing Home  value = AO1
        ParamUtil.setSessionAttr(bpc.request, "roleNameGroupId", (Serializable) roleNameGroupId);

        if (userAccId != null) {
            OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(userAccId);
            String userId = orgUserDto.getUserId();
            ParamUtil.setRequestAttr(bpc.request, "userIdName", userId);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
        }
    }

    public void addRole(BaseProcessClass bpc) {
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        String actionType = ParamUtil.getString(bpc.request, "crud_action_type");
        if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        Map<String, String> roleMap = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request, "roleMap");
        Map<String, String> roleNameGroupId = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request, "roleNameGroupId");
        List<String> groupIds = IaisCommonUtils.genNewArrayList();
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
        List<String> assignRoles = IaisCommonUtils.genNewArrayList();
        String[] removeRoles = ParamUtil.getStrings(bpc.request, "removeRole");
        String userAccId = orgUserDto.getId();
        String[] assignRoleOthers = ParamUtil.getStrings(bpc.request, "assignRoleOther");
        String assignRoleAo1 = ParamUtil.getString(bpc.request, "assignRoleAo1");
        String assignRoleAo1Lead = ParamUtil.getString(bpc.request, "assignRoleAo1Lead");
        String assignRoleIns = ParamUtil.getString(bpc.request, "assignRoleIns");
        String assignRoleInsLead = ParamUtil.getString(bpc.request, "assignRoleInsLead");
        String assignRolePso = ParamUtil.getString(bpc.request, "assignRolePso");
        String assignRolePsoLead = ParamUtil.getString(bpc.request, "assignRolePsoLead");
        //AO1
        if(!StringUtil.isEmpty(assignRoleAo1)){
            String ao1GroupSelect = ParamUtil.getString(bpc.request, "ao1GroupSelect");
            if(!StringUtil.isEmpty(ao1GroupSelect)){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setGroupId(ao1GroupSelect);
                userGroupCorrelationDto.setIsLeadForGroup(0);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String roleId = roleMap.get(assignRoleAo1);
                assignRoles.add(roleId);
                groupIds.add(ao1GroupSelect);
            }
        }
        //AO1Leader
        if(!StringUtil.isEmpty(assignRoleAo1Lead)){
            String ao1GroupLeadSelect = ParamUtil.getString(bpc.request, "ao1GroupLeadSelect");
            if(!StringUtil.isEmpty(ao1GroupLeadSelect)){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setGroupId(ao1GroupLeadSelect);
                userGroupCorrelationDto.setIsLeadForGroup(1);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String roleId = roleMap.get(assignRoleAo1Lead);
                assignRoles.add(roleId);
                groupIds.add(assignRoleAo1Lead);
            }
        }
        //ins
        if(!StringUtil.isEmpty(assignRoleIns)){
            String insGroupSelect = ParamUtil.getString(bpc.request, "insGroupSelect");
            if(!StringUtil.isEmpty(insGroupSelect)){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setGroupId(insGroupSelect);
                userGroupCorrelationDto.setIsLeadForGroup(0);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String roleId = roleMap.get(assignRoleIns);
                assignRoles.add(roleId);
                groupIds.add(insGroupSelect);
            }
        }
        //insLead
        if(!StringUtil.isEmpty(assignRoleInsLead)){
            String insGroupLeadSelect = ParamUtil.getString(bpc.request, "insGroupLeadSelect");
            if(!StringUtil.isEmpty(insGroupLeadSelect)){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setGroupId(insGroupLeadSelect);
                userGroupCorrelationDto.setIsLeadForGroup(1);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String roleId = roleMap.get(assignRoleInsLead);
                assignRoles.add(roleId);
                groupIds.add(insGroupLeadSelect);
            }
        }
        //pso
        if(!StringUtil.isEmpty(assignRolePso)){
            String psoGroupSelect = ParamUtil.getString(bpc.request, "psoGroupSelect");
            if(!StringUtil.isEmpty(psoGroupSelect)){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setGroupId(psoGroupSelect);
                userGroupCorrelationDto.setIsLeadForGroup(0);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String roleId = roleMap.get(assignRolePso);
                assignRoles.add(roleId);
                groupIds.add(psoGroupSelect);
            }
        }
        //psoLead
        if(!StringUtil.isEmpty(assignRolePsoLead)){
            String psoGroupLeadSelect = ParamUtil.getString(bpc.request, "psoGroupLeadSelect");
            if(!StringUtil.isEmpty(psoGroupLeadSelect)){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setGroupId(psoGroupLeadSelect);
                userGroupCorrelationDto.setIsLeadForGroup(1);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String roleId = roleMap.get(assignRolePsoLead);
                assignRoles.add(roleId);
                groupIds.add(psoGroupLeadSelect);
            }
        }
        //others
        if(assignRoleOthers!=null){
            for(String assignRole : assignRoleOthers){
                String roleId = roleMap.get(assignRole);
                assignRoles.add(roleId);
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_AO2.equals(roleId)){
                    String groupName = "Level 2 Approval" ;
                    String groupId = roleNameGroupId.get(groupName);
                    groupIds.add(groupId);
                }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_AO3.equals(roleId)){
                    String groupName = "Level 3 Approval" ;
                    String groupId = roleNameGroupId.get(groupName);
                    groupIds.add(groupId);
                }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_ASO.equals(roleId)){
                    String groupName = "Admin Screening officer" ;
                    String groupId = roleNameGroupId.get(groupName);
                    groupIds.add(groupId);
                }
            }
        }
        if (assignRoles != null) {
            List<OrgUserRoleDto> orgUserRoleDtos = IaisCommonUtils.genNewArrayList();
            List<EgpUserRoleDto> egpUserRoleDtos = IaisCommonUtils.genNewArrayList();
            for (String roleId : assignRoles) {
                OrgUserRoleDto orgUserRoleDto = new OrgUserRoleDto();
                orgUserRoleDto.setUserAccId(userAccId);
                orgUserRoleDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                orgUserRoleDto.setRoleName(roleId);
                orgUserRoleDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                orgUserRoleDtos.add(orgUserRoleDto);
                EgpUserRoleDto egpUserRoleDto = new EgpUserRoleDto();
                egpUserRoleDto.setUserId(orgUserDto.getUserId());
                egpUserRoleDto.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
                egpUserRoleDto.setRoleId(roleId);
                egpUserRoleDto.setPermission("A");
                //egpUserRoleDto.isSystem()
                egpUserRoleDtos.add(egpUserRoleDto);
            }
            intranetUserService.assignRole(orgUserRoleDtos);
            intranetUserService.createEgpRoles(egpUserRoleDtos);

            //add group
            List<UserGroupCorrelationDto> userGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
            for(String groupId : groupIds){
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                userGroupCorrelationDto.setGroupId(groupId);
                userGroupCorrelationDto.setIsLeadForGroup(0);
                userGroupCorrelationDto.setAuditTrailDto(auditTrailDto);
                userGroupCorrelationDtos.add(userGroupCorrelationDto);
            }
            intranetUserService.addUserGroupId(userGroupCorrelationDtos);
        }
        if (removeRoles != null) {
            List<String> removeRoleIds = IaisCommonUtils.genNewArrayList();
            for (String removeRole : removeRoles) {
                removeRoleIds.add(removeRole);
            }
            List<String> removeRoleNames = IaisCommonUtils.genNewArrayList();
            List<OrgUserRoleDto> orgUserRoleDtos = intranetUserService.getOrgUserRoleDtoById(removeRoleIds);
            if (!IaisCommonUtils.isEmpty(orgUserRoleDtos)) {
                for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                    orgUserRoleDto.setAuditTrailDto(auditTrailDto);
                    String roleName = orgUserRoleDto.getRoleName();
                    //AO1
                    removeRoleNames.add(roleName);
                }
            }
            intranetUserService.removeRole(orgUserRoleDtos);
            intranetUserService.removeEgpRoles(AppConsts.HALP_EGP_DOMAIN, orgUserDto.getUserId(), removeRoleNames);

            //remove group

        }
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        ParamUtil.setRequestAttr(bpc.request, "userAccId", userAccId);
        return;
    }

    public void doImport(BaseProcessClass bpc) throws IOException, DocumentException {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile sessionFile = (CommonsMultipartFile) request.getFile("xmlFile");
        File file = File.createTempFile("temp", "xml");
        File xmlFile = inputStreamToFile(sessionFile.getInputStream(), file);
        List<OrgUserDto> orgUserDtos = importXML(xmlFile);
        ParamUtil.setSessionAttr(bpc.request, "orgUserDtos", (Serializable) orgUserDtos);
        return;
    }

    public void prepareImportAck(BaseProcessClass bpc) {
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        List<OrgUserDto> orgUserDtos = (List<OrgUserDto>) ParamUtil.getSessionAttr(bpc.request, "orgUserDtos");
        //do valiant
        List<OrgUserUpLoadDto> orgUserUpLoadDtos = IaisCommonUtils.genNewArrayList();
//        List<String> userIds = IaisCommonUtils.genNewArrayList();
//        String dup = "Duplication of record(s)";
//        List<String> msgDup = IaisCommonUtils.genNewArrayList();
//        msgDup.add(dup);
//        for (OrgUserDto orgUserDto : orgUserDtos) {
//            OrgUserUpLoadDto orgUserUpLoadDto = new OrgUserUpLoadDto();
//            List<String> valiant = valiantDto(orgUserDto);
//            String userId = orgUserDto.getUserId();
//            if (!IaisCommonUtils.isEmpty(valiant)) {
//                if (StringUtil.isEmpty(userId)) {
//                    orgUserUpLoadDto.setUserId("-");
//                } else {
//                    orgUserUpLoadDto.setUserId(userId);
//                }
//                orgUserUpLoadDto.setMsg(valiant);
//                orgUserUpLoadDtos.add(orgUserUpLoadDto);
//            }
//            if (userIds.contains(userId)) {
//                orgUserUpLoadDto.setUserId(userId);
//                orgUserUpLoadDto.setMsg(msgDup);
//                orgUserUpLoadDtos.add(orgUserUpLoadDto);
//            } else {
//                userIds.add(userId);
//            }
//        }
        //1 you mei you chong fu de
        List<OrgUserDto> existUsersNew = IaisCommonUtils.genNewArrayList();
        List<OrgUserDto> existUsersOld = IaisCommonUtils.genNewArrayList();
        for (OrgUserDto orgUserDto : orgUserDtos) {
            orgUserDto.setAuditTrailDto(auditTrailDto);
            String userId = orgUserDto.getUserId();
            if (!StringUtil.isEmpty(userId)) {
                OrgUserDto intranetUserByUserId = intranetUserService.findIntranetUserByUserId(userId);
                if (intranetUserByUserId != null) {
                    existUsersNew.add(orgUserDto);
                    transform(intranetUserByUserId);
                    existUsersOld.add(intranetUserByUserId);
                }
            }
        }
        if (IaisCommonUtils.isEmpty(existUsersNew)) {
            for (OrgUserDto orgUserDto : orgUserDtos) {
                OrgUserUpLoadDto orgUserUpLoadDto = new OrgUserUpLoadDto();
                List<String> valiant = valiantDto(orgUserDto);
                String userId = orgUserDto.getUserId();
                if (!IaisCommonUtils.isEmpty(valiant)) {
                    if (StringUtil.isEmpty(userId)) {
                        orgUserUpLoadDto.setUserId("-");
                    } else {
                        orgUserUpLoadDto.setUserId(userId);
                    }
                    orgUserUpLoadDto.setMsg(valiant);
                    orgUserUpLoadDtos.add(orgUserUpLoadDto);
                }
            }
            if (IaisCommonUtils.isEmpty(orgUserUpLoadDtos)) {
                List<String> msg = IaisCommonUtils.genNewArrayList();
                String s = "add success !";
                msg.add(s);
                intranetUserService.createIntranetUsers(orgUserDtos);
                for (OrgUserDto orgUserDto : orgUserDtos) {
                    saveEgpUser(orgUserDto);
                    OrgUserUpLoadDto orgUserUpLoadDto = new OrgUserUpLoadDto();
                    String userId = orgUserDto.getUserId();
                    orgUserUpLoadDto.setMsg(msg);
                    orgUserUpLoadDto.setUserId(userId);
                    orgUserUpLoadDtos.add(orgUserUpLoadDto);
                }
            }
            ParamUtil.setRequestAttr(bpc.request, "orgUserUpLoadDtos", orgUserUpLoadDtos);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
//        if (!IaisCommonUtils.isEmpty(orgUserUpLoadDtos)) {
//            ParamUtil.setRequestAttr(bpc.request, "orgUserUpLoadDtos", orgUserUpLoadDtos);
//            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
//        }
        if (!IaisCommonUtils.isEmpty(existUsersNew)) {
            ParamUtil.setRequestAttr(bpc.request, "existUsersNew", existUsersNew);
            ParamUtil.setRequestAttr(bpc.request, "existUsersOld", existUsersOld);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
    }

    private void transform(OrgUserDto orgUserDto) {
        String branchUnit = orgUserDto.getBranchUnit();
        String designation = orgUserDto.getDesignation();
        String division = orgUserDto.getDivision();
        String firstName = orgUserDto.getFirstName();
        String lastName = orgUserDto.getLastName();
        String remarks = orgUserDto.getRemarks();
        String mobileNo = orgUserDto.getMobileNo();
        String officeTelNo = orgUserDto.getOfficeTelNo();
        String salutation = orgUserDto.getSalutation();
        String displayName = orgUserDto.getDisplayName();
        String organization = orgUserDto.getOrganization();

        if (StringUtil.isEmpty(branchUnit)) {
            orgUserDto.setBranchUnit(null);
        }
        if (StringUtil.isEmpty(designation)) {
            orgUserDto.setDesignation(null);
        }
        if (StringUtil.isEmpty(division)) {
            orgUserDto.setDivision(null);
        }
        if (StringUtil.isEmpty(firstName)) {
            orgUserDto.setFirstName(null);
        }
        if (StringUtil.isEmpty(lastName)) {
            orgUserDto.setFirstName(null);
        }
        if (StringUtil.isEmpty(remarks)) {
            orgUserDto.setRemarks(remarks);
        }
        if (StringUtil.isEmpty(mobileNo)) {
            orgUserDto.setMobileNo(null);
        }
        if (StringUtil.isEmpty(officeTelNo)) {
            orgUserDto.setOfficeTelNo(null);
        }
        if (StringUtil.isEmpty(salutation)) {
            orgUserDto.setSalutation(null);
        }
        if (StringUtil.isEmpty(displayName)) {
            orgUserDto.setDisplayName(null);
        }
        if (StringUtil.isEmpty(organization)) {
            orgUserDto.setOrganization(null);
        }
    }

    public void importSwitch(BaseProcessClass bpc) {
        List<OrgUserDto> orgUserDtos = (List<OrgUserDto>) ParamUtil.getRequestAttr(bpc.request, "orgUserDtos");
        ParamUtil.setRequestAttr(bpc.request, "orgUserDtos", orgUserDtos);
        String actionType = ParamUtil.getString(bpc.request, "crud_action_type");
        if ("back".equals(actionType)) {
            ParamUtil.setSessionAttr(bpc.request, "orgUserDtos", null);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        }
        return;
    }

    public void importSubmit(BaseProcessClass bpc) {
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        List<OrgUserDto> orgUserDtos = (List<OrgUserDto>) ParamUtil.getSessionAttr(bpc.request, "orgUserDtos");
        List<OrgUserDto> orgUserDtosNew = IaisCommonUtils.genNewArrayList();
        List<OrgUserDto> orgUserDtosOld = IaisCommonUtils.genNewArrayList();
        List<OrgUserUpLoadDto> orgUserUpLoadValiantDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(orgUserDtos)) {
            for (OrgUserDto orgUserDto : orgUserDtos) {
                OrgUserUpLoadDto orgUserUpLoadDto = new OrgUserUpLoadDto();
                List<String> valiant = valiantDto(orgUserDto);
                String userId = orgUserDto.getUserId();
                if (!IaisCommonUtils.isEmpty(valiant)) {
                    if (StringUtil.isEmpty(userId)) {
                        orgUserUpLoadDto.setUserId("-");
                    } else {
                        orgUserUpLoadDto.setUserId(userId);
                    }
                    orgUserUpLoadDto.setMsg(valiant);
                    orgUserUpLoadValiantDtos.add(orgUserUpLoadDto);
                }
            }
            if (!IaisCommonUtils.isEmpty(orgUserUpLoadValiantDtos)) {
                ParamUtil.setRequestAttr(bpc.request, "orgUserUpLoadDtos", orgUserUpLoadValiantDtos);
                return;
            }
            for (OrgUserDto orgUserDto : orgUserDtos) {
                String userId = orgUserDto.getUserId();
                OrgUserDto oldOrgUserDto = intranetUserService.findIntranetUserByUserId(userId);
                orgUserDto.setAuditTrailDto(auditTrailDto);
                if (oldOrgUserDto != null) {
                    String id = oldOrgUserDto.getId();
                    orgUserDto.setId(id);
                    oldOrgUserDto.setAuditTrailDto(auditTrailDto);
                    orgUserDtosOld.add(oldOrgUserDto);
                } else {
                    orgUserDtosNew.add(orgUserDto);
                }
            }
            //new
            intranetUserService.createIntranetUsers(orgUserDtosNew);
            for (OrgUserDto orgUserDto : orgUserDtosNew) {
                saveEgpUser(orgUserDto);
            }
            //update
            if (!IaisCommonUtils.isEmpty(orgUserDtosOld)) {
                for (OrgUserDto orgUserDto : orgUserDtos) {
                    orgUserDto.setAuditTrailDto(auditTrailDto);
                    intranetUserService.updateOrgUser(orgUserDto);
                    editEgpUser(orgUserDto);
                }
            }
            List<OrgUserUpLoadDto> orgUserUpLoadDtos = IaisCommonUtils.genNewArrayList();
            List<String> msg = IaisCommonUtils.genNewArrayList();
            String s = "add success !";
            msg.add(s);
            for (OrgUserDto orgUserDto : orgUserDtos) {
                String userId = orgUserDto.getUserId();
                OrgUserUpLoadDto orgUserUpLoadDto = new OrgUserUpLoadDto();
                orgUserUpLoadDto.setUserId(userId);
                orgUserUpLoadDto.setMsg(msg);
                orgUserUpLoadDtos.add(orgUserUpLoadDto);
            }
            ParamUtil.setRequestAttr(bpc.request, "orgUserUpLoadDtos", orgUserUpLoadDtos);
        }
        ParamUtil.setSessionAttr(bpc.request, "orgUserDtos", null);
        return;
    }

    public void importBack(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, "orgUserDtos", null);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        return;
    }

    public void doExport(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String[] ids = ParamUtil.getStrings(request, "userUid");
        if (ids == null || ids.length == 0) {
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, "ids", ids);
        return;
    }

    public void changeStatus(BaseProcessClass bpc) {

    }

    public void saveStatus(BaseProcessClass bpc) {
        String userId = ParamUtil.getRequestString(bpc.request, "statusUserId");
        ParamUtil.setRequestAttr(bpc.request, "statusUserId", userId);
        String actionType = ParamUtil.getString(bpc.request, "crud_action_type");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        String userDomain = "intranet";
        OrgUserDto orgUserDto;
        ClientUser clientUser;
        if (!StringUtil.isEmpty(userId)) {
            clientUser = intranetUserService.getUserByIdentifier(userId, userDomain);
            orgUserDto = intranetUserService.findIntranetUserByUserId(userId);
            if (clientUser == null || orgUserDto == null) {
                errorMap.put("userId", "This user is not exist.");
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                return;
            } else {
                String status = orgUserDto.getStatus();
                if (IntranetUserConstant.DEACTIVATE.equals(actionType) && IntranetUserConstant.COMMON_STATUS_DEACTIVATED.equals(status)) {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    errorMap.put("userId", "This user is already in Deactivated status.");
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    return;
                } else if (IntranetUserConstant.DEACTIVATE.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_DEACTIVATED.equals(status) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_DEACTIVATED);
                    orgUserDto.setAuditTrailDto(auditTrailDto);
                    intranetUserService.updateOrgUser(orgUserDto);
                    clientUser.setAccountStatus(ClientUser.STATUS_INACTIVE);
                    intranetUserService.updateEgpUser(clientUser);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.REDEACTIVATE.equals(actionType) && IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status)) {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    errorMap.put("userId", "This user is already in Active status.");
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    return;
                } else if (IntranetUserConstant.REDEACTIVATE.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
                    orgUserDto.setAuditTrailDto(auditTrailDto);
                    intranetUserService.updateOrgUser(orgUserDto);
                    clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
                    intranetUserService.updateEgpUser(clientUser);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.TERMINATE.equals(actionType) && IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    errorMap.put("userId", "This user is already in Terminated status.");
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    return;
                } else if (IntranetUserConstant.TERMINATE.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_TERMINATED);
                    orgUserDto.setAuditTrailDto(auditTrailDto);
                    intranetUserService.updateOrgUser(orgUserDto);
                    clientUser.setAccountStatus(ClientUser.STATUS_TERMINATED);
                    intranetUserService.updateEgpUser(clientUser);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.UNLOCK.equals(actionType) && IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status)) {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    errorMap.put("userId", "This user is already in Active status.");
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    return;
                } else if (IntranetUserConstant.UNLOCK.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
                    orgUserDto.setAuditTrailDto(auditTrailDto);
                    intranetUserService.updateOrgUser(orgUserDto);
                    clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
                    intranetUserService.updateEgpUser(clientUser);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    errorMap.put("userId", "Terminated users cannot be reactivated.");
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    return;
                }
            }
        } else {
            errorMap.put("userId", "ERR0009");
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
    }

    public void doSearch(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String displayName = ParamUtil.getRequestString(request, "searchDisplayName");
        String userId = ParamUtil.getRequestString(request, "searchUserId");
        String email = ParamUtil.getRequestString(request, "searchEmail");
        String status = ParamUtil.getRequestString(request, "searchStatus");
        String privilege = ParamUtil.getRequestString(request, "privilege");
        String role = ParamUtil.getRequestString(request, "role");
        ParamUtil.setRequestAttr(request, "searchDisplayName", displayName);
        ParamUtil.setRequestAttr(request, "searchUserId", userId);
        ParamUtil.setRequestAttr(request, "searchEmail", email);
        ParamUtil.setRequestAttr(request, "searchStatus", status);
        ParamUtil.setRequestAttr(request, "privilege", privilege);
        ParamUtil.setRequestAttr(request, "role", role);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (!StringUtil.isEmpty(userId)) {
            searchParam.addFilter("userId", userId, true);
        }
        if (!StringUtil.isEmpty(displayName)) {
            searchParam.addFilter("displayName", displayName, true);
        }
        if (!StringUtil.isEmpty(email)) {
            searchParam.addFilter("email", email, true);
        }
        if (!StringUtil.isEmpty(status)) {
            searchParam.addFilter("status", status, true);
        }
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery", searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        ParamUtil.setSessionAttr(request, IntranetUserConstant.SEARCH_PARAM, searchParam);
        ParamUtil.setRequestAttr(request, IntranetUserConstant.SEARCH_RESULT, searchResult);

    }

    public void doSorting(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        SearchResultHelper.doSort(request, filterParameter);
    }

    public void doPaging(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        SearchResultHelper.doPage(request, filterParameter);
    }

    /*utils*/
    private OrgUserDto prepareOrgUserDto(HttpServletRequest request) {
        OrgUserDto orgUserDto = new OrgUserDto();
        String userId = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_USERID);
        String displayName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String startDateStr = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_STARTDATE);
        Date startDate = DateUtil.parseDate(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        String endDateStr = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        String[] salutation = ParamUtil.getStrings(request, IntranetUserConstant.INTRANET_SALUTATION);
        if (!StringUtil.isEmpty(salutation[0])) {
            orgUserDto.setSalutation(salutation[0]);
        }
        String firstName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_LASTNAME);
        String division = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_REMARKS);
        String organization = ParamUtil.getRequestString(request, "organization");

        orgUserDto.setUserId(userId);
        orgUserDto.setFirstName(firstName);
        orgUserDto.setLastName(lastName);
        orgUserDto.setDisplayName(displayName);
        orgUserDto.setAccountActivateDatetime(startDate);
        orgUserDto.setAccountDeactivateDatetime(endDate);
        orgUserDto.setOrgId(IntranetUserConstant.ORGANIZATION);
        orgUserDto.setDivision(division);
        orgUserDto.setOrganization(organization);
        orgUserDto.setBranchUnit(branch);
        orgUserDto.setEmail(email);
        orgUserDto.setMobileNo(mobileNo);
        orgUserDto.setOfficeTelNo(officeNo);
        orgUserDto.setRemarks(remarks);
        orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
        orgUserDto.setUserDomain(IntranetUserConstant.DOMAIN_INTRANET);
        orgUserDto.setAvailable(Boolean.FALSE);
        return orgUserDto;
    }

    private OrgUserDto prepareEditOrgUserDto(BaseProcessClass bpc) {
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        String[] salutation = ParamUtil.getStrings(bpc.request, IntranetUserConstant.INTRANET_SALUTATION);
        if (!"".equals(salutation[0])) {
            orgUserDto.setSalutation(salutation[0]);
        }
        String firstName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_LASTNAME);
        String division = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_REMARKS);
        String organization = ParamUtil.getRequestString(bpc.request, "organization");

        orgUserDto.setOrganization(organization);
        orgUserDto.setFirstName(firstName);
        orgUserDto.setLastName(lastName);
        orgUserDto.setDisplayName(displayName);
        orgUserDto.setAccountDeactivateDatetime(endDate);
        orgUserDto.setOrgId(IntranetUserConstant.ORGANIZATION);
        orgUserDto.setDivision(division);
        orgUserDto.setBranchUnit(branch);
        orgUserDto.setEmail(email);
        orgUserDto.setMobileNo(mobileNo);
        orgUserDto.setOfficeTelNo(officeNo);
        orgUserDto.setRemarks(remarks);
        orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
        orgUserDto.setUserDomain(IntranetUserConstant.DOMAIN_INTRANET);
        return orgUserDto;
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
            log.error(StringUtil.changeForLog("===========egpUser can not found============"));
        }
    }

    private Boolean deleteEgpUser(String userId) {
        return intranetUserService.deleteEgpUser(AppConsts.HALP_EGP_DOMAIN, userId);
    }

    private void prepareOption(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<SelectOption> salutation = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Mr", "Mr");
        SelectOption so2 = new SelectOption("Ms", "Ms");
        SelectOption so3 = new SelectOption("Mrs", "Mrs");
        SelectOption so4 = new SelectOption("Mdm", "Mdm");
        SelectOption so5 = new SelectOption("Dr", "Dr");
        salutation.add(so1);
        salutation.add(so2);
        salutation.add(so3);
        salutation.add(so4);
        salutation.add(so5);
        List<SelectOption> statusOption = IaisCommonUtils.genNewArrayList();
        SelectOption status1 = new SelectOption("I", "I");
        SelectOption status2 = new SelectOption("A", "A");
        SelectOption status3 = new SelectOption("E", "E");
        SelectOption status4 = new SelectOption("L", "L");
        SelectOption status5 = new SelectOption("S", "S");
        SelectOption status6 = new SelectOption("T", "T");
        statusOption.add(status1);
        statusOption.add(status2);
        statusOption.add(status3);
        statusOption.add(status4);
        statusOption.add(status5);
        statusOption.add(status6);
        ParamUtil.setSessionAttr(request, "salutation", (Serializable) salutation);
        ParamUtil.setSessionAttr(request, "statusOption", (Serializable) statusOption);
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
    }

    private List<OrgUserDto> importXML(File file) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        //root
        Element root = document.getRootElement();
        //ele
        List list = root.elements();
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < list.size(); i++) {
            try {
                Element element = (Element) list.get(i);
                String userId = element.element("userId").getText();
                String displayName = element.element("displayName").getText();
                String startDateStr = element.element("accountActivationStart").getText();
                Date startDate = DateUtil.parseDate(startDateStr, "dd/MM/yyyy");
                String endDateStr = element.element("accountActivationEnd").getText();
                Date endDate = DateUtil.parseDate(endDateStr, "dd/MM/yyyy");
                String salutation = element.element("salutation").getText();
                String firstName = element.element("firstName").getText();
                String lastName = element.element("lastName").getText();
                String organization = element.element("organization").getText();
                String division = element.element("division").getText();
                String branchUnit = element.element("branchUnit").getText();
                String mobileNo = element.element("mobileNo").getText();
                String officeNo = element.element("officeNo").getText();
                String email = element.element("email").getText();
                String remarks = element.element("remarks").getText();
                OrgUserDto orgUserDto = new OrgUserDto();
                if (!StringUtil.isEmpty(userId)) {
                    orgUserDto.setUserId(userId);
                }
                if (!StringUtil.isEmpty(firstName)) {
                    orgUserDto.setFirstName(firstName);
                }
                if (!StringUtil.isEmpty(lastName)) {
                    orgUserDto.setLastName(lastName);
                }
                if (!StringUtil.isEmpty(displayName)) {
                    orgUserDto.setDisplayName(displayName);
                }
                if (!StringUtil.isEmpty(startDate)) {
                    orgUserDto.setAccountActivateDatetime(startDate);
                }
                if (!StringUtil.isEmpty(endDate)) {
                    orgUserDto.setAccountDeactivateDatetime(endDate);
                }
                if (!StringUtil.isEmpty(branchUnit)) {
                    orgUserDto.setBranchUnit(branchUnit);
                }
                if (!StringUtil.isEmpty(division)) {
                    orgUserDto.setDivision(division);
                }
                if (!StringUtil.isEmpty(salutation)) {
                    orgUserDto.setSalutation(salutation);
                }
                if (!StringUtil.isEmpty(organization)) {
                    orgUserDto.setOrganization(organization);
                }
                if (!StringUtil.isEmpty(email)) {
                    orgUserDto.setEmail(email);
                }
                if (!StringUtil.isEmpty(mobileNo)) {
                    orgUserDto.setMobileNo(mobileNo);
                }
                if (!StringUtil.isEmpty(officeNo)) {
                    orgUserDto.setOfficeTelNo(officeNo);
                }
                if (!StringUtil.isEmpty(remarks)) {
                    orgUserDto.setRemarks(remarks);
                }
                if (!StringUtil.isEmpty(startDateStr)) {
                    orgUserDto.setStartDateStr(startDateStr);
                }
                if (!StringUtil.isEmpty(endDateStr)) {
                    orgUserDto.setEndDateStr(endDateStr);
                }
                orgUserDto.setUserDomain(AppConsts.USER_DOMAIN_INTRANET);
                orgUserDto.setOrgId(IntranetUserConstant.ORGANIZATION);
                orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
                orgUserDto.setUserDomain(IntranetUserConstant.DOMAIN_INTRANET);
                orgUserDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                orgUserDto.setAvailable(Boolean.FALSE);
                orgUserDtos.add(orgUserDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        return orgUserDtos;
    }

    private List<String> valiantDto(OrgUserDto orgUserDto) {
        List<String> errors = IaisCommonUtils.genNewArrayList();
        String userId = orgUserDto.getUserId();
        if (!StringUtil.isEmpty(userId)) {
            if (!userId.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(.{1,64})$")) {
                String error = "Please enter alphanumeric character.";
                errors.add(error);
            }
        } else {
            String error = "User ID is a mandatory field.";
            errors.add(error);
        }
        String displayName = orgUserDto.getDisplayName();
        if (StringUtil.isEmpty(displayName)) {
            String error = "Display Name is a mandatory field.";
            errors.add(error);
        }

        Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
        String startDateStr = orgUserDto.getStartDateStr();
        if (StringUtil.isEmpty(startDateStr)) {
            String error = "Account Activation Start is a mandatory field.";
            errors.add(error);
        }
        if (!StringUtil.isEmpty(startDateStr) && accountActivateDatetime == null) {
            String error = "Please key in a valid Account Activation Start date.";
            errors.add(error);
        }
        Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
        String endDateStr = orgUserDto.getEndDateStr();
        if (StringUtil.isEmpty(endDateStr)) {
            String error = "Account Activation End is a mandatory field.";
            errors.add(error);
        }
        if (!StringUtil.isEmpty(endDateStr) && accountDeactivateDatetime == null) {
            String error = "Please key in a valid Account Activation End date.";
            errors.add(error);
        }
        if (accountDeactivateDatetime != null && accountActivateDatetime != null) {
            boolean after = accountDeactivateDatetime.after(accountActivateDatetime);
            if (!after) {
                String error = "Account Activation Start cannot be later than Account Activation End.";
                errors.add(error);
            }
        }
        String lastName = orgUserDto.getLastName();
        if (StringUtil.isEmpty(lastName)) {
            String error = "Last Name is a mandatory field.";
            errors.add(error);
        }
        String firstName = orgUserDto.getFirstName();
        if (StringUtil.isEmpty(firstName)) {
            String error = "First Name is a mandatory field.";
            errors.add(error);
        }
        String email = orgUserDto.getEmail();
        if (StringUtil.isEmpty(email)) {
            String error = "Email is a mandatory field.";
            errors.add(error);
        } else {
            if (!ValidationUtils.isEmail(email)) {
                String error = "Please key in a valid email address.";
                errors.add(error);
            }
        }
        String mobileNo = orgUserDto.getMobileNo();
        if (!StringUtil.isEmpty(mobileNo)) {
            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                errors.add("Please key in a valid mobile number.");
            }
        }
        String officeTelNo = orgUserDto.getOfficeTelNo();
        if (!StringUtil.isEmpty(officeTelNo)) {
            if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                errors.add("Please key in a valid office number.");
            }
        }
        return errors;
    }

    /**
     * StartStep: importUserRole
     *
     * @param bpc
     * @throws
     */
    public void importUserRole(BaseProcessClass bpc) throws IOException, DocumentException {
        log.debug(StringUtil.changeForLog("the importUserRole start ...."));
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile sessionFile = (CommonsMultipartFile) request.getFile("userRoleUpload");
        int userFileSize = (int)ParamUtil.getSessionAttr(bpc.request, "userFileSize");
        File file = File.createTempFile("temp", "xml");
        File xmlFile = inputStreamToFile(sessionFile.getInputStream(), file);
        //validate xml file
        List<EgpUserRoleDto> egpUserRoleDtos = IaisCommonUtils.genNewArrayList();
        Map<String, String> fileErrorMap = intranetUserService.importRoleXmlValidation(xmlFile, userFileSize, sessionFile, egpUserRoleDtos);
        if(fileErrorMap != null && fileErrorMap.size() > 0){
            if(fileErrorMap.containsKey("userRoleUploadError")){
                ParamUtil.setRequestAttr(bpc.request,"ackSuccessFlag", "");
            } else {
                ParamUtil.setRequestAttr(bpc.request,"ackSuccessFlag", AppConsts.FAIL);
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(fileErrorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        } else {
            egpUserRoleDtos = intranetUserService.importRoleXml(xmlFile);
            ParamUtil.setRequestAttr(bpc.request,"ackSuccessFlag", AppConsts.SUCCESS);
        }
        ParamUtil.setRequestAttr(bpc.request,"egpUserRoleDtos", egpUserRoleDtos);
    }

    /**
     * StartStep: exportUserRole
     *
     * @param bpc
     * @throws
     */
    public void exportUserRole(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the exportUserRole start ...."));
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String[] ids = ParamUtil.getStrings(request, "userUid");
        if (ids == null || ids.length == 0) {
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, "ids", ids);
    }

    private List<SelectOption> getStatusOption() {
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("CMSTAT001", "Active");
        SelectOption so2 = new SelectOption("OUSTAT005", "Deactivated");
        SelectOption so3 = new SelectOption("OUSTAT004", "Terminated");
        SelectOption so4 = new SelectOption("OUSTAT001", "Expired");
        SelectOption so5 = new SelectOption("OUSTAT002", "Locked");
        result.add(so1);
        result.add(so2);
        result.add(so3);
        result.add(so4);
        result.add(so5);
        return result;
    }

    private List<SelectOption> getRoleOption() {
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Admin", "Admin");
        SelectOption so2 = new SelectOption("Professional", "Professional");
        SelectOption so3 = new SelectOption("Inspector", "Inspector");
        result.add(so1);
        result.add(so2);
        result.add(so3);
        return result;
    }

    private List<SelectOption> getprivilegeOption() {
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Admin Screening Task", "Admin Screening Task");
        SelectOption so2 = new SelectOption("Approve a Particular Application Stage", "Approve a Particular Application Stage");
        SelectOption so3 = new SelectOption("Access a Particular Online Enquiry or Report", "access a particular Online Enquiry or Report");
        result.add(so1);
        result.add(so2);
        result.add(so3);
        return result;
    }

    private static File inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return file;
    }
}
