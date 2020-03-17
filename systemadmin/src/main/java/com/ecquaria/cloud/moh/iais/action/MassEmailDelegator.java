package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "MassEmailDelegator")
@Slf4j
public class MassEmailDelegator {

    private SearchParam searchParam;
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";
    private static final String EMAIL = "email";
    private static final String SMS = "sms";
    private static final String BOTH = "both";
    @Autowired
    DistributionListService distributionListService;

    public void start(BaseProcessClass bpc){
        searchParam = new SearchParam(DistributionListDto.class.getName());
        searchParam.setPageSize(10);
        searchParam.setPageNo(1);
        searchParam.setSort("ID", SearchParam.ASCENDING);
        AuditTrailHelper.auditFunction("distrribution", "distrribution");

        ParamUtil.setRequestAttr(bpc.request, "firstOption", "Please select");
        ParamUtil.setRequestAttr(bpc.request, "firstValue", " ");
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        searchParam.addFilter("status", AppConsts.COMMON_STATUS_ACTIVE,true);
        searchParam.setSort("CREATED_DT", SearchParam.DESCENDING);
        CrudHelper.doPaging(searchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "queryMassDistributionList",searchParam);
        SearchResult<DistributionListDto> searchResult = distributionListService.distributionList(searchParam);
        ParamUtil.setRequestAttr(bpc.request,"distributionSearchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"distributionSearchParam",searchParam);

    }

    /**
     * create
     * @param bpc
     */
    public void create(BaseProcessClass bpc){
        setServiceSelect(bpc);
        ParamUtil.setRequestAttr(bpc.request, "firstOption", "Please select");
        ParamUtil.setRequestAttr(bpc.request, "firstValue", null);

    }

    /**
     * doPrepare
     * @param bpc
     */
    public void save(BaseProcessClass bpc){
        String[] mode =  ParamUtil.getStrings(bpc.request, "mode");
        String id =  ParamUtil.getString(bpc.request, "distributionId");
        String name =  ParamUtil.getString(bpc.request, "name");
        String service =  ParamUtil.getString(bpc.request, "service");
        String role =  ParamUtil.getString(bpc.request, "role");
        String dismode = "";
        if(mode != null ) {
            if (mode.length > 1) {
                dismode = BOTH;
            } else {
                dismode = mode[0];
            }
        }
        DistributionListDto distributionListDto = new DistributionListDto();
        distributionListDto.setService(service);
        distributionListDto.setDisname(name);
        distributionListDto.setMode(dismode);
        distributionListDto.setRole(role);
        ValidationResult validationResult = WebValidationHelper.validateProperty(distributionListDto, "save");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
            ParamUtil.setRequestAttr(bpc.request,"distribution",distributionListDto);
        }else{
            if(id != null)
            {
                distributionListDto.setId(id);
            }
            distributionListService.saveDistributionList(distributionListDto);
            ParamUtil.setRequestAttr(bpc.request, "firstOption", "Please select");
            ParamUtil.setRequestAttr(bpc.request, "firstValue", " ");
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
        }

    }

    /**
     * delete
     * @param bpc
     */
    public void delete(BaseProcessClass bpc){
        String[] checkboxlist =  ParamUtil.getStrings(bpc.request, "checkboxlist");
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
        String distributionSwitch = ParamUtil.getRequestString(bpc.request,"distributionSwitch");
        String recipientsSwitch = ParamUtil.getRequestString(bpc.request,"recipientsSwitch");
        String service = ParamUtil.getString(bpc.request,"service");
        String serviceName = ParamUtil.getRequestString(bpc.request,"serviceName");
        searchParam.getParams().clear();
        searchParam.getFilters().clear();
        searchParam.setPageNo(1);
        if(!StringUtil.isEmpty(distributionSwitch)){
            searchParam.addFilter("description", "%" + distributionSwitch + "%",true);
        }
        if(!StringUtil.isEmpty(recipientsSwitch)){
            searchParam.addFilter("recipients",  "%" +recipientsSwitch + "%",true);
        }
        if(!StringUtil.isEmpty(service)){
            searchParam.addFilter("service",  service,true);
        }else{
            service = " ";
        }
        if(StringUtil.isEmpty(serviceName)){
            serviceName = "Please select";
        }
        setServiceSelect(bpc);
        ParamUtil.setRequestAttr(bpc.request,"distributionSwitch",distributionSwitch);
        ParamUtil.setRequestAttr(bpc.request,"recipientsSwitch",recipientsSwitch);
        ParamUtil.setRequestAttr(bpc.request,"firstValue",service);
        ParamUtil.setRequestAttr(bpc.request,"firstOption",serviceName);
    }

    /**
     * edit
     * @param bpc
     */
    public void edit(BaseProcessClass bpc){
        String id =  ParamUtil.getString(bpc.request, "editDistribution");
        DistributionListDto distributionListDto = distributionListService.getDistributionListById(id);
        setServiceSelect(bpc);
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
        ParamUtil.setSessionAttr(bpc.request, "service", (Serializable) selectOptionArrayList);
    }
}
