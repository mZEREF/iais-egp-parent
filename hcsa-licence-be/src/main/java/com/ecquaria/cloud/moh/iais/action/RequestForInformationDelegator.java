package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RequestForInformationDelegator
 *
 * @author junyu
 * @date 2019/12/14
 */
@Slf4j
@Delegator("requestForInformationDelegator")
public class RequestForInformationDelegator {
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    LicenceService licenceService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private InsRepService insRepService;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    LicInspNcEmailService licInspNcEmailService;
    @Autowired
    OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    EmailClient emailClient;
    private final String RFI_QUERY="ReqForInfoQuery";
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

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);

        // 		Start->OnStepProcess
    }


    public void preSearchLic(BaseProcessClass bpc) {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo= ParamUtil.getString(request,"search_no");
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();

        if (!StringUtil.isEmpty(searchNo)) {
            filters.put("licence_no", searchNo);
        }
        licenceParameter.setFilters(filters);

        SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);

        CrudHelper.doPaging(licParam,bpc.request);

        QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
        if (licParam != null) {
            SearchResult<RfiLicenceQueryDto> licResult = requestForInformationService.licenceDoQuery(licParam);

            if(!StringUtil.isEmpty(licResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                for (RfiLicenceQueryDto rfiLicenceQueryDto:licResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    String licStatus= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiLicenceQueryDto.getLicenceStatus()}).get(0).getText();
                    reqForInfoSearchListDto.setLicenceId(rfiLicenceQueryDto.getId());
                    reqForInfoSearchListDto.setLicenceStatus(licStatus);
                    reqForInfoSearchListDto.setLicenceNo(rfiLicenceQueryDto.getLicenceNo());
                    reqForInfoSearchListDto.setAppId(rfiLicenceQueryDto.getAppId());
                    reqForInfoSearchListDto.setServiceName(rfiLicenceQueryDto.getServiceName());
                    reqForInfoSearchListDto.setStartDate(rfiLicenceQueryDto.getStartDate());
                    reqForInfoSearchListDto.setExpiryDate(rfiLicenceQueryDto.getExpiryDate());
                    reqForInfoSearchListDto.setHciCode(rfiLicenceQueryDto.getHciCode());
                    reqForInfoSearchListDto.setHciName(rfiLicenceQueryDto.getHciName());
                    reqForInfoSearchListDto.setBlkNo(rfiLicenceQueryDto.getBlkNo());
                    reqForInfoSearchListDto.setBuildingName(rfiLicenceQueryDto.getBuildingName());
                    reqForInfoSearchListDto.setUnitNo(rfiLicenceQueryDto.getUnitNo());
                    reqForInfoSearchListDto.setStreetName(rfiLicenceQueryDto.getStreetName());
                    reqForInfoSearchListDto.setFloorNo(rfiLicenceQueryDto.getFloorNo());
                    reqForInfoSearchListDto.setLicenseeId(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicPremId(rfiLicenceQueryDto.getLicPremId());

                    LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);

                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        ParamUtil.setRequestAttr(request,"SearchParam", licParam);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request, "search_no",searchNo);
        // 		preLicSearch->OnStepProcess
    }


    public void preSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearch>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo= ParamUtil.getString(request,"search_no");
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();

        if (!StringUtil.isEmpty(searchNo)) {
            filters.put("appNo", searchNo);
        }

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
                    QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
                    SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                    if(licResult.getRowCount()!=0) {
                        for (RfiLicenceQueryDto lic:licResult.getRows()
                        ) {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();

                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
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
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }

        ParamUtil.setRequestAttr(request,"SearchParam", appParam);

        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request, "search_no",searchNo);
        // 		preBasicSearch->OnStepProcess
    }


    public void preSearchLicence(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();

        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		preSearchLicence->OnStepProcess
    }

    public void preSearchApplication(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        // 		preSearchApplication->OnStepProcess
    }

    public void doSearchApplication(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();


        String applicationNo = ParamUtil.getString(bpc.request, "application_no");

        String applicationType = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String subDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String toDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);

        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();

        if(!StringUtil.isEmpty(applicationNo)){
            filters.put("appNo", applicationNo);
        }
        if(!StringUtil.isEmpty(applicationType)){
            filters.put("appType", applicationType);
        }
        if(!StringUtil.isEmpty(status)){
            filters.put("appStatus", status);
        }
        if(!StringUtil.isEmpty(subDate)){
            filters.put("subDate", subDate);
        }
        if(!StringUtil.isEmpty(toDate)){
            filters.put("toDate",toDate);
        }
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
                    Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
                    if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                        filter.put("app_id", rfiApplicationQueryDto.getId());
                    }
                    licenceParameter.setFilters(filter);
                    SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
                    QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
                    SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                    if(licResult.getRowCount()!=0) {
                        for (RfiLicenceQueryDto lic:licResult.getRows()
                        ) {
                            ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                            rfiApplicationQueryDtoToReqForInfoSearchListDto(rfiApplicationQueryDto,reqForInfoSearchListDto);
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
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
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        if(!StringUtil.isEmpty(subDate)){
            appParam.getFilters().put("subDate",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(toDate)){
            appParam.getFilters().put("toDate",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        ParamUtil.setRequestAttr(request,"SearchParam", appParam);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		doSearchApplication->OnStepProcess
    }

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(RfiApplicationQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto){
        reqForInfoSearchListDto.setAppId(rfiApplicationQueryDto.getId());
        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getApplicationType()}).get(0).getText();
        reqForInfoSearchListDto.setAppCorrId(rfiApplicationQueryDto.getAppCorrId());
        reqForInfoSearchListDto.setApplicationType(appType);
        reqForInfoSearchListDto.setApplicationNo(rfiApplicationQueryDto.getApplicationNo());
        reqForInfoSearchListDto.setApplicationStatus(rfiApplicationQueryDto.getApplicationStatus());
        reqForInfoSearchListDto.setServiceName(rfiApplicationQueryDto.getSvcId());
        reqForInfoSearchListDto.setHciCode(rfiApplicationQueryDto.getHciCode());
        reqForInfoSearchListDto.setHciName(rfiApplicationQueryDto.getHciName());
        reqForInfoSearchListDto.setBlkNo(rfiApplicationQueryDto.getBlkNo());
        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        String riskLevel = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getRiskLevel()}).get(0).getText();
        reqForInfoSearchListDto.setCurrentRiskTagging(riskLevel);
        try{
            reqForInfoSearchListDto.setPastComplianceHistory("Full");
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = insepctionNcCheckListService.getNcItemDtoByAppCorrId(rfiApplicationQueryDto.getAppCorrId());
            for (AppPremisesPreInspectionNcItemDto nc:appPremisesPreInspectionNcItemDtos
            ) {
                if(nc.getIsRecitfied()==0){
                    reqForInfoSearchListDto.setPastComplianceHistory("Partial");
                }
            }
        }catch (Exception e){
            reqForInfoSearchListDto.setPastComplianceHistory("-");
        }

        log.debug(StringUtil.changeForLog("licenseeId start ...."+rfiApplicationQueryDto.getLicenseeId()));
        if(rfiApplicationQueryDto.getLicenseeId()!=null){
            reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
            try {
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
                reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
            } catch (Exception e) {

            }
        }

    }

    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();



        String licenceNo = ParamUtil.getString(bpc.request, "licence_no");

        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, "licence_status");
        String subDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String toDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> filters=IaisCommonUtils.genNewHashMap();

        if(!StringUtil.isEmpty(licenceNo)){
            filters.put("licence_no", licenceNo);
        }
        if(!StringUtil.isEmpty(licenceStatus)){
            filters.put("licence_status", licenceStatus);
        }
        if(!StringUtil.isEmpty(subDate)){
            filters.put("start_date", subDate);
        }
        if(!StringUtil.isEmpty(toDate)){
            filters.put("expiry_date", toDate);
        }
        if(!StringUtil.isEmpty(serviceLicenceType)){
            List<String> svcNames= new ArrayList<>(0);
            svcNames.add(serviceLicenceType);
            filters.put("svc_names",svcNames);
        }
        licenceParameter.setFilters(filters);

        SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);

        CrudHelper.doPaging(licParam,bpc.request);

        QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
        if (licParam != null) {
            SearchResult<RfiLicenceQueryDto> licResult = requestForInformationService.licenceDoQuery(licParam);

            if(!StringUtil.isEmpty(licResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=IaisCommonUtils.genNewArrayList();
                for (RfiLicenceQueryDto rfiLicenceQueryDto:licResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    reqForInfoSearchListDto.setLicenceId(rfiLicenceQueryDto.getId());
                    String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiLicenceQueryDto.getLicenceStatus()}).get(0).getText();
                    reqForInfoSearchListDto.setLicenceStatus(licStatus);
                    reqForInfoSearchListDto.setLicenceNo(rfiLicenceQueryDto.getLicenceNo());
                    reqForInfoSearchListDto.setAppId(rfiLicenceQueryDto.getAppId());
                    reqForInfoSearchListDto.setServiceName(rfiLicenceQueryDto.getServiceName());
                    reqForInfoSearchListDto.setStartDate(rfiLicenceQueryDto.getStartDate());
                    reqForInfoSearchListDto.setExpiryDate(rfiLicenceQueryDto.getExpiryDate());
                    reqForInfoSearchListDto.setHciCode(rfiLicenceQueryDto.getHciCode());
                    reqForInfoSearchListDto.setHciName(rfiLicenceQueryDto.getHciName());
                    reqForInfoSearchListDto.setBlkNo(rfiLicenceQueryDto.getBlkNo());
                    reqForInfoSearchListDto.setBuildingName(rfiLicenceQueryDto.getBuildingName());
                    reqForInfoSearchListDto.setUnitNo(rfiLicenceQueryDto.getUnitNo());
                    reqForInfoSearchListDto.setStreetName(rfiLicenceQueryDto.getStreetName());
                    reqForInfoSearchListDto.setFloorNo(rfiLicenceQueryDto.getFloorNo());
                    reqForInfoSearchListDto.setLicenseeId(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicPremId(rfiLicenceQueryDto.getLicPremId());
                    LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);

                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        if(!StringUtil.isEmpty(subDate)){
            licParam.getFilters().put("start_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(toDate)){
            licParam.getFilters().put("expiry_date",Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                    AppConsts.DEFAULT_DATE_FORMAT));
        }
        if(!StringUtil.isEmpty(serviceLicenceType)){
            licParam.getFilters().put("licSvcName",serviceLicenceType);
        }
        ParamUtil.setRequestAttr(request,"SearchParam", licParam);
        ParamUtil.setRequestAttr(request,"serviceLicenceType",serviceLicenceType);
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		doSearchLicence->OnStepProcess
    }

    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
        // 		doSearchLicenceAfter->OnStepProcess
    }
    public void preRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void doSearchApplicationAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplicationAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
