package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RequestForInformationDelegator
 *
 * @author junyu
 * @date 2019/12/14
 */
@Slf4j
@Delegator("requestForInformationDelegator")
public class RequestForInformationDelegator {
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    ApplicationClient applicationClient;
    FilterParameter licenceParameter = new FilterParameter();
    FilterParameter applicationParameter = new FilterParameter();

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");

        // 		Start->OnStepProcess
    }

    public void doSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		doSearch->OnStepProcess
    }

    public void preBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		preBasicSearch->OnStepProcess
    }

    public void doBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo=ParamUtil.getString(request,"search_no");
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request,"searchNo",searchNo);

        // 		doBasicSearch->OnStepProcess
    }

    public void preSearchLicence(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();

        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		preSearchLicence->OnStepProcess
    }

    public void preSearchApplication(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        // 		preSearchApplication->OnStepProcess
    }

    public void doSearchApplication(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();


        String searchNo= (String) ParamUtil.getSessionAttr(request,"searchNo");


        String application_no = ParamUtil.getString(bpc.request, "application_no");
        if(StringUtil.isEmpty(application_no)){
            application_no = searchNo;
        }

        String application_type = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String sub_date = ParamUtil.getString(bpc.request, "sub_date");
        String to_date = ParamUtil.getString(bpc.request, "to_date");






        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		doSearchApplication->OnStepProcess
    }

    public void doSearchLicence(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        String searchNo= (String) ParamUtil.getSessionAttr(request,"searchNo");

        String licence_no = ParamUtil.getString(bpc.request, "licence_no");
        if(StringUtil.isEmpty(licence_no)){
            licence_no = searchNo;
        }
        String service_licence_type = ParamUtil.getString(bpc.request, "service_licence_type");
        String licence_status = ParamUtil.getString(bpc.request, "licence_status");
        String sub_date = ParamUtil.getString(bpc.request, "sub_date");
        String to_date = ParamUtil.getString(bpc.request, "to_date");
        //List<String> svcNames=
        licenceParameter.setClz(RfiLicenceQueryDto.class);
        licenceParameter.setSearchAttr("licParam");
        licenceParameter.setResultAttr("licResult");
        Map<String,Object> filters=new HashMap<>();

        if(!StringUtil.isEmpty(licence_no)){
            filters.put("licence_no", licence_no);
        }
        if(!StringUtil.isEmpty(licence_status)){
            filters.put("licence_status", licence_status);
        }
        if(!StringUtil.isEmpty(sub_date)){
            filters.put("start_date", sub_date);
        }
        if(!StringUtil.isEmpty(to_date)){
            filters.put("expiry_date", to_date);
        }
        licenceParameter.setFilters(filters);
        licenceParameter.setPageNo(0);
        licenceParameter.setPageSize(10);
        licenceParameter.setSortField("id");
        licenceParameter.setSortType(SearchParam.ASCENDING);
        SearchParam licParam = SearchResultHelper.getSearchParam(request, true,licenceParameter);
        QueryHelp.setMainSql("ReqForInfoQuery","licenceQuery",licParam);
        if (licParam != null) {
            SearchResult<RfiLicenceQueryDto> licResult = requestForInformationService.licenceDoQuery(licParam);

            if(!StringUtil.isEmpty(licResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiLicenceQueryDto rfiLicenceQueryDto:licResult.getRows()
                     ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    reqForInfoSearchListDto.setLicenceStatus(rfiLicenceQueryDto.getLicenceStatus());
                    reqForInfoSearchListDto.setLicenceNo(rfiLicenceQueryDto.getLicenceNo());
                    reqForInfoSearchListDto.setAppId(rfiLicenceQueryDto.getAppId());
                    reqForInfoSearchListDto.setServiceName(rfiLicenceQueryDto.getServiceName());
                    reqForInfoSearchListDto.setStartDate(rfiLicenceQueryDto.getStartDate());
                    reqForInfoSearchListDto.setExpiryDate(rfiLicenceQueryDto.getExpiryDate());

                    applicationParameter.setClz(RfiApplicationQueryDto.class);
                    applicationParameter.setSearchAttr("appParam");
                    applicationParameter.setResultAttr("appResult");
                    Map<String,Object> filter=new HashMap<>();
                    if(!StringUtil.isEmpty(rfiLicenceQueryDto.getAppId())){
                        filter.put("id", rfiLicenceQueryDto.getAppId());
                    }
                    applicationParameter.setFilters(filter);
                    applicationParameter.setPageNo(0);
                    applicationParameter.setPageSize(10);
                    applicationParameter.setSortField("application_no");
                    applicationParameter.setSortType(SearchParam.ASCENDING);
                    SearchParam appParam = SearchResultHelper.getSearchParam(request, true,applicationParameter);
                    QueryHelp.setMainSql("ReqForInfoQuery","applicationQuery",appParam);
                    SearchResult<RfiApplicationQueryDto> appResult =applicationClient.searchApp(appParam).getEntity();
                    RfiApplicationQueryDto app=appResult.getRows().get(0);

                    reqForInfoSearchListDto.setApplicationType(app.getApplicationType());
                    reqForInfoSearchListDto.setApplicationNo(app.getApplicationNo());
                    reqForInfoSearchListDto.setHciCode(app.getHciCode());
                    reqForInfoSearchListDto.setHciName(app.getHciName());
                    reqForInfoSearchListDto.setBlkNo(app.getBlkNo());
                    reqForInfoSearchListDto.setBuildingName(app.getBuildingName());
                    reqForInfoSearchListDto.setUnitNo(app.getUnitNo());
                    reqForInfoSearchListDto.setStreetName(app.getStreetName());
                    reqForInfoSearchListDto.setFloorNo(app.getFloorNo());

                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setSessionAttr(request,"SearchParam", licParam);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		doSearchLicence->OnStepProcess
    }

    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void doSearchApplicationAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplicationAfter>>>>>>>>>>>>>>>>requestForInformation");
        // 		doSearchApplicationAfter->OnStepProcess
    }

    public void preAppInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        // 		preAppInfo->OnStepProcess
    }

    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        // 		preReqForInfo->OnStepProcess
    }

    public void preSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		preSearch->OnStepProcess
    }

    public void doCloseRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>doCloseRfi>>>>>>>>>>>>>>>>requestForInformation");
        // 		doCloseRfi->OnStepProcess
    }

    public void doReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>doReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        // 		doReqForInfo->OnStepProcess
    }
}
