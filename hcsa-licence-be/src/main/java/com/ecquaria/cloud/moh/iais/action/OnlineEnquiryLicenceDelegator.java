package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
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
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
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
    private static final String LICENCE_ID = "licenceId";
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private RequestForInformationService requestForInformationService;
    @Autowired
    private LicCommService licCommService;

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
            licencId= MaskUtil.unMaskValue(LICENCE_ID,licencId);
            ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,licencId);

        }
    }
    
    
    public void preLicInfo(BaseProcessClass bpc){
        String licencId = (String) ParamUtil.getSessionAttr(bpc.request, LICENCE_ID);

        if (!StringUtil.isEmpty(licencId)) {
            AppSubmissionDto appSubmissionDto = licCommService.viewAppSubmissionDto(licencId);
            if (appSubmissionDto != null) {
                DealSessionUtil.initView(appSubmissionDto);
                //set audit trail licNo
                AuditTrailHelper.setAuditLicNo(appSubmissionDto.getLicenceNo());
                appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
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
    public void preApplicationsSearch(BaseProcessClass bpc){}
    public void inspectionsStep(BaseProcessClass bpc){}
    public void preInspectionsSearch(BaseProcessClass bpc){}
    public void adHocRfiStep(BaseProcessClass bpc){}
    public void preAdHocRfiSearch(BaseProcessClass bpc){}
    public void preAdHocRfiInfo(BaseProcessClass bpc){}
    public void step13(BaseProcessClass bpc){}
}
