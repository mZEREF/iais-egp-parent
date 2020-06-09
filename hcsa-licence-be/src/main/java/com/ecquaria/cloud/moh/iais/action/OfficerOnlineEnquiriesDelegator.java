package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.google.common.collect.ImmutableSet;
import ecq.commons.sequence.uuid.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * OfficerOnlineEnquiriesDelegator
 *
 * @author junyu
 * @date 2020/2/7
 */
@Delegator(value = "onlineEnquiriesDelegator")
@Slf4j
public class OfficerOnlineEnquiriesDelegator {
    private static final Set<String> COUNTS = ImmutableSet.of("1", "2", "5");
    private static final Set<String> COUNTS1 = ImmutableSet.of("3", "4");
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
    CessationBeService cessationBeService;
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    InspectionRectificationProService inspectionRectificationProService;

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
        AuditTrailHelper.auditFunction("OnlineEnquiries Management", "OnlineEnquiries Config");
        ParamUtil.setSessionAttr(request,"isASO",0);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        for (String role:loginContext.getRoleIds()
             ) {
            if(role.equals(RoleConsts.USER_ROLE_ASO)||role.equals(RoleConsts.USER_ROLE_ASO_LEAD)){
                ParamUtil.setSessionAttr(request,"isASO",1);
            }
        }
        Integer isCease= (Integer) ParamUtil.getRequestAttr(request,"isCease");
        if(isCease!=null&&isCease==1){
            ParamUtil.setSessionAttr(request,"cease",1);
        }
        else {
            ParamUtil.setSessionAttr(request,"cease",0);
        }
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);
        ParamUtil.setSessionAttr(request,SEARCH_NO,null);

        // 		Start->OnStepProcess
    }


    public void preBasicSearch(BaseProcessClass bpc)  {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>OnlineEnquiries");

        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"SearchResult", null);
        String searchNo=ParamUtil.getString(request,SEARCH_NO);
        ParamUtil.setSessionAttr(request,SEARCH_NO,searchNo);
        String count=ParamUtil.getString(request,"searchChk");
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
            if(count==null){
                count="0";
            }
        }
        ParamUtil.setSessionAttr(request,"count", count);
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        if(searchNo!=null) {
            switch (count) {
                case "2":
                    filter.put("appNo", searchNo);
                    break;
                case "3":
                    filter.put("licence_no", searchNo);
                    break;
                case "1":
                    filter.put("hciName", searchNo);
                    break;
                case "5":
                    filter.put("personnelName", searchNo);
                    serviceParameter.setFilters(filter);
                    break;
                case "4":
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
                        if(licenseeIds.size()==0){
                            licenseeIds.add(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ID);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if(COUNTS.contains(count)){
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
                                reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                                reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());

                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }
                        else {
                            licenceIds.add(UUID.randomUUID().toString());
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            reqForInfoSearchListDto.setLicenceStatus("-");
                            reqForInfoSearchListDto.setLicenceNo("-");
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
            ParamUtil.setRequestAttr(request,"SearchParam", appParam);
        }
        else if(COUNTS1.contains(count)){
            SearchResultHelper.doPage(request,licenceParameter);
            licenceParameter.setFilters(filter);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            if(licenseeIds.size()!=0){
                String typeStr = SqlHelper.constructInCondition("lic.licensee_id",licenseeIds.size());
                int indx = 0;
                for (String s : licenseeIds){
                    licParam.addFilter("lic.licensee_id"+indx, s);
                    indx++;
                }
                licParam.addParam("licenseeId",typeStr);
            }

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


                    if(!StringUtil.isEmpty(lic.getAppId())){
                        filter.put("id", lic.getAppId());
                        filter.remove("svc_names");
                        applicationParameter.setFilters(filter);

                        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                        appParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                        SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                        if(!StringUtil.isEmpty(appResult)){
                            for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                            ) {
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                                reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                                List<String> addressList = IaisCommonUtils.genNewArrayList();
                                for (PremisesDto premisesDto:premisesDtoList
                                ) {
                                    addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                                    reqForInfoSearchListDto.setAddress(addressList);
                                }
                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }else {
                            List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                            reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                            reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                            List<String> addressList = IaisCommonUtils.genNewArrayList();
                            for (PremisesDto premisesDto:premisesDtoList
                            ) {
                                addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                                reqForInfoSearchListDto.setAddress(addressList);
                            }
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                }
                setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
            }
            ParamUtil.setRequestAttr(request,"SearchParam", licParam);
        }

        // 		preBasicSearch->OnStepProcess
    }

    private void setSearchResult(HttpServletRequest request,SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult,List<String> licenceIds,List<ReqForInfoSearchListDto> reqForInfoSearchListDtos){


        Map<String,Boolean> licIds= cessationBeService.listResultCeased(licenceIds);

        for(ReqForInfoSearchListDto rfi:reqForInfoSearchListDtos){
            if("Active".equals(rfi.getLicenceStatus())){
                try {
                    rfi.setIsCessation(licIds.get(rfi.getLicenceId())?1:0);
                }catch (NullPointerException e){
                    rfi.setIsCessation(0);
                }
            }else {
                rfi.setIsCessation(0);
            }
        }
        String uenNo = ParamUtil.getString(request, "uen_no");
        if(!StringUtil.isEmpty(uenNo)){
            if( "3".equals(ParamUtil.getSessionAttr(request,"count"))||"2".equals(ParamUtil.getSessionAttr(request,"count"))
            ) {
                List<LicenseeDto> licenseeDtos= organizationClient.getLicenseeDtoByUen(uenNo).getEntity();
                if(licenseeDtos==null) {
                    reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                }
            }
        }
        searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
        ParamUtil.setSessionAttr(request,"SearchResult", searchListDtoSearchResult);
    }

    public void doBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doBasicSearch>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;

        String count=ParamUtil.getString(request,"searchChk");

        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
        }
        ParamUtil.setSessionAttr(request,"count",count);
        preSelectOption(request);
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);

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
        log.info("=======>>>>>preSearchLicence>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        // 		preSearchLicence->OnStepProcess
    }


    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        ParamUtil.setSessionAttr(request,"SearchResult", null);
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
        String licenseeIdNo = ParamUtil.getString(bpc.request, "licensee_idNo");
        String licenseeName = ParamUtil.getString(bpc.request, "licensee_name");
        String licenseeRegnNo = ParamUtil.getString(bpc.request, "licensee_regn_no");
        String personnelId = ParamUtil.getString(bpc.request, "personnelId");
        String personnelName = ParamUtil.getString(bpc.request, "personnelName");
        String personnelRegnNo = ParamUtil.getString(bpc.request, "personnelRegnNo");
        String personnelRole = ParamUtil.getString(bpc.request, "personnelRole");
        String appSubDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String appSubToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        String licStaDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licStaToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_start_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_date")),
                SystemAdminBaseConstants.DATE_FORMAT);

        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        List<String> svcNames=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();

        String count=ParamUtil.getString(request,"searchChk");
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
        }
        switch (count) {
            case "2":
                if(!StringUtil.isEmpty(applicationNo)){
                    filters.put("appNo", applicationNo);
                }
                if(!StringUtil.isEmpty(applicationType)){
                    filters.put("appType", applicationType);
                }
                if(!StringUtil.isEmpty(status)){
                    filters.put("appStatus", status);
                }
                if(!StringUtil.isEmpty(appSubDate)){
                    filters.put("subDate", appSubDate);
                }
                if(!StringUtil.isEmpty(appSubToDate)){
                    filters.put("toDate",appSubToDate);
                }
                if(!StringUtil.isEmpty(licStaDate)){
                    filters.put("start_date", licStaDate);count="3";
                }
                if(!StringUtil.isEmpty(licStaToDate)){
                    filters.put("start_to_date",licStaToDate);count="3";
                }
                if(!StringUtil.isEmpty(licExpDate)){
                    filters.put("expiry_start_date", licExpDate);count="3";
                }
                if(!StringUtil.isEmpty(licExpToDate)){
                    filters.put("expiry_date",licExpToDate);count="3";
                }
                if(!StringUtil.isEmpty(licenceNo)){
                    filters.put("licence_no", licenceNo);count="3";
                }
                if(!StringUtil.isEmpty(licenceStatus)){
                    filters.put("licence_status", licenceStatus);count="3";
                }
                if(!StringUtil.isEmpty(serviceLicenceType)){
                    filters.put("svc_name", serviceLicenceType);count="3";
                }
                if(!StringUtil.isEmpty(svcSubType)){
                    filters.put("serviceSubTypeName", svcSubType);count="3";
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
                    count="3";
                    List<LicenseeDto> licenseeDtos= organizationClient.getLicenseeDtoByUen(uenNo).getEntity();
                    if(licenseeDtos!=null) {
                        for (LicenseeDto licensee:licenseeDtos
                        ) {
                            licenseeIds.add(licensee.getId());
                        }
                    }
                }
                break;
            case "1":
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
                break;
            case "5":
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
                break;
            case "4":
                if(!StringUtil.isEmpty(licenseeIdNo)){
                    filters.put("licenseeIdNo",licenseeIdNo);
                }
                if(!StringUtil.isEmpty(licenseeName)){
                    filters.put("licenseeName", licenseeName);
                }
                licenseeParameter.setFilters(filters);
                SearchParam licenseeParam = SearchResultHelper.getSearchParam(request, licenseeParameter,true);
                QueryHelp.setMainSql(RFI_QUERY,"licenseeQuery",licenseeParam);
                if (!licenseeParam.getFilters().isEmpty()) {
                    SearchResult<LicenseeQueryDto> licenseeParamResult = onlineEnquiriesService.searchLicenseeIdsParam(licenseeParam);
                    for (LicenseeQueryDto r:licenseeParamResult.getRows()
                    ) {
                        licenseeIds.add(r.getId());
                    }
                    if(licenseeIds.size()==0){
                        licenseeIds.add(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ID);
                    }
                }
                if(!StringUtil.isEmpty(licenseeRegnNo)){
                    filters.put("licenseeRegnNo",licenseeRegnNo);
                }
                break;
            default:
                break;
        }

        ParamUtil.setSessionAttr(request,"count",count);
        if(COUNTS.contains(count)){
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
                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);

                            }
                        }
                        else {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            reqForInfoSearchListDto.setLicenceStatus("-");
                            reqForInfoSearchListDto.setLicenceNo("-");
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

            licenceParameter.setFilters(filters);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            if(licenseeIds.size()!=0){
                String typeStr = SqlHelper.constructInCondition("lic.licensee_id",licenseeIds.size());
                int indx = 0;
                for (String s : licenseeIds){
                    licParam.addFilter("lic.licensee_id"+indx, s);
                    indx++;
                }
                licParam.addParam("licenseeId",typeStr);
            }

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


                    if(!StringUtil.isEmpty(lic.getAppId())){
                        filters.put("id", lic.getAppId());
                        filters.remove("svc_names");
                        applicationParameter.setFilters(filters);

                        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                        appParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                        SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                        if(!StringUtil.isEmpty(appResult)){
                            for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                            ) {
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                                reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                                List<String> addressList = IaisCommonUtils.genNewArrayList();
                                for (PremisesDto premisesDto:premisesDtoList
                                ) {
                                    addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                                    reqForInfoSearchListDto.setAddress(addressList);
                                }
                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }else {
                            List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                            reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                            reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                            List<String> addressList = IaisCommonUtils.genNewArrayList();
                            for (PremisesDto premisesDto:premisesDtoList
                            ) {
                                addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                                reqForInfoSearchListDto.setAddress(addressList);
                            }
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
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
            if(ParamUtil.getString(request,"applicationChk")!=null||ParamUtil.getString(request,"licenceChk")!=null) {
                List<LicenseeDto> licenseeDtos= organizationClient.getLicenseeDtoByUen(uenNo).getEntity();
                if(licenseeDtos==null) {
                    licParam.setPageSize(0);
                }
            }
        }
        ParamUtil.setSessionAttr(request,"SearchParam", licParam);
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
        if(rfiApplicationQueryDto.getHciName()==null){
            reqForInfoSearchListDto.setHciName("-");
        }
        reqForInfoSearchListDto.setBlkNo(rfiApplicationQueryDto.getBlkNo());

        reqForInfoSearchListDto.setCurrentRiskTagging(MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getRiskLevel()}).get(0).getText());
        if(rfiApplicationQueryDto.getRiskLevel()==null){
            reqForInfoSearchListDto.setCurrentRiskTagging("-");
        }
        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        addressList.add(MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode()));
        reqForInfoSearchListDto.setAddress(addressList);

        try{
            List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(rfiApplicationQueryDto.getAppCorrId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
            if(appPremisesRecommendationDtos.size()>=2){
                reqForInfoSearchListDto.setTwoLastComplianceHistory("Full");
            }
            else {
                reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
            }
            if(appPremisesRecommendationDtos.size()>=1){
                reqForInfoSearchListDto.setLastComplianceHistory("Full");
                List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = insepctionNcCheckListService.getNcItemDtoByAppCorrId(rfiApplicationQueryDto.getAppCorrId());
                if(appPremisesPreInspectionNcItemDtos.size()!=0){
                    for (AppPremisesPreInspectionNcItemDto nc:appPremisesPreInspectionNcItemDtos
                    ) {
                        if(nc.getIsRecitfied()==0){
                            reqForInfoSearchListDto.setLastComplianceHistory("Partial");
                        }
                    }
                }
                AdCheckListShowDto adCheckListShowDto = fillupChklistService.getAdhoc(rfiApplicationQueryDto.getAppCorrId());
                if(adCheckListShowDto!=null){
                    List<AdhocNcCheckItemDto> adItemList = adCheckListShowDto.getAdItemList();
                    if(adItemList!=null && !adItemList.isEmpty()){
                        for(AdhocNcCheckItemDto temp:adItemList){
                            if(temp.getRectified()){
                                reqForInfoSearchListDto.setLastComplianceHistory("Partial");
                            }
                        }
                    }
                }
            }else {
                reqForInfoSearchListDto.setLastComplianceHistory("-");
            }
        }catch (Exception e){
            reqForInfoSearchListDto.setLastComplianceHistory("-");
            reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
        }
        log.debug(StringUtil.changeForLog("licenseeId start ...."+rfiApplicationQueryDto.getLicenseeId()));
        if(rfiApplicationQueryDto.getLicenseeId()!=null){
            reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
            try {
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
                reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
    }


    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        try{
            String id = ParamUtil.getMaskedString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
            ParamUtil.setSessionAttr(request,"id",id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        String count=ParamUtil.getString(request,"searchChk");
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
        }
        ParamUtil.setSessionAttr(request,"count",count);
        String [] appIds=ParamUtil.getStrings(request,"appIds");
        Set<String> licIdsSet=IaisCommonUtils.genNewHashSet();
        Set<String> licRfiIdsSet=IaisCommonUtils.genNewHashSet();
        try{
            for (String appId : appIds) {
                String is = appId.split("\\|")[1];
                String isActive = appId.split("\\|")[3];
                if ("1".equals(is)) {
                    licIdsSet.add(appId.split("\\|")[2]);
                }
                if ("Active".equals(isActive)) {
                    licRfiIdsSet.add(appId.split("\\|")[2]);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        List<String> licIds=IaisCommonUtils.genNewArrayList();
        licIds.addAll(licIdsSet);
        List<String> licRfiIds=IaisCommonUtils.genNewArrayList();
        licRfiIds.addAll(licRfiIdsSet);
        ParamUtil.setSessionAttr(request,"licIds", (Serializable) licIds);
        ParamUtil.setSessionAttr(request,"licRfiIds", (Serializable) licRfiIds);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void callCessation(BaseProcessClass bpc) {
        log.info("=======>>>>>callCessation>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, "licIds");
        ParamUtil.setSessionAttr(request,"licIds", (Serializable) licenceIds);

        // 		callCessation->OnStepProcess
    }

    public void callReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>callReqForInfo>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, "licIds");
        ParamUtil.setSessionAttr(request,"licIds", (Serializable) licenceIds);

        // 		callReqForInfo->OnStepProcess
    }


    public void preLicDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preLicDetails>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setLicInfo(request);
        // 		preLicDetails->OnStepProcess
    }

    public void preInspReport(BaseProcessClass bpc) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.preInspReport(request);
        // 		preAppInfo->OnStepProcess
    }

    public void preAppDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setAppInfo(request);
    }


    private Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();


        return errMap;
    }

    @GetMapping(value = "officer-online-enquiries-information-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchResult<ReqForInfoSearchListDto>  results = (SearchResult<ReqForInfoSearchListDto>) ParamUtil.getSessionAttr(request,"SearchResult");

        List<ReqForInfoSearchListDto> queryList=IaisCommonUtils.genNewArrayList();
        if (!Objects.isNull(results)){
            for (ReqForInfoSearchListDto info:results.getRows()
            ) {
                info.setServiceName(HcsaServiceCacheHelper.getServiceNameById(info.getServiceName()));
            }

             queryList = results.getRows();
        }

        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.setClz(ReqForInfoSearchListDto.class);
        excelWriter.setFileName("Officer Online Enquiries Information_Search_Template");

        try {
            file = excelWriter.writerToExcel(queryList);
        } catch (Exception e) {
            log.error("=======>fileHandler error >>>>>", e);
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }
}
