package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "MassEmailDelegator")
@Slf4j
public class MassEmailDelegator {

    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";
    private static final String EMAIL = "Email";
    private static final String SMS = "SMS";
    private static final String SEARCHPARAM = "massEmailSearchParam";
    @Autowired
    DistributionListService distributionListService;

    public void start(BaseProcessClass bpc){
        SearchParam searchParam = new SearchParam(DistributionListDto.class.getName());
        searchParam.setPageSize(10);
        searchParam.setPageNo(1);
        searchParam.setSort("CREATED_DT", SearchParam.DESCENDING);
        searchParam.addFilter("status", AppConsts.COMMON_STATUS_ACTIVE,true);
        AuditTrailHelper.auditFunction("MassEmail", "MassEmailDelegator");
        setSearchparam(bpc,searchParam);
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "queryMassDistributionList",searchParam);
        SearchResult<DistributionListDto> searchResult = distributionListService.distributionList(searchParam);
        setServiceSelect(bpc);
        searchRole(bpc);
        ParamUtil.setRequestAttr(bpc.request,"distributionSearchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"distributionSearchParam",searchParam);
    }

    /**
     * create
     * @param bpc
     */
    public void create(BaseProcessClass bpc){
        setServiceSelect(bpc);
        ParamUtil.setSessionAttr(bpc.request,"distribution",null);
        setModeSelection(bpc);
        String service =  ParamUtil.getString(bpc.request, "service");
        if(service == null){
            setRoleSelection(bpc,"");
        }else{
            setRoleSelection(bpc,HcsaServiceCacheHelper.getServiceByCode(service).getId());
        }

    }

    /**
     * doPrepare
     * @param bpc
     */
    public void save(BaseProcessClass bpc){
        String mode = ParamUtil.getString(bpc.request, "mode");
        String id =  ParamUtil.getMaskedString(bpc.request, "distributionId");
        String name =  ParamUtil.getString(bpc.request, "name");
        String service =  ParamUtil.getString(bpc.request, "service");
        String role =  ParamUtil.getString(bpc.request, "role");
        String email = ParamUtil.getString(bpc.request, "email");

        DistributionListWebDto distributionListDto = new DistributionListWebDto();
        if(email != null){
            List<String> rnemaillist = Arrays.asList(email.split("\r\n"));
            List<String> commaemaillist = Arrays.asList(email.split(","));
            if(rnemaillist.size() > commaemaillist.size() ){
                distributionListDto.setEmailAddress(rnemaillist);
            }else{
                distributionListDto.setEmailAddress(commaemaillist);
            }
        }
        if(id != null)
        {
            distributionListDto.setId(id);
        }
        distributionListDto.setService(service);
        distributionListDto.setDisname(name);
        distributionListDto.setMode(mode);
        distributionListDto.setRole(role);
        distributionListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        ParamUtil.setSessionAttr(bpc.request,"distribution",distributionListDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(distributionListDto, "save");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            String emailAddress = StringUtils.join(distributionListDto.getEmailAddress(),"\r\n");
            ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
            ParamUtil.setRequestAttr(bpc.request,"distribution",distributionListDto);
        }else{
            distributionListService.saveDistributionList(distributionListDto);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
        }

    }

    /**
     * delete
     * @param bpc
     */
    public void delete(BaseProcessClass bpc){
        String[] checkboxlist =  ParamUtil.getMaskedStrings(bpc.request, "checkboxlist");
        if(checkboxlist != null && checkboxlist.length > 0){
            List<String> list = Arrays.asList(checkboxlist);
            distributionListService.deleteDistributionList(list);
        }

    }

