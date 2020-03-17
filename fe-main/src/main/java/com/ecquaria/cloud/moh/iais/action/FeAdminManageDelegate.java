package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.impl.OrgUserManageServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
    public static String organizationId = "D8FF6F4E-5C30-EA11-BE78-000C29D29DB0";
    private static final String ACTIVE ="CMSTAT001";
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
        SearchParam searchParam = new SearchParam(FeAdminQueryDto.class.getName());
        searchParam.setSort("ID", SearchParam.ASCENDING);
        searchParam.addFilter("orgid",organizationId,true);
        searchParam.addFilter("roleid", RoleConsts.USER_ROLE_ORG_ADMIN,true);
        QueryHelp.setMainSql("interInboxQuery", "feAdminList",searchParam);
        SearchResult<FeAdminQueryDto> feAdminQueryDtoSearchResult = orgUserManageService.getFeAdminList(searchParam);
        for (FeAdminQueryDto item:feAdminQueryDtoSearchResult.getRows()
             ) {
            item.setSalutation(MasterCodeUtil.getCodeDesc(item.getSalutation()));
            if(RoleConsts.USER_ROLE_ORG_ADMIN.equals(item.getRoleId())){
                item.setRoleId(AppConsts.TRUE);
            }else{
                item.setRoleId(AppConsts.FALSE);
            }
            if(ACTIVE.equals(item.getStatus())){
                item.setStatus("1");
            }else{
                item.setStatus("0");
            }

        }
        CrudHelper.doPaging(searchParam,bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "feAdmin",feAdminQueryDtoSearchResult);
        ParamUtil.setRequestAttr(bpc.request, "feAdminSearchParam",searchParam);

    }

    /**
     * AutoStep: validation
     *
     * @param bpc
     * @throws
     */
    public void createValidation(BaseProcessClass bpc) {
        FeAdminDto accountDto = new FeAdminDto();
        String email = ParamUtil.getString(bpc.request,"email");
        String uenNo = ParamUtil.getString(bpc.request,"uenNo");
        String idNo = ParamUtil.getString(bpc.request,"idNo");
        String salutation = ParamUtil.getString(bpc.request,"salutation");
        String firstName = ParamUtil.getString(bpc.request,"firstName");
        String lastName = ParamUtil.getString(bpc.request,"lastName");
        String role = ParamUtil.getString(bpc.request,"role");
        accountDto.setEmailAddr(email);
        accountDto.setFirstName(firstName);
        accountDto.setLastName(lastName);
        accountDto.setIdNo(idNo);
        accountDto.setUenNo(uenNo);
        accountDto.setSalutation(salutation);
        accountDto.setIsAdmin(role);
        accountDto.setOrgId(organizationId);
        ParamUtil.setRequestAttr(bpc.request, "account", accountDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(accountDto, "create");
        if (validationResult.isHasErrors()){
            log.error("****************Error");
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, "email", email);
            ParamUtil.setRequestAttr(bpc.request, "idNo", idNo);
            ParamUtil.setRequestAttr(bpc.request, "salutation", salutation);
            ParamUtil.setRequestAttr(bpc.request, "firstName", firstName);
            ParamUtil.setRequestAttr(bpc.request, "lastName", lastName);
            ParamUtil.setRequestAttr(bpc.request, "role", role);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.FALSE);
        }else{
            Map<String,String> successMap = new HashMap<>();
            successMap.put("save","suceess");
            orgUserManageService.addAdminAccount(accountDto);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
        }
    }

    public void create(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************create"));
        OrganizationDto organizationDto = orgUserManageService.getOrganizationById(organizationId);
        String uenNo = organizationDto.getUenNo();
        ParamUtil.setSessionAttr(bpc.request,"uenNo", uenNo);
    }

    public void edit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************edit"));
        String userId = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);

        List<SelectOption> mcStatusSelectList = IaisCommonUtils.genNewArrayList();
        mcStatusSelectList.add(new SelectOption("NRIC", "NRIC"));
        mcStatusSelectList.add(new SelectOption("FIN", "FIN"));
        ParamUtil.setRequestAttr(bpc.request, "mcStatusSelectList", mcStatusSelectList);


        FeUserDto feUserDto = orgUserManageService.getUserAccount(userId);
        String sulationSelect = "";
        String sulationSelectDesc = "";
        String idTypeSelect = "";
        if(StringUtil.isEmpty(feUserDto.getSalutation())){
            sulationSelect = "Please Select";
        }else{
            sulationSelectDesc = MasterCodeUtil.getCodeDesc(feUserDto.getSalutation());
            sulationSelect = feUserDto.getSalutation();
        }
        if(StringUtil.isEmpty(feUserDto.getIdType())){
            idTypeSelect = "Please Select";
        }else{
            idTypeSelect = feUserDto.getIdType();
        }
        ParamUtil.setRequestAttr(bpc.request,"user",feUserDto);
        ParamUtil.setRequestAttr(bpc.request,"sulationSelect",sulationSelect);
        ParamUtil.setRequestAttr(bpc.request,"salutationDesc",sulationSelectDesc);
        ParamUtil.setRequestAttr(bpc.request,"idTypeSelect",idTypeSelect);
    }


    /**
     * AutoStep: InsertDatabase
     *
     * @param bpc
     * @throws
     */
    public void editValidation(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
        String name = ParamUtil.getString(bpc.request,"name");
        String salutation = ParamUtil.getString(bpc.request,"salutation");
        String idType = ParamUtil.getString(bpc.request,"idType");
        String idNo = ParamUtil.getString(bpc.request,"idNo");
        String designation = ParamUtil.getString(bpc.request,"designation");
        String mobileNo = ParamUtil.getString(bpc.request,"mobileNo");
        String officeNo = ParamUtil.getString(bpc.request,"officeNo");
        String email = ParamUtil.getString(bpc.request,"email");
        String id = ParamUtil.getString(bpc.request,"id");
        String orgId = ParamUtil.getString(bpc.request,"orgId");
        String userId = ParamUtil.getString(bpc.request,"userId");
        String firstName = ParamUtil.getString(bpc.request,"firstName");
        String lastName = ParamUtil.getString(bpc.request,"lastName");

        FeUserDto feUserDto = new FeUserDto();
        feUserDto.setName(name);
        if(StringUtil.isEmpty(salutation)){
            feUserDto.setSalutation( ParamUtil.getString(bpc.request,"firstsolutationvalue"));
        }else{
            feUserDto.setSalutation(salutation);
        }
        if(StringUtil.isEmpty(idType)){
            feUserDto.setIdType( ParamUtil.getString(bpc.request,"firstidType"));
        }else{
            feUserDto.setIdType(idType);
        }
        feUserDto.setId(id);
        feUserDto.setUserId(userId);
        feUserDto.setOrgId(orgId);
        feUserDto.setIdNo(idNo);
        feUserDto.setDesignation(designation);
        feUserDto.setMobileNo(mobileNo);
        feUserDto.setOfficeNo(officeNo);
        feUserDto.setEmailAddr(email);
        feUserDto.setLastName(lastName);
        feUserDto.setFirstName(firstName);

        ParamUtil.setRequestAttr(bpc.request, "user", feUserDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "edit");

        if (validationResult.isHasErrors()){
            log.error("****************Error");
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, "email", email);
            ParamUtil.setRequestAttr(bpc.request, "mobileNo", mobileNo);
            ParamUtil.setRequestAttr(bpc.request, "officeNo", officeNo);
            ParamUtil.setRequestAttr(bpc.request, "idNo", idNo);
            ParamUtil.setRequestAttr(bpc.request, "idTypeSelect", feUserDto.getIdType());
            String salutationDesc = MasterCodeUtil.getCodeDesc(feUserDto.getSalutation());
            ParamUtil.setRequestAttr(bpc.request, "salutationDesc", salutationDesc);
            ParamUtil.setRequestAttr(bpc.request, "sulationSelect", feUserDto.getSalutation());
            ParamUtil.setRequestAttr(bpc.request, "name", name);
            ParamUtil.setRequestAttr(bpc.request, "designation", designation);

            List<SelectOption> mcStatusSelectList = IaisCommonUtils.genNewArrayList();
            mcStatusSelectList.add(new SelectOption("NRIC", "NRIC"));
            mcStatusSelectList.add(new SelectOption("FIN", "FIN"));
            ParamUtil.setRequestAttr(bpc.request, "mcStatusSelectList", mcStatusSelectList);

            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.FALSE);
        }else{
            Map<String,String> successMap = new HashMap<>();
            successMap.put("save","suceess");
            orgUserManageService.editUserAccount(feUserDto);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
        }
    }


}
