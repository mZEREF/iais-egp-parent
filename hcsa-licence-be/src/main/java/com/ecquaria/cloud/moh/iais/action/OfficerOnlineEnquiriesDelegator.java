package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
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
    ConfigCommService configCommService;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    CessationBeService cessationBeService;
    @Autowired
    HcsaChklClient hcsaChklClient;
    @Autowired
    SystemParamConfig systemParamConfig;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private ApplicationService applicationService;

    private static final String IS_BASIC = "isBasic";
    private static final String SEARCH_RESULT = "SearchResult";
    private static final String SEARCH_PARAM = "SearchParam";
    private static final String COUNT = "count";
    private static final String LIC_IDS = "licIds";
    private static final String SEARCH_CHK = "searchChk";
    private static final String APP_NO = "appNo";
    private static final String LICENCE_NO = "licence_no";
    private static final String HCI_NAME = "hciName";
    private static final String PERSONNEL_NAME = "personnelName";
    private static final String LICENSEE_NAME = "licenseeName";
    private static final String UEN_NO = "uen_no";
    private static final String APPLICENCE_QUERY = "appLicenceQuery";
    private static final String APPLICATION_STATUS = "application_status";
    private static final String LICENCE_STATUS = "licence_status";
    private static final String PERSONNEL_ID = "personnelId";
    private static final String PERSONNEL_REGNNO = "personnelRegnNo";
    private static final String PERSONNEL_ROLE = "personnelRole";
    private static final String SUB_DATE = "sub_date";
    private static final String TO_DATE = "to_date";
    private static final String START_DATE = "start_date";

    private static final String SEARCH_NO="searchNo";
    private static final String RFI_QUERY="ReqForInfoQuery";
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter appLicenceParameter = new FilterParameter.Builder()
            .clz(ApplicationLicenceQueryDto.class)
            .searchAttr("licParam")
            .resultAttr("licResult")
            .sortField("LIC_APP_KEY_ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();




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
        ParamUtil.setSessionAttr(request,IS_BASIC,null);
        ParamUtil.setSessionAttr(request,SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request,SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request,COUNT, null);
        ParamUtil.setSessionAttr(request,LIC_IDS, null);
        ParamUtil.setSessionAttr(request,"licRfiIds", null);
        ParamUtil.setSessionAttr(request, "kpiInfo", null);

        appLicenceParameter.setPageNo(1);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        appLicenceParameter.setPageSize(pageSize);
        // 		Start->OnStepProcess
    }


    public void preBasicSearch(BaseProcessClass bpc)  {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>OnlineEnquiries");

        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,IS_BASIC,Boolean.TRUE);
        ParamUtil.setSessionAttr(request,SEARCH_RESULT, null);
        String searchNo=ParamUtil.getString(request,SEARCH_NO);
        ParamUtil.setSessionAttr(request,SEARCH_NO,searchNo);
        String count=ParamUtil.getString(request,SEARCH_CHK);
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,COUNT);
            if(count==null){
                count="0";
            }
        }
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        if(searchNo!=null) {
            switch (count) {
                case "2":
                    filter.put(APP_NO, searchNo);
                    break;
                case "3":
                    filter.put(LICENCE_NO, searchNo);
                    break;
                case "1":
                    filter.put(HCI_NAME, searchNo);
                    break;
                case "5":
                    filter.put(PERSONNEL_NAME, searchNo);
                    break;
                case "4":
                    filter.put(LICENSEE_NAME, searchNo);
                    break;
                case "6":
                    filter.put(UEN_NO, searchNo);
                    break;
                default:
                    break;
            }
        }

        if(!"0".equals(count)){
            appLicenceParameter.setFilters(filter);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, appLicenceParameter,true);
            CrudHelper.doPaging(appParam,bpc.request);
            String countOld= (String) ParamUtil.getSessionAttr(request,COUNT);
            if(countOld!=null&&!countOld.equals(count)){
                appParam.setPageNo(1);
            }
            QueryHelp.setMainSql(RFI_QUERY,APPLICENCE_QUERY,appParam);
            if (appParam != null) {
                SearchResult<ApplicationLicenceQueryDto> appResult = requestForInformationService.appLicenceDoQuery(appParam);
                if(appResult.getRowCount()!=0){
                    SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();

                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto:appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            licenceIds.add(rfiApplicationQueryDto.getLicenceId());
                            setReqForInfoSearchListDtoLicenceInfo(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        }
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
            ParamUtil.setSessionAttr(request,SEARCH_PARAM, appParam);
        }
        ParamUtil.setSessionAttr(request,COUNT, count);
        // 		preBasicSearch->OnStepProcess
    }


    private void setSearchResult(HttpServletRequest request,SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult,List<String> licenceIds,List<ReqForInfoSearchListDto> reqForInfoSearchListDtos){


        Map<String,Boolean> licIds= cessationBeService.listResultCeased(licenceIds);

        for(ReqForInfoSearchListDto rfi:reqForInfoSearchListDtos){
            rfi.setIsCessation(2);
            try {
                if(rfi.getLicenceId()!=null){
                    LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(rfi.getLicenceId()).getEntity();
                    if(licIds.get(rfi.getLicenceId())){
                        if(licenceDto.getStatus().equals(ApplicationConsts.LICENCE_STATUS_ACTIVE)){
                            rfi.setIsCessation(1);// can
                        }else if(IaisEGPHelper.isActiveMigrated()&&licenceDto.getStatus().equals(ApplicationConsts.LICENCE_STATUS_APPROVED)&&licenceDto.getMigrated()!=0){
                            rfi.setIsCessation(1);// can
                        }else {
                            rfi.setIsCessation(0);//not ACTIVE
                        }
                    }else {
                        rfi.setIsCessation(2);// not can
                    }
                }
            }catch (Exception e){
                rfi.setIsCessation(2);// not can
            }
        }

        searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
        if(reqForInfoSearchListDtos.size()<1 &&searchListDtoSearchResult.getRowCount()<=10){
            searchListDtoSearchResult.setRowCount(reqForInfoSearchListDtos.size());
        }
        ParamUtil.setSessionAttr(request,SEARCH_RESULT, searchListDtoSearchResult);
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

        String count=ParamUtil.getString(request,SEARCH_CHK);

        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,COUNT);
        }
        ParamUtil.setSessionAttr(request,COUNT,count);
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
        String status = ParamUtil.getString(bpc.request, APPLICATION_STATUS);
        String licenceNo = ParamUtil.getString(bpc.request, LICENCE_NO);
        String uenNo = ParamUtil.getString(bpc.request, UEN_NO);
        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, LICENCE_STATUS);
        String svcSubType=ParamUtil.getString(bpc.request,"service_sub_type");
        String hciCode = ParamUtil.getString(bpc.request, "hci_code");
        String hciName = ParamUtil.getString(bpc.request, "hci_name");
        String hciStreetName = ParamUtil.getString(bpc.request, "hci_street_name");
        String hciPostalCode = ParamUtil.getString(bpc.request, "hci_postal_code");
        String licenseeIdNo = ParamUtil.getString(bpc.request, "licensee_idNo");
        String licenseeName = ParamUtil.getString(bpc.request, "licensee_name");
        String licenseeRegnNo = ParamUtil.getString(bpc.request, "licensee_regn_no");
        String personnelId = ParamUtil.getString(bpc.request, PERSONNEL_ID);
        String personnelName = ParamUtil.getString(bpc.request, PERSONNEL_NAME);
        String personnelRegnNo = ParamUtil.getString(bpc.request, PERSONNEL_REGNNO);
        String personnelRole = ParamUtil.getString(bpc.request, PERSONNEL_ROLE);
        String appSubDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SUB_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String appSubToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, TO_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        String licStaDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, START_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licStaToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_start_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String count=ParamUtil.getString(request,SEARCH_CHK);
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,COUNT);
        }
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        switch (count) {
            case "3":
                if(!StringUtil.isEmpty(applicationNo)){
                    filters.put(APP_NO, applicationNo);
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
                    filters.put(START_DATE, licStaDate);
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
                    filters.put(LICENCE_NO, licenceNo);
                }
                if(!StringUtil.isEmpty(licenceStatus)){
                    filters.put(LICENCE_STATUS, licenceStatus);
                }
                if(!StringUtil.isEmpty(serviceLicenceType)){
                    filters.put("svc_name", serviceLicenceType);
                }
                if(!StringUtil.isEmpty(svcSubType)){
                    filters.put("serviceSubTypeName", svcSubType);
                }
                if(!StringUtil.isEmpty(uenNo)){
                    filters.put(UEN_NO, uenNo);
                }
                break;
            case "1":
                if(!StringUtil.isEmpty(hciCode)){
                    filters.put("hciCode", hciCode);
                }
                if(!StringUtil.isEmpty(hciName)){
                    filters.put(HCI_NAME, hciName);
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
                    filters.put(PERSONNEL_ID, personnelId);
                }
                if(!StringUtil.isEmpty(personnelName)){
                    filters.put(PERSONNEL_NAME,personnelName);
                }
                if(!StringUtil.isEmpty(personnelRegnNo)){
                    filters.put(PERSONNEL_REGNNO, personnelRegnNo);
                }
                if(!StringUtil.isEmpty(personnelRole)){
                    filters.put(PERSONNEL_ROLE, personnelRole);
                }
                break;
            case "4":
                if(!StringUtil.isEmpty(licenseeIdNo)){
                    filters.put("licenseeIdNo",licenseeIdNo);
                }
                if(!StringUtil.isEmpty(licenseeName)){
                    filters.put(LICENSEE_NAME, licenseeName);
                }
                if(!StringUtil.isEmpty(licenseeRegnNo)){
                    filters.put("licenseeRegnNo",licenseeRegnNo);
                }
                if(!StringUtil.isEmpty(uenNo)){
                    filters.put(UEN_NO, uenNo);
                }
                break;
            default:
                break;
        }
        appLicenceParameter.setFilters(filters);
        SearchParam licParam = SearchResultHelper.getSearchParam(request, appLicenceParameter,true);
        setSearchParamDate(request, uenNo, appSubDate, appSubToDate, licStaDate, licStaToDate, licExpDate, licExpToDate, svcSubType,licParam);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap ;
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
        ParamUtil.setSessionAttr(request,IS_BASIC,Boolean.FALSE);
        preSelectOption(request);
        ParamUtil.setSessionAttr(request,SEARCH_RESULT, null);
        String applicationNo = ParamUtil.getString(bpc.request, "application_no");
        String applicationType = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, APPLICATION_STATUS);
        String licenceNo = ParamUtil.getString(bpc.request, LICENCE_NO);
        String uenNo = ParamUtil.getString(bpc.request, UEN_NO);
        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, LICENCE_STATUS);
        String svcSubType=ParamUtil.getString(bpc.request,"service_sub_type");
        String hciCode = ParamUtil.getString(bpc.request, "hci_code");
        String hciName = ParamUtil.getString(bpc.request, "hci_name");
        String hciStreetName = ParamUtil.getString(bpc.request, "hci_street_name");
        String hciPostalCode = ParamUtil.getString(bpc.request, "hci_postal_code");
        String licenseeIdNo = ParamUtil.getString(bpc.request, "licensee_idNo");
        String licenseeName = ParamUtil.getString(bpc.request, "licensee_name");
        String licenseeRegnNo = ParamUtil.getString(bpc.request, "licensee_regn_no");
        String personnelId = ParamUtil.getString(bpc.request, PERSONNEL_ID);
        String personnelName = ParamUtil.getString(bpc.request, PERSONNEL_NAME);
        String personnelRegnNo = ParamUtil.getString(bpc.request, PERSONNEL_REGNNO);
        String personnelRole = ParamUtil.getString(bpc.request, PERSONNEL_ROLE);
        String appSubDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SUB_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String appSubToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, TO_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        String licStaDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, START_DATE)),
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
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        String count=ParamUtil.getString(request,SEARCH_CHK);
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,COUNT);
        }
        ParamUtil.setSessionAttr(request,COUNT,count);
        switch (count) {
            case "2":
            case "3":
                int appCount=0;
                int licCount=0;
                if(!StringUtil.isEmpty(applicationNo)){
                    filters.put(APP_NO, applicationNo);appCount++;
                }
                if(!StringUtil.isEmpty(applicationType)){
                    filters.put("appType", applicationType);appCount++;
                }
                if(!StringUtil.isEmpty(status)){
                    filters.put("appStatus", status);
                    appCount++;
                }
                if(!StringUtil.isEmpty(appSubDate)){
                    filters.put("subDate", appSubDate);appCount++;
                }
                if(!StringUtil.isEmpty(appSubToDate)){
                    filters.put("toDate",appSubToDate);appCount++;
                }
                if(!StringUtil.isEmpty(licStaDate)){
                    filters.put(START_DATE, licStaDate);licCount++;
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
                    filters.put(LICENCE_NO, licenceNo);licCount++;
                }
                if(!StringUtil.isEmpty(licenceStatus)){
                    filters.put(LICENCE_STATUS, licenceStatus);licCount++;
                }
                if(!StringUtil.isEmpty(serviceLicenceType)){
                    filters.put("svc_name", serviceLicenceType);
                    List<HcsaServiceDto> svcDto = configCommService.getHcsaServiceByNames(Collections.singletonList(serviceLicenceType));
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
                    filters.put(UEN_NO, uenNo);
                }
                if(appCount!=licCount){
                    if(appCount>licCount){
                        count="2";
                    }else {
                        count="3";
                    }
                }
                ParamUtil.setSessionAttr(request,COUNT,count);
                break;
            case "1":
                if(!StringUtil.isEmpty(hciCode)){
                    filters.put("hciCode", hciCode);count="3";
                }
                if(!StringUtil.isEmpty(hciName)){
                    filters.put(HCI_NAME, hciName);
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
                    filters.put(PERSONNEL_ID, personnelId);
                }
                if(!StringUtil.isEmpty(personnelName)){
                    filters.put(PERSONNEL_NAME,personnelName);
                }
                if(!StringUtil.isEmpty(personnelRegnNo)){
                    filters.put(PERSONNEL_REGNNO, personnelRegnNo);
                }
                if(!StringUtil.isEmpty(personnelRole)){
                    filters.put(PERSONNEL_ROLE, personnelRole);
                }
                break;
            case "4":
                if(!StringUtil.isEmpty(licenseeIdNo)){
                    filters.put("licenseeIdNo",licenseeIdNo);
                }
                if(!StringUtil.isEmpty(licenseeName)){
                    filters.put(LICENSEE_NAME, licenseeName);
                }
                if(!StringUtil.isEmpty(licenseeRegnNo)){
                    filters.put("licenseeRegnNo",licenseeRegnNo);
                }
                if(!StringUtil.isEmpty(uenNo)){
                    filters.put(UEN_NO, uenNo);
                }
                break;
            default:
                break;
        }

        Map<String, String> errorMap ;
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
            String countOld= (String) ParamUtil.getSessionAttr(request,COUNT);
            if(!countOld.equals(count)){
                appParam.setPageNo(1);
            }
            if(status!=null){
                setSearchParamAppStatus(status,appParam);
            }else {
                appParam.removeFilter("appStatus");
            }

            if (appParam != null) {
                SearchResult<ApplicationLicenceQueryDto> appResult;
                if (status != null && status.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                    QueryHelp.setMainSql(RFI_QUERY,"appLicenceForCommPoolQuery",appParam);
                    appResult = requestForInformationService.appLicenceDoForCommPoolQuery(appParam);
                }else {
                    QueryHelp.setMainSql(RFI_QUERY,APPLICENCE_QUERY,appParam);
                    appResult = requestForInformationService.appLicenceDoQuery(appParam);
                }
                if(appResult.getRowCount()!=0){
                    SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto:appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            licenceIds.add(rfiApplicationQueryDto.getLicenceId());
                            setReqForInfoSearchListDtoLicenceInfo(rfiApplicationQueryDto,reqForInfoSearchListDto);
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

    private void setSearchParamAppStatus(String status,SearchParam appParam ){
        switch (status){
            case ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS:
                appParam.addParam("appStatusAll", "(oev.PMT_STATUS = 'PMT02' OR oev.PMT_STATUS = 'PMT01')");
                break;
            case ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO:
                appParam.addParam("appStatusAll", "(oev.PMT_STATUS = 'PMT03' )");
                break;
            default:
                List<String> inParams = IaisCommonUtils.genNewArrayList();
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Inspection");
                }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Screening");
                } else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Approval");
                }else if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Approved");
                }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Re-Scheduling");
                }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Scheduling");
                }else if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Clarification");
                }else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Rejected");
                }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Clarification");
                }else if(ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Inspector Enquire");
                }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Internal Clarification");
                }else if(ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE.equals(status)){
                    inParams = MasterCodeUtil.getCodeKeyByCodeValue("Professional Screening Officer Enquire");
                }
                else{
                    inParams.add(status);
                }
                SqlHelper.builderInSql(appParam, "oev.appStatus", "appStatusAll", inParams);
        }
    }

    private void setSearchParamDate(HttpServletRequest request, String uenNo, String appSubDate, String appSubToDate, String licStaDate, String licStaToDate, String licExpDate, String licExpToDate,String svcSubType, SearchParam licParam) throws ParseException {
        if (licParam==null){
            return;
        }
        if(!StringUtil.isEmpty(licStaDate)){
            licParam.getFilters().put(START_DATE, Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, START_DATE)),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(licStaToDate)){
            licParam.getFilters().put("start_to_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_to_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(appSubDate)){
            licParam.getFilters().put("subDate",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SUB_DATE)),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(appSubToDate)){
            licParam.getFilters().put("toDate",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, TO_DATE)),
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

        String appStatus=ParamUtil.getString(request, APPLICATION_STATUS);

        ParamUtil.setSessionAttr(request,SEARCH_PARAM, licParam);
    }


    private void setReqForInfoSearchListDtoLicenceInfo(ApplicationLicenceQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto){
        String licStatus = MasterCodeUtil.getCodeDesc(rfiApplicationQueryDto.getLicenceStatus());
        reqForInfoSearchListDto.setLicenceStatus(licStatus);
        reqForInfoSearchListDto.setLicenceNo(rfiApplicationQueryDto.getLicenceNo());
        reqForInfoSearchListDto.setStartDate(rfiApplicationQueryDto.getStartDate());
        reqForInfoSearchListDto.setExpiryDate(rfiApplicationQueryDto.getExpiryDate());
        if(reqForInfoSearchListDto.getAppId()==null){
            reqForInfoSearchListDto.setServiceName(rfiApplicationQueryDto.getServiceName());
        }
        List<String> addressList1 = IaisCommonUtils.genNewArrayList();
        reqForInfoSearchListDto.setHciCode(rfiApplicationQueryDto.getLicHciCode());
        reqForInfoSearchListDto.setHciName(rfiApplicationQueryDto.getLicHciName());
        reqForInfoSearchListDto.setUen(rfiApplicationQueryDto.getLicUenNo());
        addressList1.add(rfiApplicationQueryDto.getLicPremType());
        reqForInfoSearchListDto.setAddress(addressList1);

        List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(rfiApplicationQueryDto.getLicenceId()).getEntity();
        String appPremisesKey=IaisCommonUtils.getPremisesKey(rfiApplicationQueryDto.getHciName(),rfiApplicationQueryDto.getPostalCode(),rfiApplicationQueryDto.getBlkNo(),"","",rfiApplicationQueryDto.getFloorNo(),rfiApplicationQueryDto.getUnitNo(),null);

        boolean addressEquals=false;
        for (PremisesDto premisesDto:premisesDtoList
        ) {
            String licPremisesKey=IaisCommonUtils.getPremisesKey(premisesDto.getHciName(),premisesDto.getPostalCode(),premisesDto.getBlkNo(),"","",premisesDto.getFloorNo(),premisesDto.getUnitNo(),null);
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList=hcsaLicenceClient.getPremisesFloorUnits(premisesDto.getId()).getEntity();
            String licAddress=IaisCommonUtils.getAddressForApp(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode(),appPremisesOperationalUnitDtoList);
            if(StringUtil.isEmpty(premisesDto.getBlkNo())){
                licAddress=" "+licAddress;
            }
            if(appPremisesKey.equals(licPremisesKey)){
                reqForInfoSearchListDto.setHciCode(premisesDto.getHciCode());
                reqForInfoSearchListDto.setHciName(premisesDto.getHciName());
                addressEquals=true;
            }
            addressList1.add(licAddress);
        }
        if(addressEquals){
            reqForInfoSearchListDto.setAddress(addressList1);
        }

    }

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(ApplicationLicenceQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto){
        reqForInfoSearchListDto.setAppId(rfiApplicationQueryDto.getId());
        String appType= MasterCodeUtil.getCodeDesc(rfiApplicationQueryDto.getApplicationType());
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
        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        if(rfiApplicationQueryDto.getAppUenNo()!=null){
            reqForInfoSearchListDto.setUen(rfiApplicationQueryDto.getAppUenNo());
        }else {
            reqForInfoSearchListDto.setUen("-");
        }
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        addressList.add(rfiApplicationQueryDto.getPremType());
        reqForInfoSearchListDto.setAddress(addressList);

        if( rfiApplicationQueryDto.getLastIsNc()==null){
            reqForInfoSearchListDto.setLastComplianceHistory("-");
            reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
        }else {
            if(rfiApplicationQueryDto.getLastIsNc()==0){
                reqForInfoSearchListDto.setLastComplianceHistory("Full");
            }else {
                reqForInfoSearchListDto.setLastComplianceHistory("Partial");
            }
            if(rfiApplicationQueryDto.getSecIsNc()==null){
                reqForInfoSearchListDto.setTwoLastComplianceHistory("-");
            }else {
                if(rfiApplicationQueryDto.getSecIsNc()==0){
                    reqForInfoSearchListDto.setTwoLastComplianceHistory("Full");
                }else {
                    reqForInfoSearchListDto.setTwoLastComplianceHistory("Partial");
                }
            }
        }
        if(rfiApplicationQueryDto.getRiskScore()==null){
            reqForInfoSearchListDto.setCurrentRiskTagging("-");

        }else {
            reqForInfoSearchListDto.setCurrentRiskTagging(MasterCodeUtil.getCodeDesc(rfiApplicationQueryDto.getRiskScore()));
        }

        if(rfiApplicationQueryDto.getAppLicenseeIdName()!=null){
            reqForInfoSearchListDto.setLicenseeName(rfiApplicationQueryDto.getAppLicenseeIdName());
        }else if(rfiApplicationQueryDto.getLicLicenseeIdName()!=null){
            reqForInfoSearchListDto.setLicenseeName(rfiApplicationQueryDto.getLicLicenseeIdName());
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
            log.info("no Masked id");
        }
        String count=ParamUtil.getString(request,SEARCH_CHK);
        if(count==null){
            count= (String) ParamUtil.getSessionAttr(request,COUNT);
        }
        ParamUtil.setSessionAttr(request,COUNT,count);
        String [] appIds=ParamUtil.getStrings(request,"appIds");
        Set<String> licIdsSet=IaisCommonUtils.genNewHashSet();
        Set<String> licRfiIdsSet=IaisCommonUtils.genNewHashSet();
        try{
            if(appIds!=null){
                for (String appId : appIds) {
                    String[] appIdSplit=appId.split("\\|");
                    String is = appIdSplit[1];
                    if(appIdSplit.length>2){
                        String isActive = appIdSplit[3];
                        if ("1".equals(is)) {
                            licIdsSet.add(appIdSplit[2]);
                        }
                        if ("Active".equals(isActive)) {
                            licRfiIdsSet.add(appIdSplit[2]);
                        }
                    }
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
        ParamUtil.setSessionAttr(request,LIC_IDS, (Serializable) licIds);
        ParamUtil.setSessionAttr(request,"licRfiIds", (Serializable) licRfiIds);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void callCessation(BaseProcessClass bpc) {
        log.info("=======>>>>>callCessation>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, LIC_IDS);
        ParamUtil.setSessionAttr(request,LIC_IDS, (Serializable) licenceIds);

        // 		callCessation->OnStepProcess
    }

    public void callReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>callReqForInfo>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, LIC_IDS);
        ParamUtil.setSessionAttr(request,LIC_IDS, (Serializable) licenceIds);

        // 		callReqForInfo->OnStepProcess
    }


    public void preLicDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preLicDetails>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setLicInfo(request);
        if(ParamUtil.getString(bpc.request, "preInspReport")!=null){
            ParamUtil.setRequestAttr(bpc.request, "preInspReport", "1");
        }
        // 		preLicDetails->OnStepProcess
    }

    public void preInspReport(BaseProcessClass bpc) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>OnlineEnquiries");
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(bpc.request, "kpiInfo", kpiInfo);
        HttpServletRequest request=bpc.request;
        String appPremisesCorrelationId=ParamUtil.getMaskedString(request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        String licenceId = (String) ParamUtil.getSessionAttr(request, "id");
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appPremisesCorrelationId);

        //get vehicleNoList for edit
        List<String> vehicleNoList = applicationService.getVehicleNoByFlag(InspectionConstants.SWITCH_ACTION_VIEW, applicationViewDto);
        //sort AppSvcVehicleDto List
        applicationService.sortAppSvcVehicleListToShow(vehicleNoList, applicationViewDto);
        onlineEnquiriesService.getInspReport(bpc,appPremisesCorrelationId,licenceId);
        ParamUtil.setRequestAttr(bpc.request, "applicationViewDto", applicationViewDto);
        // 		preAppInfo->OnStepProcess
    }

    public void preAppDetails(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setAppInfo(request);
    }

    public void check(BaseProcessClass bpc) {
        log.info("=======>>>>>check>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        Boolean b= (Boolean) ParamUtil.getSessionAttr(request,IS_BASIC);
        ParamUtil.setRequestAttr(bpc.request, "isCheck", "N");
        if(b){
            ParamUtil.setRequestAttr(bpc.request, "isCheck", "Y");
        }

    }

    public void backBasic(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>backBasic>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        List<String> svcIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        String count=(String) ParamUtil.getSessionAttr(request,COUNT);
        SearchParam parm = (SearchParam) ParamUtil.getSessionAttr(request,SEARCH_PARAM);
        switch (count) {
            case "2":
            case "3":
                int appCount=0;
                int licCount=0;
                if(!StringUtil.isEmpty(parm.getFilters().get(APP_NO))){
                    filters.put(APP_NO, parm.getFilters().get(APP_NO));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appType"))){
                    filters.put("appType", parm.getFilters().get("appType"));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appStatus"))){
                    String status= (String) parm.getFilters().get("appStatus");
                    filters.put("appStatus", status);
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
                if(!StringUtil.isEmpty(parm.getFilters().get(START_DATE))){
                    filters.put(START_DATE, Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get(START_DATE)),
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
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENCE_NO))){
                    filters.put(LICENCE_NO, parm.getFilters().get(LICENCE_NO));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENCE_STATUS))){
                    filters.put(LICENCE_STATUS, parm.getFilters().get(LICENCE_STATUS));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("svc_name"))){
                    filters.put("svc_name", parm.getFilters().get("svc_name"));
                    List<HcsaServiceDto> svcDto = configCommService.getHcsaServiceByNames(Collections.singletonList((String) parm.getFilters().get("svc_name")));
                    //filters.put("svc_id",svcDto.getId());
                    for (HcsaServiceDto r:svcDto
                    ) {
                        svcIds.add(r.getId());
                    }
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("serviceSubTypeName"))){
                    filters.put("serviceSubTypeName", parm.getFilters().get("serviceSubTypeName"));

                }
                if(!StringUtil.isEmpty(parm.getFilters().get(UEN_NO))){
                    filters.put(UEN_NO, parm.getFilters().get(UEN_NO));
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
                if(!StringUtil.isEmpty(parm.getFilters().get(HCI_NAME))){
                    filters.put(HCI_NAME, parm.getFilters().get(HCI_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciPostalCode"))){
                    filters.put("hciPostalCode", parm.getFilters().get("hciPostalCode"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciStreetName"))){
                    filters.put("hciStreetName", parm.getFilters().get("hciStreetName"));
                }
                break;
            case "5":
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_ID))){
                    filters.put(PERSONNEL_ID, parm.getFilters().get(PERSONNEL_ID));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_NAME))){
                    filters.put(PERSONNEL_NAME,parm.getFilters().get(PERSONNEL_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_REGNNO))){
                    filters.put(PERSONNEL_REGNNO, parm.getFilters().get(PERSONNEL_REGNNO));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_ROLE))){
                    filters.put(PERSONNEL_ROLE, parm.getFilters().get(PERSONNEL_ROLE));
                }
                break;
            case "4":
            case "6":
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeIdNo"))){
                    filters.put("licenseeIdNo",parm.getFilters().get("licenseeIdNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENSEE_NAME))){
                    filters.put(LICENSEE_NAME, parm.getFilters().get(LICENSEE_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeRegnNo"))){
                    filters.put("licenseeRegnNo",parm.getFilters().get("licenseeRegnNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(UEN_NO))){
                    filters.put(UEN_NO, parm.getFilters().get(UEN_NO));
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
                if(parm!=null){
                    appParam.setPageNo(parm.getPageNo());
                    appParam.setPageSize(parm.getPageSize());
                    if(parm.getFilters()!=null){
                        String status= (String) parm.getFilters().get("appStatus");
                        if(status!=null){
                            setSearchParamAppStatus(status,appParam);
                        }else {
                            appParam.removeFilter("appStatus");
                        }
                    }
                }

                SearchResult<ApplicationLicenceQueryDto> appResult;
                if (parm!=null&&parm.getFilters()!=null&&parm.getFilters().get("appStatus") != null && parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                    QueryHelp.setMainSql(RFI_QUERY,"appLicenceForCommPoolQuery",appParam);
                    appResult = requestForInformationService.appLicenceDoForCommPoolQuery(appParam);
                }else {
                    QueryHelp.setMainSql(RFI_QUERY,APPLICENCE_QUERY,appParam);
                    appResult = requestForInformationService.appLicenceDoQuery(appParam);
                }
                if (appResult.getRowCount() != 0) {
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos = IaisCommonUtils.genNewArrayList();

                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto : appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto = new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto, reqForInfoSearchListDto);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            licenceIds.add(rfiApplicationQueryDto.getLicenceId());
                            setReqForInfoSearchListDtoLicenceInfo(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        }
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);
                }
            }
        }
    }

    public void backAdvanced(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>backAdvanced>>>>>>>>>>>>>>>>OnlineEnquiries");
        HttpServletRequest request=bpc.request;
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();
        List<String> svcIds=IaisCommonUtils.genNewArrayList();
        List<String> licenceIds=IaisCommonUtils.genNewArrayList();
        String count=(String) ParamUtil.getSessionAttr(request,COUNT);
        SearchParam parm = (SearchParam) ParamUtil.getSessionAttr(request,SEARCH_PARAM);
        switch (count) {
            case "2":
            case "3":
                int appCount=0;
                int licCount=0;
                if(!StringUtil.isEmpty(parm.getFilters().get(APP_NO))){
                    filters.put(APP_NO, parm.getFilters().get(APP_NO));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appType"))){
                    filters.put("appType", parm.getFilters().get("appType"));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appStatus"))){
                    filters.put("appStatus", parm.getFilters().get("appStatus"));
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
                if(!StringUtil.isEmpty(parm.getFilters().get(START_DATE))){
                    filters.put(START_DATE, Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get(START_DATE)),
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
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENCE_NO))){
                    filters.put(LICENCE_NO, parm.getFilters().get(LICENCE_NO));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENCE_STATUS))){
                    filters.put(LICENCE_STATUS, parm.getFilters().get(LICENCE_STATUS));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("svc_name"))){
                    filters.put("svc_name", parm.getFilters().get("svc_name"));
                    List<HcsaServiceDto> svcDto = configCommService.getHcsaServiceByNames(Collections.singletonList((String) parm.getFilters().get("svc_name")));
                    //filters.put("svc_id",svcDto.getId());
                    for (HcsaServiceDto r:svcDto
                    ) {
                        svcIds.add(r.getId());
                    }
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("serviceSubTypeName"))){
                    filters.put("serviceSubTypeName", parm.getFilters().get("serviceSubTypeName"));

                }
                if(!StringUtil.isEmpty(parm.getFilters().get(UEN_NO))){
                    filters.put(UEN_NO, parm.getFilters().get(UEN_NO));
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
                if(!StringUtil.isEmpty(parm.getFilters().get(HCI_NAME))){
                    filters.put(HCI_NAME, parm.getFilters().get(HCI_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciPostalCode"))){
                    filters.put("hciPostalCode", parm.getFilters().get("hciPostalCode"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciStreetName"))){
                    filters.put("hciStreetName", parm.getFilters().get("hciStreetName"));
                }
                break;
            case "5":
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_ID))){
                    filters.put(PERSONNEL_ID, parm.getFilters().get(PERSONNEL_ID));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_NAME))){
                    filters.put(PERSONNEL_NAME,parm.getFilters().get(PERSONNEL_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_REGNNO))){
                    filters.put(PERSONNEL_REGNNO, parm.getFilters().get(PERSONNEL_REGNNO));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_ROLE))){
                    filters.put(PERSONNEL_ROLE, parm.getFilters().get(PERSONNEL_ROLE));
                }
                break;
            case "4":
            case "6":
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeIdNo"))){
                    filters.put("licenseeIdNo",parm.getFilters().get("licenseeIdNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENSEE_NAME))){
                    filters.put(LICENSEE_NAME, parm.getFilters().get(LICENSEE_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeRegnNo"))){
                    filters.put("licenseeRegnNo",parm.getFilters().get("licenseeRegnNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(UEN_NO))){
                    filters.put(UEN_NO, parm.getFilters().get(UEN_NO));
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
                if(parm!=null){
                    appParam.setPageNo(parm.getPageNo());
                    appParam.setPageSize(parm.getPageSize());
                    if(parm.getFilters()!=null){
                        String status= (String) parm.getFilters().get("appStatus");
                        if(status!=null){
                            setSearchParamAppStatus(status,appParam);
                        }else {
                            appParam.removeFilter("appStatus");
                        }
                    }
                }

                SearchResult<ApplicationLicenceQueryDto> appResult;
                if (parm!=null&&parm.getFilters()!=null&&parm.getFilters().get("appStatus") != null && parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                    QueryHelp.setMainSql(RFI_QUERY,"appLicenceForCommPoolQuery",appParam);
                    appResult = requestForInformationService.appLicenceDoForCommPoolQuery(appParam);
                }else {
                    QueryHelp.setMainSql(RFI_QUERY,APPLICENCE_QUERY,appParam);
                    appResult = requestForInformationService.appLicenceDoQuery(appParam);
                }

                if (appResult.getRowCount() != 0) {
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos = IaisCommonUtils.genNewArrayList();

                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto : appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto = new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto, reqForInfoSearchListDto);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            licenceIds.add(rfiApplicationQueryDto.getLicenceId());
                            setReqForInfoSearchListDtoLicenceInfo(rfiApplicationQueryDto,reqForInfoSearchListDto);
                        }
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                    //searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                    setSearchResult( request, searchListDtoSearchResult, licenceIds, reqForInfoSearchListDtos);

                }
            }
        }

        preSelectOption(request);


    }

    private Map<String, String> validateDate(HttpServletRequest request) throws ParseException {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String appSubDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SUB_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String appSubToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, TO_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        String licStaDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, START_DATE)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licStaToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "start_to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_start_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String licExpToDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "expiry_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        if(!StringUtil.isEmpty(appSubDate)&&!StringUtil.isEmpty(appSubToDate)){
            if(appSubDate.compareTo(appSubToDate)>0){
                errMap.put(TO_DATE,"OEN_ERR001");
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
        String count=(String) ParamUtil.getSessionAttr(request,COUNT);
        SearchParam parm = (SearchParam) ParamUtil.getSessionAttr(request,SEARCH_PARAM);
        switch (count) {
            case "2":
            case "3":
                int appCount=0;
                int licCount=0;
                if(!StringUtil.isEmpty(parm.getFilters().get(APP_NO))){
                    filters.put(APP_NO, parm.getFilters().get(APP_NO));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appType"))){
                    filters.put("appType", parm.getFilters().get("appType"));appCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("appStatus"))){
                    filters.put("appStatus", parm.getFilters().get("appStatus"));
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
                if(!StringUtil.isEmpty(parm.getFilters().get(START_DATE))){
                    filters.put(START_DATE, Formatter.formatDateTime(Formatter.parseDate((String) parm.getFilters().get(START_DATE)),
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
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENCE_NO))){
                    filters.put(LICENCE_NO, parm.getFilters().get(LICENCE_NO));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENCE_STATUS))){
                    filters.put(LICENCE_STATUS, parm.getFilters().get(LICENCE_STATUS));licCount++;
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("svc_name"))){
                    filters.put("svc_name", parm.getFilters().get("svc_name"));
                    List<HcsaServiceDto> svcDto = configCommService.getHcsaServiceByNames(Collections.singletonList((String) parm.getFilters().get("svc_name")));
                    //filters.put("svc_id",svcDto.getId());
                    for (HcsaServiceDto r:svcDto
                    ) {
                        svcIds.add(r.getId());
                    }
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("serviceSubTypeName"))){
                    filters.put("serviceSubTypeName", parm.getFilters().get("serviceSubTypeName"));

                }
                if(!StringUtil.isEmpty(parm.getFilters().get(UEN_NO))){
                    filters.put(UEN_NO, parm.getFilters().get(UEN_NO));
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
                if(!StringUtil.isEmpty(parm.getFilters().get(HCI_NAME))){
                    filters.put(HCI_NAME, parm.getFilters().get(HCI_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciPostalCode"))){
                    filters.put("hciPostalCode", parm.getFilters().get("hciPostalCode"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("hciStreetName"))){
                    filters.put("hciStreetName", parm.getFilters().get("hciStreetName"));
                }
                break;
            case "5":
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_ID))){
                    filters.put(PERSONNEL_ID, parm.getFilters().get(PERSONNEL_ID));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_NAME))){
                    filters.put(PERSONNEL_NAME,parm.getFilters().get(PERSONNEL_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_REGNNO))){
                    filters.put(PERSONNEL_REGNNO, parm.getFilters().get(PERSONNEL_REGNNO));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(PERSONNEL_ROLE))){
                    filters.put(PERSONNEL_ROLE, parm.getFilters().get(PERSONNEL_ROLE));
                }
                break;
            case "4":
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeIdNo"))){
                    filters.put("licenseeIdNo",parm.getFilters().get("licenseeIdNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(LICENSEE_NAME))){
                    filters.put(LICENSEE_NAME, parm.getFilters().get(LICENSEE_NAME));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get("licenseeRegnNo"))){
                    filters.put("licenseeRegnNo",parm.getFilters().get("licenseeRegnNo"));
                }
                if(!StringUtil.isEmpty(parm.getFilters().get(UEN_NO))){
                    filters.put(UEN_NO, parm.getFilters().get(UEN_NO));
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
                if(parm!=null&&parm.getFilters()!=null){
                    String status= (String) parm.getFilters().get("appStatus");
                    if(status!=null){
                        setSearchParamAppStatus(status,appParam);
                    }else {
                        appParam.removeFilter("appStatus");
                    }
                }
                SearchResult<ApplicationLicenceQueryDto> appResult ;
                if (parm!=null&&parm.getFilters()!=null&&parm.getFilters().get("appStatus") != null && parm.getFilters().get("appStatus").equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                    QueryHelp.setMainSql(RFI_QUERY,"appLicenceForCommPoolQuery",appParam);
                    appResult = requestForInformationService.appLicenceDoForCommPoolQuery(appParam);
                }else {
                    QueryHelp.setMainSql(RFI_QUERY,APPLICENCE_QUERY,appParam);
                    appResult = requestForInformationService.appLicenceDoQuery(appParam);

                }
                if (appResult.getRowCount() != 0) {
                    searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                    List<ReqForInfoSearchListDto> reqForInfoSearchListDtos = IaisCommonUtils.genNewArrayList();

                    for (ApplicationLicenceQueryDto rfiApplicationQueryDto : appResult.getRows()
                    ) {
                        ReqForInfoSearchListDto reqForInfoSearchListDto = new ReqForInfoSearchListDto();
                        rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto, reqForInfoSearchListDto);
                        reqForInfoSearchListDto.setLicenceId(rfiApplicationQueryDto.getLicenceId());
                        if(rfiApplicationQueryDto.getLicenceId()!=null){
                            reqForInfoSearchListDto.setHciCode(rfiApplicationQueryDto.getLicHciCode());
                            reqForInfoSearchListDto.setHciName(rfiApplicationQueryDto.getLicHciName());
                            if(rfiApplicationQueryDto.getLicHciName()==null){
                                reqForInfoSearchListDto.setHciName("-");
                            }
                            reqForInfoSearchListDto.setUen(rfiApplicationQueryDto.getLicUenNo());
                            if(reqForInfoSearchListDto.getAppId()==null){
                                reqForInfoSearchListDto.setServiceName(rfiApplicationQueryDto.getServiceName());
                                reqForInfoSearchListDto.setBlkNo(rfiApplicationQueryDto.getLicBlkNo());
                                reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getLicBuildingName());
                                reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getLicUnitNo());
                                reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getLicStreetName());
                                reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getLicFloorNo());
                                List<String> addressList = IaisCommonUtils.genNewArrayList();
                                addressList.add(rfiApplicationQueryDto.getLicPremType());
                                reqForInfoSearchListDto.setAddress(addressList);
                            }
                            String licStatus = MasterCodeUtil.getCodeDesc(rfiApplicationQueryDto.getLicenceStatus());
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
        log.debug("============> End of the query!");
        List<ReqForInfoSearchListDto> queryList=IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> entity = configCommService.allHcsaService();
        Map<String,String> mapIdSvcName=IaisCommonUtils.genNewHashMap();
        for (HcsaServiceDto svc:entity
        ) {
            mapIdSvcName.put(svc.getId(),svc.getSvcName());
        }
        for (ReqForInfoSearchListDto info:searchListDtoSearchResult.getRows()
        ) {
            try {
                if(mapIdSvcName.get(info.getServiceName())!=null){
                    info.setServiceName(mapIdSvcName.get(info.getServiceName()));
                }
            }catch (Exception e){
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
