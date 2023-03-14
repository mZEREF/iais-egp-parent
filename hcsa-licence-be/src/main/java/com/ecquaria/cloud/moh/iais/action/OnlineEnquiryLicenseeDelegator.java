package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenseeEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenseeQueryResultsDto;
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
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.AppCommClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * OnlineEnquiryLicenceDelegator
 *
 * @author junyu
 * @date 2022/12/12
 */
@Delegator(value = "onlineEnquiryLicenseeDelegator")
@Slf4j
public class OnlineEnquiryLicenseeDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter licTabParameter = new FilterParameter.Builder()
            .clz(LicenceQueryResultsDto.class)
            .searchAttr("licTabParam")
            .resultAttr("licTabResult")
            .sortField("BUSINESS_NAME").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter lisParameter = new FilterParameter.Builder()
            .clz(LicenseeQueryResultsDto.class)
            .searchAttr("lisParam")
            .resultAttr("licenseeResult")
            .sortField("LICENSEE_NAME").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();


    private static final String LICENCE_ID = "licenceId";
    private static final String LICENSEE_ID = "licenseeId";
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private RequestForInformationService requestForInformationService;
    @Autowired
    private OnlineEnquiryLicenceDelegator onlineEnquiryLicenceDelegator;

    @Autowired
    private AppCommClient appCommClient;

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        lisParameter.setPageSize(pageSize);
        lisParameter.setPageNo(1);
        lisParameter.setSortField("LICENSEE_NAME");
        lisParameter.setSortType(SearchParam.ASCENDING);
        ParamUtil.setSessionAttr(bpc.request,"licenseeEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "lisParam",null);
        ParamUtil.setSessionAttr(bpc.request, LICENSEE_ID,null);
        ParamUtil.setSessionAttr(bpc.request, LICENCE_ID,null);
        ParamUtil.setSessionAttr(bpc.request, "licAppMain",null);
        ParamUtil.setSessionAttr(bpc.request, "lisLicTab",null);
        ParamUtil.setSessionAttr(bpc.request, "licAppTab",null);
        ParamUtil.setSessionAttr(bpc.request, "appInsStep",null);
        ParamUtil.setSessionAttr(bpc.request, "licInsStep",null);
    }





    public void preSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "lisParam");

        List<SelectOption> licenseeTypeOption =getLicenseeTypeOption();
        ParamUtil.setRequestAttr(request,"licenseeTypeOption", licenseeTypeOption);

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                lisParameter.setSortType(sortType);
                lisParameter.setSortField(sortFieldName);
            }
            LicenseeEnquiryFilterDto lisFilterDto=setLisEnquiryFilterDto(request);

            setQueryFilter(lisFilterDto,lisParameter);
            if(lisParameter.getFilters().isEmpty()){
                return;
            }

            SearchParam lisParam = SearchResultHelper.getSearchParam(request, lisParameter,true);

            if(searchParam!=null){
                lisParam.setPageNo(searchParam.getPageNo());
                lisParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(lisParam,bpc.request);
            QueryHelp.setMainSql("hcsaOnlineEnquiry","licenseeOnlineEnquiry",lisParam);
            SearchResult<LicenseeQueryResultsDto> licenseeResult = onlineEnquiriesService.searchLicenseeQueryResult(lisParam);
            ParamUtil.setRequestAttr(request,"licenseeResult",licenseeResult);
            ParamUtil.setSessionAttr(request,"lisParam",lisParam);
        }else {
            SearchResult<LicenseeQueryResultsDto> licenseeResult = onlineEnquiriesService.searchLicenseeQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"licenseeResult",licenseeResult);
            ParamUtil.setSessionAttr(request,"lisParam",searchParam);
        }
    }

    List<SelectOption> getLicenseeTypeOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(MasterCodeUtil.getCodeDesc(OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY), MasterCodeUtil.getCodeDesc(OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY)));
        selectOptions.add(new SelectOption(MasterCodeUtil.getCodeDesc(OrganizationConstants.LICENSEE_SUB_TYPE_SOLO), MasterCodeUtil.getCodeDesc(OrganizationConstants.LICENSEE_SUB_TYPE_SOLO)));
        selectOptions.add(new SelectOption(MasterCodeUtil.getCodeDesc(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL), MasterCodeUtil.getCodeDesc(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL)));

        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    private LicenseeEnquiryFilterDto setLisEnquiryFilterDto(HttpServletRequest request) {
        LicenseeEnquiryFilterDto filterDto=new LicenseeEnquiryFilterDto();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String licenseeType=ParamUtil.getString(request,"licenseeType");
        filterDto.setLicenseeType(licenseeType);
        String organisationName=ParamUtil.getString(request,"organisationName");
        filterDto.setOrganisationName(organisationName);
        String licenseeIdNo=ParamUtil.getString(request,"licenseeIdNo");
        filterDto.setLicenseeIdNo(licenseeIdNo);
        String licenseeName=ParamUtil.getString(request,"licenseeName");
        filterDto.setLicenseeName(licenseeName);
        String searchNumber = ParamUtil.getString(request,"Search");
        if (ReflectionUtil.isEmpty(filterDto) && "1".equals(searchNumber)){
            errorMap.put("checkAllFileds", MessageUtil.getMessageDesc("Please enter at least one search filter to proceed with search"));
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        ParamUtil.setSessionAttr(request,"licenseeEnquiryFilterDto",filterDto);
        return filterDto;
    }

    private void setQueryFilter(LicenseeEnquiryFilterDto filterDto, FilterParameter lisParameter) {
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getLicenseeIdNo()!=null) {
            filter.put("getLicenseeIdNo", filterDto.getLicenseeIdNo());
        }
        if(filterDto.getLicenseeName()!=null) {
            filter.put("getLicenseeName", filterDto.getLicenseeName());
        }
        if(filterDto.getOrganisationName()!=null){
            filter.put("getOrganisationName",filterDto.getOrganisationName());
        }
        if(filterDto.getLicenseeType()!=null){
            filter.put("getLicenseeType",filterDto.getLicenseeType());
        }
        lisParameter.setFilters(filter);
    }

    

    

    public void nextStep(BaseProcessClass bpc){
        String licenseeId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(licenseeId)) {
            try {
                licenseeId= MaskUtil.unMaskValue(LICENSEE_ID,licenseeId);
                ParamUtil.setSessionAttr(bpc.request, LICENSEE_ID,licenseeId);
                String p = systemParamConfig.getPagingSize();
                String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
                pageSize= Integer.valueOf(defaultValue);

                licTabParameter.setPageSize(pageSize);
                licTabParameter.setPageNo(1);
                licTabParameter.setSortField("BUSINESS_NAME");
                licTabParameter.setSortType(SearchParam.ASCENDING);
                ParamUtil.setSessionAttr(bpc.request,"licenceTabEnquiryFilterDto",null);
                ParamUtil.setSessionAttr(bpc.request, "licTabParam",null);
            }catch (Exception e){
                log.info("no LICENCE_ID");
            }
        }
    }
    
    
    public void preLicenceSearch(BaseProcessClass bpc){

        HttpServletRequest request=bpc.request;
        String licenseeId = (String) ParamUtil.getSessionAttr(bpc.request, LICENSEE_ID);
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> mosdTypeOption =onlineEnquiryLicenceDelegator.getMosdTypeOption();
        ParamUtil.setRequestAttr(request,"mosdTypeOption", mosdTypeOption);
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "lisLicTab","back");
        if (!StringUtil.isEmpty(licenseeId)) {

            SubLicenseeDto subLicenseeDto=hcsaLicenceClient.getSubLicenseesById(licenseeId).getEntity();
            if(subLicenseeDto==null){
                subLicenseeDto=appCommClient.getSubLicenseeDtoById(licenseeId).getEntity();
            }
            if(StringUtil.isEmpty(subLicenseeDto.getUenNo())){
                subLicenseeDto.setUenNo("-");
            }
            ParamUtil.setSessionAttr(request, "subLicenseeDto", subLicenseeDto);

        }

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "licTabParam");

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                licTabParameter.setSortType(sortType);
                licTabParameter.setSortField(sortFieldName);
            }
            LicenceEnquiryFilterDto filterDto=new LicenceEnquiryFilterDto();
            String licenceNo=ParamUtil.getString(request,"licenceNo");
            filterDto.setLicenceNo(licenceNo);
            String mosdType=ParamUtil.getString(request,"mosdType");
            filterDto.setMosdType(mosdType);
            String[] serviceName=ParamUtil.getStrings(request,"serviceName");
            filterDto.setServiceName(Arrays.asList(serviceName));
            String businessName=ParamUtil.getString(request,"businessName");
            filterDto.setBusinessName(businessName);
            String licenceStatus=ParamUtil.getString(request,"licenceStatus");
            filterDto.setLicenceStatus(licenceStatus);
            ParamUtil.setSessionAttr(request,"licenceEnquiryFilterDto",filterDto);

            onlineEnquiryLicenceDelegator.setQueryFilter(filterDto,licTabParameter);

            SearchParam licTabParam = SearchResultHelper.getSearchParam(request, licTabParameter,true);

            
            if(searchParam!=null){
                ParamUtil.setRequestAttr(bpc.request, "preActive", "1");
                licTabParam.setPageNo(searchParam.getPageNo());
                licTabParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(licTabParam,bpc.request);
            licTabParam.addFilter("getLicenseeId",licenseeId,true);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","licenceOnlineEnquiry",licTabParam);
            SearchResult<LicenceQueryResultsDto> licTabResult = onlineEnquiriesService.searchLicenceQueryResult(licTabParam);
            ParamUtil.setRequestAttr(request,"licTabResult",licTabResult);
            ParamUtil.setSessionAttr(request,"licTabParam",licTabParam);
        }else {
            SearchResult<LicenceQueryResultsDto> licTabResult = onlineEnquiriesService.searchLicenceQueryResult(searchParam);
            ParamUtil.setRequestAttr(bpc.request, "preActive", "1");
            ParamUtil.setRequestAttr(request,"licTabResult",licTabResult);
            ParamUtil.setSessionAttr(request,"licTabParam",searchParam);
        }
    }

    public void licenseeStep(BaseProcessClass bpc){
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


    public void perLicInfo(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://")
                .append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTRANET/MohLicenceOnlineEnquiry/1/preLicInfo");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }



}
