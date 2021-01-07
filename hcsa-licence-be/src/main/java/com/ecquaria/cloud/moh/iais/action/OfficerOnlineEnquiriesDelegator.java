package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComplianceHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SysParamUtil;
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
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
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
import java.util.Collections;
import java.util.Comparator;
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
    FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    CessationBeService cessationBeService;
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    InspectionRectificationProService inspectionRectificationProService;
    @Autowired
    HcsaChklClient hcsaChklClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;

    private static final String SEARCH_NO="searchNo";
    private static final String RFI_QUERY="ReqForInfoQuery";


    FilterParameter appLicenceParameter = new FilterParameter.Builder()
            .clz(ApplicationLicenceQueryDto.class)
            .searchAttr("licParam")
            .resultAttr("licResult")
            .sortField("application_no").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(SysParamUtil.getDefaultPageSize()).build();

    FilterParameter licenseeParameter = new FilterParameter.Builder()
            .clz(LicenseeQueryDto.class)
            .searchAttr("licenseeParam")
            .resultAttr("licenseeResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(SysParamUtil.getDefaultPageSize()).build();



    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY);
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
        appLicenceParameter.setPageNo(1);
        licenseeParameter.setPageNo(1);
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
                            licenseeIds.add(UUID.randomUUID().toString());
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if(!"0".equals(count)){
            appLicenceParameter.setFilters(filter);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, appLicenceParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
            String countOld= (String) ParamUtil.getSessionAttr(request,"count");
            if(countOld!=null&&!countOld.equals(count)){
                appParam.setPageNo(1);
            }
            if(licenseeIds.size()!=0){
                String typeStr = SqlHelper.constructInCondition("oev.APP_LICENSEE_ID",licenseeIds.size());
                int indx = 0;
                for (String s : licenseeIds){
                    appParam.addFilter("oev.APP_LICENSEE_ID"+indx, s);
                    indx++;
                }
                appParam.addParam("licenseeId",typeStr);
            }
            QueryHelp.setMainSql(RFI_QUERY,"appLicenceQuery",appParam);
            if (appParam != null) {
                SearchResult<ApplicationLicenceQueryDto> appResult = requestForInformationService.appLicenceDoQuery(appParam);
                Map<String,String> licesee=organizationClient.getAllLicenseeIdName().getEntity();
                if(appResult.getRowCount()!=0){
                    SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();

                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto:appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,licesee);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            licenceIds.add(rfiApplicationQueryDto.getLicenceId());
                            List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(rfiApplicationQueryDto.getLicenceId()).getEntity();
                            List<String> addressList1 = IaisCommonUtils.genNewArrayList();
                            for (PremisesDto premisesDto:premisesDtoList
                            ) {
                                String appAddress=MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode());
                                String licAddress=MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                                addressList1.add(licAddress);  //NOSONAR
                                if(appAddress.equals(licAddress)){
                                    reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                    reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                }
                            }
                            reqForInfoSearchListDto.setAddress(addressList1);
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getLicenceStatus()}).get(0).getText();
                            reqForInfoSearchListDto.setLicenceStatus(licStatus);
                            reqForInfoSearchListDto.setLicenceNo(rfiApplicationQueryDto.getLicenceNo());
                            reqForInfoSearchListDto.setStartDate(rfiApplicationQueryDto.getStartDate());
                            reqForInfoSearchListDto.setExpiryDate(rfiApplicationQueryDto.getExpiryDate());
                        }
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
            ParamUtil.setSessionAttr(request,"SearchParam", appParam);
        }
        ParamUtil.setSessionAttr(request,"count", count);
        // 		preBasicSearch->OnStepProcess
    }

    private void setSearchResult(HttpServletRequest request,SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult,List<String> licenceIds,List<ReqForInfoSearchListDto> reqForInfoSearchListDtos){


        Map<String,Boolean> licIds= cessationBeService.listResultCeased(licenceIds);

        for(ReqForInfoSearchListDto rfi:reqForInfoSearchListDtos){
            if(!"Inactive".equals(rfi.getLicenceStatus())){
                try {
                    if(rfi.getLicenceId()!=null){
                        rfi.setIsCessation(licIds.get(rfi.getLicenceId())?1:0);
                    }else {
                        rfi.setIsCessation(0);
                    }
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
        if(reqForInfoSearchListDtos.size()<1 &&searchListDtoSearchResult.getRowCount()<=10){
            searchListDtoSearchResult.setRowCount(reqForInfoSearchListDtos.size());
        }
        ParamUtil.setSessionAttr(request,"SearchResult", searchListDtoSearchResult);
        try{
            boolean last=searchListDtoSearchResult.getRows().get(searchListDtoSearchResult.getRows().size()-1).getAddress().size()>1;
            boolean last2=searchListDtoSearchResult.getRows().get(searchListDtoSearchResult.getRows().size()-2).getAddress().size()>1;
            if(last||last2){
                ParamUtil.setRequestAttr(request,"blank",Boolean.TRUE);
            }
        }catch (Exception e){
            log.info(e.getMessage());
            try {
                boolean last3=searchListDtoSearchResult.getRows().get(searchListDtoSearchResult.getRows().size()-1).getAddress().size()>1;
                if(last3){
                    ParamUtil.setRequestAttr(request,"blank",Boolean.TRUE);
                }
            }catch (Exception e2){
                log.info(e2.getMessage());
            }
        }

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
            case "3":
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
        appLicenceParameter.setFilters(filters);
        SearchParam licParam = SearchResultHelper.getSearchParam(request, appLicenceParameter,true);
        setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate, svcSubType,licParam);
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
        List<String> svcIds=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        String count=ParamUtil.getString(request,"searchChk");
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,"count");
        }
        ParamUtil.setSessionAttr(request,"count",count);
        switch (count) {
            case "2":
            case "3":
                int appCount=0;
                int licCount=0;
                if(!StringUtil.isEmpty(applicationNo)){
                    filters.put("appNo", applicationNo);appCount++;
                }
                if(!StringUtil.isEmpty(applicationType)){
                    filters.put("appType", applicationType);appCount++;
                }
                if(!StringUtil.isEmpty(status)){
                    if(status.equals(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS)){
                        filters.put("appGrpPmtStatus", status);
                    }else
                    if(!status.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                        filters.put("appStatus", status);
                    }
                    appCount++;
                }
                if(!StringUtil.isEmpty(appSubDate)){
                    filters.put("subDate", appSubDate);appCount++;
                }
                if(!StringUtil.isEmpty(appSubToDate)){
                    filters.put("toDate",appSubToDate);appCount++;
                }
                if(!StringUtil.isEmpty(licStaDate)){
                    filters.put("start_date", licStaDate);licCount++;
                }
                if(!StringUtil.isEmpty(licStaToDate)){
                    filters.put("start_to_date",licStaToDate);licCount++;
                }
                if(!StringUtil.isEmpty(licExpDate)){
                    filters.put("expiry_start_date", licExpDate);licCount++;
                }
                if(!StringUtil.isEmpty(licExpToDate)){
                    filters.put("expiry_date",licExpToDate);licCount++;
                }
                if(!StringUtil.isEmpty(licenceNo)){
                    filters.put("licence_no", licenceNo);licCount++;
                }
                if(!StringUtil.isEmpty(licenceStatus)){
                    filters.put("licence_status", licenceStatus);licCount++;
                }
                if(!StringUtil.isEmpty(serviceLicenceType)){
                    filters.put("svc_name", serviceLicenceType);
                    List<HcsaServiceDto> svcDto = hcsaConfigClient.getHcsaServiceByNames(Collections.singletonList(serviceLicenceType)).getEntity();
                    //filters.put("svc_id",svcDto.getId());
                    for (HcsaServiceDto r:svcDto
                    ) {
                        svcIds.add(r.getId());
                    }
                }
                if(!StringUtil.isEmpty(svcSubType)){
                    filters.put("serviceSubTypeName", svcSubType);
                }
                if(!StringUtil.isEmpty(uenNo)){
                    licCount=10;
                    List<LicenseeDto> licenseeDtos= organizationClient.getLicenseeDtoByUen(uenNo).getEntity();
                    if(licenseeDtos!=null) {
                        for (LicenseeDto licensee:licenseeDtos
                        ) {
                            licenseeIds.add(licensee.getId());
                        }
                    }
                }
                if(appCount!=licCount){
                    if(appCount>licCount){
                        count="2";
                    }else {
                        count="3";
                    }
                }
                ParamUtil.setSessionAttr(request,"count",count);
                break;
            case "1":
                if(!StringUtil.isEmpty(hciCode)){
                    filters.put("hciCode", hciCode);count="3";
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
                        licenseeIds.add(UUID.randomUUID().toString());
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
            appLicenceParameter.setFilters(filters);
            SearchParam licParam = SearchResultHelper.getSearchParam(request, appLicenceParameter,true);
            setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate,svcSubType, licParam);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }
        if(!"0".equals(count)){
            if(svcIds.size()!=0){
                filters.put("svc_ids", svcIds);
            }
            appLicenceParameter.setFilters(filters);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, appLicenceParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
            String countOld= (String) ParamUtil.getSessionAttr(request,"count");
            if(!countOld.equals(count)){
                appParam.setPageNo(1);
            }
            if(status!=null && status.equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                appParam.addParam("appStatus_APPROVED", "(oev.appStatus = 'APST005' OR oev.appStatus = 'APST050')");
            }
            if(!StringUtil.isEmpty(svcSubType) ){
                List<HcsaServiceSubTypeDto> subTypeNames= hcsaChklClient.listSubTypePhase1().getEntity();
                StringBuilder sb=new StringBuilder();
                if ("HIV".equals(svcSubType)) {
                    sb.append('(');
                    for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto:subTypeNames){
                        if(hcsaServiceSubTypeDto.getSubtypeName().contains("HIV")){
                            sb.append(" oev.APP_SCOPE_NAME = '").append(hcsaServiceSubTypeDto.getId()).append("' OR ");
                        }
                    }
                    sb.append("'1' = '0')");
                } else if ("PIG_Screening".equals(svcSubType)) {
                    sb.append('(');
                    for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto:subTypeNames){
                        if("Pre-implantation Genetic Screening".equals(hcsaServiceSubTypeDto.getSubtypeName())){
                            sb.append(" oev.APP_SCOPE_NAME = '").append(hcsaServiceSubTypeDto.getId()).append("' OR ");
                        }
                    }
                    sb.append("'1' = '0')");
                } else if ("PIG_Diagnosis".equals(svcSubType)) {
                    sb.append('(');
                    for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto:subTypeNames){
                        if("Pre-implantation Genetic Diagnosis".equals(hcsaServiceSubTypeDto.getSubtypeName())){
                            sb.append(" oev.APP_SCOPE_NAME = '").append(hcsaServiceSubTypeDto.getId()).append("' OR ");
                        }
                    }
                    sb.append("'1' = '0')");
                }
                appParam.addParam("appSubStatus_HIV", sb);
            }
            if(licenseeIds.size()!=0){
                String typeStr = SqlHelper.constructInCondition("oev.APP_LICENSEE_ID",licenseeIds.size());
                int indx = 0;
                for (String s : licenseeIds){
                    appParam.addFilter("oev.APP_LICENSEE_ID"+indx, s);
                    indx++;
                }
                appParam.addParam("licenseeId",typeStr);
            }
            QueryHelp.setMainSql(RFI_QUERY,"appLicenceQuery",appParam);
            if (appParam != null) {
                SearchResult<ApplicationLicenceQueryDto> appResult = requestForInformationService.appLicenceDoQuery(appParam);
                Map<String,String> licesee=organizationClient.getAllLicenseeIdName().getEntity();

                if(appResult.getRowCount()!=0){
                    SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto:appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto,licesee);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            licenceIds.add(rfiApplicationQueryDto.getLicenceId());
                            List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(rfiApplicationQueryDto.getLicenceId()).getEntity();
                            List<String> addressList1 = IaisCommonUtils.genNewArrayList();
                            for (PremisesDto premisesDto:premisesDtoList
                            ) {
                                String appAddress=MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode());
                                String licAddress=MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                                addressList1.add(licAddress);  //NOSONAR
                                if(appAddress.equals(licAddress)){
                                    reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                    reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                }
                            }
                            reqForInfoSearchListDto.setAddress(addressList1);
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getLicenceStatus()}).get(0).getText();
                            reqForInfoSearchListDto.setLicenceStatus(licStatus);
                            reqForInfoSearchListDto.setLicenceNo(rfiApplicationQueryDto.getLicenceNo());
                            reqForInfoSearchListDto.setStartDate(rfiApplicationQueryDto.getStartDate());
                            reqForInfoSearchListDto.setExpiryDate(rfiApplicationQueryDto.getExpiryDate());
                        }
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
            setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate,svcSubType, appParam);
        }


        // 		doSearchLicence->OnStepProcess
    }

    private void setSearchParamDate(HttpServletRequest request, String uenNo, String appSubDate, String appSubToDate, String licStaDate, String licStaToDate, String licExpDate, String licExpToDate,String svcSubType, SearchParam licParam) throws ParseException {
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

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(ApplicationLicenceQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto,Map<String,String> licesee){
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
            AppPremisesRecommendationDto appPreRecommentdationDtoDateRoot= fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(rfiApplicationQueryDto.getAppCorrId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
            AppPremisesRecommendationDto appPreRecommentdationDtoDate = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(rfiApplicationQueryDto.getAppCorrId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();

            if(appPreRecommentdationDtoDateRoot!=null&&appPreRecommentdationDtoDate!=null){

                List<ComplianceHistoryDto> complianceHistoryDtos= IaisCommonUtils.genNewArrayList();
                Set<String> appIds=IaisCommonUtils.genNewHashSet();
                AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(rfiApplicationQueryDto.getAppCorrId()).getEntity();
                if (appPremPreInspectionNcDto != null) {
                    String ncId = appPremPreInspectionNcDto.getId();
                    List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
                    if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                        ComplianceHistoryDto complianceHistoryDto=new ComplianceHistoryDto();
                        complianceHistoryDto.setComplianceTag("Partial");
                        reqForInfoSearchListDto.setLastComplianceHistory("Partial");
                        complianceHistoryDto.setSortDate(Formatter.formatDateTime(appPreRecommentdationDtoDate.getRecomInDate(), "yyyy-MM-dd"));
                        complianceHistoryDtos.add(complianceHistoryDto);
                        appIds.add(rfiApplicationQueryDto.getId());
                    }
                } else {
                    ComplianceHistoryDto complianceHistoryDto=new ComplianceHistoryDto();
                    reqForInfoSearchListDto.setLastComplianceHistory("Full");
                    complianceHistoryDto.setComplianceTag("Full");
                    complianceHistoryDto.setSortDate(Formatter.formatDateTime(appPreRecommentdationDtoDate.getRecomInDate(), "yyyy-MM-dd"));
                    complianceHistoryDtos.add(complianceHistoryDto);
                    appIds.add(rfiApplicationQueryDto.getId());

                }
                ApplicationDto applicationDto =applicationClient.getApplicationById(rfiApplicationQueryDto.getId()).getEntity();
                if(applicationDto!=null&&applicationDto.getOriginLicenceId()!=null) {
                    complianceHistoryDtos= onlineEnquiriesService.complianceHistoryDtosByLicId(complianceHistoryDtos,applicationDto.getOriginLicenceId(),appIds);
                }
                complianceHistoryDtos.sort(Comparator.comparing(ComplianceHistoryDto::getSortDate));
                reqForInfoSearchListDto.setComplianceHistoryDtos(complianceHistoryDtos);
            }else {
                reqForInfoSearchListDto.setLastComplianceHistory("-");
                reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
            }
        }catch (Exception e){
            log.info(e.getMessage(),e);
            reqForInfoSearchListDto.setLastComplianceHistory("-");
            reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
        }



        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        addressList.add(MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(),rfiApplicationQueryDto.getStreetName(),rfiApplicationQueryDto.getBuildingName(),rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),rfiApplicationQueryDto.getPostalCode()));
        reqForInfoSearchListDto.setAddress(addressList);

        reqForInfoSearchListDto.setCurrentRiskTagging(rfiApplicationQueryDto.getRiskScore());
        if(rfiApplicationQueryDto.getAppLicenseeId()!=null){
            reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getAppLicenseeId());
            reqForInfoSearchListDto.setLicenseeName(licesee.get(rfiApplicationQueryDto.getAppLicenseeId()));

        }else if(rfiApplicationQueryDto.getLicenseeId()!=null){
            reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
            reqForInfoSearchListDto.setLicenseeName(licesee.get(rfiApplicationQueryDto.getLicenseeId()));

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
            log.error("no Masked id", e);
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
                String[] appIdSplit=appId.split("\\|");
                String is = appIdSplit[1];
                String isActive = appIdSplit[3];
                if ("1".equals(is)&&"Active".equals(isActive)) {
                    licIdsSet.add(appIdSplit[2]);
                }
                if ("Active".equals(isActive)) {
                    licRfiIdsSet.add(appIdSplit[2]);
                }
            }
        }catch (Exception e){
            log.debug(StringUtil.changeForLog("appIds and licenceIds ===>" + JsonUtil.parseToJson(appIds)));
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
        List<String> svcIds=IaisCommonUtils.genNewArrayList();
        List<String> licenseeIds=IaisCommonUtils.genNewArrayList();
        String count=(String) ParamUtil.getSessionAttr(request,"count");
        SearchParam parm = (SearchParam) ParamUtil.getSessionAttr(request,"SearchParam");
        switch (count) {
            case "2":
            case "3":
                int appCount=0;
                int licCount=0;
                if(!StringUtil.isEmpty(parm.getFilters().get("appNo"))){
                    filters.put("appNo", parm.getFilters().get("appNo"));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appType"))){
                    filters.put("appType", parm.getFilters().get("appType"));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appStatus"))){
                    if(parm.getFilters().get("appStatus").equals(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS)){
                        filters.put("appGrpPmtStatus", parm.getFilters().get("appStatus"));
                    }else
                    if(!parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)){
                        filters.put("appStatus", parm.getFilters().get("appStatus"));
                    }
                    appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("subDate"))){
                    filters.put("subDate", Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("subDate")),
                            SystemAdminBaseConstants.DATE_FORMAT));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("toDate"))){
                    filters.put("toDate",Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("toDate")),
                            SystemAdminBaseConstants.DATE_FORMAT));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("start_date"))){
                    filters.put("start_date", Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("start_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("start_to_date"))){
                    filters.put("start_to_date",Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("start_to_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("expiry_start_date"))){
                    filters.put("expiry_start_date", Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("expiry_start_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("expiry_date"))){
                    filters.put("expiry_date",Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get("expiry_date")),
                            SystemAdminBaseConstants.DATE_FORMAT));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licence_no"))){
                    filters.put("licence_no", parm.getFilters().get("licence_no"));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licence_status"))){
                    filters.put("licence_status", parm.getFilters().get("licence_status"));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("svc_name"))){
                    filters.put("svc_name", parm.getFilters().get("svc_name"));
                    List<HcsaServiceDto> svcDto = hcsaConfigClient.getHcsaServiceByNames(Collections.singletonList((String) parm.getFilters().get("svc_name"))).getEntity();
                    //filters.put("svc_id",svcDto.getId());
                    for (HcsaServiceDto r:svcDto
                    ) {
                        svcIds.add(r.getId());
                    }
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("serviceSubTypeName"))){
                    filters.put("serviceSubTypeName", parm.getFilters().get("serviceSubTypeName"));

                }
                if(!StringUtil.isEmpty(parm.getFilters().get("uen_no"))){
                    licCount=10;
                    List<LicenseeDto> licenseeDtos= organizationClient.getLicenseeDtoByUen((String) parm.getFilters().get("uen_no")).getEntity();
                    if(licenseeDtos!=null) {
                        for (LicenseeDto licensee:licenseeDtos
                        ) {
                            licenseeIds.add(licensee.getId());
                        }
                    }
                }
                if(appCount!=licCount){
                    if(appCount>licCount){
                        count="2";
                    }else {
                        count="3";
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
                        licenseeIds.add(UUID.randomUUID().toString());
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
        if(!"0".equals(count)){
            if (svcIds.size() != 0) {
                filters.put("svc_ids", svcIds);
            }
            appLicenceParameter.setFilters(filters);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, appLicenceParameter, true);
            if (appParam != null) {
                appParam.setPageNo(0);
                if (parm.getFilters().get("appStatus") != null && parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_APPROVED)) {
                    appParam.addParam("appStatus_APPROVED", "(oev.appStatus = 'APST005' OR oev.appStatus = 'APST050')");
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("serviceSubTypeName")) ){
                    List<HcsaServiceSubTypeDto> subTypeNames= hcsaChklClient.listSubTypePhase1().getEntity();
                    StringBuilder sb=new StringBuilder();
                    Object serviceSubTypeName = parm.getFilters().get("serviceSubTypeName");
                    if ("HIV".equals(serviceSubTypeName)) {
                        sb.append('(');
                        for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto:subTypeNames){
                            if(hcsaServiceSubTypeDto.getSubtypeName().contains("HIV")){
                                sb.append(" oev.APP_SCOPE_NAME = '").append(hcsaServiceSubTypeDto.getId()).append("' OR ");
                            }
                        }
                        sb.append("'1' = '0')");
                    } else if ("PIG_Screening".equals(serviceSubTypeName)) {
                        sb.append('(');
                        for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto:subTypeNames){
                            if("Pre-implantation Genetic Screening".equals(hcsaServiceSubTypeDto.getSubtypeName())){
                                sb.append(" oev.APP_SCOPE_NAME = '").append(hcsaServiceSubTypeDto.getId()).append("' OR ");
                            }
                        }
                        sb.append("'1' = '0')");
                    } else if ("PIG_Diagnosis".equals(serviceSubTypeName)) {
                        sb.append('(');
                        for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto:subTypeNames){
                            if("Pre-implantation Genetic Diagnosis".equals(hcsaServiceSubTypeDto.getSubtypeName())){
                                sb.append(" oev.APP_SCOPE_NAME = '").append(hcsaServiceSubTypeDto.getId()).append("' OR ");
                            }
                        }
                        sb.append("'1' = '0')");
                    }
                    appParam.addParam("appSubStatus_HIV", sb);
                }
                if(licenseeIds.size()!=0){
                    String typeStr = SqlHelper.constructInCondition("oev.APP_LICENSEE_ID",licenseeIds.size());
                    int indx = 0;
                    for (String s : licenseeIds){
                        appParam.addFilter("oev.APP_LICENSEE_ID"+indx, s);
                        indx++;
                    }
                    appParam.addParam("licenseeId",typeStr);
                }
                QueryHelp.setMainSql(RFI_QUERY, "appLicenceQuery", appParam);
                if (appParam != null) {
                    SearchResult<ApplicationLicenceQueryDto> appResult = requestForInformationService.appLicenceDoQuery(appParam);
                    Map<String,String> licesee=organizationClient.getAllLicenseeIdName().getEntity();

                    if (appResult.getRowCount() != 0) {
                        searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                        List<ReqForInfoSearchListDto> reqForInfoSearchListDtos = IaisCommonUtils.genNewArrayList();

                        for (ApplicationLicenceQueryDto rfiApplicationQueryDto : appResult.getRows()
                        ) {
                            ReqForInfoSearchListDto reqForInfoSearchListDto = new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto, reqForInfoSearchListDto, licesee);
                            reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                            if(rfiApplicationQueryDto.getLicenceId()!=null){
                                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(rfiApplicationQueryDto.getLicenceId()).getEntity();
                                for (PremisesDto premisesDto : premisesDtoList
                                ) {
                                    String appAddress = MiscUtil.getAddress(rfiApplicationQueryDto.getBlkNo(), rfiApplicationQueryDto.getStreetName(), rfiApplicationQueryDto.getBuildingName(), rfiApplicationQueryDto.getFloorNo(), rfiApplicationQueryDto.getUnitNo(), rfiApplicationQueryDto.getPostalCode());
                                    String licAddress = MiscUtil.getAddress(premisesDto.getBlkNo(), premisesDto.getStreetName(), premisesDto.getBuildingName(), premisesDto.getFloorNo(), premisesDto.getUnitNo(), premisesDto.getPostalCode());
                                    if (appAddress.equals(licAddress)) {
                                        reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                                        reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                                    }
                                }
                                String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getLicenceStatus()}).get(0).getText();
                                reqForInfoSearchListDto.setLicenceStatus(licStatus);
                                reqForInfoSearchListDto.setLicenceNo(rfiApplicationQueryDto.getLicenceNo());
                                reqForInfoSearchListDto.setStartDate(rfiApplicationQueryDto.getStartDate());
                                reqForInfoSearchListDto.setExpiryDate(rfiApplicationQueryDto.getExpiryDate());
                            }
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                        searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                    }
                }
            }
        }
        log.debug("============> End of the query!");
        List<ReqForInfoSearchListDto> queryList=IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> entity = hcsaConfigClient.allHcsaService().getEntity();
        Map<String,String> mapIdSvcName=IaisCommonUtils.genNewHashMap();
        for (HcsaServiceDto svc:entity
             ) {
            mapIdSvcName.put(svc.getId(),svc.getSvcName());
        }
        for (ReqForInfoSearchListDto info:searchListDtoSearchResult.getRows()
        ) {
            try {
                info.setServiceName(mapIdSvcName.get(info.getServiceName()));
            }catch (Exception e){
                info.setServiceName("-");
                log.debug("ServiceName is null");
            }
            try {
                info.setAddresses(info.getAddress().get(0));
            }catch (Exception e){
                info.setAddresses("-");
                log.debug("Addresse is null");
            }
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