// 		doSearchApplicationAfter->OnStepProcess
    }

    public void preAppInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String appCorrId = (String) ParamUtil.getSessionAttr(request, "id");
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(appCorrId);
        applicationViewDto.getApplicationDto().setApplicationType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationViewDto.getApplicationDto().getId());

        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =  appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto=null;
        if(oldAppSubmissionDto!=null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto,bpc.request);
        }
        /*************************/
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        if(appSvcRelatedInfoDto != null){
            String serviceId = appSvcRelatedInfoDto.getServiceId();
            hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocationList = IaisCommonUtils.genNewArrayList();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            String hciName = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getHciName();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getConveyanceVehicleNo();
            }

            if(!StringUtil.isEmpty(hciName) && allocationDto !=null && allocationDto.size()>0 ){
                for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                    List<AppSvcChckListDto> appSvcChckListDtoList = null;
                    if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                        String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                        String idNo = appSvcDisciplineAllocationDto.getIdNo();
                        //set chkLstName
                        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                        if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                    appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                }
                            }
                        }
                        if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                            for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                                if(hcsaSvcSubtypeOrSubsumedDto!=null){
                                    appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                }
                                if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                    appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                }
                            }
                        }
                        //set selCgoName
                        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                            for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                if(idNo.equals(appSvcCgoDto.getIdNo())){
                                    appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                }
                            }
                        }
                        reloadDisciplineAllocationList.add(appSvcDisciplineAllocationDto);
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationList);

        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationType()}).get(0).getText();
        applicationViewDto.setApplicationType(appType);
        List<HcsaServiceDto> hcsaServiceDto=hcsaConfigClient.getHcsaService(new ArrayList<>(Collections.singleton(applicationViewDto.getApplicationDto().getServiceId()))).getEntity();
        ParamUtil.setRequestAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setRequestAttr(request,"hcsaServiceDto",hcsaServiceDto.get(0));
        // 		preAppInfo->OnStepProcess
    }




    private HcsaSvcSubtypeOrSubsumedDto getHcsaSvcSubtypeOrSubsumedDtoById(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,String id){
        HcsaSvcSubtypeOrSubsumedDto result = null;
        if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)&&!StringUtil.isEmpty(id)){
            for (HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto : hcsaSvcSubtypeOrSubsumedDtos){
                if(id.equals(hcsaSvcSubtypeOrSubsumedDto.getId())){
                    result = hcsaSvcSubtypeOrSubsumedDto;
                    break;
                }else{
                    result = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDto.getList(),id);
                    if(result!=null){
                        break;
                    }
                }
            }
        }
        return result;
    }


    private  AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, AppSubmissionDto appSubmissionDto, HttpServletRequest request){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto=new AppSvcRelatedInfoDto();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
            List<AppSvcDisciplineAllocationDto> allocationDto = null;
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
            if(appSvcRelatedInfoDto != null){
                String serviceId = appSvcRelatedInfoDto.getServiceId();
                hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
                allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
            }
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
                String hciName = "";
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                    hciName = appGrpPremisesDto.getHciName();
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                    hciName = appGrpPremisesDto.getConveyanceVehicleNo();
                }

                if(!StringUtil.isEmpty(hciName) && allocationDto !=null && !allocationDto.isEmpty() ){
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                        List<AppSvcChckListDto> appSvcChckListDtoList = null;
                        if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                            String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                            String idNo = appSvcDisciplineAllocationDto.getIdNo();
                            //set chkLstName
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                            if(appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()){
                                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                    if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                    }
                                }
                            }
                            if(appSvcChckListDtoList != null && !appSvcChckListDtoList.isEmpty()){
                                for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                                    if(hcsaSvcSubtypeOrSubsumedDto!=null){
                                        appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                    }
                                    if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                        appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                    }
                                }
                            }
                            //set selCgoName
                            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                            if(appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()){
                                for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                    if(idNo.equals(appSvcCgoDto.getIdNo())){
                                        appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                    }
                                }
                            }
                            reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                        }
                    }
                }
                reloadDisciplineAllocationMap.put(hciName, reloadDisciplineAllocation);
            }
            ParamUtil.setSessionAttr(request, "reloadOld", (Serializable) reloadDisciplineAllocationMap);

        }

        return  appSvcRelatedInfoDto;
    }

    public void preLicInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preLicInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setLicInfo(request);
        // 		preAppInfo->OnStepProcess
    }

    public void preInspReport(BaseProcessClass bpc) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.preInspReport(request);
        // 		preAppInfo->OnStepProcess
    }

    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String  licPremId = (String) ParamUtil.getSessionAttr(request,"id");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoList= requestForInformationService.searchLicPremisesReqForInfo(licPremId);
        for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoList
             ) {
            OrganizationLicDto organizationLicDto= organizationClient.getOrganizationLicDtoByLicenseeId(licPreRfi.getLicenseeId()).getEntity();
            if(organizationLicDto.getLicenseeEntityDto()!=null){
                licPreRfi.setEmail(Arrays.toString(organizationLicDto.getLicenseeEntityDto().getOfficeEmailAddr()));
            }
        }

        ParamUtil.setRequestAttr(request, "isValid", "Y");
