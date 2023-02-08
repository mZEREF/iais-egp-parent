package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicAppMainEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicAppMainQueryResultDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OnlinedrpEnquiryDelegator
 *
 * @author junyu
 * @date 2022/5/5
 */
@Delegator(value = "onlineEnquiryLicAppMainDelegator")
@Slf4j
public class OnlineLicAppMainEnquiryDelegator {

    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter mainParameter = new FilterParameter.Builder()
            .clz(LicAppMainQueryResultDto.class)
            .searchAttr("mainParam")
            .resultAttr("mainResult")
            .sortField("APP_BUSINESS_NAME").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private LicCommService licCommService;
    private static final String LICENCE_ID = "licenceId";
    private static final String LICENSEE_ID = "licenseeId";
    private static final String APP_ID = "appId";

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.MODULE_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        mainParameter.setPageSize(pageSize);
        mainParameter.setPageNo(1);
        mainParameter.setSortField("APP_BUSINESS_NAME");
        mainParameter.setSortType(SearchParam.ASCENDING);
        ParamUtil.setSessionAttr(bpc.request,"mainEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "mainParam",null);
        ParamUtil.setSessionAttr(bpc.request, LICENSEE_ID,null);
        ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,null);
        ParamUtil.setSessionAttr(bpc.request, APP_ID,null);

    }

    public void preSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "mainParam");



        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                mainParameter.setSortType(sortType);
                mainParameter.setSortField(sortFieldName);
            }
            LicAppMainEnquiryFilterDto filterDto=setMainEnquiryFilterDto(request);

            setQueryFilter(filterDto,mainParameter);
            if(mainParameter.getFilters().isEmpty()){
                return;
            }

            SearchParam mainParam = SearchResultHelper.getSearchParam(request, mainParameter,true);

            if(searchParam!=null){
                mainParam.setPageNo(searchParam.getPageNo());
                mainParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(mainParam,bpc.request);
            QueryHelp.setMainSql("hcsaOnlineEnquiry","mainOnlineEnquiry",mainParam);
            List<SelectOption> appTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_TYPE);
            List<SelectOption> appStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_STATUS);
            List<SelectOption> licStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_LICENCE_STATUS);
            MasterCodePair mcp = new MasterCodePair("mainView.app_type", "app_type_desc", appTypes);
            MasterCodePair mcp_status = new MasterCodePair("mainView.appStatus", "app_status_desc", appStatus);
            MasterCodePair mcp_lic_status = new MasterCodePair("mainView.LicSTATUS", "lic_status_desc", licStatus);
            mainParam.addMasterCode(mcp);
            mainParam.addMasterCode(mcp_status);
            mainParam.addMasterCode(mcp_lic_status);
            SearchResult<LicAppMainQueryResultDto> mainResult = onlineEnquiriesService.searchMainQueryResult(mainParam);
            for (LicAppMainQueryResultDto queryResultsDto : mainResult.getRows()) {
                AppSubmissionDto appSubmissionDto = licCommService.viewAppSubmissionDto(queryResultsDto.getLicenceId());
                if (StringUtil.isNotEmpty(appSubmissionDto)) {
                    SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
                    if (StringUtil.isNotEmpty(subLicenseeDto)) {
                        if ("LICTSUB001".equals(subLicenseeDto.getLicenseeType())) {
                            queryResultsDto.setAppLicenseeIdNo(subLicenseeDto.getUenNo());
                        }
                    }
                }
                if (StringUtil.isEmpty(queryResultsDto.getAppLicenseeIdNo())) {
                    queryResultsDto.setAppLicenseeIdNo("-");
                }
            }
            ParamUtil.setRequestAttr(request,"mainResult",mainResult);
            ParamUtil.setSessionAttr(request,"mainParam",mainParam);
        }else {
            SearchResult<LicAppMainQueryResultDto> mainResult = onlineEnquiriesService.searchMainQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"mainResult",mainResult);
            ParamUtil.setSessionAttr(request,"mainParam",searchParam);
        }
    }

    private LicAppMainEnquiryFilterDto setMainEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        LicAppMainEnquiryFilterDto filterDto=new LicAppMainEnquiryFilterDto();
        String licenceNo=ParamUtil.getString(request,"licenceNo");
        filterDto.setLicenceNo(licenceNo);
        String applicationNo=ParamUtil.getString(request,"applicationNo");
        filterDto.setApplicationNo(applicationNo);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        String licenceStatus=ParamUtil.getString(request,"licenceStatus");
        filterDto.setLicenceStatus(licenceStatus);
        String licenseeIdNo=ParamUtil.getString(request,"licenseeIdNo");
        filterDto.setLicenseeIdNo(licenseeIdNo);
        String licenseeName=ParamUtil.getString(request,"licenseeName");
        filterDto.setLicenseeName(licenseeName);
        Date inspectionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "inspectionDateFrom"));
        filterDto.setInspectionDateFrom(inspectionDateFrom);
        Date inspectionDateTo= Formatter.parseDate(ParamUtil.getString(request, "inspectionDateTo"));
        filterDto.setInspectionDateTo(inspectionDateTo);
