package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InsTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OnlineEnquiryLicenceDelegator
 *
 * @author junyu
 * @date 2022/12/12
 */
@Delegator(value = "onlineEnquiryLicenceDelegator")
@Slf4j
public class OnlineEnquiryLicenceDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter licParameter = new FilterParameter.Builder()
            .clz(LicenceQueryResultsDto.class)
            .searchAttr("licParam")
            .resultAttr("licenceResult")
            .sortField("LICENCE_ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter appTabParameter = new FilterParameter.Builder()
            .clz(ApplicationTabQueryResultsDto.class)
            .searchAttr("appTabParam")
            .resultAttr("appTabResult")
            .sortField("APP_ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter insTabParameter = new FilterParameter.Builder()
            .clz(InspectionTabQueryResultsDto.class)
            .searchAttr("insTabParam")
            .resultAttr("insTabResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter rfiTabParameter = new FilterParameter.Builder()
            .clz(RfiTabQueryResultsDto.class)
            .searchAttr("rfiTabParam")
            .resultAttr("rfiTabResult")
            .sortField("RFI_ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();


    private static final String LICENCE_ID = "licenceId";
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private RequestForInformationService requestForInformationService;
    @Autowired
    private LicCommService licCommService;
    @Autowired
    private RequestForInformationDelegator requestForInformationDelegator;

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        licParameter.setPageSize(pageSize);
        licParameter.setPageNo(1);
        licParameter.setSortField("LICENCE_ID");
        licParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"licenceEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "licParam",null);

    }

    List<SelectOption> getMosdTypeOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_PERMANENT, ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_CONVEYANCE, ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE, ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_MOBILE, ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_REMOTE, ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW));

        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    List<SelectOption> getInspectionTypeOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW+" Inspection", ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW+" Inspection", ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW+" Inspection", ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW+" Inspection", ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW+" Inspection", ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW+" Inspection"));

        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    public void preSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> mosdTypeOption =getMosdTypeOption();
        ParamUtil.setRequestAttr(request,"mosdTypeOption", mosdTypeOption);
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "licParam");



        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                licParameter.setSortType(sortType);
                licParameter.setSortField(sortFieldName);
            }
            LicenceEnquiryFilterDto licFilterDto=setLicEnquiryFilterDto(request);

            setQueryFilter(licFilterDto,licParameter);

            SearchParam licParam = SearchResultHelper.getSearchParam(request, licParameter,true);

            if(searchParam!=null){
                licParam.setPageNo(searchParam.getPageNo());
                licParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(licParam,bpc.request);
            QueryHelp.setMainSql("hcsaOnlineEnquiry","licenceOnlineEnquiry",licParam);
            SearchResult<LicenceQueryResultsDto> licenceResult = onlineEnquiriesService.searchLicenceQueryResult(licParam);
            ParamUtil.setRequestAttr(request,"licenceResult",licenceResult);
            ParamUtil.setSessionAttr(request,"licParam",licParam);
        }else {
            SearchResult<LicenceQueryResultsDto> licenceResult = onlineEnquiriesService.searchLicenceQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"licenceResult",licenceResult);
            ParamUtil.setSessionAttr(request,"licParam",searchParam);
        }
    }

    private void setQueryFilter(LicenceEnquiryFilterDto filterDto, FilterParameter licParameter) {
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getLicenceNo()!=null) {
            filter.put("getLicenceNo", filterDto.getLicenceNo());
        }
        if(filterDto.getStreetName()!=null) {
            filter.put("getStreetName", filterDto.getStreetName());
        }
        if(filterDto.getLicenceStatus()!=null){
            filter.put("getLicenceStatus",filterDto.getLicenceStatus());
        }
        if(filterDto.getLicenseeIdNo()!=null){
            filter.put("getLicenseeIdNo",filterDto.getLicenseeIdNo());
        }
        if(filterDto.getBusinessName()!=null){
            filter.put("getBusinessName", filterDto.getBusinessName());
        }

        if(filterDto.getLicenseeName()!=null){
            filter.put("getLicenseeName",filterDto.getLicenseeName());
        }
        if(filterDto.getMosdType()!=null){
            filter.put("getMosdType",filterDto.getMosdType());
        }
        if(filterDto.getPostalCode()!=null){
            filter.put("getPostalCode",filterDto.getPostalCode());
        }
        if(filterDto.getVehicleNo()!=null){
            filter.put("getVehicleNo",filterDto.getVehicleNo());
        }
        if(filterDto.getServiceName()!=null){
            HcsaServiceDto hcsaServiceDto= HcsaServiceCacheHelper.getServiceByCode(filterDto.getServiceName());
            filter.put("getServiceName",hcsaServiceDto.getSvcName());
        }
        licParameter.setFilters(filter);
    }

    private LicenceEnquiryFilterDto setLicEnquiryFilterDto(HttpServletRequest request) {
        LicenceEnquiryFilterDto filterDto=new LicenceEnquiryFilterDto();
        String licenceNo=ParamUtil.getString(request,"licenceNo");
        filterDto.setLicenceNo(licenceNo);
        String mosdType=ParamUtil.getString(request,"mosdType");
        filterDto.setMosdType(mosdType);
        String postalCode=ParamUtil.getString(request,"postalCode");
        filterDto.setPostalCode(postalCode);
        String streetName=ParamUtil.getString(request,"streetName");
        filterDto.setStreetName(streetName);
        String serviceName=ParamUtil.getString(request,"serviceName");
        filterDto.setServiceName(serviceName);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        String licenceStatus=ParamUtil.getString(request,"licenceStatus");
        filterDto.setLicenceStatus(licenceStatus);
        String licenseeIdNo=ParamUtil.getString(request,"licenseeIdNo");
        filterDto.setLicenseeIdNo(licenseeIdNo);
        String licenseeName=ParamUtil.getString(request,"licenseeName");
        filterDto.setLicenseeName(licenseeName);
        String vehicleNo=ParamUtil.getString(request,"vehicleNo");
        filterDto.setVehicleNo(vehicleNo);
        ParamUtil.setSessionAttr(request,"licenceEnquiryFilterDto",filterDto);
        return filterDto;
    }

    public void nextStep(BaseProcessClass bpc){
        String licencId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(licencId)) {
            try {
                licencId= MaskUtil.unMaskValue(LICENCE_ID,licencId);
                ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,licencId);
            }catch (Exception e){
                log.info("no LICENCE_ID");
            }


        }
    }
    
    
    public void preLicInfo(BaseProcessClass bpc){
        String licencId = (String) ParamUtil.getSessionAttr(bpc.request, LICENCE_ID);

        if (!StringUtil.isEmpty(licencId)) {
            AppSubmissionDto appSubmissionDto = licCommService.viewAppSubmissionDto(licencId);
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licencId).getEntity();
            if (appSubmissionDto != null) {
                DealSessionUtil.initView(appSubmissionDto);
                //set audit trail licNo
                AuditTrailHelper.setAuditLicNo(appSubmissionDto.getLicenceNo());
                appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, "licenceDto", licenceDto);
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Licence Details");
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    return;
                }
                if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appSubmissionDto.getAppType())) {
                    return;
                }
                AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = null;
                if (oldAppSubmissionDto != null) {
                    List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
                    if (appSvcRelatedInfoDtoList != null) {
                        oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
                    }
                }

                AppSvcRelatedInfoDto appSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);

                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                if (appSvcDocDtoLit != null) {
                    for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                        String svcDocId = appSvcDocDto.getSvcDocId();
                        if (StringUtil.isEmpty(svcDocId)) {
                            continue;
                        }
                        HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                        if (entity != null) {
                            appSvcDocDto.setUpFileName(entity.getDocTitle());
                        }
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
            }

            String p = systemParamConfig.getPagingSize();
            String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
            pageSize= Integer.valueOf(defaultValue);

            appTabParameter.setPageSize(pageSize);
            appTabParameter.setPageNo(1);
            appTabParameter.setSortField("APP_ID");
            appTabParameter.setSortType(SearchParam.DESCENDING);
            ParamUtil.setSessionAttr(bpc.request,"applicationTabEnquiryFilterDto",null);
            ParamUtil.setSessionAttr(bpc.request, "appTabParam",null);
            insTabParameter.setPageSize(pageSize);
            insTabParameter.setPageNo(1);
            insTabParameter.setSortField("ID");
            insTabParameter.setSortType(SearchParam.DESCENDING);
            ParamUtil.setSessionAttr(bpc.request,"insTabEnquiryFilterDto",null);
            ParamUtil.setSessionAttr(bpc.request, "insTabParam",null);
            rfiTabParameter.setPageSize(pageSize);
            rfiTabParameter.setPageNo(1);
            rfiTabParameter.setSortField("RFI_ID");
            rfiTabParameter.setSortType(SearchParam.DESCENDING);
            ParamUtil.setSessionAttr(bpc.request,"rfiTabEnquiryFilterDto",null);
            ParamUtil.setSessionAttr(bpc.request, "rfiTabParam",null);
        }
    }
    private AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
            appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
        }
        return appSvcRelatedInfoDto;
    }
    public void licStep(BaseProcessClass bpc){}
    public void appStep(BaseProcessClass bpc){}
    public void preApplicationsSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, "preActive", "1");

        String licencId = (String) ParamUtil.getSessionAttr(bpc.request, LICENCE_ID);
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "appTabParam");

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                appTabParameter.setSortType(sortType);
                appTabParameter.setSortField(sortFieldName);
            }
            ApplicationTabEnquiryFilterDto appFilterDto=setAppEnquiryFilterDto(request);

            setAppQueryFilter(appFilterDto,appTabParameter);
            SearchParam appParam = SearchResultHelper.getSearchParam(request, appTabParameter,true);

            if(appFilterDto.getAppStatus()!=null){
                setSearchParamAppStatus(appFilterDto.getAppStatus(),appParam,"atqrv");
            }else {
                appParam.removeFilter("getAppStatus");
            }
            if(searchParam!=null){
                appParam.setPageNo(searchParam.getPageNo());
                appParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(appParam,bpc.request);
            appParam.addFilter("licenceId",licencId,true);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","applicationTabOnlineEnquiry",appParam);
            SearchResult<ApplicationTabQueryResultsDto> appTabResult = onlineEnquiriesService.searchLicenceAppTabQueryResult(appParam);
            ParamUtil.setRequestAttr(request,"appTabResult",appTabResult);
            ParamUtil.setSessionAttr(request,"appTabParam",appParam);
        }else {
            SearchResult<ApplicationTabQueryResultsDto> appTabResult = onlineEnquiriesService.searchLicenceAppTabQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"appTabResult",appTabResult);
            ParamUtil.setSessionAttr(request,"appTabParam",searchParam);
        }
    }

    private void setAppQueryFilter(ApplicationTabEnquiryFilterDto filterDto, FilterParameter appTabParameter) {
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getApplicationNo()!=null) {
            filter.put("getApplicationNo", filterDto.getApplicationNo());
        }
        if(filterDto.getVehicleNo()!=null) {
            filter.put("getVehicleNo", filterDto.getVehicleNo());
        }
        if(filterDto.getAppStatus()!=null){
            filter.put("getAppStatus",filterDto.getAppStatus());
        }
        if(filterDto.getBusinessName()!=null){
            filter.put("getBusinessName", filterDto.getBusinessName());
        }

        appTabParameter.setFilters(filter);
    }

    private void setSearchParamAppStatus(String status,SearchParam appParam ,String viewName){
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
        SqlHelper.builderInSql(appParam, viewName+".appStatus", "appStatusAll", inParams);
    }


    private ApplicationTabEnquiryFilterDto setAppEnquiryFilterDto(HttpServletRequest request) {
        ApplicationTabEnquiryFilterDto filterDto=new ApplicationTabEnquiryFilterDto();
        String applicationNo=ParamUtil.getString(request,"applicationNo");
        filterDto.setApplicationNo(applicationNo);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        String vehicleNo=ParamUtil.getString(request,"vehicleNo");
        filterDto.setVehicleNo(vehicleNo);
        String appStatus=ParamUtil.getString(request,"appStatus");
        filterDto.setAppStatus(appStatus);

        ParamUtil.setSessionAttr(request,"applicationTabEnquiryFilterDto",filterDto);
        return filterDto;
    }

    public void inspectionsStep(BaseProcessClass bpc){

        String corrId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(corrId)) {
            try {
                corrId= MaskUtil.unMaskValue("appCorrId",corrId);
                ParamUtil.setSessionAttr(bpc.request, "appCorrId",corrId);
            }catch (Exception e){
                log.info("no appCorrId");
            }
        }

    }
    public void preInspectionsSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, "preActive", "3");

        String licencId = (String) ParamUtil.getSessionAttr(bpc.request, LICENCE_ID);
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        List<SelectOption> inspectionTypeOption =getInspectionTypeOption();
        ParamUtil.setRequestAttr(request,"inspectionTypeOption", inspectionTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "insTabParam");

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                insTabParameter.setSortType(sortType);
                insTabParameter.setSortField(sortFieldName);
            }
            InsTabEnquiryFilterDto filterDto=setInsEnquiryFilterDto(request);

            setInsQueryFilter(filterDto,insTabParameter);

            SearchParam insTabParam = SearchResultHelper.getSearchParam(request, insTabParameter,true);

            if(filterDto.getAppStatus()!=null){
                setSearchParamAppStatus(filterDto.getAppStatus(),insTabParam,"insTab");
            }else {
                insTabParam.removeFilter("getAppStatus");
            }
            if(searchParam!=null){
                insTabParam.setPageNo(searchParam.getPageNo());
                insTabParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(insTabParam,bpc.request);
            insTabParam.addFilter("licenceId",licencId,true);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","inspectionsTabOnlineEnquiry",insTabParam);
            SearchResult<InspectionTabQueryResultsDto> insTabResult = onlineEnquiriesService.searchLicenceInsTabQueryResult(insTabParam);
            ParamUtil.setRequestAttr(request,"insTabResult",insTabResult);
            ParamUtil.setSessionAttr(request,"insTabParam",insTabParam);
        }else {
            SearchResult<InspectionTabQueryResultsDto> insTabResult = onlineEnquiriesService.searchLicenceInsTabQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"insTabResult",insTabResult);
            ParamUtil.setSessionAttr(request,"insTabParam",searchParam);
        }
    }

    private InsTabEnquiryFilterDto setInsEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        InsTabEnquiryFilterDto filterDto=new InsTabEnquiryFilterDto();
        String applicationNo=ParamUtil.getString(request,"applicationNo");
        filterDto.setApplicationNo(applicationNo);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        String vehicleNo=ParamUtil.getString(request,"vehicleNo");
        filterDto.setVehicleNo(vehicleNo);
        String appStatus=ParamUtil.getString(request,"appStatus");
        filterDto.setAppStatus(appStatus);
        Date inspectionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "inspectionDateFrom"));
        filterDto.setInspectionDateFrom(inspectionDateFrom);
        Date inspectionDateTo= Formatter.parseDate(ParamUtil.getString(request, "inspectionDateTo"));
        filterDto.setInspectionDateTo(inspectionDateTo);
        String inspectionType=ParamUtil.getString(request,"inspectionType");
        filterDto.setInspectionType(inspectionType);
        ParamUtil.setSessionAttr(request,"insTabEnquiryFilterDto",filterDto);
        return filterDto;
    }

    private void setInsQueryFilter(InsTabEnquiryFilterDto filterDto, FilterParameter insTabParameter) {

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getApplicationNo()!=null) {
            filter.put("getApplicationNo", filterDto.getApplicationNo());
        }
        if(filterDto.getVehicleNo()!=null) {
            filter.put("getVehicleNo", filterDto.getVehicleNo());
        }
        if(filterDto.getAppStatus()!=null){
            filter.put("getAppStatus",filterDto.getAppStatus());
        }
        if(filterDto.getBusinessName()!=null){
            filter.put("getBusinessName", filterDto.getBusinessName());
        }
        if(filterDto.getInspectionType()!=null){
            filter.put("getInspectionType", filterDto.getInspectionType());
        }
        if(filterDto.getInspectionDateFrom()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getInspectionDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("getInspectionDateFrom", dateTime);
        }

        if(filterDto.getInspectionDateTo()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getInspectionDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("getInspectionDateTo", dateTime);
        }

        insTabParameter.setFilters(filter);
    }


    public void adHocRfiStep(BaseProcessClass bpc){
        String rfiId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(rfiId)) {
            try {
                rfiId= MaskUtil.unMaskValue("reqInfoId",rfiId);
                ParamUtil.setSessionAttr(bpc.request, "reqInfoId",rfiId);
            }catch (Exception e){
                log.info("no RFI_ID");
            }
        }

    }
    public void preAdHocRfiSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, "preActive", "2");

        String licencId = (String) ParamUtil.getSessionAttr(bpc.request, LICENCE_ID);