//        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

//        String userId = loginContext.getUserId();
//        ParamUtil.setRequestAttr(request, "isValid", "N");
//        try{
//            List<String> userIds=requestForInformationService.getActionBysByLicPremCorrId(licPremId);
//            for (String id:userIds
//            ) {
//                if (userId.equals(id)){
//                    ParamUtil.setRequestAttr(request, "isValid", "Y");
//                }
//            }
//        }catch (Exception e){
//            log.info(e.getMessage());
//        }

        if(!licPremisesReqForInfoDtoList.isEmpty()) {
            for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoList
                 ) {
                if(StringUtil.isEmpty(licPreRfi.getUserReply())){
                    ParamUtil.setSessionAttr(request, "licenceNo", licPreRfi.getLicenceNo());
                }
            }
        }
        else {
            ParamUtil.setSessionAttr(request, "licenceNo", "");
        }

        ParamUtil.setRequestAttr(request,"licPreReqForInfoDtoList", licPremisesReqForInfoDtoList);

//        List<SelectOption> selectOptions=MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.REQUEST_FOR_SUPPORTING_DOCUMENTS,RequestForInformationConstants.REQUEST_FOR_INFORMATION,RequestForInformationConstants.OTHERS});
//        ParamUtil.setRequestAttr(request,"selectOptions", selectOptions);
        // 		preReqForInfo->OnStepProcess
    }

    public void doReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>doReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        String reqInfoId = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(bpc.request, "reqInfoId", reqInfoId);
        // 		doReqForInfo->OnStepProcess
    }

    public void preNewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preNewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;

        // 		preNewRfi->OnStepProcess
    }
    public void doCreateRequest(BaseProcessClass bpc) throws ParseException, IOException, TemplateException {
        log.info("=======>>>>>doCreateRequest>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "id");
        LicenceViewDto licenceViewDto=licInspNcEmailService.getLicenceDtoByLicPremCorrId(licPremId);
        StringBuilder officerRemarks=new StringBuilder();
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();
        licPremisesReqForInfoDto.setReqType(RequestForInformationConstants.AD_HOC);
        String date=ParamUtil.getString(request, "Due_date");
        Date dueDate;
        Calendar calendar = Calendar.getInstance();
        if(!StringUtil.isEmpty(date)){
            dueDate= Formatter.parseDate(date);
        }
        else {
            calendar.add(Calendar.DATE,7);
            dueDate =calendar.getTime();
        }
        licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
        String rfiTitle=ParamUtil.getString(request, "rfiTitle");
        officerRemarks.append(rfiTitle).append(" ");
        licPremisesReqForInfoDto.setLicPremId(licPremId);
        String reqType=ParamUtil.getString(request,"reqType");
        boolean isNeedDoc=false;
        officerRemarks.append(" |Information");
        if(!StringUtil.isEmpty(reqType)&&"on".equals(reqType)) {
            isNeedDoc = true;
            officerRemarks.append(" |Supporting Documents");
        }
        licPremisesReqForInfoDto.setNeedDocument(isNeedDoc);
        licPremisesReqForInfoDto.setOfficerRemarks(officerRemarks.toString());
        licPremisesReqForInfoDto.setRequestDate(new Date());
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        licPremisesReqForInfoDto.setRequestUser(loginContext.getUserId());

        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.createLicPremisesReqForInfo(licPremisesReqForInfoDto);

        String templateId="BF0EFC2A-250C-EA11-BE78-000C29D29DB0";
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto1.getReqInfoId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<p>   1. ").append("Information ").append(rfiTitle).append("</p>");
        map.put("APPLICANT_NAME",StringUtil.viewHtml(licenseeDto.getName()));
        if(licPremisesReqForInfoDto1.isNeedDocument()){
            stringBuilder.append("<p>   2. ").append("Documentations  ").append(rfiTitle).append("</p>");
        }
        map.put("DETAILS",StringUtil.viewHtml("<p>"+"</p>"));
        String url = "https://" + systemParamConfig.getInterServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                "?licenseeId=" + licenseeId;
        map.put("A_HREF", url);
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);


        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        licPremisesReqForInfoDto1.setAction("create");
        log.info("=======>>>>>Create Lic Request for Information reqInfoId "+licPremisesReqForInfoDto1.getReqInfoId());
        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto1,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization());

        //send message to FE user.
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(rfiEmailTemplateDto.getSubject());
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
        interMessageDto.setService_id(svcDto.getId());
        interMessageDto.setMsgContent(mesContext);
        interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
        interMessageDto.setUserId(licenseeId);
        //interMessageDto.setUserId("B4C95F9B-5D30-EA11-BE78-000C29D29DB0");
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
        log.debug(StringUtil.changeForLog("the do requestForInformation end ...."));

        try {
            EmailDto emailDto=new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(rfiEmailTemplateDto.getSubject());
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(licPremId);
            String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
        }catch (Exception e){
            log.info(e.getMessage());
        }
        // 		doCreateRequest->OnStepProcess
    }

    public void doCancel(BaseProcessClass bpc) {
        log.info("=======>>>>>doCancel>>>>>>>>>>>>>>>>requestForInformation");
        String reqInfoId = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        requestForInformationService.deleteLicPremisesReqForInfo(reqInfoId);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();
        licPremisesReqForInfoDto.setReqInfoId(reqInfoId);
        licPremisesReqForInfoDto.setAction("delete");
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
        // 		doCancel->OnStepProcess
    }
    public void doAccept(BaseProcessClass bpc) {
        log.info("=======>>>>>doAccept>>>>>>>>>>>>>>>>requestForInformation");
        String reqInfoId = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(reqInfoId);
        requestForInformationService.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);
        licPremisesReqForInfoDto.setReqInfoId(reqInfoId);
        licPremisesReqForInfoDto.setAction("delete");
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
        // 		doAccept->OnStepProcess
    }
    public void preViewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preViewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = (String) ParamUtil.getSessionAttr(bpc.request, "reqInfoId");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(id);
        licPremisesReqForInfoDto.setOfficerRemarks(licPremisesReqForInfoDto.getOfficerRemarks().split("\\|")[0]);
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
        // 		preViewRfi->OnStepProcess
    }
    public void doUpdate(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>preCancel>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String reqInfoId = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        String date=ParamUtil.getString(request, "Due_date");
        Date dueDate;
        Calendar calendar = Calendar.getInstance();
        if(!StringUtil.isEmpty(date)){
            dueDate= Formatter.parseDate(date);
        }
        else {
            calendar.add(Calendar.DATE,RequestForInformationConstants.REMIND_INTERVAL_DAY);
            dueDate =calendar.getTime();
        }
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(reqInfoId);
        licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
        requestForInformationService.updateLicPremisesReqForInfo(licPremisesReqForInfoDto);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        licPremisesReqForInfoDto.setAction("update");
        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();        // 		doUpdate->OnStepProcess
    }


    @GetMapping(value = "/file-repo")
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =requestForInformationService.downloadFile(fileRepoId);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }
}
