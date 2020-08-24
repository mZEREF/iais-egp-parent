package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
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
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private static final Set<String> COUNTS = ImmutableSet.of( "1","2", "5");
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
    @Autowired
    HcsaChklClient hcsaChklClient;
    @Autowired
    private ApplicationClient applicationClient;

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
        licenceParameter.setPageNo(1);
        applicationParameter.setPageNo(1);
        licenseeParameter.setPageNo(1);
        serviceParameter.setPageNo(1);
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
            String countOld= (String) ParamUtil.getSessionAttr(request,"count");
            if(!countOld.equals(count)){
                appParam.setPageNo(1);
            }
            QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
            if (appParam != null) {
                SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

                if(appResult.getRowCount()!=0){
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
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,lic.getId());
                                String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                                reqForInfoSearchListDto.setLicenceId(lic.getId());
                                licenceIds.add(lic.getId());
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciCode())){
                                    for (PremisesDto p :premisesDtoList
                                            ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciCode(p.getHciCode());
                                        }
                                    }
                                }
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciName())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciName(p.getHciName());
                                        }
                                    }
                                }
                                for (PremisesDto premisesDto:premisesDtoList
                                     ) {
                                    String appAddress=MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode());
                                    String licAddress=MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                                    if(appAddress.equals(licAddress)){
                                        reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                        reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                    }
                                }
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
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,null);
                            reqForInfoSearchListDto.setLicenceStatus("-");
                            reqForInfoSearchListDto.setLicenceNo("-");
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
            ParamUtil.setSessionAttr(request,"SearchParam", appParam);
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
            String countOld= (String) ParamUtil.getSessionAttr(request,"count");
            if(!countOld.equals(count)){
                licParam.setPageNo(1);
            }
            QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
            SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
            SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
            if(licResult.getRowCount()!=0) {
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
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


                    if(lic.getAppId()!=null){
                        filter.put("id", lic.getAppId());
                        filter.remove("svc_names");
                        applicationParameter.setFilters(filter);

                        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                        appParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                        SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                        if(appResult.getRowCount()!=0){
                            for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                            ) {
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,lic.getId());
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciCode())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciCode(p.getHciCode());
                                        }
                                    }
                                }
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciName())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciName(p.getHciName());
                                        }
                                    }
                                }
                                List<String> addressList = IaisCommonUtils.genNewArrayList();
                                for (PremisesDto premisesDto:premisesDtoList
                                ) {
                                    String appAddress=MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode());
                                    String licAddress=MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                                    addressList.add(licAddress);  //NOSONAR
                                    if(appAddress.equals(licAddress)){
                                        reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                        reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                    }
                                }
                                reqForInfoSearchListDto.setAddress(addressList);
                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }

                    } else {
                        List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                        reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                        reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(lic.getServiceName()).getEntity();
                        reqForInfoSearchListDto.setServiceName(svcDto.getId());
                        try {
                            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(lic.getLicenseeId());
                            reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        List<String> addressList = IaisCommonUtils.genNewArrayList();
                        for (PremisesDto premisesDto:premisesDtoList
                        ) {
                            addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                        }
                        reqForInfoSearchListDto.setAddress(addressList);
                        if(lic.getLicAppId()==null){
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                }
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
            }
            ParamUtil.setSessionAttr(request,"SearchParam", licParam);
        }
        ParamUtil.setSessionAttr(request,"count", count);
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
        if(reqForInfoSearchListDtos.size()<1){
            searchListDtoSearchResult.setRowCount(reqForInfoSearchListDtos.size());
        }
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
    public void preSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>preSearchLicence>>>>>>>>>>>>>>>>OnlineEnquiries");
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
        String count=ParamUtil.getString(request,"searchChk");
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
        }
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        switch (count) {
            case "2":
                if(!StringUtil.isEmpty(applicationNo)){
                    filters.put("appNo", applicationNo);
                }
                if(!StringUtil.isEmpty(applicationType)){
                    filters.put("appType", applicationType);
                }
                if(!StringUtil.isEmpty(status)){
                    if(status.equals(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS)){
                        filters.put("appGrpPmtStatus", status);
                    }else
                    if(!status.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                        filters.put("appStatus", status);
                    }
                }
                if(!StringUtil.isEmpty(appSubDate)){
                    filters.put("subDate", appSubDate);
                }
                if(!StringUtil.isEmpty(appSubToDate)){
                    filters.put("toDate",appSubToDate);
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
                    filters.put("svc_name", serviceLicenceType);
                }
                if(!StringUtil.isEmpty(svcSubType)){
                    filters.put("serviceSubTypeName", svcSubType);

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
                if(!StringUtil.isEmpty(licenseeRegnNo)){
                    filters.put("licenseeRegnNo",licenseeRegnNo);
                }
                break;
            default:
                break;
        }
        licenceParameter.setFilters(filters);
        SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
        setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate, licParam);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validateDate(request);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
        }
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

        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");


        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        List<String> svcNames=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();

        String count=ParamUtil.getString(request,"searchChk");
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
        }
        ParamUtil.setSessionAttr(request,"count",count);
        switch (count) {
            case "2":
                if(!StringUtil.isEmpty(applicationNo)){
                    filters.put("appNo", applicationNo);
                }
                if(!StringUtil.isEmpty(applicationType)){
                    filters.put("appType", applicationType);
                }
                if(!StringUtil.isEmpty(status)){
                    if(status.equals(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS)){
                        filters.put("appGrpPmtStatus", status);
                    }else
                        if(!status.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                            filters.put("appStatus", status);
                        }
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
                ParamUtil.setSessionAttr(request,"count",count);
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

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validateDate(request);
        if (!errorMap.isEmpty()) {
            licenceParameter.setFilters(filters);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate, licParam);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }
        if(COUNTS.contains(count)){
            SearchResultHelper.doPage(request,applicationParameter);
            applicationParameter.setFilters(filters);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
            if(status!=null && status.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                appParam.addParam("appStatus_APPROVED", "(app.status = 'APST005' OR app.status = 'APST050')");
            }
            CrudHelper.doPaging(appParam,bpc.request);
            QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
            if (appParam != null) {
                SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

                if(appResult.getRowCount()!=0){
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
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,lic.getId());
                                String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                                reqForInfoSearchListDto.setLicenceId(lic.getId());
                                licenceIds.add(lic.getId());
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciCode())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciCode(p.getHciCode());
                                        }
                                    }
                                }
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciName())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciName(p.getHciName());
                                        }
                                    }
                                }
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
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,null);
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


                    if(lic.getAppId()!=null){
                        filters.put("id", lic.getAppId());
                        filters.remove("svc_names");
                        applicationParameter.setFilters(filters);

                        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                        if(status!=null&&status.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                            appParam.addParam("appStatus_APPROVED", "(app.status = 'APST005' OR app.status = 'APST050')");
                        }
                        appParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                        SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                        if(appResult.getRowCount()!=0){
                            for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                            ) {
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,lic.getId());
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciCode())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciCode(p.getHciCode());
                                        }
                                    }
                                }
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciName())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciName(p.getHciName());
                                        }
                                    }
                                }
                                List<String> addressList = IaisCommonUtils.genNewArrayList();
                                for (PremisesDto premisesDto:premisesDtoList
                                ) {
                                    String appAddress=MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode());
                                    String licAddress=MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                                    addressList.add(licAddress);  //NOSONAR
                                    if(appAddress.equals(licAddress)){
                                        reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                        reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                    }
                                }
                                reqForInfoSearchListDto.setAddress(addressList);
                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }
                    }else {
                        List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                        reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                        reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(lic.getServiceName()).getEntity();
                        reqForInfoSearchListDto.setServiceName(svcDto.getId());
                        try {
                            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(lic.getLicenseeId());
                            reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        List<String> addressList = IaisCommonUtils.genNewArrayList();
                        for (PremisesDto premisesDto:premisesDtoList
                        ) {
                            addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                        }
                        reqForInfoSearchListDto.setAddress(addressList);
                        if(lic.getLicAppId()==null){
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
        String appStatus=ParamUtil.getString(request, "application_status");
        if(!StringUtil.isEmpty(appStatus)&&appStatus.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
            licParam.getFilters().put("appStatus",appStatus);
        }
        ParamUtil.setSessionAttr(request,"SearchParam", licParam);
    }

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(RfiApplicationQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto,String licenceId){
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

        try {
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(rfiApplicationQueryDto.getApplicationType());
            hcsaRiskScoreDto.setLicId(licenceId);
            List<ApplicationDto> applicationDtos = new ArrayList<>(1);
            ApplicationDto applicationDto =applicationClient.getApplicationById(rfiApplicationQueryDto.getId()).getEntity();
            if(applicationDto!=null&&licenceId!=null){
                applicationDtos.add(applicationDto);
                hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
                hcsaRiskScoreDto.setServiceId(rfiApplicationQueryDto.getSvcId());
                HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
                String riskLevel = entity.getRiskLevel();
                reqForInfoSearchListDto.setCurrentRiskTagging(MasterCodeUtil.retrieveOptionsByCodes(new String[]{riskLevel}).get(0).getText());
            }
        }catch (Exception e){
            reqForInfoSearchListDto.setCurrentRiskTagging("-");
            log.info(e.getMessage(),e);
        }


        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        addressList.add(MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode()));
        reqForInfoSearchListDto.setAddress(addressList);

        //add listReportNcRectifiedDto and add ncItemId
        if(licenceId!=null){
            List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceId).getEntity();

            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(licAppCorrelationDtos.get(0).getApplicationId()).getEntity();
            AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremisesCorrelationDto.getId()).getEntity();
            if (appPremPreInspectionNcDto != null) {
                String ncId = appPremPreInspectionNcDto.getId();
                List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
                if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                    reqForInfoSearchListDto.setLastComplianceHistory("Partial");
                }
            } else {
                reqForInfoSearchListDto.setLastComplianceHistory("Full");
            }
            try {
                AppPremisesCorrelationDto appPremisesCorrelationDto1 = applicationClient.getAppPremisesCorrelationDtosByAppId(licAppCorrelationDtos.get(1).getApplicationId()).getEntity();
                AppPremPreInspectionNcDto appPremPreInspectionNcDto1 = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremisesCorrelationDto1.getId()).getEntity();
                if (appPremPreInspectionNcDto1 != null) {
                    String ncId = appPremPreInspectionNcDto1.getId();
                    List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
                    if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                        reqForInfoSearchListDto.setTwoLastComplianceHistory("Partial");
                    }
                } else {
                    reqForInfoSearchListDto.setTwoLastComplianceHistory("Full");
                }
            }catch (Exception e){
                reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
            }

        }else {
            AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(rfiApplicationQueryDto.getAppCorrId()).getEntity();
            if (appPremPreInspectionNcDto != null) {
                String ncId = appPremPreInspectionNcDto.getId();
                List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
                if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                    reqForInfoSearchListDto.setLastComplianceHistory("Partial");
                }
            } else {
                reqForInfoSearchListDto.setLastComplianceHistory("Full");
            }
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
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
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


    private Map<String, String> validateDate(HttpServletRequest request) throws ParseException {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
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
        if(!StringUtil.isEmpty(appSubDate)&&!StringUtil.isEmpty(appSubToDate)){
            if(appSubDate.compareTo(appSubToDate)>0){
                errMap.put("to_date","OEN_ERR001");
            }
        }
        if(!StringUtil.isEmpty(licStaDate)&&!StringUtil.isEmpty(licStaToDate)){
            if(licStaDate.compareTo(licStaToDate)>0){
                errMap.put("start_to_date","OEN_ERR002");
            }
        }
        if(!StringUtil.isEmpty(licExpDate)&&!StringUtil.isEmpty(licExpToDate)){
            if(licExpDate.compareTo(licExpToDate)>0){
                errMap.put("expiry_date","OEN_ERR003");
            }
        }

        return errMap;
    }

    @GetMapping(value = "officer-online-enquiries-information-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        List<String> svcNames=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        String count=(String) ParamUtil.getSessionAttr(request,"count");
        SearchParam parm = (SearchParam) ParamUtil.getSessionAttr(request,"SearchParam");
        switch (count) {
            case "2":
                if(!StringUtil.isEmpty(parm.getFilters().get("appNo"))){
                    filters.put("appNo", parm.getFilters().get("appNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appType"))){
                    filters.put("appType", parm.getFilters().get("appType"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appStatus"))){
                    if(parm.getFilters().get("appStatus").equals(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS)){
                        filters.put("appGrpPmtStatus", parm.getFilters().get("appStatus"));
                    }else
                    if(!parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                        filters.put("appStatus", parm.getFilters().get("appStatus"));
                    }
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("subDate"))){
                    filters.put("subDate", Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("subDate")),
                            SystemAdminBaseConstants.DATE_FORMAT));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("toDate"))){
                    filters.put("toDate",Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("toDate")),
                            SystemAdminBaseConstants.DATE_FORMAT));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("start_date"))){
                    filters.put("start_date", Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("start_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("start_to_date"))){
                    filters.put("start_to_date",Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("start_to_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("expiry_start_date"))){
                    filters.put("expiry_start_date", Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("expiry_start_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("expiry_date"))){
                    filters.put("expiry_date",Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("expiry_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licence_no"))){
                    filters.put("licence_no", parm.getFilters().get("licence_no"));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licence_status"))){
                    filters.put("licence_status", parm.getFilters().get("licence_status"));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("svc_name"))){
                    filters.put("svc_name", parm.getFilters().get("svc_name"));count="3";
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("serviceSubTypeName"))){
                    filters.put("serviceSubTypeName", parm.getFilters().get("serviceSubTypeName"));count="3";
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
                if(!StringUtil.isEmpty(parm.getFilters().get("uen_no"))){
                    count="3";
                    List<LicenseeDto> licenseeDtos= organizationClient.getLicenseeDtoByUen((String) parm.getFilters().get("uen_no")).getEntity();
                    if(licenseeDtos!=null) {
                        for (LicenseeDto licensee:licenseeDtos
                        ) {
                            licenseeIds.add(licensee.getId());
                        }
                    }
                }
                break;
            case "1":
                if(!StringUtil.isEmpty(parm.getFilters().get("hciCode"))){
                    filters.put("hciCode", parm.getFilters().get("hciCode"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciName"))){
                    filters.put("hciName", parm.getFilters().get("hciName"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciPostalCode"))){
                    filters.put("hciPostalCode", parm.getFilters().get("hciPostalCode"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciStreetName"))){
                    filters.put("hciStreetName", parm.getFilters().get("hciStreetName"));
                }
                break;
            case "5":
                if(!StringUtil.isEmpty(parm.getFilters().get("personnelId"))){
                    filters.put("personnelId", parm.getFilters().get("personnelId"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("personnelName"))){
                    filters.put("personnelName",parm.getFilters().get("personnelName"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("personnelRegnNo"))){
                    filters.put("personnelRegnNo", parm.getFilters().get("personnelRegnNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("personnelRole"))){
                    filters.put("personnelRole", parm.getFilters().get("personnelRole"));
                }
                break;
            case "4":
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeIdNo"))){
                    filters.put("licenseeIdNo",parm.getFilters().get("licenseeIdNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeName"))){
                    filters.put("licenseeName", parm.getFilters().get("licenseeName"));
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
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeRegnNo"))){
                    filters.put("licenseeRegnNo",parm.getFilters().get("licenseeRegnNo"));
                }
                break;
            default:
                break;
        }

        SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
        if(COUNTS.contains(count)){
            applicationParameter.setFilters(filters);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
            if (appParam != null) {
            appParam.setPageNo(0);
            if(parm.getFilters().get("appStatus")!=null && parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                appParam.addParam("appStatus_APPROVED", "(app.status = 'APST005' OR app.status = 'APST050')");
            }
            QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

                if(appResult.getRowCount()!=0){
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
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,lic.getId());
                                String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                                reqForInfoSearchListDto.setLicenceId(lic.getId());
                                licenceIds.add(lic.getId());
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciCode())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciCode(p.getHciCode());
                                        }
                                    }
                                }
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciName())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciName(p.getHciName());
                                        }
                                    }
                                }
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
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,null);
                            reqForInfoSearchListDto.setLicenceStatus("-");
                            reqForInfoSearchListDto.setLicenceNo("-");
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                }
            }
        }
        else {

            if(svcNames.size()!=0){
                filters.put("svc_names", svcNames);
            }

            licenceParameter.setFilters(filters);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
            licParam.setPageNo(0);
            if(licenseeIds.size()!=0){
                String typeStr = SqlHelper.constructInCondition("lic.licensee_id",licenseeIds.size());
                int indx = 0;
                for (String s : licenseeIds){
                    licParam.addFilter("lic.licensee_id"+indx, s);
                    indx++;
                }
                licParam.addParam("licenseeId",typeStr);
            }

            QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
            SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
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


                    if(lic.getAppId()!=null){
                        filters.put("id", lic.getAppId());
                        filters.remove("svc_names");
                        applicationParameter.setFilters(filters);

                        SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);
                        if(parm.getFilters().get("appStatus")!=null&&parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                            appParam.addParam("appStatus_APPROVED", "(app.status = 'APST005' OR app.status = 'APST050')");
                        }
                        appParam.setPageNo(0);
                        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
                        SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);
                        if(appResult.getRowCount()!=0){
                            for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                            ) {
                                rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,lic.getId());
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciCode())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciCode(p.getHciCode());
                                        }
                                    }
                                }
                                if(StringUtil.isEmpty(reqForInfoSearchListDto.getHciName())){
                                    for (PremisesDto p :premisesDtoList
                                    ) {
                                        if(p.getStreetName().equals(rfiApplicationQueryDto.getStreetName())&&p.getPostalCode().equals(rfiApplicationQueryDto.getPostalCode())){
                                            reqForInfoSearchListDto.setHciName(p.getHciName());
                                        }
                                    }
                                }
                                List<String> addressList = IaisCommonUtils.genNewArrayList();
                                for (PremisesDto premisesDto:premisesDtoList
                                ) {
                                    String appAddress=MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode());
                                    String licAddress=MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                                    addressList.add(licAddress);   //NOSONAR
                                    if(appAddress.equals(licAddress)){
                                        reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                        reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                    }
                                }
                                reqForInfoSearchListDto.setAddress(addressList);
                                reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                            }
                        }

                    }else {
                        List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(lic.getId()).getEntity();
                        reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                        reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(lic.getServiceName()).getEntity();
                        reqForInfoSearchListDto.setServiceName(svcDto.getId());
                        try {
                            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(lic.getLicenseeId());
                            reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        List<String> addressList = IaisCommonUtils.genNewArrayList();
                        for (PremisesDto premisesDto:premisesDtoList
                        ) {
                            addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                        }
                        reqForInfoSearchListDto.setAddress(addressList);
                        if(lic.getLicAppId()==null){
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
            }
        }

        List<ReqForInfoSearchListDto> queryList=IaisCommonUtils.genNewArrayList();
        for (ReqForInfoSearchListDto info:searchListDtoSearchResult.getRows()
        ) {
            info.setServiceName(HcsaServiceCacheHelper.getServiceNameById(info.getServiceName()));
        }

        queryList = searchListDtoSearchResult.getRows();

        try {
            file = ExcelWriter.writerToExcel(queryList, ReqForInfoSearchListDto.class, "Officer Online Enquiries Information_Search_Template");
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
