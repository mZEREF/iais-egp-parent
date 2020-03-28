package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
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
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(10).build();

    FilterParameter serviceParameter = new FilterParameter.Builder()
            .clz(HcsaSvcQueryDto.class)
            .searchAttr("svcParam")
            .resultAttr("svcResult")
            .sortField("id").pageNo(1).pageSize(10).sortType(SearchParam.ASCENDING).build();

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

        String searchNo=ParamUtil.getString(request,SEARCH_NO);
        ParamUtil.setRequestAttr(request,SEARCH_NO,searchNo);
        int count=0;
        if(ParamUtil.getString(request,"hciChk")!=null){
            count=1;
        }
        if(ParamUtil.getString(request,"applicationChk")!=null){
            count=2;
        }
        if(ParamUtil.getString(request,"licenceChk")!=null){
            count=3;
        }
        if(ParamUtil.getString(request,"licenseeChk")!=null){
            count=4;
        }
        if(ParamUtil.getString(request,"servicePersonnelChk")!=null){
            count=5;
        }
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        //List<String> svcNames=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
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
                    //svcNames=IaisCommonUtils.genNewArrayList();
                    filter.put("personnelName", searchNo);
                    serviceParameter.setFilters(filter);
//                    SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
//                    QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
//                    if (!serviceParam.getFilters().isEmpty()) {
//                        SearchResult<HcsaSvcQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
//                        for (HcsaSvcQueryDto r:serviceParamResult.getRows()
//                        ) {
//                            svcNames.add(r.getServiceName());
//                        }
//                    }
                    break;
                case 4:
                    licenseeIds=IaisCommonUtils.genNewArrayList();
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

        if(count==1||count==2||count==5){
            SearchResultHelper.doPage(request,applicationParameter);
            applicationParameter.setFilters(filter);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
            QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
            if (appParam != null) {
                SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

                if(!StringUtil.isEmpty(appResult)){
                    SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                    for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                    ) {

                        if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                            filter.put("app_id", rfiApplicationQueryDto.getId());
                        }
//                        if(svcNames.size()!=0){
//                            filter.put("svc_names", svcNames);
//                        }
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
            SearchResultHelper.doPage(request,licenceParameter);
//            if(svcNames.size()!=0){
//                filter.put("svc_names", svcNames);
//            }
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
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
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


                    if(!StringUtil.isEmpty(lic.getAppId())){
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
        if(ParamUtil.getString(request,"hciChk")!=null){
            count[0]=1;
        }
        if(ParamUtil.getString(request,"applicationChk")!=null){
            count[1]=2;
        }
        if(ParamUtil.getString(request,"licenceChk")!=null){
            count[2]=3;
        }
        if(ParamUtil.getString(request,"licenseeChk")!=null){
            count[3]=4;
        }
        if(ParamUtil.getString(request,"servicePersonnelChk")!=null){
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
        List<SelectOption> servicePersonnelRoleOption=onlineEnquiriesService.getServicePersonnelRoleOption();
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licSvcSubTypeOption", licSvcSubTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        ParamUtil.setRequestAttr(request,"servicePersonnelRoleOption", servicePersonnelRoleOption);
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
        String personnelId = ParamUtil.getString(bpc.request, "personnelId");
        String personnelName = ParamUtil.getString(bpc.request, "personnelName");
        String personnelRegnNo = ParamUtil.getString(bpc.request, "personnelRegnNo");
        String personnelRole = ParamUtil.getString(bpc.request, "personnelRole");
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
        List<String> svcNames=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        int[] count={0,0,0,0,0};
        if(ParamUtil.getString(request,"hciChk")!=null){
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
        if(ParamUtil.getString(request,"applicationChk")!=null||ParamUtil.getString(request,"licenceChk")!=null){
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
            if(!StringUtil.isEmpty(serviceLicenceType)){
                svcNames.add(serviceLicenceType);
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
                    licenseeIds = IaisCommonUtils.genNewArrayList();
                    licenseeIds.add(licenseeDto.getId());
                }
            }
        }

        if(ParamUtil.getString(request,"licenseeChk")!=null){
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
                licenseeIds=IaisCommonUtils.genNewArrayList();
                for (LicenseeQueryDto r:licenseeParamResult.getRows()
                ) {
                    licenseeIds.add(r.getId());
                }

            }
        }
        if(ParamUtil.getString(request,"servicePersonnelChk")!=null){
            count[4]=5;
            if(!StringUtil.isEmpty(personnelId)){
                filters.put("personnelId", personnelId);
            }
            if(!StringUtil.isEmpty(personnelName)){
                filters.put("personnelName",personnelName);
            }
            if(!StringUtil.isEmpty(personnelRegnNo)){
                filters.put("personnelRegnNo", personnelRegnNo);
            }
            if(!StringUtil.isEmpty(personnelRole)){
                filters.put("personnelRole", personnelRole);
            }
//            serviceParameter.setFilters(filters);
//            SearchParam serviceParam = SearchResultHelper.getSearchParam(request, serviceParameter,true);
//            QueryHelp.setMainSql(RFI_QUERY,"serviceQuery",serviceParam);
//            if (!serviceParam.getFilters().isEmpty()) {
//                SearchResult<HcsaSvcQueryDto> serviceParamResult = onlineEnquiriesService.searchSvcNamesParam(serviceParam);
//                for (HcsaSvcQueryDto r:serviceParamResult.getRows()
//                ) {
//                    svcNames.add(r.getServiceName());
//                }
//
//            }
        }
        if (Arrays.equals(count, new int[]{0, 0, 0, 0, 0})) {
            count= new int[]{1, 2, 3, 4, 5};
        }
        ParamUtil.setSessionAttr(request,"choose",count);
        if(count[0]==1||count[1]==2||count[4]==5){
            SearchResultHelper.doPage(request,applicationParameter);
            applicationParameter.setFilters(filters);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
            QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
            if (appParam != null) {
                SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

                if(!StringUtil.isEmpty(appResult)){
                    SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                    for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                    ) {

                        if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                            filters.put("app_id", rfiApplicationQueryDto.getId());
                        }
//                        if(svcNames.size()!=0){
//                            filters.put("svc_names", svcNames);
//                        }
                        if(licenseeIds.size()!=0){
                            filters.put("licenseeIds", licenseeIds);
                        }
                        licenceParameter.setFilters(filters);
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
            setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate, appParam);
        }
        else {

            if(svcNames.size()!=0){
                filters.put("svc_names", svcNames);
            }
            if(licenseeIds.size()!=0){
                filters.put("licenseeIds", licenseeIds);
            }
            licenceParameter.setFilters(filters);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            CrudHelper.doPaging(licParam,bpc.request);
            QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
            SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
            SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
            if(licResult.getRowCount()!=0) {
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
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


                    if(!StringUtil.isEmpty(lic.getAppId())){
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
                    }

                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                }
                setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
            }
            setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate, licParam);
        }


        // 		doSearchLicence->OnStepProcess
    }

    private void setSearchParamDate(HttpServletRequest request, String uenNo, String appSubDate, String appSubToDate, String licStaDate, String licStaToDate, String licExpDate, String licExpToDate, SearchParam licParam) throws ParseException {
        if(!StringUtil.isEmpty(licStaDate)){
            licParam.getFilters().put("start_date", Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(licStaToDate)){
            licParam.getFilters().put("start_to_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_to_date")),
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
            licParam.getFilters().put("expiry_start_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_start_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(licExpToDate)){
            licParam.getFilters().put("expiry_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(uenNo)){
            licParam.getFilters().put("uen_no",uenNo);
        }
        ParamUtil.setRequestAttr(request,"SearchParam", licParam);
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
        String riskLevel = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getRiskLevel()}).get(0).getText();
        reqForInfoSearchListDto.setCurrentRiskTagging(riskLevel);
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
        String [] appIds=ParamUtil.getStrings(request,"appIds");
        List<String> applIds=IaisCommonUtils.genNewArrayList();
        try{
            Collections.addAll(applIds, appIds);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        List<String> licIds=onlineEnquiriesService.getLicIdsByappIds(applIds);
        List<String> licenceIds=cessationClient.getlicIdToCessation(licIds).getEntity();
        licIds.removeIf(licId -> !licenceIds.contains(licId));
        ParamUtil.setSessionAttr(request,"licIds", (Serializable) licIds);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void callCessation(BaseProcessClass bpc) {
        log.info("=======>>>>>callCessation>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, "licIds");
        ParamUtil.setSessionAttr(request,"licIds", (Serializable) licenceIds);

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
        onlineEnquiriesService.setAppInfo(request);
    }


    @GetMapping(value = "/valid-licenceId")
    public @ResponseBody
    List<String> reloadRevEmail(HttpServletRequest request)  {
        String[] appIds=ParamUtil.getMaskedStrings(request,"appIds");
        List<String> applIds=IaisCommonUtils.genNewArrayList();
        try{
            Collections.addAll(applIds, appIds);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        List<String> licIds=onlineEnquiriesService.getLicIdsByappIds(applIds);
        List<String> licenceIds=cessationClient.getlicIdToCessation(licIds).getEntity();
        licIds.removeIf(licId -> !licenceIds.contains(licId));
        return licIds;
    }


}