//        List<SelectOption> inspectionTypeOption =getInspectionTypeOption();
//        ParamUtil.setRequestAttr(request,"inspectionTypeOption", inspectionTypeOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "insTabParam");

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                rfiTabParameter.setSortType(sortType);
                rfiTabParameter.setSortField(sortFieldName);
            }
            RfiTabEnquiryFilterDto filterDto=setRfiEnquiryFilterDto(request);

            setRfiQueryFilter(filterDto,rfiTabParameter);

            SearchParam rfiTabParam = SearchResultHelper.getSearchParam(request, rfiTabParameter,true);

            if(searchParam!=null){
                rfiTabParam.setPageNo(searchParam.getPageNo());
                rfiTabParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(rfiTabParam,bpc.request);
            rfiTabParam.addFilter("licenceId",licencId,true);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","adHocRfiTabOnlineEnquiry",rfiTabParam);
            SearchResult<RfiTabQueryResultsDto> rfiTabResult = onlineEnquiriesService.searchLicenceRfiTabQueryResult(rfiTabParam);
            ParamUtil.setRequestAttr(request,"rfiTabResult",rfiTabResult);
            ParamUtil.setSessionAttr(request,"rfiTabParam",rfiTabParam);
        }else {
            SearchResult<RfiTabQueryResultsDto> rfiTabResult = onlineEnquiriesService.searchLicenceRfiTabQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"rfiTabResult",rfiTabResult);
            ParamUtil.setSessionAttr(request,"rfiTabParam",searchParam);
        }
    }

    private void setRfiQueryFilter(RfiTabEnquiryFilterDto filterDto, FilterParameter rfiTabParameter) {
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getLicenceNo()!=null) {
            filter.put("getLicenceNo", filterDto.getLicenceNo());
        }
        if(filterDto.getRequestedBy()!=null) {
            filter.put("getRequestedBy", filterDto.getRequestedBy());
        }

        if(filterDto.getDueDateFrom()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getDueDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("getDueDateFrom", dateTime);
        }

        if(filterDto.getDueDateTo()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getDueDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("getDueDateTo", dateTime);
        }
        if(filterDto.getRequestDateFrom()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getRequestDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("getRequestDateFrom", dateTime);
        }

        if(filterDto.getRequestDateTo()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getRequestDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("getRequestDateTo", dateTime);
        }


        rfiTabParameter.setFilters(filter);
    }

    private RfiTabEnquiryFilterDto setRfiEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        RfiTabEnquiryFilterDto filterDto=new RfiTabEnquiryFilterDto();
        String licenceNo=ParamUtil.getString(request,"licenceNo");
        filterDto.setLicenceNo(licenceNo);
        String requestedBy=ParamUtil.getString(request,"requestedBy");
        filterDto.setRequestedBy(requestedBy);

        Date requestDateFrom= Formatter.parseDate(ParamUtil.getString(request, "requestDateFrom"));
        filterDto.setRequestDateFrom(requestDateFrom);
        Date requestDateTo= Formatter.parseDate(ParamUtil.getString(request, "requestDateTo"));
        filterDto.setRequestDateTo(requestDateTo);
        Date dueDateFrom= Formatter.parseDate(ParamUtil.getString(request, "dueDateFrom"));
        filterDto.setDueDateFrom(dueDateFrom);
        Date dueDateTo= Formatter.parseDate(ParamUtil.getString(request, "dueDateTo"));
        filterDto.setDueDateTo(dueDateTo);

        ParamUtil.setSessionAttr(request,"rfiTabEnquiryFilterDto",filterDto);
        return filterDto;
    }

    public void preAdHocRfiInfo(BaseProcessClass bpc) throws ParseException {
        requestForInformationDelegator.preViewRfi(bpc);
    }
    public void step13(BaseProcessClass bpc){}

    public void preInspectionReport(BaseProcessClass bpc){
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(bpc.request, "kpiInfo", kpiInfo);
        HttpServletRequest request=bpc.request;
        String appPremisesCorrelationId=(String) ParamUtil.getSessionAttr(request, "appCorrId");
        String licenceId = (String) ParamUtil.getSessionAttr(request, LICENCE_ID);
        onlineEnquiriesService.getInspReport(request,appPremisesCorrelationId,licenceId);
    }
    public void backInsTab(BaseProcessClass bpc){}
}
