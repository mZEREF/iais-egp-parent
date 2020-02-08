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

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,SEARCH_NO,null);
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);

        // 		Start->OnStepProcess
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


    public void doSearchApplication(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
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
        // 		doSearchApplication->OnStepProcess
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

    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        String searchNo= (String) ParamUtil.getSessionAttr(request,SEARCH_NO);

        String licence_no = ParamUtil.getString(bpc.request, "licence_no");
        if(StringUtil.isEmpty(licence_no)){
            licence_no = searchNo;
        }
        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, "licence_status");
        String subDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String toDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> filters=new HashMap<>(10);

        if(!StringUtil.isEmpty(licence_no)){
            filters.put("licence_no", licence_no);
        }
        if(!StringUtil.isEmpty(licenceStatus)){
            filters.put("licence_status", licenceStatus);
        }
        if(!StringUtil.isEmpty(subDate)){
            filters.put("start_date", subDate);
        }
        if(!StringUtil.isEmpty(toDate)){
            filters.put("expiry_date", toDate);
        }
        licenceParameter.setFilters(filters);

        SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
        QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
        if (licParam != null) {
            SearchResult<RfiLicenceQueryDto> licResult = requestForInformationService.licenceDoQuery(licParam);

            if(!StringUtil.isEmpty(licResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiLicenceQueryDto rfiLicenceQueryDto:licResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    String licStatus= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiLicenceQueryDto.getLicenceStatus()}).get(0).getText();
                    reqForInfoSearchListDto.setLicenceStatus(licStatus);
                    reqForInfoSearchListDto.setLicenceNo(rfiLicenceQueryDto.getLicenceNo());
                    reqForInfoSearchListDto.setAppId(rfiLicenceQueryDto.getAppId());
                    reqForInfoSearchListDto.setServiceName(rfiLicenceQueryDto.getServiceName());
                    reqForInfoSearchListDto.setStartDate(rfiLicenceQueryDto.getStartDate());
                    reqForInfoSearchListDto.setExpiryDate(rfiLicenceQueryDto.getExpiryDate());
                    reqForInfoSearchListDto.setHciCode(rfiLicenceQueryDto.getHciCode());
                    reqForInfoSearchListDto.setHciName(rfiLicenceQueryDto.getHciName());
                    reqForInfoSearchListDto.setBlkNo(rfiLicenceQueryDto.getBlkNo());
                    reqForInfoSearchListDto.setBuildingName(rfiLicenceQueryDto.getBuildingName());
                    reqForInfoSearchListDto.setUnitNo(rfiLicenceQueryDto.getUnitNo());
                    reqForInfoSearchListDto.setStreetName(rfiLicenceQueryDto.getStreetName());
                    reqForInfoSearchListDto.setFloorNo(rfiLicenceQueryDto.getFloorNo());
                    reqForInfoSearchListDto.setLicenseeId(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicPremId(rfiLicenceQueryDto.getLicPremId());
//                    reqForInfoSearchListDto.setCurrentRiskTagging(rfiLicenceQueryDto.getRiskLevel());
                    LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                    if(!StringUtil.isEmpty(serviceLicenceType)){
                        boolean isAdd=false;
                        List<String> svcNames=requestForInformationService.getSvcNamesByType(serviceLicenceType);
                        for (String svcName:svcNames
                        ) {
                            if(svcName.equals(reqForInfoSearchListDto.getServiceName())){
                                isAdd=true;
                            }
                        }
                        if(isAdd){
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    else {
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        ParamUtil.setRequestAttr(request,"SearchParam", licParam);
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		doSearchLicence->OnStepProcess
    }

    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
        // 		doSearchLicenceAfter->OnStepProcess
    }


    public void preAppInfo(BaseProcessClass bpc) {
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
