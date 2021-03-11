package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.*;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import com.ecquaria.cloud.role.Role;
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
import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * @author weilu
 * @date 2019/12/24 15:15
 */
@Delegator(value = "IntranetUser")
@Slf4j
public class MohIntranetUserDelegator {

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
        ParamUtil.setSessionAttr(bpc.request, "orgUserRoleDtos", null);
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, IntranetUserConstant.SEARCH_PARAM, OrgUserQueryDto.class.getName(),
                IntranetUserConstant.INTRANET_USER_SORT_COLUMN, SearchParam.ASCENDING, false);
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
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.SEARCH_PARAM);
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
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        try {
            id = ParamUtil.getMaskedString(bpc.request, "maskUserId");
        } catch (MaskAttackException e) {
            log.error(e.getMessage(), e);
            try {
                bpc.response.sendRedirect("https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe) {
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        if (id != null && orgUserDto == null) {
            List<SelectOption> statusOptions = getStatusOption();
            ParamUtil.setSessionAttr(bpc.request, "statusOptions", (Serializable) statusOptions);
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
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto, "edit");
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            Map<String, String> validationResultMap = validationResult.retrieveAll();
            errorMap.putAll(validationResultMap);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
            return;
        }
        orgUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        intranetUserService.updateOrgUser(orgUserDto);
        editEgpUser(orgUserDto);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_USER_UPDATE);

        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);

        AuditTrailHelper.callSaveAuditTrail(auditTrailDto);

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
            orgUserDto.setAvailable(Boolean.FALSE);
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
            try {
                userAccId = ParamUtil.getMaskedString(bpc.request, "maskUserId");
            } catch (MaskAttackException e) {
                log.error(e.getMessage(), e);
                try {
                    bpc.response.sendRedirect("https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
                } catch (IOException ioe) {
                    log.error(ioe.getMessage(), ioe);
                    return;
                }
            }
        }
        List<Role> rolesByDomain = intranetUserService.getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
        List<String> assignRoleOptionFull = IaisCommonUtils.genNewArrayList();
        Map<String, String> roleMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> roleMap1 = IaisCommonUtils.genNewHashMap();
        Map<String, String> roleNameGroupId = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(rolesByDomain)) {
            for (Role role : rolesByDomain) {
                String roleName = role.getName();//APPROVE 1
                String assignRoleId = role.getId();//AO1
                assignRoleOptionFull.add(roleName);
                roleMap.put(roleName, assignRoleId);
                roleMap1.put(assignRoleId, roleName);
            }
        }
        // already assign role
        List<OrgUserRoleDto> orgUserRoleDtos = intranetUserService.retrieveRolesByuserAccId(userAccId);
        List<String> existRoleIds = IaisCommonUtils.genNewArrayList();
        Map<String, String> roleNameAndIdMap = IaisCommonUtils.genNewHashMap();
        List<String> alreadyAssignRoleOptionFull = IaisCommonUtils.genNewArrayList();
        if (orgUserRoleDtos != null && !userAccId.isEmpty()) {
            for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                String roleId = orgUserRoleDto.getRoleName();
                String roleNameFull = roleMap1.get(roleId);
                String assignRoleId = orgUserRoleDto.getId();
                alreadyAssignRoleOptionFull.add(roleNameFull);
                roleNameAndIdMap.put(roleNameFull, assignRoleId);
                existRoleIds.add(roleId);
            }
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
        assignRoleOptionFull.removeAll(alreadyAssignRoleOptionFull);
        ParamUtil.setRequestAttr(bpc.request, "assignRoleOption", assignRoleOptionFull);//Professional Screening  - Nursing Home
        ParamUtil.setRequestAttr(bpc.request, "roleNameAndIdMap", roleNameAndIdMap);
        ParamUtil.setSessionAttr(bpc.request, "psoGroupOptions", (Serializable) psoGroupOptions);
        ParamUtil.setSessionAttr(bpc.request, "ao1GroupOptions", (Serializable) ao1GroupOptions);
        ParamUtil.setSessionAttr(bpc.request, "insGroupOptions", (Serializable) insGroupOptions);
        ParamUtil.setSessionAttr(bpc.request, "roleMap", (Serializable) roleMap);
        //key = Professional Screening  - Nursing Home  value = AO1
        ParamUtil.setSessionAttr(bpc.request, "roleNameGroupId", (Serializable) roleNameGroupId);
        ParamUtil.setSessionAttr(bpc.request, "orgUserRoleDtos", (Serializable) existRoleIds);

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
        Map<String, Integer> groupIds = IaisCommonUtils.genNewHashMap();
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
        if (!StringUtil.isEmpty(assignRoleAo1)) {
            String ao1GroupSelect = ParamUtil.getString(bpc.request, "ao1GroupSelect");
            if (!StringUtil.isEmpty(ao1GroupSelect)) {
                String roleId = roleMap.get(assignRoleAo1);
                assignRoles.add(roleId);
                Integer integer = groupIds.get(ao1GroupSelect);
                if (integer != null && integer == 1) {
                    return;
                } else {
                    groupIds.put(ao1GroupSelect, 0);
                }
            }
        }
        //AO1Leader
        if (!StringUtil.isEmpty(assignRoleAo1Lead)) {
            String ao1GroupLeadSelect = ParamUtil.getString(bpc.request, "ao1GroupLeadSelect");
            if (!StringUtil.isEmpty(ao1GroupLeadSelect)) {
                String roleId = roleMap.get(assignRoleAo1Lead);
                assignRoles.add(roleId);
                groupIds.put(ao1GroupLeadSelect, 1);
            }
        }
        //ins
        if (!StringUtil.isEmpty(assignRoleIns)) {
            String insGroupSelect = ParamUtil.getString(bpc.request, "insGroupSelect");
            if (!StringUtil.isEmpty(insGroupSelect)) {
                String roleId = roleMap.get(assignRoleIns);
                assignRoles.add(roleId);
                Integer integer = groupIds.get(insGroupSelect);
                if (integer != null && integer == 1) {
                    return;
                } else {
                    groupIds.put(insGroupSelect, 0);
                }
            }
        }
        //insLead
        if (!StringUtil.isEmpty(assignRoleInsLead)) {
            String insGroupLeadSelect = ParamUtil.getString(bpc.request, "insGroupLeadSelect");
            if (!StringUtil.isEmpty(insGroupLeadSelect)) {
                String roleId = roleMap.get(assignRoleInsLead);
                assignRoles.add(roleId);
                groupIds.put(insGroupLeadSelect, 1);
            }
        }
        //pso
        if (!StringUtil.isEmpty(assignRolePso)) {
            String psoGroupSelect = ParamUtil.getString(bpc.request, "psoGroupSelect");
            if (!StringUtil.isEmpty(psoGroupSelect)) {
                String roleId = roleMap.get(assignRolePso);
                assignRoles.add(roleId);
                Integer integer = groupIds.get(psoGroupSelect);
                if (integer != null && integer == 1) {
                    return;
                } else {
                    groupIds.put(psoGroupSelect, 0);
                }
            }
        }
        //psoLead
        if (!StringUtil.isEmpty(assignRolePsoLead)) {
            String psoGroupLeadSelect = ParamUtil.getString(bpc.request, "psoGroupLeadSelect");
            if (!StringUtil.isEmpty(psoGroupLeadSelect)) {
                String roleId = roleMap.get(assignRolePsoLead);
                assignRoles.add(roleId);
                groupIds.put(psoGroupLeadSelect, 1);
            }
        }
        //others
        if (assignRoleOthers != null) {
            for (String assignRole : assignRoleOthers) {
                String roleId = roleMap.get(assignRole);
                assignRoles.add(roleId);
                if (RoleConsts.USER_ROLE_AO2.equals(roleId) || RoleConsts.USER_ROLE_AO2_LEAD.equals(roleId)) {
                    String groupName = "Level 2 Approval";
                    String groupId = roleNameGroupId.get(groupName);
                    if (RoleConsts.USER_ROLE_AO2.equals(roleId)) {
                        Integer integer = groupIds.get(groupId);
                        if (integer != null && integer == 1) {
                            continue;
                        } else {
                            groupIds.put(groupId, 0);
                        }
                    } else {
                        groupIds.put(groupId, 1);
                    }

                } else if (RoleConsts.USER_ROLE_AO3.equals(roleId) || RoleConsts.USER_ROLE_AO3_LEAD.equals(roleId)) {
                    String groupName = "Level 3 Approval";
                    String groupId = roleNameGroupId.get(groupName);
                    if (RoleConsts.USER_ROLE_AO3.equals(roleId)) {
                        Integer integer = groupIds.get(groupId);
                        if (integer != null && integer == 1) {
                            continue;
                        } else {
                            groupIds.put(groupId, 0);
                        }
                    } else {
                        groupIds.put(groupId, 1);
                    }
                } else if (RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_ASO_LEAD.equals(roleId)) {
                    String groupName = "Admin Screening officer";
                    String groupId = roleNameGroupId.get(groupName);
                    if (RoleConsts.USER_ROLE_ASO.equals(roleId)) {
                        Integer integer = groupIds.get(groupId);
                        if (integer != null && integer == 1) {
                            continue;
                        } else {
                            groupIds.put(groupId, 0);
                        }
                    } else {
                        groupIds.put(groupId, 1);
                    }
                }
            }
        }
        if (!IaisCommonUtils.isEmpty(assignRoles)) {
            List<OrgUserRoleDto> orgUserRoleDtos = IaisCommonUtils.genNewArrayList();
            List<EgpUserRoleDto> egpUserRoleDtos = IaisCommonUtils.genNewArrayList();
            List<String> roleIds = intranetUserService.getRoleIdByUserId(userAccId);
            if (assignRoles.contains(RoleConsts.USER_ROLE_PSO_LEAD)) {
                assignRoles.add(RoleConsts.USER_ROLE_PSO);
            }
            if (assignRoles.contains(RoleConsts.USER_ROLE_ASO_LEAD)) {
                assignRoles.add(RoleConsts.USER_ROLE_ASO);
            }
            if (assignRoles.contains(RoleConsts.USER_ROLE_AO1_LEAD)) {
                assignRoles.add(RoleConsts.USER_ROLE_AO1);
            }
            if (assignRoles.contains(RoleConsts.USER_ROLE_AO2_LEAD)) {
                assignRoles.add(RoleConsts.USER_ROLE_AO2);
            }
            if (assignRoles.contains(RoleConsts.USER_ROLE_AO3_LEAD)) {
                assignRoles.add(RoleConsts.USER_ROLE_AO3);
            }
            if (assignRoles.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)) {
                assignRoles.add(RoleConsts.USER_ROLE_INSPECTIOR);
            }
            assignRoles.removeAll(roleIds);
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
                egpUserRoleDtos.add(egpUserRoleDto);
            }
            intranetUserService.assignRole(orgUserRoleDtos);
            intranetUserService.createEgpRoles(egpUserRoleDtos);

            //add group
            List<UserGroupCorrelationDto> userGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
            groupIds.forEach((groupId, isLeader) -> {
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(userAccId);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                userGroupCorrelationDto.setGroupId(groupId);
                userGroupCorrelationDto.setIsLeadForGroup(isLeader);
                userGroupCorrelationDto.setAuditTrailDto(auditTrailDto);
                userGroupCorrelationDtos.add(userGroupCorrelationDto);
            });
            if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtos)) {
                List<String> groupIdsSearch = IaisCommonUtils.genNewArrayList();
                ListIterator<UserGroupCorrelationDto> iterator = userGroupCorrelationDtos.listIterator();
                while (iterator.hasNext()) {
                    UserGroupCorrelationDto dto = iterator.next();
                    Integer isLeadForGroup = dto.getIsLeadForGroup();
                    String groupId = dto.getGroupId();
                    groupIdsSearch.clear();
                    groupIdsSearch.add(groupId);
                    if (isLeadForGroup == 1) {
                        List<UserGroupCorrelationDto> userGroupCorrelationDtosTemp = intranetUserService.getUserGroupCorrelationDtos(userAccId, groupIdsSearch, 0);
                        if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtosTemp)) {
                            for (UserGroupCorrelationDto groupDto : userGroupCorrelationDtosTemp) {
                                String id = groupDto.getId();
                                dto.setId(id);
                                dto.setIsLeadForGroup(1);
                            }
                        }
                    } else {
                        List<UserGroupCorrelationDto> userGroupCorrelationDtosTemp = intranetUserService.getUserGroupCorrelationDtos(userAccId, groupIdsSearch, 1);
                        if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtosTemp)) {
                            iterator.remove();
                        }
                    }
                }
                if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtos)) {
                    intranetUserService.addUserGroupId(userGroupCorrelationDtos);
                }
            }
        }
        List<String> existRoles = (List<String>) ParamUtil.getSessionAttr(bpc.request, "orgUserRoleDtos");
        if (removeRoles != null) {
            List<String> removeRoleIds = IaisCommonUtils.genNewArrayList();
            for (String removeRole : removeRoles) {
                String maskRoleId = MaskUtil.unMaskValue("maskRoleId", removeRole);
                removeRoleIds.add(maskRoleId);
            }
            List<String> removeRoleNames = IaisCommonUtils.genNewArrayList();
            List<OrgUserRoleDto> orgUserRoleDtos = intranetUserService.getOrgUserRoleDtoById(removeRoleIds);
            if (!IaisCommonUtils.isEmpty(orgUserRoleDtos)) {
                List<String> removeRole = IaisCommonUtils.genNewArrayList();
                for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                    String roleName = orgUserRoleDto.getRoleName();
                    removeRole.add(roleName);
                }
                for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                    String roleName = orgUserRoleDto.getRoleName();
                    if (!roleName.contains("LEAD")) {
                        String roleLeader = roleName + "_LEAD";
                        if (existRoles.contains(roleLeader) && !removeRole.contains(roleLeader)) {
                            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                            ParamUtil.setRequestAttr(bpc.request, "userAccId", userAccId);
                            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                            errorMap.put("roleLeader", "USER_ERR019");
                            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                            return;
                        }
                    }
                }
                List<UserGroupCorrelationDto> userGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                Map<String, List<String>> roleIdGrpIdMap = intranetUserService.getRoleIdGrpIdMap();
                for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                    orgUserRoleDto.setAuditTrailDto(auditTrailDto);
                    String roleName = orgUserRoleDto.getRoleName();
                    //AO1
                    removeRoleNames.add(roleName);
                    List<String> list = roleIdGrpIdMap.get(roleName);
                    if (roleName.contains("LEAD")) {
                        List<UserGroupCorrelationDto> userGroupCorrelationDtosTemp = intranetUserService.getUserGroupCorrelationDtos(userAccId, list, 1);
                        if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtosTemp)) {
                            for (UserGroupCorrelationDto dto : userGroupCorrelationDtosTemp) {
                                dto.setIsLeadForGroup(0);
                            }
                            userGroupCorrelationDtos.addAll(userGroupCorrelationDtosTemp);
                        }
                    } else {
                        List<UserGroupCorrelationDto> userGroupCorrelationDtosTemp = intranetUserService.getUserGroupCorrelationDtos(userAccId, list, 0);
                        if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtosTemp)) {
                            for (UserGroupCorrelationDto dto : userGroupCorrelationDtosTemp) {
                                dto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                            }
                            userGroupCorrelationDtos.addAll(userGroupCorrelationDtosTemp);
                        }
                        List<UserGroupCorrelationDto> userGroupCorrelationDtosTemp1 = intranetUserService.getUserGroupCorrelationDtos(userAccId, list, 1);
                        if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtosTemp1)) {
                            for (UserGroupCorrelationDto dto : userGroupCorrelationDtosTemp1) {
                                dto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                            }
                            userGroupCorrelationDtos.addAll(userGroupCorrelationDtosTemp1);
                        }
                    }
                }

                Boolean isSuccessEgp = intranetUserService.removeEgpRoles(AppConsts.HALP_EGP_DOMAIN, orgUserDto.getUserId(), removeRoleNames);
                if(isSuccessEgp){
                    intranetUserService.removeRole(orgUserRoleDtos);
                    //remove group
                    if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtos)) {
                        intranetUserService.addUserGroupId(userGroupCorrelationDtos);
                    }
                }
            }
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
        if (!IaisCommonUtils.isEmpty(existUsersNew)) {
            ParamUtil.setRequestAttr(bpc.request, "existUsersNew", existUsersNew);
            ParamUtil.setRequestAttr(bpc.request, "existUsersOld", existUsersOld);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
    }

    private void transform(OrgUserDto orgUserDto) {
        String branchUnit = orgUserDto.getBranchUnit();
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
            if (!IaisCommonUtils.isEmpty(orgUserDtosNew)) {
                intranetUserService.createIntranetUsers(orgUserDtosNew);
                for (OrgUserDto orgUserDto : orgUserDtosNew) {
                    saveEgpUser(orgUserDto);
                }
            }
            //update
            if (!IaisCommonUtils.isEmpty(orgUserDtosOld)) {
                for (OrgUserDto orgUserDto : orgUserDtos) {
                    orgUserDto.setAuditTrailDto(auditTrailDto);
                    String id = orgUserDto.getId();
                    if (!StringUtil.isEmpty(id)) {
                        intranetUserService.updateOrgUser(orgUserDto);
                        editEgpUser(orgUserDto);
                    }
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

    public void importUserRole(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the importUserRole start ...."));
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile sessionFile = (CommonsMultipartFile) request.getFile("userRoleUpload");
        int userFileSize = (int) ParamUtil.getSessionAttr(bpc.request, "userFileSize");
        File file = File.createTempFile("temp", "xml");
        File xmlFile = inputStreamToFile(sessionFile.getInputStream(), file);
        //validate xml file
        List<EgpUserRoleDto> egpUserRoleDtos = IaisCommonUtils.genNewArrayList();
        Map<String, String> fileErrorMap = intranetUserService.importRoleXmlValidation(xmlFile, userFileSize, sessionFile, egpUserRoleDtos);
        if (fileErrorMap != null && fileErrorMap.size() > 0) {
            ParamUtil.setRequestAttr(bpc.request, "ackSuccessFlag", AppConsts.FAIL);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(fileErrorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        } else {
            egpUserRoleDtos = intranetUserService.importRoleXml(xmlFile);
            ParamUtil.setRequestAttr(bpc.request, "ackSuccessFlag", AppConsts.SUCCESS);
        }
        ParamUtil.setRequestAttr(bpc.request, "egpUserRoleDtos", egpUserRoleDtos);
    }

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

    public void changeStatus(BaseProcessClass bpc) {

    }

    public void saveStatus(BaseProcessClass bpc) {
//        String userId = ParamUtil.getRequestString(bpc.request, "statusUserId");
//        ParamUtil.setRequestAttr(bpc.request, "statusUserId", userId);
//        String actionType = ParamUtil.getString(bpc.request, "crud_action_type");
//        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
//        if ("back".equals(actionType)) {
//            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
//            return;
//        }
//        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
//        OrgUserDto orgUserDto;
//        ClientUser clientUser;
//        if (!StringUtil.isEmpty(userId)) {
//            clientUser = intranetUserService.getUserByIdentifier(userId, AppConsts.HALP_EGP_DOMAIN);
//            orgUserDto = intranetUserService.findIntranetUserByUserId(userId);
//            if (clientUser == null || orgUserDto == null) {
//                errorMap.put("userId", "USER_ERR012");
//                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//                return;
//            } else {
//                String status = orgUserDto.getStatus();
//                if (IntranetUserConstant.DEACTIVATE.equals(actionType) && IntranetUserConstant.COMMON_STATUS_DEACTIVATED.equals(status)) {
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//                    errorMap.put("userId", "USER_ERR004");
//                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//                    return;
//                } else if (IntranetUserConstant.DEACTIVATE.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_DEACTIVATED.equals(status) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
//                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_DEACTIVATED);
//                    orgUserDto.setAvailable(Boolean.FALSE);
//                    orgUserDto.setAuditTrailDto(auditTrailDto);
//                    String endDate = DateFormatUtils.format(new Date(), "dd/MM/yyyy");
//                    orgUserDto.setEndDateStr(endDate);
//                    intranetUserService.updateOrgUser(orgUserDto);
//                    clientUser.setAccountStatus(ClientUser.STATUS_INACTIVE);
//                    intranetUserService.updateEgpUser(clientUser);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
//                    return;
//                } else if (IntranetUserConstant.REDEACTIVATE.equals(actionType) && IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status)) {
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//                    errorMap.put("userId", "USER_ERR005");
//                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//                    return;
//                } else if (IntranetUserConstant.REDEACTIVATE.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
//                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
//                    orgUserDto.setAuditTrailDto(auditTrailDto);
//                    orgUserDto.setAvailable(Boolean.TRUE);
//                    intranetUserService.updateOrgUser(orgUserDto);
//                    clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
//                    intranetUserService.updateEgpUser(clientUser);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
//                    return;
//                } else if (IntranetUserConstant.TERMINATE.equals(actionType) && IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//                    errorMap.put("userId", "USER_ERR014");
//                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//                    return;
//                } else if (IntranetUserConstant.TERMINATE.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
//                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_TERMINATED);
//                    orgUserDto.setAvailable(Boolean.FALSE);
//                    orgUserDto.setAuditTrailDto(auditTrailDto);
//                    intranetUserService.updateOrgUser(orgUserDto);
//                    clientUser.setAccountStatus(ClientUser.STATUS_TERMINATED);
//                    intranetUserService.updateEgpUser(clientUser);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
//                    return;
//                } else if (IntranetUserConstant.UNLOCK.equals(actionType) && IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status)) {
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//                    errorMap.put("userId", "USER_ERR005");
//                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//                    return;
//                } else if (IntranetUserConstant.UNLOCK.equals(actionType) && !IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status) && !IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
//                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
//                    orgUserDto.setAvailable(Boolean.TRUE);
//                    orgUserDto.setAuditTrailDto(auditTrailDto);
//                    intranetUserService.updateOrgUser(orgUserDto);
//                    clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
//                    intranetUserService.updateEgpUser(clientUser);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
//                    return;
//                } else if (IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status)) {
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//                    errorMap.put("userId", "USER_ERR016");
//                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//                    return;
//                }
//            }
//        } else {
//            errorMap.put("userId", MessageUtil.replaceMessage("GENERAL_ERR0006", "User ID", "field"));
//            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
//            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
//            return;
//        }
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
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, IntranetUserConstant.SEARCH_PARAM, OrgUserQueryDto.class.getName(),
                IntranetUserConstant.INTRANET_USER_SORT_COLUMN, SearchParam.ASCENDING, true);
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
        if (!StringUtil.isEmpty(role)) {
            searchParam.addFilter("role", role, true);
        }
        searchParam.setPageNo(1);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery", searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        ParamUtil.setSessionAttr(request, IntranetUserConstant.SEARCH_PARAM, searchParam);
        ParamUtil.setRequestAttr(request, IntranetUserConstant.SEARCH_RESULT, searchResult);

    }

    public void doSorting(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, IntranetUserConstant.SEARCH_PARAM);
        HalpSearchResultHelper.doSort(request, searchParam);
    }

    public void doPaging(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, IntranetUserConstant.SEARCH_PARAM);
        HalpSearchResultHelper.doPage(request, searchParam);
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
        String available = ParamUtil.getRequestString(request, "available");
        if (StringUtil.isEmpty(available)) {
            orgUserDto.setAvailable(Boolean.FALSE);
        } else {
            orgUserDto.setAvailable(Boolean.TRUE);
        }
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
        Date today = new Date();
        orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
        if(startDate != null && startDate.after(today)){
            orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_DEACTIVATED);
        }
        orgUserDto.setUserDomain(IntranetUserConstant.DOMAIN_INTRANET);
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
        String status = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_STATUS);
        String lastName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_LASTNAME);
        String division = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_REMARKS);
        String organization = ParamUtil.getRequestString(bpc.request, "organization");
        String available = ParamUtil.getRequestString(bpc.request, "available");
        if (StringUtil.isEmpty(available)) {
            orgUserDto.setAvailable(Boolean.FALSE);
        } else {
            orgUserDto.setAvailable(Boolean.TRUE);
        }
        orgUserDto.setOrganization(organization);
        orgUserDto.setFirstName(firstName);
        orgUserDto.setStatus(status);
        if(!IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status)){
            orgUserDto.setAvailable(Boolean.FALSE);
        }
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
            log.debug(StringUtil.changeForLog("===========egpUser can not found============"));
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

    private List<OrgUserDto> importXML(File file) {
        List list = null;
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        try {
            SAXReader saxReader = new SAXReader();//NOSONAR
            Document document = saxReader.read(file);
            //root
            Element root = document.getRootElement();
            //ele
            list = root.elements();
            if (list != null) {
                Element elementCheck = (Element)list.get(0);
                elementCheck.element("displayName").getText();
            }
        } catch (Exception e) {
            OrgUserDto orgUserDto = new OrgUserDto();
            orgUserDto.setXmlError("error");
            orgUserDtos.add(orgUserDto);
            log.error(e.getMessage(), e);
        }
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                OrgUserDto orgUserDto = new OrgUserDto();
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
                    orgUserDto.setXmlError("error");
                    orgUserDtos.add(orgUserDto);
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
        }

        return orgUserDtos;
    }

    private List<String> valiantDto(OrgUserDto orgUserDto) {
        List<String> errors = IaisCommonUtils.genNewArrayList();
        String xmlError = orgUserDto.getXmlError();
        if (!StringUtil.isEmpty(xmlError)) {
            if("errorName".equals(xmlError)){
                errors.add("The filename should not be more than 100 characters");
            }else {
                errors.add("Please upload correct format");
            }
            return errors;
        }
        String userId = orgUserDto.getUserId();
        if (!StringUtil.isEmpty(userId)) {
            if (!userId.matches("^[A-Za-z0-9]+$")) {
                String error = "Please enter alphanumeric character.";
                errors.add(error);
            }
        } else {
            String error = "User ID is a mandatory field";
            errors.add(error);
        }
        String displayName = orgUserDto.getDisplayName();
        if (StringUtil.isEmpty(displayName)) {
            String error = "Display Name is a mandatory field";
            errors.add(error);
        }

        Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
        String startDateStr = orgUserDto.getStartDateStr();
        if (StringUtil.isEmpty(startDateStr)) {
            String error = "Account Activation Start is a mandatory field";
            errors.add(error);
        }
        if (!StringUtil.isEmpty(startDateStr) && accountActivateDatetime == null) {
            String error = "Please key in a valid Account Activation Start date";
            errors.add(error);
        }
        Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
        String endDateStr = orgUserDto.getEndDateStr();
        if (StringUtil.isEmpty(endDateStr)) {
            String error = "Account Activation End is a mandatory field";
            errors.add(error);
        }
        if (!StringUtil.isEmpty(endDateStr) && accountDeactivateDatetime == null) {
            String error = "Please key in a valid Account Activation End date";
            errors.add(error);
        }
        if (accountDeactivateDatetime != null && accountActivateDatetime != null) {
            boolean after = accountDeactivateDatetime.after(accountActivateDatetime);
            if (!after) {
                String error = "Account Activation Start cannot be later than Account Activation End";
                errors.add(error);
            }
        }
        String lastName = orgUserDto.getLastName();
        if (StringUtil.isEmpty(lastName)) {
            String error = "Last Name is a mandatory field";
            errors.add(error);
        }
        String firstName = orgUserDto.getFirstName();
        if (StringUtil.isEmpty(firstName)) {
            String error = "First Name is a mandatory field";
            errors.add(error);
        }
        String email = orgUserDto.getEmail();
        if (StringUtil.isEmpty(email)) {
            String error = "Email is a mandatory field";
            errors.add(error);
        } else {
            if (!ValidationUtils.isEmail(email)) {
                String error = "Please key in a valid email address";
                errors.add(error);
            }
        }
        String mobileNo = orgUserDto.getMobileNo();
        if (!StringUtil.isEmpty(mobileNo)) {
            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                errors.add("Please key in a valid mobile number");
            }
        }
        String officeTelNo = orgUserDto.getOfficeTelNo();
        if (!StringUtil.isEmpty(officeTelNo)) {
            if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                errors.add("Please key in a valid office number");
            }
        }
        return errors;
    }


    private List<SelectOption> getStatusOption() {
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("CMSTAT001", "Active");
        SelectOption so2 = new SelectOption("OUSTAT005", "Deactivated");
        SelectOption so3 = new SelectOption("OUSTAT001", "Expired");
        SelectOption so4 = new SelectOption("OUSTAT002", "Locked");
        SelectOption so5 = new SelectOption("OUSTAT004", "Terminated");
        result.add(so1);
        result.add(so2);
        result.add(so3);
        result.add(so4);
        result.add(so5);
        return result;
    }

    private List<SelectOption> getRoleOption() {
        List<Role> rolesByDomain = intranetUserService.getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        for (Role role : rolesByDomain) {
            String roleName = role.getName();
            String roleId = role.getId();
            SelectOption so = new SelectOption(roleId, roleName);
            result.add(so);
        }
        return result;
    }

    private List<SelectOption> getprivilegeOption() {
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
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
