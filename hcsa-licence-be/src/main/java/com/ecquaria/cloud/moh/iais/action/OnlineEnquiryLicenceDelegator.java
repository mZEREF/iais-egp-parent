package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InsTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
    private static final String APP_ID = "appId";

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
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private ApplicationClient applicationClient;

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
        ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,null);
        ParamUtil.setSessionAttr(bpc.request, APP_ID,null);
    }

    List<SelectOption> getMosdTypeOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW, ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW, ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW, ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW, ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW, ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW));

        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    List<SelectOption> getInspectionTypeOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW, ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW, ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW, ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW, ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW+" Inspection"));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW, ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW+" Inspection"));

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
            if(licParameter.getFilters().isEmpty()){
                return;
            }

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

    public void setQueryFilter(LicenceEnquiryFilterDto filterDto, FilterParameter licParameter) {
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
        if(IaisCommonUtils.isNotEmpty(filterDto.getServiceName())){
            filter.put("getServiceName",filterDto.getServiceName());
        }
        licParameter.setFilters(filter);
    }

    public LicenceEnquiryFilterDto setLicEnquiryFilterDto(HttpServletRequest request) {
        LicenceEnquiryFilterDto filterDto=new LicenceEnquiryFilterDto();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String licenceNo=ParamUtil.getString(request,"licenceNo");
        filterDto.setLicenceNo(licenceNo);
        String mosdType=ParamUtil.getString(request,"mosdType");
        filterDto.setMosdType(mosdType);
        String postalCode=ParamUtil.getString(request,"postalCode");
        filterDto.setPostalCode(postalCode);
        String streetName=ParamUtil.getString(request,"streetName");
        filterDto.setStreetName(streetName);
        String[] serviceName=ParamUtil.getStrings(request,"serviceName");
        filterDto.setServiceName(Arrays.asList(serviceName));
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
        String searchNumber = ParamUtil.getString(request,"Search");
        if ((ReflectionUtil.isEmpty(filterDto)) && "1".equals(searchNumber)){
            errorMap.put("checkAllFileds", MessageUtil.getMessageDesc("Please enter at least one search filter to proceed with search"));
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
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
            List<String> licenceIds=IaisCommonUtils.genNewArrayList();
            licenceIds.add(licencId);
            String oldId=licenceDto.getOriginLicenceId();
            while (StringUtil.isNotEmpty(oldId)){
                LicenceDto oldLicenceDto = hcsaLicenceClient.getLicDtoById(oldId).getEntity();
                if(oldLicenceDto!=null){
                    licenceIds.add(oldId);
                    oldId=oldLicenceDto.getOriginLicenceId();
                }else {
                    break;
                }
            }
            SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "licParam");
            if(searchParam==null){
                searchParam=new SearchParam(LicenceQueryResultsDto.class.getName());
            }
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);
            searchParam.setFilters(IaisCommonUtils.genNewHashMap());
            searchParam.setParams(new HashMap<>());
            searchParam.addFilter("licenceIds", licenceIds, true);
            QueryHelp.setMainSql("hcsaOnlineEnquiry","licenceOnlineEnquiry",searchParam);
            SearchResult<LicenceQueryResultsDto> licenceResult = onlineEnquiriesService.searchLicenceQueryResult(searchParam);
            ParamUtil.setSessionAttr(bpc.request,"licenceHistoryList", (Serializable) licenceResult.getRows());
            if (appSubmissionDto != null) {
                DealSessionUtil.initView(appSubmissionDto);
                //set audit trail licNo
                AuditTrailHelper.setAuditLicNo(appSubmissionDto.getLicenceNo());
                ParamUtil.setSessionAttr(bpc.request,"isSingle",0);
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, "licenceDto", licenceDto);
                LicenseeDto newLicenceDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                ParamUtil.setSessionAttr(bpc.request,"newLicenceDto", newLicenceDto);
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Licence Details");
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
                }
                List<DocumentShowDto> documentShowDtoList = appSvcRelatedInfoDto.getDocumentShowDtoList();
                for (DocumentShowDto documentShowDto : documentShowDtoList) {
                    for (DocSectionDto docSectionDto : documentShowDto.getDocSectionList()) {
                        for (DocSecDetailDto docSecDetailDto : docSectionDto.getDocSecDetailList()) {
                            docSecDetailDto.setDisplayTitle(IaisCommonUtils.getDocDisplayTitle(docSecDetailDto.getPsnType(), docSecDetailDto.getDocTitle(), docSecDetailDto.getPsnTypeIndex(), Boolean.FALSE));
                        }
                    }
                }
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
                //set internal files
                List<AppIntranetDocDto> appIntranetDocDtoList =IaisCommonUtils.genNewArrayList();
                List<LicAppCorrelationDto> licAppCorrelationDtos = hcsaLicenceClient.getLicCorrBylicId(licencId).getEntity();
                if(IaisCommonUtils.isNotEmpty(licAppCorrelationDtos)){
                    for (LicAppCorrelationDto licApp:licAppCorrelationDtos
                    ) {
                        AppPremisesCorrelationDto appPremisesCorrelationDto=applicationClient.getAppPremisesCorrelationDtosByAppId(licApp.getApplicationId()).getEntity();

                        List<AppIntranetDocDto> intranetDocDtos =  fillUpCheckListGetAppClient.getAppIntranetDocListByPremIdAndStatus(appPremisesCorrelationDto.getId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();

                        if(IaisCommonUtils.isNotEmpty(intranetDocDtos)){
                            for(AppIntranetDocDto intranetDocDto : intranetDocDtos){
                                intranetDocDto.setDocSize(intranetDocDto.getDocSize()+"KB");
                                appIntranetDocDtoList.add(intranetDocDto);
                            }
                        }

                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "appIntranetDocDtoList", (Serializable) appIntranetDocDtoList);



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

    public void licStep(BaseProcessClass bpc){
        String appId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(appId)) {
            try {
                appId= MaskUtil.unMaskValue(APP_ID,appId);
                ParamUtil.setSessionAttr(bpc.request, APP_ID,appId);

            }catch (Exception e){
                log.info("no APP_ID");
            }
        }
        String licencId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(licencId)) {
            try {
                licencId= MaskUtil.unMaskValue(LICENCE_ID,licencId);
                ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,licencId);
            }catch (Exception e){
                log.info("no LICENCE_ID");
            }
        }
        String payLicNo = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(payLicNo)) {
            try {
                payLicNo= MaskUtil.unMaskValue("payLicNo",payLicNo);
                ParamUtil.setSessionAttr(bpc.request, "payLicNo",payLicNo);
                ParamUtil.setSessionAttr(bpc.request, "payLicStep",payLicNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppInsStep",null);
                ParamUtil.setSessionAttr(bpc.request, "payAppStep",null);
                StringBuilder url = new StringBuilder();
                url.append("https://")
                        .append(bpc.request.getServerName())
                        .append("/hcsa-licence-web/eservice/INTRANET/MohPaymentOnlineEnquiry/1/preSearch");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }catch (Exception e){
                log.info("no payLicNo");
            }
        }
    }
    public void appStep(BaseProcessClass bpc){
        String appId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(appId)) {
            try {
                appId= MaskUtil.unMaskValue(APP_ID,appId);
                ParamUtil.setSessionAttr(bpc.request, APP_ID,appId);
                StringBuilder url = new StringBuilder();
                url.append("https://")
                        .append(bpc.request.getServerName())
                        .append("/hcsa-licence-web/eservice/INTRANET/MohApplicationOnlineEnquiry/1/preAppInfo");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }catch (Exception e){
                log.info("no APP_ID");
            }
        }
    }
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

    public void setAppQueryFilter(ApplicationTabEnquiryFilterDto filterDto, FilterParameter appTabParameter) {
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

        if(filterDto.getApplicationType()!=null) {
            filter.put("getApplicationType", filterDto.getApplicationType());
        }
        if(filterDto.getPostalCode()!=null) {
            filter.put("getPostalCode", filterDto.getPostalCode());
        }
        if(filterDto.getStreetName()!=null){
            filter.put("getStreetName",filterDto.getStreetName());
        }
        if(filterDto.getAutoApproved()!=null){
            filter.put("getAutoApproved", filterDto.getAutoApproved());
        }

        if(filterDto.getLicenseeIdNo()!=null) {
            filter.put("getLicenseeIdNo", filterDto.getLicenseeIdNo());
        }
        if(filterDto.getLicenseeName()!=null) {
            filter.put("getLicenseeName", filterDto.getLicenseeName());
        }
        if(filterDto.getAssignedOfficer()!=null){
            filter.put("getAssignedOfficer",filterDto.getAssignedOfficer());
        }

        appTabParameter.setFilters(filter);
    }

    public void setSearchParamAppStatus(String status,SearchParam appParam ,String viewName){
        List<String> inParams = IaisCommonUtils.genNewArrayList();
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION.equals(status)){
            inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Inspection");
        }else if(ApplicationConsts.PENDING_ASO_REPLY.equals(status)){
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
        SqlHelper.builderInSql(appParam, viewName+".APP_STATUS", "appStatusAll", inParams);
    }


    public ApplicationTabEnquiryFilterDto setAppEnquiryFilterDto(HttpServletRequest request) {
        ApplicationTabEnquiryFilterDto filterDto=new ApplicationTabEnquiryFilterDto();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String applicationNo=ParamUtil.getString(request,"applicationNo");
        filterDto.setApplicationNo(applicationNo);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        String vehicleNo=ParamUtil.getString(request,"vehicleNo");
        filterDto.setVehicleNo(vehicleNo);
        String appStatus=ParamUtil.getString(request,"appStatus");
        filterDto.setAppStatus(appStatus);

        String applicationType=ParamUtil.getString(request,"applicationType");
        filterDto.setApplicationType(applicationType);
        String postalCode=ParamUtil.getString(request,"postalCode");
        filterDto.setPostalCode(postalCode);
        String streetName=ParamUtil.getString(request,"streetName");
        filterDto.setStreetName(streetName);
        String autoApproved=ParamUtil.getString(request,"autoApproved");
        filterDto.setAutoApproved(autoApproved);
        String licenseeIdNo=ParamUtil.getString(request,"licenseeIdNo");
        filterDto.setLicenseeIdNo(licenseeIdNo);
        String licenseeName=ParamUtil.getString(request,"licenseeName");
        filterDto.setLicenseeName(licenseeName);
        String assignedOfficer=ParamUtil.getString(request,"assignedOfficer");
        filterDto.setAssignedOfficer(assignedOfficer);
        String searchNumber = ParamUtil.getString(request,"Search");
        if (ReflectionUtil.isEmpty(filterDto) && "1".equals(searchNumber)){
            errorMap.put("checkAllFileds", MessageUtil.getMessageDesc("Please enter at least one search filter to proceed with search"));
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
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

    public InsTabEnquiryFilterDto setInsEnquiryFilterDto(HttpServletRequest request) throws ParseException {
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

    public void setInsQueryFilter(InsTabEnquiryFilterDto filterDto, FilterParameter insTabParameter) {

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
        List<SelectOption> asoNameOption =getAsoNameOption();
        ParamUtil.setRequestAttr(request,"rfiUserOption", asoNameOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "rfiTabParam");

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

    private List<SelectOption> getAsoNameOption() {

        List<OrgUserDto> userList= organizationClient.retrieveUserRoleByRoleId(RoleConsts.USER_ROLE_ASO).getEntity();
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        if(IaisCommonUtils.isNotEmpty(userList)){
            for (OrgUserDto user:userList
            ) {
                if(user!=null){
                    selectOptions.add(new SelectOption(user.getDisplayName(), user.getDisplayName()));
                }
            }
            selectOptions.sort(Comparator.comparing(SelectOption::getText));
        }
        return selectOptions;
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

    public void preInspectionReport(BaseProcessClass bpc) throws IOException {

        StringBuilder url = new StringBuilder();
        url.append("https://")
                .append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTRANET/MohInspectionOnlineEnquiry/1/perDetails");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);

    }
    public void backInsTab(BaseProcessClass bpc){}
}