    /**
     * search
     * @param bpc
     */
    public void search(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        String distributionName = ParamUtil.getRequestString(bpc.request,"distributionName");
        String role = ParamUtil.getString(bpc.request, "role");
        String service = ParamUtil.getString(bpc.request, "service");
        searchParam.getParams().clear();
        searchParam.getFilters().clear();
        searchParam.setPageNo(1);
        searchParam.setPageSize(10);
        searchParam.setSort("CREATED_DT", SearchParam.DESCENDING);
        searchParam.addFilter("status", AppConsts.COMMON_STATUS_ACTIVE,true);
        if(!StringUtil.isEmpty(distributionName)){
            searchParam.addFilter("description", "%" + distributionName + "%",true);
        }else{
            distributionName = null;
        }
        if(!StringUtil.isEmpty(role)){
            searchParam.addFilter("recipients",  "%" +role + "%",true);
        }else{
            role = null;
        }
        if(!StringUtil.isEmpty(service)){
            searchParam.addFilter("service",  service,true);
        }else{
            service = null;
        }
        setServiceSelect(bpc);
        setSearchparam(bpc,searchParam);
        ParamUtil.setRequestAttr(bpc.request,"distributionName",distributionName);
        ParamUtil.setRequestAttr(bpc.request,"role",role);
        ParamUtil.setRequestAttr(bpc.request,"service",service);
    }

    /**
     * edit
     * @param bpc
     */
    public void edit(BaseProcessClass bpc){
        String id =  ParamUtil.getMaskedString(bpc.request, "editDistribution");
        DistributionListWebDto distributionListDto = distributionListService.getDistributionListById(id);
        setServiceSelect(bpc);
        setModeSelection(bpc);
        setRoleSelection(bpc, HcsaServiceCacheHelper.getServiceByCode(distributionListDto.getService()).getId());
        distributionListDto.setId(MaskUtil.maskValue("distributionId",distributionListDto.getId()));
        String emailAddress = StringUtils.join(distributionListDto.getEmailAddress(),"\n");
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
        ParamUtil.setRequestAttr(bpc.request,"distribution",distributionListDto);
        ParamUtil.setRequestAttr(bpc.request, "firstOption", distributionListDto.getService());
    }

    private void setServiceSelect(BaseProcessClass bpc){
        List<HcsaServiceDto> hcsaServiceDtoList = distributionListService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }

        List<HcsaServiceDto> baseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        List<HcsaServiceDto> specifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        baseService.addAll(specifiedService);
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceDto item : baseService) {
            selectOptionArrayList.add(new SelectOption(item.getSvcCode(),item.getSvcName()));
        }
        ParamUtil.setRequestAttr(bpc.request, "serviceSelection", (Serializable) selectOptionArrayList);
    }

    private void setModeSelection(BaseProcessClass bpc){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption(EMAIL,EMAIL));
        selectOptions.add(new SelectOption(SMS,SMS));
        ParamUtil.setRequestAttr(bpc.request, "modeSelection",  (Serializable) selectOptions);
    }

    private void setRoleSelection(BaseProcessClass bpc, String service){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(service != null && !StringUtils.isEmpty(service)){
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = distributionListService.roleByServiceId(service,AppConsts.COMMON_STATUS_ACTIVE);
            for (HcsaSvcPersonnelDto item:hcsaSvcPersonnelDtoList
            ) {
                selectOptions.add(new SelectOption(item.getPsnType(),roleName(item.getPsnType())));
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "roleSelection",  (Serializable) selectOptions);
    }

    private void searchRole(BaseProcessClass bpc){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,"CGO"));
        selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_PO,"Principal Officer"));
        selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO,"Deputy Principal Officer"));
        selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,"Licensee"));
        selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_AP,"Authorised Person"));
        selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT,"MedAlert"));
        ParamUtil.setRequestAttr(bpc.request, "roleSelection",  (Serializable) selectOptions);
    }

    private String roleName(String roleAbbreviation){
        String roleName = "";
        switch (roleAbbreviation){
            case ApplicationConsts.PERSONNEL_PSN_TYPE_CGO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_PO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_PRINCIPAL_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_DPO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_DEPUTY_PRINCIPAL_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_MAP:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT;
                break;
                default:
                    roleName = roleAbbreviation;
                    break;
        }
        return roleName;
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return (SearchParam) ParamUtil.getSessionAttr(bpc.request,SEARCHPARAM);
    }

    private void setSearchparam(BaseProcessClass bpc,SearchParam searchParam){
        ParamUtil.setSessionAttr(bpc.request,SEARCHPARAM,searchParam);
    }
}
