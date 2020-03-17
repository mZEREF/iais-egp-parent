package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    LicenceService licenceService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    InsRepClient insRepClient;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    CessationClient cessationClient;


    private static final String SEARCH_NO="searchNo";
    private static final String RFI_QUERY="ReqForInfoQuery";
    FilterParameter licenceParameter = new FilterParameter.Builder()
            .clz(RfiLicenceQueryDto.class)
            .searchAttr("licParam")
            .resultAttr("licResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(10).build();

    FilterParameter applicationParameter = new FilterParameter.Builder()
            .clz(RfiApplicationQueryDto.class)
            .searchAttr("appParam")
            .resultAttr("appResult")
            .sortField("application_no").pageNo(1).pageSize(10).sortType(SearchParam.ASCENDING).build();

    FilterParameter licenseeParameter = new FilterParameter.Builder()
            .clz(LicenseeQueryDto.class)
            .searchAttr("licenseeParam")
            .resultAttr("licenseeResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(0).pageSize(10).build();

    FilterParameter serviceParameter = new FilterParameter.Builder()
            .clz(HcsaSvcQueryDto.class)
            .searchAttr("svcParam")
            .resultAttr("svcResult")
            .sortField("id").pageNo(0).pageSize(10).sortType(SearchParam.ASCENDING).build();

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
        SearchResultHelper.doPage(request,applicationParameter);
        String searchNo=ParamUtil.getString(request,SEARCH_NO);
        ParamUtil.setRequestAttr(request,SEARCH_NO,searchNo);
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
        List<String> svcNames=new ArrayList<>();
        List<String> licenseeIds=new ArrayList<>();
        List<String> licenceIds=new ArrayList<>();
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
                    svcNames=new ArrayList<>();
                    filter.put("serviceName", searchNo);
                    serviceParameter.setFilters(filter);
                    SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
                    if (!serviceParam.getFilters().isEmpty()) {
                        SearchResult<HcsaSvcQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
                        for (HcsaSvcQueryDto r:serviceParamResult.getRows()
                        ) {
                            svcNames.add(r.getServiceName());
                        }
                    }
                    break;
                case 4:
                    licenseeIds=new ArrayList<>();
                    filter.put("licenseeName", searchNo);
                    licenseeParameter.setFilters(filter);
                    SearchParam licenseeParam = SearchResultHelper.getSearchParam(request, licenseeParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"licenseeQuery",licenseeParam);
                    if (!licenseeParam.getFilters().isEmpty()) {
                        SearchResult<LicenseeQueryDto> licenseeParamResult = onlineEnquiriesService.searchLicenseeIdsParam(licenseeParam);
                        for (LicenseeQueryDto r:licenseeParamResult.getRows()
                        ) {
                            licenseeIds.add(r.getId());
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if(count==1||count==2){
            applicationParameter.setFilters(filter);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
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
                        if(svcNames.size()!=0){
                            filter.put("svc_names", svcNames);
                        }
                        if(licenseeIds.size()!=0){
                            filter.put("licenseeIds", licenseeIds);
                        }
                        licenceParameter.setFilters(filter);
                        SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
                        licParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
                        SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                        if(licResult.getRowCount()!=0) {
                            for (RfiLicenceQueryDto lic:licResult.getRows()
                            ) {
                                ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                                String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                                reqForInfoSearchListDto.setLicenceId(lic.getId());
                                licenceIds.add(lic.getId());
                                reqForInfoSearchListDto.setLicenceStatus(licStatus);
                                reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                                //reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                                reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                                reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                                reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());

                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }
                        else {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
            ParamUtil.setRequestAttr(request,"SearchParam", appParam);
        }
        else {

            if(svcNames.size()!=0){
                filter.put("svc_names", svcNames);
            }
            if(licenseeIds.size()!=0){
                filter.put("licenseeIds", licenseeIds);
            }
            licenceParameter.setFilters(filter);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            CrudHelper.doPaging(licParam,bpc.request);
            QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
            SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
            SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
            if(licResult.getRowCount()!=0) {
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                for (RfiLicenceQueryDto lic:licResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                    reqForInfoSearchListDto.setLicenceId(lic.getId());
                    licenceIds.add(lic.getId());
                    reqForInfoSearchListDto.setLicenceStatus(licStatus);
                    reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                    //reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                    reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                    reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                    reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());


                    filter.put("id", lic.getAppId());
                    filter.remove("svc_names");
                    filter.remove("licenseeIds");
                    applicationParameter.setFilters(filter);

                    SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                    appParam.setPageNo(0);
                    QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                    SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                    if(!StringUtil.isEmpty(appResult)){
                        for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                        ) {
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        }
                    }

                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                }
                setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
            }
            ParamUtil.setRequestAttr(request,"SearchParam", licParam);
        }

        ParamUtil.setRequestAttr(request,"count", count);
        // 		preBasicSearch->OnStepProcess
    }

    private void setSearchResult(HttpServletRequest request,SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult,List<String> licenceIds,List<ReqForInfoSearchListDto> reqForInfoSearchListDtos){


        List<String> licIds=cessationClient.getlicIdToCessation(licenceIds).getEntity();
        for(ReqForInfoSearchListDto rfi:reqForInfoSearchListDtos){
            if("Active".equals(rfi.getLicenceStatus())){
                rfi.setIsCessation(licIds.contains(rfi.getLicenceId())?1:0);
            }else {
                rfi.setIsCessation(0);
            }
        }
        searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
        ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
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
        preSelectOption(request);
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo=ParamUtil.getString(request,"search_no");
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request,SEARCH_NO,searchNo);

        // 		doBasicSearch->OnStepProcess
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licSvcSubTypeOption=requestForInformationService.getLicSvcSubTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licSvcSubTypeOption", licSvcSubTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
    }
    public void preSearchLicence(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        // 		preSearchLicence->OnStepProcess
    }


    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);

        String applicationNo = ParamUtil.getString(bpc.request, "application_no");
        String applicationType = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String licenceNo = ParamUtil.getString(bpc.request, "licence_no");
        String uenNo = ParamUtil.getString(bpc.request, "uen_no");
        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, "licence_status");
        String svcSubType=ParamUtil.getString(bpc.request,"service_sub_type");
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
        String licStaDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licStaToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_start_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_date")),
                SystemAdminBaseConstants.DATE_FORMAT);

        Map<String,Object> filters=new HashMap<>(10);
        List<String> svcNames=new ArrayList<>();
        List<String> licenseeIds=new ArrayList<>();
        List<String> licenceIds=new ArrayList<>();
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
            if(!StringUtil.isEmpty(licenceNo)){
                filters.put("licence_no", licenceNo);
            }
            if(!StringUtil.isEmpty(licenceStatus)){
                filters.put("licence_status", licenceStatus);
            }
            if(!StringUtil.isEmpty(svcSubType)){
                filters.put("serviceSubTypeName", svcSubType);
                serviceParameter.setFilters(filters);
                SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
                QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
                if (!serviceParam.getFilters().isEmpty()) {
                    SearchResult<HcsaSvcQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
                    for (HcsaSvcQueryDto r:serviceParamResult.getRows()
                    ) {
                        svcNames.add(r.getServiceName());
                    }

                }

            }
            if(!StringUtil.isEmpty(uenNo)){
                LicenseeDto licenseeDto= organizationClient.getLicenseeDtoByUen(uenNo).getEntity();
                if(licenseeDto!=null) {
                    licenseeIds = new ArrayList<>();
                    licenseeIds.add(licenseeDto.getId());
                }
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
                SearchResult<LicenseeQueryDto> licenseeParamResult = onlineEnquiriesService.searchLicenseeIdsParam(licenseeParam);
                licenseeIds=new ArrayList<>();
                for (LicenseeQueryDto r:licenseeParamResult.getRows()
                ) {
                    licenseeIds.add(r.getId());
                }

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
                SearchResult<HcsaSvcQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
                for (HcsaSvcQueryDto r:serviceParamResult.getRows()
                ) {
                    svcNames.add(r.getServiceName());
                }

            }
        }
        if (Arrays.equals(count, new int[]{0, 0, 0, 0, 0})) {
            count= new int[]{1, 2, 3, 4, 5};
        }
        ParamUtil.setSessionAttr(request,"choose",count);
        SearchParam licParam=SearchResultHelper.getSearchParam(request, licenceParameter,true);
        if(count[0]==1||count[1]==2){
            applicationParameter.setFilters(filters);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
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
                        if(svcNames.size()!=0){
                            filters.put("svc_names", svcNames);
                        }
                        if(licenseeIds.size()!=0){
                            filters.put("licenseeIds", licenseeIds);
                        }
                        licenceParameter.setFilters(filters);
                        licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
                        licParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
                        SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                        if(licResult.getRowCount()!=0) {
                            for (RfiLicenceQueryDto lic:licResult.getRows()
                            ) {
                                ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                                String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                                reqForInfoSearchListDto.setLicenceId(lic.getId());
                                licenceIds.add(lic.getId());
                                reqForInfoSearchListDto.setLicenceStatus(licStatus);
                                reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                                //reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                                reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                                reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                                reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());
                                if(!StringUtil.isEmpty(serviceLicenceType)){
                                    ParamUtil.setRequestAttr(request,"serviceLicenceType",serviceLicenceType);
                                    boolean isAdd=false;
                                    List<String> serviceNames=requestForInformationService.getSvcNamesByType(serviceLicenceType);
                                    for (String svcName:serviceNames
                                    ) {
                                        if(svcName.equals(lic.getServiceName())){
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
                        else {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }

        }
        else {

            if(svcNames.size()!=0){
                filters.put("svc_names", svcNames);
            }
            if(licenseeIds.size()!=0){
                filters.put("licenseeIds", licenseeIds);
            }
            licenceParameter.setFilters(filters);
            licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            CrudHelper.doPaging(licParam,bpc.request);
            QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
            SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
            SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
            if(licResult.getRowCount()!=0) {
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                for (RfiLicenceQueryDto lic:licResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                    reqForInfoSearchListDto.setLicenceId(lic.getId());
                    licenceIds.add(lic.getId());
                    reqForInfoSearchListDto.setLicenceStatus(licStatus);
                    reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                    //reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                    reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                    reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                    reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());


                    filters.put("id", lic.getAppId());
                    filters.remove("svc_names");
                    filters.remove("licenseeIds");
                    applicationParameter.setFilters(filters);

                    SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                    appParam.setPageNo(0);
                    QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                    SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                    if(!StringUtil.isEmpty(appResult)){
                        for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                        ) {
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        }
                    }

                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                }
                setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
            }

        }
        if(!StringUtil.isEmpty(licStaDate)){
            licParam.getFilters().put("start_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(licStaToDate)){
            licParam.getFilters().put("start_to_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(appSubDate)){
            licParam.getFilters().put("subDate",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(appSubToDate)){
            licParam.getFilters().put("toDate",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(licExpDate)){
            licParam.getFilters().put("expiry_start_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(licExpToDate)){
            licParam.getFilters().put("expiry_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(uenNo)){
            licParam.getFilters().put("uen_no",uenNo);
        }
        ParamUtil.setRequestAttr(request,"SearchParam", licParam);

        // 		doSearchLicence->OnStepProcess
    }

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(RfiApplicationQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto){
        reqForInfoSearchListDto.setAppId(rfiApplicationQueryDto.getId());
        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getApplicationType()}).get(0).getText();
        reqForInfoSearchListDto.setApplicationType(appType);
        reqForInfoSearchListDto.setAppCorrId(rfiApplicationQueryDto.getAppCorrId());
        reqForInfoSearchListDto.setApplicationNo(rfiApplicationQueryDto.getApplicationNo());
        reqForInfoSearchListDto.setApplicationStatus(rfiApplicationQueryDto.getApplicationStatus());
        reqForInfoSearchListDto.setServiceName(rfiApplicationQueryDto.getSvcId());
        reqForInfoSearchListDto.setHciCode(rfiApplicationQueryDto.getHciCode());
        reqForInfoSearchListDto.setHciName(rfiApplicationQueryDto.getHciName());
        reqForInfoSearchListDto.setBlkNo(rfiApplicationQueryDto.getBlkNo());
        reqForInfoSearchListDto.setCurrentRiskTagging(rfiApplicationQueryDto.getRiskLevel());
        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        try {
            List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(rfiApplicationQueryDto.getAppCorrId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
            if(appPremisesRecommendationDtos.size()>=2){
                reqForInfoSearchListDto.setTwoLastComplianceHistory("Full");
            }
            else {
                reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
            }
        }catch (Exception e){
            reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
        }

        try{
            reqForInfoSearchListDto.setLastComplianceHistory("Full");
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = insepctionNcCheckListService.getNcItemDtoByAppCorrId(rfiApplicationQueryDto.getAppCorrId());
            for (AppPremisesPreInspectionNcItemDto nc:appPremisesPreInspectionNcItemDtos
            ) {
                if(nc.getIsRecitfied()==0){
                    reqForInfoSearchListDto.setLastComplianceHistory("Partial");
                }
            }
        }catch (Exception e){
            reqForInfoSearchListDto.setPastComplianceHistory("-");
        }
        //String riskLevel = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getRiskLevel()}).get(0).getText();
        reqForInfoSearchListDto.setCurrentRiskTagging(rfiApplicationQueryDto.getRiskLevel());
        log.debug(StringUtil.changeForLog("licenseeId start ...."+rfiApplicationQueryDto.getLicenseeId()));
        if(rfiApplicationQueryDto.getLicenseeId()!=null){
            reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
            try {
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
                reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
            } catch (Exception e) {
                log.info(e.getMessage());
            }

        }
    }


    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void callCessation(BaseProcessClass bpc) {
        log.info("=======>>>>>callCessation>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licId = (String) ParamUtil.getSessionAttr(request, "id");
        List<String> licIds=new ArrayList<>();
        licIds.add(licId);
        ParamUtil.setSessionAttr(request,"licIds", (Serializable) licIds);

        // 		callCessation->OnStepProcess
    }

    public void preLicDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preLicDetails>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setLicInfo(request);
        // 		preLicDetails->OnStepProcess
    }

    public void preInspReport(BaseProcessClass bpc) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.preInspReport(request);
        // 		preAppInfo->OnStepProcess
    }

    public void preAppDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String appCorrId = (String) ParamUtil.getSessionAttr(request, "id");
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(appCorrId);
        AppInsRepDto appInsRepDto=insRepClient.getAppInsRepDto(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationViewDto.getApplicationDto().getId());
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(appInsRepDto.getLicenseeId());
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =  appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto=null;
        if(oldAppSubmissionDto!=null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto,bpc.request);
        }
        /*************************/
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        if(appSvcRelatedInfoDto != null){
            String serviceId = appSvcRelatedInfoDto.getServiceId();
            hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocationList = new ArrayList<>();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            String hciName = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getHciName();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getConveyanceVehicleNo();
            }

            if(!StringUtil.isEmpty(hciName) && allocationDto !=null && allocationDto.size()>0 ){
                for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                    List<AppSvcChckListDto> appSvcChckListDtoList = null;
                    if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                        String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                        String idNo = appSvcDisciplineAllocationDto.getIdNo();
                        //set chkLstName
                        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                        if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                    appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                }
                            }
                        }
                        if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                            for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                                if(hcsaSvcSubtypeOrSubsumedDto!=null){
                                    appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                }
                                if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                    appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                }
                            }
                        }
                        //set selCgoName
                        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                            for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                if(idNo.equals(appSvcCgoDto.getIdNo())){
                                    appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                }
                            }
                        }
                        reloadDisciplineAllocationList.add(appSvcDisciplineAllocationDto);
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationList);

        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationType()}).get(0).getText();
        applicationViewDto.setApplicationType(appType);
        List<HcsaServiceDto> hcsaServiceDto=hcsaConfigClient.getHcsaService(new ArrayList<>(Collections.singleton(applicationViewDto.getApplicationDto().getServiceId()))).getEntity();
        applicationViewDto.getApplicationDto().setApplicationType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
        ParamUtil.setRequestAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setRequestAttr(request,"authorisedPersonList",appInsRepDto.getPrincipalOfficer());
        ParamUtil.setRequestAttr(request,"hcsaServiceDto",hcsaServiceDto.get(0));
        ParamUtil.setRequestAttr(request,"licenseeDto",licenseeDto);
        // 		preAppInfo->OnStepProcess
    }




    private HcsaSvcSubtypeOrSubsumedDto getHcsaSvcSubtypeOrSubsumedDtoById(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,String id){
        HcsaSvcSubtypeOrSubsumedDto result = null;
        if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)&&!StringUtil.isEmpty(id)){
            for (HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto : hcsaSvcSubtypeOrSubsumedDtos){
                if(id.equals(hcsaSvcSubtypeOrSubsumedDto.getId())){
                    result = hcsaSvcSubtypeOrSubsumedDto;
                    break;
                }else{
                    result = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDto.getList(),id);
                    if(result!=null){
                        break;
                    }
                }
            }
        }
        return result;
    }


    private  AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, AppSubmissionDto appSubmissionDto, HttpServletRequest request){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto=new AppSvcRelatedInfoDto();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
            List<AppSvcDisciplineAllocationDto> allocationDto = null;
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
            if(appSvcRelatedInfoDto != null){
                String serviceId = appSvcRelatedInfoDto.getServiceId();
                hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
                allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
            }
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = new HashMap<>();
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = new ArrayList<>();
                String hciName = "";
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                    hciName = appGrpPremisesDto.getHciName();
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                    hciName = appGrpPremisesDto.getConveyanceVehicleNo();
                }

                if(!StringUtil.isEmpty(hciName) && allocationDto !=null && !allocationDto.isEmpty() ){
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                        List<AppSvcChckListDto> appSvcChckListDtoList = null;
                        if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                            String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                            String idNo = appSvcDisciplineAllocationDto.getIdNo();
                            //set chkLstName
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                            if(appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()){
                                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                    if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                    }
                                }
                            }
                            if(appSvcChckListDtoList != null && !appSvcChckListDtoList.isEmpty()){
                                for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                    HcsaSvcSubtypeOrSubsumedDto  hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                                    if(hcsaSvcSubtypeOrSubsumedDto!=null){
                                        appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                    }
                                    if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                        appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                    }
                                }
                            }
                            //set selCgoName
                            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                            if(appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()){
                                for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                    if(idNo.equals(appSvcCgoDto.getIdNo())){
                                        appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                    }
                                }
                            }
                            reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                        }
                    }
                }
                reloadDisciplineAllocationMap.put(hciName, reloadDisciplineAllocation);
            }
            ParamUtil.setSessionAttr(request, "reloadOld", (Serializable) reloadDisciplineAllocationMap);

        }

        return  appSvcRelatedInfoDto;
    }



}
