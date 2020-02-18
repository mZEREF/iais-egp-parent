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
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
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
import java.util.Arrays;
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
            .sortField("licensee_id").sortType(SearchParam.ASCENDING).pageNo(0).pageSize(10).build();

    FilterParameter serviceParameter = new FilterParameter.Builder()
            .clz(String.class)
            .searchAttr("svcParam")
            .resultAttr("svcResult")
            .sortField("SVC_NAME").pageNo(0).pageSize(10).sortType(SearchParam.ASCENDING).build();

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,SEARCH_NO,null);
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);

        // 		Start->OnStepProcess
    }


    public void preBasicSearch(BaseProcessClass bpc)  {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>requestForInformation");

        HttpServletRequest request = bpc.request;
        String searchNo= (String) ParamUtil.getSessionAttr(request,SEARCH_NO);
        if(ParamUtil.getString(request,SEARCH_NO)!=null){
            searchNo=ParamUtil.getString(request,SEARCH_NO);
        }
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
        if(searchNo!=null) {
            switch (count) {
                case 2:
                    filter.put("appNo", searchNo);
                    break;
                case 3:
                    filter.put("licence_no", searchNo);
                    break;
                case 1:
                    filter.put("hciName", searchNo);
                    break;
                case 5:
                    filter.put("serviceName", searchNo);
                    serviceParameter.setFilters(filter);
                    SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
                    if (!serviceParam.getFilters().isEmpty()) {
                        SearchResult<RfiLicenceQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
                        List<String> svcNames=new ArrayList<>();
                        for (RfiLicenceQueryDto r:serviceParamResult.getRows()
                        ) {
                            svcNames.add(r.getServiceName());
                        }
                        filter.put("svc_names",svcNames);
                    }
                    break;
                case 4:
                    filter.put("licenseeName", searchNo);
                    licenseeParameter.setFilters(filter);
                    SearchParam licenseeParam = SearchResultHelper.getSearchParam(request, licenseeParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"licenseeQuery",licenseeParam);
                    if (!licenseeParam.getFilters().isEmpty()) {
                        SearchResult<RfiLicenceQueryDto> licenseeParamResult = onlineEnquiriesService.searchLicenseeIdsParam(licenseeParam);
                        List<String> licenseeIds=new ArrayList<>();
                        for (RfiLicenceQueryDto r:licenseeParamResult.getRows()
                        ) {
                            licenseeIds.add(r.getLicenseeId());
                        }
                        filter.put("licenseeIds",licenseeIds);
                    }
                    break;
                default:
                    break;
            }
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
                    else if(count==2){
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


        int[] count={0,0,0,0,0};
        if(ParamUtil.getString(request,"hci")!=null){
            count[0]=1;
        }
        if(ParamUtil.getString(request,"application")!=null){
            count[1]=2;
        }
        if(ParamUtil.getString(request,"licence")!=null){
            count[2]=3;
        }
        if(ParamUtil.getString(request,"licensee")!=null){
            count[3]=4;
        }
        if(ParamUtil.getString(request,"servicePersonnel")!=null){
            count[4]=5;
        }
        if (Arrays.equals(count, new int[]{0, 0, 0, 0, 0})) {
            count= new int[]{1, 2, 3, 4, 5};
        }
        ParamUtil.setSessionAttr(request,"choose",count);
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
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
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		preSearchLicence->OnStepProcess
    }


    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();


        String applicationNo = ParamUtil.getString(bpc.request, "application_no");
        String applicationType = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String licence_no = ParamUtil.getString(bpc.request, "licence_no");
        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, "licence_status");
        String hciCode = ParamUtil.getString(bpc.request, "hci_code");
        String hciName = ParamUtil.getString(bpc.request, "hci_name");
        String hciStreetName = ParamUtil.getString(bpc.request, "hci_street_name");
        String hciPostalCode = ParamUtil.getString(bpc.request, "hci_postal_code");
        String licenseeId = ParamUtil.getString(bpc.request, "licensee_id");
        String licenseeName = ParamUtil.getString(bpc.request, "licensee_name");
        String licenseeRegnNo = ParamUtil.getString(bpc.request, "licensee_regn_no");
        String serviceId = ParamUtil.getString(bpc.request, "service_id");
        String serviceName = ParamUtil.getString(bpc.request, "service_name");
        String serviceRegnNo = ParamUtil.getString(bpc.request, "service_regn_no");
        String serviceRole = ParamUtil.getString(bpc.request, "service_role");
        String appSubDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String appSubToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licStaDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licStaToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);

        Map<String,Object> filters=new HashMap<>(10);

        int[] count={0,0,0,0,0};
        if(ParamUtil.getString(request,"hci")!=null){
            count[0]=1;
            if(!StringUtil.isEmpty(hciCode)){
                filters.put("hciCode", hciCode);
            }
            if(!StringUtil.isEmpty(hciName)){
                filters.put("hciName", hciName);
            }
            if(!StringUtil.isEmpty(hciPostalCode)){
                filters.put("hciPostalCode", hciPostalCode);
            }
            if(!StringUtil.isEmpty(hciStreetName)){
                filters.put("hciStreetName", hciStreetName);
            }
        }
        if(ParamUtil.getString(request,"application")!=null||ParamUtil.getString(request,"licence")!=null){
            count[2]=3;
            if(!StringUtil.isEmpty(applicationNo)){
                filters.put("appNo", applicationNo);count[1]=2;
            }
            if(!StringUtil.isEmpty(applicationType)){
                filters.put("appType", applicationType);count[1]=2;
            }
            if(!StringUtil.isEmpty(status)){
                filters.put("appStatus", status);count[1]=2;
            }
            if(!StringUtil.isEmpty(appSubDate)){
                filters.put("subDate", appSubDate);count[1]=2;
            }
            if(!StringUtil.isEmpty(appSubToDate)){
                filters.put("toDate",appSubToDate);count[1]=2;
            }
            if(!StringUtil.isEmpty(licStaDate)){
                filters.put("start_date", licStaDate);
            }
            if(!StringUtil.isEmpty(licStaToDate)){
                filters.put("start_to_date",licStaToDate);
            }
            if(!StringUtil.isEmpty(licExpDate)){
                filters.put("expiry_start_date", licExpDate);
            }
            if(!StringUtil.isEmpty(licExpToDate)){
                filters.put("expiry_date",licExpToDate);
            }
            if(!StringUtil.isEmpty(licence_no)){
                filters.put("licence_no", licence_no);
            }
            if(!StringUtil.isEmpty(licenceStatus)){
                filters.put("licence_status", licenceStatus);
            }
        }

        if(ParamUtil.getString(request,"licensee")!=null){
            count[3]=4;
            if(!StringUtil.isEmpty(licenseeId)){
                filters.put("licenseeId",licenseeId);
            }
            if(!StringUtil.isEmpty(licenseeName)){
                filters.put("licenseeName", licenseeName);
            }
            if(!StringUtil.isEmpty(licenseeRegnNo)){
                filters.put("licenseeRegnNo",licenseeRegnNo);
            }
            licenseeParameter.setFilters(filters);
            SearchParam licenseeParam = SearchResultHelper.getSearchParam(request, licenseeParameter,true);
            QueryHelp.setMainSql(RFI_QUERY,"licenseeQuery",licenseeParam);
            if (!licenseeParam.getFilters().isEmpty()) {
                SearchResult<RfiLicenceQueryDto> licenseeParamResult = onlineEnquiriesService.searchLicenseeIdsParam(licenseeParam);
                List<String> licenseeIds=new ArrayList<>();
                for (RfiLicenceQueryDto r:licenseeParamResult.getRows()
                ) {
                    licenseeIds.add(r.getLicenseeId());
                }
                filters.put("licenseeIds",licenseeIds);
            }
        }
        if(ParamUtil.getString(request,"servicePersonnel")!=null){
            count[4]=5;
            if(!StringUtil.isEmpty(serviceId)){
                filters.put("serviceId", serviceId);
            }
            if(!StringUtil.isEmpty(serviceName)){
                filters.put("serviceName",serviceName);
            }
            if(!StringUtil.isEmpty(serviceRegnNo)){
                filters.put("serviceRegnNo", serviceRegnNo);
            }
            if(!StringUtil.isEmpty(serviceRole)){
                filters.put("serviceRole", serviceRole);
            }
            serviceParameter.setFilters(filters);
            SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
            QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
            if (!serviceParam.getFilters().isEmpty()) {
                SearchResult<RfiLicenceQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
                List<String> svcNames=new ArrayList<>();
                for (RfiLicenceQueryDto r:serviceParamResult.getRows()
                ) {
                    svcNames.add(r.getServiceName());
                }
                filters.put("svc_names",svcNames);
            }
        }
        if (Arrays.equals(count, new int[]{0, 0, 0, 0, 0})) {
            count= new int[]{1, 2, 3, 4, 5};
        }
        ParamUtil.setSessionAttr(request,"choose",count);


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

                    if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                        filters.put("app_id", rfiApplicationQueryDto.getId());
                    }
                    licenceParameter.setFilters(filters);
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
                    }
                    else if(count[1]!=2){
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
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
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
        log.debug(StringUtil.changeForLog("licenseeId start ...."+rfiApplicationQueryDto.getLicenseeId()));
        if(rfiApplicationQueryDto.getLicenseeId()!=null){
            reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
            reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
        }
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
        OrganizationLicDto organizationLicDto= organizationClient.getOrganizationLicDtoByLicenseeId(licenseeId).getEntity();
        ParamUtil.setRequestAttr(request,"organizationLicDto",organizationLicDto);
        // 		preAppInfo->OnStepProcess
    }

    public void preAppDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, "id");
        OrganizationLicDto organizationLicDto= organizationClient.getOrganizationLicDtoByLicenseeId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"organizationLicDto",organizationLicDto);
        // 		preAppInfo->OnStepProcess
    }









}
