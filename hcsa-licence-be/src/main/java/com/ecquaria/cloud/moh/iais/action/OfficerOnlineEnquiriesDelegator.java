package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OfficerOnlineEnquiriesDelegator
 *
 * @author junyu
 * @date 2020/2/7
 */
@Delegator(value = "onlineEnquiriesDelegator")
@Slf4j
public class OfficerOnlineEnquiriesDelegator {
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    ApplicationViewService applicationViewService;

    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    OnlineEnquiriesService onlineEnquiriesService;

    private final String SEARCH_NO="searchNo";
    private final String RFI_QUERY="ReqForInfoQuery";
    FilterParameter licenceParameter = new FilterParameter.Builder()
            .clz(RfiLicenceQueryDto.class)
            .searchAttr("licParam")
            .resultAttr("licResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(0).pageSize(10).build();

    FilterParameter applicationParameter = new FilterParameter.Builder()
            .clz(RfiApplicationQueryDto.class)
            .searchAttr("appParam")
            .resultAttr("appResult")
            .sortField("application_no").pageNo(0).pageSize(10).sortType(SearchParam.ASCENDING).build();

    FilterParameter licenseeParameter = new FilterParameter.Builder()
            .clz(String.class)
            .searchAttr("licenseeParam")
            .resultAttr("licenseeResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(0).pageSize(10).build();

    FilterParameter serviceParameter = new FilterParameter.Builder()
            .clz(String.class)
            .searchAttr("svcParam")
            .resultAttr("svcResult")
            .pageNo(0).pageSize(10).sortType(SearchParam.ASCENDING).build();

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,SEARCH_NO,null);
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);

        // 		Start->OnStepProcess
    }


    public void preBasicSearch(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>requestForInformation");

        HttpServletRequest request = bpc.request;
        String searchNo= (String) ParamUtil.getSessionAttr(request,SEARCH_NO);

        int count=0;
        if(ParamUtil.getString(request,"hci")!=null){
            count=1;
        }
        if(ParamUtil.getString(request,"application")!=null){
            count=2;
        }
        if(ParamUtil.getString(request,"licence")!=null){
            count=3;
        }
        if(ParamUtil.getString(request,"licensee")!=null){
            count=4;
        }
        if(ParamUtil.getString(request,"servicePersonnel")!=null){
            count=5;
        }
        Map<String,Object> filter=new HashMap<>();
        switch (count){
            case 1:filter.put("appNo", searchNo);break;
            case 2:filter.put("licence_no", searchNo);break;
            case 3:filter.put("hciName", searchNo);break;
            case 4:filter.put("svcName", searchNo);break;
            case 5:filter.put("name", searchNo);break;
            default:break;
        }

        licenseeParameter.setFilters(filter);
        SearchParam licenseeParam = SearchResultHelper.getSearchParam(request, licenseeParameter,true);
        QueryHelp.setMainSql(RFI_QUERY,"licenseeQuery",licenseeParam);
        if (licenseeParam != null) {
            SearchResult<String> licenseeParamResult = onlineEnquiriesService.searchLicenseeIdsParam(licenseeParam);
            filter.put("licenseeIds",licenseeParamResult.getRows());
        }

        licenseeParameter.setFilters(filter);
        SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
        QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
        if (serviceParam != null) {
            SearchResult<String> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
            filter.put("svc_names",serviceParamResult);
        }

        applicationParameter.setFilters(filter);
        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
        if (appParam != null) {
            SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

            if(!StringUtil.isEmpty(appResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                ) {

                    if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                        filter.put("app_id", rfiApplicationQueryDto.getId());
                    }
                    licenceParameter.setFilters(filter);
                    SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
                    SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                    if(licResult.getRowCount()!=0) {
                        for (RfiLicenceQueryDto lic:licResult.getRows()
                        ) {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                            reqForInfoSearchListDto.setLicenceStatus(licStatus);
                            reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                            reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                            reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                            reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                            reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());
//                            reqForInfoSearchListDto.setCurrentRiskTagging(lic.getRiskLevel());
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    else {
                        ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        ParamUtil.setRequestAttr(request,"SearchParam", appParam);

        // 		preBasicSearch->OnStepProcess
    }

    public void doBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo=ParamUtil.getString(request,"search_no");
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request,SEARCH_NO,searchNo);

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


    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();


        String searchNo= (String) ParamUtil.getSessionAttr(request,SEARCH_NO);


        String applicationNo = ParamUtil.getString(bpc.request, "application_no");
        if(StringUtil.isEmpty(applicationNo)){
            applicationNo = searchNo;
        }
        String applicationType = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String subDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String toDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);

        Map<String,Object> filters=new HashMap<>();

        if(!StringUtil.isEmpty(applicationNo)){
            filters.put("appNo", applicationNo);
        }
        if(!StringUtil.isEmpty(applicationType)){
            filters.put("appType", applicationType);
        }
        if(!StringUtil.isEmpty(status)){
            filters.put("appStatus", status);
        }
        if(!StringUtil.isEmpty(subDate)){
            filters.put("subDate", subDate);
        }
        if(!StringUtil.isEmpty(toDate)){
            filters.put("toDate",toDate);
        }
        applicationParameter.setFilters(filters);
        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
        if (appParam != null) {
            SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

            if(!StringUtil.isEmpty(appResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                ) {
                    Map<String,Object> filter=new HashMap<>();
                    if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                        filter.put("app_id", rfiApplicationQueryDto.getId());
                    }
                    licenceParameter.setFilters(filter);
                    SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
                    SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                    if(licResult.getRowCount()!=0) {
                        for (RfiLicenceQueryDto lic:licResult.getRows()
                        ) {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                            reqForInfoSearchListDto.setLicenceStatus(licStatus);
                            reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                            reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                            reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                            reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                            reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());
//                            reqForInfoSearchListDto.setCurrentRiskTagging(lic.getRiskLevel());
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    else {
                        ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }

        ParamUtil.setRequestAttr(request,"SearchParam", appParam);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		doSearchLicence->OnStepProcess
    }

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(RfiApplicationQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto){
        reqForInfoSearchListDto.setAppId(rfiApplicationQueryDto.getId());
        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getApplicationType()}).get(0).getText();
        reqForInfoSearchListDto.setApplicationType(appType);
        reqForInfoSearchListDto.setApplicationNo(rfiApplicationQueryDto.getApplicationNo());
        reqForInfoSearchListDto.setApplicationStatus(rfiApplicationQueryDto.getApplicationStatus());
        reqForInfoSearchListDto.setHciCode(rfiApplicationQueryDto.getHciCode());
        reqForInfoSearchListDto.setHciName(rfiApplicationQueryDto.getHciName());
        reqForInfoSearchListDto.setBlkNo(rfiApplicationQueryDto.getBlkNo());
        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
        reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
    }


    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
        // 		doSearchLicenceAfter->OnStepProcess
    }


    public void preLicDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licenseeId = (String) ParamUtil.getSessionAttr(request, "id");
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        OrganizationDto organizationDto= organizationClient.getOrganizationById(licenseeDto.getOrganizationId()).getEntity();
        ParamUtil.setRequestAttr(request,"licenseeDto",licenseeDto);
        ParamUtil.setRequestAttr(request,"organizationDto",organizationDto);
        // 		preAppInfo->OnStepProcess
    }











}