//        volidata time
        if (!StringUtil.isEmpty(inspectionDateFrom) && !StringUtil.isEmpty(inspectionDateTo) && inspectionDateFrom.after(inspectionDateTo)){
            errorMap.put("inspectionDate", MessageUtil.getMessageDesc("Last Inspection Date From cannot be later than Last Inspection Date To"));
        }
//        volidata allFileds
        String searchNumber = ParamUtil.getString(request,"Search");
        if (ReflectionUtil.isEmpty(filterDto) && "1".equals(searchNumber)){
            errorMap.put("checkAllFileds", MessageUtil.getMessageDesc("Please enter at least one search filter to proceed with search"));
        }

        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        ParamUtil.setSessionAttr(request,"mainEnquiryFilterDto",filterDto);
        return filterDto;
    }


    private void setQueryFilter(LicAppMainEnquiryFilterDto filterDto, FilterParameter filterParameter){
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        if(filterDto.getLicenceNo()!=null) {
            filter.put("getLicenceNo", filterDto.getLicenceNo());
        }
        if(filterDto.getApplicationNo()!=null) {
            filter.put("getApplicationNo", filterDto.getApplicationNo());
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
        if(filterDto.getInspectionDateFrom()!=null){
            String birthDateFrom = Formatter.formatDateTime(filterDto.getInspectionDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("getInspectionDateFrom", birthDateFrom);
        }

        if(filterDto.getInspectionDateTo()!=null){
            String birthDateTo = Formatter.formatDateTime(filterDto.getInspectionDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("getInspectionDateTo", birthDateTo);
        }
        filterParameter.setFilters(filter);

    }

    public void nextStep(BaseProcessClass bpc){
        String licencId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(licencId)) {
            try {
                licencId= MaskUtil.unMaskValue(LICENCE_ID,licencId);
                ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,licencId);
                return;
            }catch (Exception e){
                log.info("no LICENCE_ID");
            }
        }
        String appId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(appId)) {
            try {
                appId= MaskUtil.unMaskValue(APP_ID,appId);
                ParamUtil.setSessionAttr(bpc.request, APP_ID,appId);
                return;
            }catch (Exception e){
                log.info("no APP_ID");
            }
        }
        if (!StringUtil.isEmpty(licencId)) {
            try {
                licencId= MaskUtil.unMaskValue(LICENSEE_ID,licencId);
                ParamUtil.setSessionAttr(bpc.request, LICENSEE_ID,licencId);
                ParamUtil.setSessionAttr(bpc.request,"licenceTabEnquiryFilterDto",null);
                ParamUtil.setSessionAttr(bpc.request, "licTabParam",null);
                StringBuilder url = new StringBuilder();
                url.append("https://")
                        .append(bpc.request.getServerName())
                        .append("/hcsa-licence-web/eservice/INTRANET/MohLicenseeOnlineEnquiry/1/preLicenceSearch");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }catch (Exception e){
                log.info("no LICENSEE_ID");
            }
        }

        if (!StringUtil.isEmpty(appId)) {
            try {
                String appCorrId= MaskUtil.unMaskValue("appCorrId",appId);
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appCorrId).getEntity();
                ParamUtil.setSessionAttr(bpc.request, "appCorrId",appPremisesCorrelationDto.getId());
                StringBuilder url = new StringBuilder();
                url.append("https://")
                        .append(bpc.request.getServerName())
                        .append("/hcsa-licence-web/eservice/INTRANET/MohInspectionOnlineEnquiry/1/perDetails");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }catch (Exception e){
                log.info("no appCorrId");
            }
        }

    }

    public void licInfoJump(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://")
                .append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTRANET/MohLicenceOnlineEnquiry/1/preLicInfo");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void appInfoJump(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://")
                .append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTRANET/MohApplicationOnlineEnquiry/1/preAppInfo");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }
}
