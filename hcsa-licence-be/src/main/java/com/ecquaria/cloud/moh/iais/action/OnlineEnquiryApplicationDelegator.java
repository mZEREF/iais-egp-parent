package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InsTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * OnlineEnquiryApplicationDelegator
 *
 * @author junyu
 * @date 2022/12/22
 */
@Delegator(value = "onlineEnquiryApplicationDelegator")
@Slf4j
public class OnlineEnquiryApplicationDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter insTabParameter = new FilterParameter.Builder()
            .clz(InspectionTabQueryResultsDto.class)
            .searchAttr("insTabParam")
            .resultAttr("insTabResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private OnlineEnquiryLicenceDelegator licenceDelegator;
    @Autowired
    private RequestForInformationService requestForInformationService;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    private static final String APP_ID = "appId";

    public void start(BaseProcessClass bpc){

    }

    public void preSearch(BaseProcessClass bpc){

    }

    public void nextStep(BaseProcessClass bpc){
        String appId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(appId)) {
            try {
                appId= MaskUtil.unMaskValue(APP_ID,appId);
                ParamUtil.setSessionAttr(bpc.request, APP_ID,appId);
            }catch (Exception e){
                log.info("no APP_ID");
            }
        }
    }

    public void preAppInfo(BaseProcessClass bpc){
        String appId = (String) ParamUtil.getSessionAttr(bpc.request, APP_ID);
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appPremisesCorrelationDto.getId());
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(appId).getEntity();
        if(licAppCorrelationDto!=null){
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licAppCorrelationDto.getLicenceId()).getEntity();
            ParamUtil.setSessionAttr(bpc.request, "licenceDto", licenceDto);
        }else {
            ParamUtil.setSessionAttr(bpc.request, "licenceDto", null);
        }


    }

    public void preInsTab(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, "preActive", "3");

        String appId = (String) ParamUtil.getSessionAttr(bpc.request, APP_ID);
        ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        List<SelectOption> inspectionTypeOption =licenceDelegator.getInspectionTypeOption();
        ParamUtil.setRequestAttr(request,"inspectionTypeOption", inspectionTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "insTabParam");

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                insTabParameter.setSortType(sortType);
                insTabParameter.setSortField(sortFieldName);
            }
            InsTabEnquiryFilterDto filterDto=licenceDelegator.setInsEnquiryFilterDto(request);

            licenceDelegator.setInsQueryFilter(filterDto,insTabParameter);

            SearchParam insTabParam = SearchResultHelper.getSearchParam(request, insTabParameter,true);

            if(filterDto.getAppStatus()!=null){
                licenceDelegator.setSearchParamAppStatus(filterDto.getAppStatus(),insTabParam,"insTab");
            }else {
                insTabParam.removeFilter("getAppStatus");
            }
            if(searchParam!=null){
                insTabParam.setPageNo(searchParam.getPageNo());
                insTabParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(insTabParam,bpc.request);
            insTabParam.addFilter("appNo",applicationDto.getApplicationNo(),true);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","inspectionsTabOnlineEnquiry",insTabParam);
            SearchResult<InspectionTabQueryResultsDto> insTabResult = onlineEnquiriesService.searchLicenceInsTabQueryResult(insTabParam);
            ParamUtil.setRequestAttr(request,"insTabResult",insTabResult);
            ParamUtil.setSessionAttr(request,"insTabParam",insTabParam);
        }else {
            SearchResult<InspectionTabQueryResultsDto> insTabResult = onlineEnquiriesService.searchLicenceInsTabQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"insTabResult",insTabResult);
            ParamUtil.setSessionAttr(request,"insTabParam",searchParam);
        }

    }

    public void insStep(BaseProcessClass bpc){

    }


    public void preInspectionReport(BaseProcessClass bpc){

    }

    public void backInsTab(BaseProcessClass bpc){

    }
}
