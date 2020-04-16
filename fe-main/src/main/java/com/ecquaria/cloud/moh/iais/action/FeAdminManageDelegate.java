package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
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
        SearchParam searchParam = new SearchParam(FeUserQueryDto.class.getName());
        searchParam.setSort("ID", SearchParam.ASCENDING);
        searchParam.addFilter("orgid",organizationId,true);
        searchParam.addFilter("roleid", RoleConsts.USER_ROLE_ORG_ADMIN,true);
        QueryHelp.setMainSql("interInboxQuery", "feUserList",searchParam);
        SearchResult<FeUserQueryDto> feAdminQueryDtoSearchResult = orgUserManageService.getFeUserList(searchParam);
        for (FeUserQueryDto item:feAdminQueryDtoSearchResult.getRows()
             ) {
            item.setSalutation(MasterCodeUtil.getCodeDesc(item.getSalutation()));
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
        String action = ParamUtil.getString(bpc.request,"action");
        if("cancel".equals(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
        }else{
            FeUserDto accountDto = new FeUserDto();
            String email = ParamUtil.getString(bpc.request,"email");
            String uenNo = ParamUtil.getString(bpc.request,"uenNo");
            String idNo = ParamUtil.getString(bpc.request,"idNo");
            String salutation = ParamUtil.getString(bpc.request,"salutation");
            String firstName = ParamUtil.getString(bpc.request,"firstName");
            String lastName = ParamUtil.getString(bpc.request,"lastName");
            String role = ParamUtil.getString(bpc.request,"role");
            accountDto.setEmail(email);
            accountDto.setFirstName(firstName);
            accountDto.setLastName(lastName);
            accountDto.setIdentityNo(idNo);
            accountDto.setUenNo(uenNo);
            accountDto.setIdType("NRIC");
            accountDto.setUserId(idNo);
            accountDto.setSalutation(salutation);
            accountDto.setUserRole(role);
            accountDto.setOrgId(organizationId);
            accountDto.setAvailable(true);
            accountDto.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            accountDto.setAvailable(true);
            Date now = new Date();
            accountDto.setAccountActivateDatetime(now);
            ParamUtil.setSessionAttr(bpc.request, "user", accountDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(accountDto, "create");
            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.FALSE);
            }else{
                Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                successMap.put("save","suceess");
                orgUserManageService.addAdminAccount(accountDto);
                orgUserManageService.saveEgpUser(accountDto);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
            }
        }

    }

    public void create(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************create"));
        OrganizationDto organizationDto = orgUserManageService.getOrganizationById(organizationId);
        FeUserDto accountDto = new FeUserDto();
        accountDto.setUenNo(organizationDto.getUenNo());
        ParamUtil.setSessionAttr(bpc.request,"user", accountDto);
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
        ParamUtil.setSessionAttr(bpc.request,"user",feUserDto);
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
        String action = ParamUtil.getString(bpc.request,"action");
        if("cancel".equals(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
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

            FeUserDto feUserDto = (FeUserDto) ParamUtil.getSessionAttr(bpc.request, "user");
            feUserDto.setDisplayName(name);
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
            feUserDto.setIdentityNo(idNo);
            feUserDto.setDesignation(designation);
            feUserDto.setMobileNo(mobileNo);
            feUserDto.setOfficeTelNo(officeNo);
            feUserDto.setEmail(email);

            ParamUtil.setSessionAttr(bpc.request, "user", feUserDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(feUserDto, "edit");

            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                List<SelectOption> mcStatusSelectList = IaisCommonUtils.genNewArrayList();
                mcStatusSelectList.add(new SelectOption("NRIC", "NRIC"));
                mcStatusSelectList.add(new SelectOption("FIN", "FIN"));
                ParamUtil.setRequestAttr(bpc.request, "mcStatusSelectList", mcStatusSelectList);

                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.FALSE);
            }else{
                Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                successMap.put("save","suceess");
                orgUserManageService.editUserAccount(feUserDto);
                orgUserManageService.updateEgpUser(feUserDto);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID, AppConsts.TRUE);
            }
        }
    }


}
