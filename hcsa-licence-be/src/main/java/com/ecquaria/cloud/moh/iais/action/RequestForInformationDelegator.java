package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.NewRfiPageListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    InsRepClient insRepClient;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    LicInspNcEmailService licInspNcEmailService;
    @Autowired
    OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    InspectionRectificationProService inspectionRectificationProService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    FillupChklistService fillupChklistService;
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
        AuditTrailHelper.auditFunction("RequestForInformation Management", "RequestForInformation Config");
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
                    List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(rfiLicenceQueryDto.getId()).getEntity();
                    reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                    reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                    List<String> addressList = IaisCommonUtils.genNewArrayList();
                    for (PremisesDto premisesDto:premisesDtoList
                    ) {
                        addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                        reqForInfoSearchListDto.setAddress(addressList);
                    }
                    reqForInfoSearchListDto.setLicenseeId(rfiLicenceQueryDto.getLicenseeId());
                    List<LicPremisesDto> licPremisesDtos=hcsaLicenceClient.getLicPremListByLicId(rfiLicenceQueryDto.getId()).getEntity();
                    reqForInfoSearchListDto.setLicPremId(licPremisesDtos.get(0).getId());
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
                            List<LicPremisesDto> licPremisesDtos=hcsaLicenceClient.getLicPremListByLicId(lic.getId()).getEntity();
                            reqForInfoSearchListDto.setLicPremId(licPremisesDtos.get(0).getId());
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
            if(status.equals(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED)){
                status=ApplicationConsts.APPLICATION_STATUS_APPROVED;
            }
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
                            List<LicPremisesDto> licPremisesDtos=hcsaLicenceClient.getLicPremListByLicId(lic.getId()).getEntity();
                            reqForInfoSearchListDto.setLicPremId(licPremisesDtos.get(0).getId());
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
        String riskLevel;
        try {
            List<ChecklistItemDto> checklistItemDtos=inspectionRectificationProService.getQuesAndClause( rfiApplicationQueryDto.getAppCorrId());
            if(checklistItemDtos.size()!=0){
                riskLevel = MasterCodeUtil.retrieveOptionsByCodes(new String[]{checklistItemDtos.get(0).getRiskLevel()}).get(0).getText();
            }else {
                riskLevel = MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getRiskLevel()}).get(0).getText();
            }
        }catch (Exception e){
            riskLevel="-";
        }
        reqForInfoSearchListDto.setCurrentRiskTagging(riskLevel);
        try{
            List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(rfiApplicationQueryDto.getAppCorrId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
            if(appPremisesRecommendationDtos.size()>=1){
                reqForInfoSearchListDto.setLastComplianceHistory("Full");
                RequestForInformationDelegator.setSearchListComplianceHistory(rfiApplicationQueryDto, reqForInfoSearchListDto, insepctionNcCheckListService, fillupChklistService);
            }
            else {
                reqForInfoSearchListDto.setLastComplianceHistory("-");
            }
        }catch (Exception e){
            reqForInfoSearchListDto.setLastComplianceHistory("-");
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

    static void setSearchListComplianceHistory(RfiApplicationQueryDto rfiApplicationQueryDto, ReqForInfoSearchListDto reqForInfoSearchListDto, InsepctionNcCheckListService insepctionNcCheckListService, FillupChklistService fillupChklistService) {
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
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
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
                    List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(rfiLicenceQueryDto.getId()).getEntity();
                    reqForInfoSearchListDto.setHciCode(premisesDtoList.get(0).getHciCode());
                    reqForInfoSearchListDto.setHciName(premisesDtoList.get(0).getHciName());
                    List<String> addressList = IaisCommonUtils.genNewArrayList();
                    for (PremisesDto premisesDto:premisesDtoList
                    ) {
                        addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                        reqForInfoSearchListDto.setAddress(addressList);
                    }
                    reqForInfoSearchListDto.setLicenseeId(rfiLicenceQueryDto.getLicenseeId());
                    List<LicPremisesDto> licPremisesDtos=hcsaLicenceClient.getLicPremListByLicId(rfiLicenceQueryDto.getId()).getEntity();
                    reqForInfoSearchListDto.setLicPremId(licPremisesDtos.get(0).getId());
                    LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                    reqForInfoSearchListDtos.add(reqForInfoSearchListDto);

                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
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
        try {
            String id = ParamUtil.getMaskedString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
            ParamUtil.setSessionAttr(request,"id",id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        // 		doSearchLicenceAfter->OnStepProcess
    }
    public void preRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = ParamUtil.getMaskedString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"id",id);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void doSearchApplicationAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplicationAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        try {
            String id = ParamUtil.getMaskedString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
            ParamUtil.setSessionAttr(request,"id",id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

// 		doSearchApplicationAfter->OnStepProcess
    }

    public void preAppInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        onlineEnquiriesService.setAppInfo(request);
        // 		preAppInfo->OnStepProcess
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
//            log.error(e.getMessage(), e);
//        }

        if(!licPremisesReqForInfoDtoList.isEmpty()) {
            for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoList
            ) {
                try {
                    licPreRfi.setEmail(IaisEGPHelper.getLicenseeEmailAddrs(licPreRfi.getLicenseeId()).get(0));
                }catch (Exception e){
                    licPreRfi.setEmail("-");
                }
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
        try {
            String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            ParamUtil.setSessionAttr(bpc.request, "reqInfoId", reqInfoId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        // 		doReqForInfo->OnStepProcess
    }

    public void preNewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preNewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        if(lengths!=null){
            List<NewRfiPageListDto> newRfiPageListDtos=IaisCommonUtils.genNewArrayList(lengths.length);
            for (String len:lengths
            ) {
                String decision=ParamUtil.getString(request, "decision"+len);
                String date= ParamUtil.getString(request, "Due_date"+len);
                String rfiTitle=ParamUtil.getString(request, "rfiTitle"+len);
                String reqType=ParamUtil.getString(request,"reqType"+len);
                String licenceNo=ParamUtil.getString(request,"licenceNo"+len);
                NewRfiPageListDto newRfiPageListDto=new NewRfiPageListDto();
                newRfiPageListDto.setDate(date);
                newRfiPageListDto.setDecision(decision);
                newRfiPageListDto.setLicenceNo(licenceNo);
                newRfiPageListDto.setRfiTitle(rfiTitle);
                newRfiPageListDto.setReqType(reqType);
                newRfiPageListDtos.add(newRfiPageListDto);
            }
            ParamUtil.setRequestAttr(bpc.request, "newRfiPageListDtos", newRfiPageListDtos);
        }
        List<SelectOption> salutationList= IaisCommonUtils.genNewArrayList();
        SelectOption selectOption=new SelectOption();
        selectOption.setValue("documents");
        selectOption.setText("Request for Supporting Documents");
        salutationList.add(selectOption);
        SelectOption selectOption1=new SelectOption();
        selectOption1.setValue("information");
        selectOption1.setText("Request for Information");
        salutationList.add(selectOption1);
        ParamUtil.setRequestAttr(bpc.request, "salutationList", salutationList);
        // 		preNewRfi->OnStepProcess
    }
    public void doCreateRequest(BaseProcessClass bpc) throws ParseException, IOException, TemplateException {
        log.info("=======>>>>>doCreateRequest>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "id");
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(request);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }

        for (String len:lengths
        ) {
            String decision=ParamUtil.getString(request, "decision"+len);
            String date= ParamUtil.getString(request, "Due_date"+len);
            String rfiTitle=ParamUtil.getString(request, "rfiTitle"+len);
            String reqType=ParamUtil.getString(request,"reqType"+len);

            LicenceViewDto licenceViewDto=licInspNcEmailService.getLicenceDtoByLicPremCorrId(licPremId);
            StringBuilder officerRemarks=new StringBuilder();
            LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();
            licPremisesReqForInfoDto.setReqType(RequestForInformationConstants.AD_HOC);
            Date dueDate;
            Calendar calendar = Calendar.getInstance();
            if(!StringUtil.isEmpty(date)){
                dueDate=Formatter.parseDate(date);
            }
            else {
                calendar.add(Calendar.DATE,14);
                dueDate =calendar.getTime();
            }
            licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
            officerRemarks.append(rfiTitle).append(' ');
            licPremisesReqForInfoDto.setLicPremId(licPremId);
            if("information".equals(decision)){
                officerRemarks.append(" |Information");
            }
            if("documents".equals(decision)){
                officerRemarks.append(" |Supporting Documents");
            }
            boolean isNeedDoc=false;
            if(!StringUtil.isEmpty(reqType)&&"on".equals(reqType)) {
                isNeedDoc = true;
            }
            licPremisesReqForInfoDto.setNeedDocument(isNeedDoc);
            licPremisesReqForInfoDto.setOfficerRemarks(officerRemarks.toString());
            licPremisesReqForInfoDto.setRequestDate(new Date());
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            licPremisesReqForInfoDto.setRequestUser(loginContext.getUserId());

            LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.createLicPremisesReqForInfo(licPremisesReqForInfoDto);

            String templateId= MsgTemplateConstants.MSG_TEMPLATE_RFI;
            InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
            String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto1.getReqInfoId()).getLicenseeId();
            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
            Map<String,Object> map=IaisCommonUtils.genNewHashMap();
            StringBuilder stringBuilder=new StringBuilder();
            if("information".equals(decision)){
                stringBuilder.append("<p>   1. ").append("Information ").append(rfiTitle).append("</p>");
            }
            if("documents".equals(decision)){
                stringBuilder.append("<p>   1. ").append("Documentations  ").append(rfiTitle).append("</p>");
            }
            map.put("APPLICANT_NAME",StringUtil.viewHtml(licenseeDto.getName()));
            map.put("DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
            map.put("COMMENTS",StringUtil.viewHtml(""));
            String url = "https://" + systemParamConfig.getInterServerName() +
                    "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                    "?licenseeId=" + licenseeId;
            map.put("A_HREF", url);
            map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
            String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
            HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
            mapPrem.put("licenseeId",licenseeId);

            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            licPremisesReqForInfoDto1.setAction("create");
            log.info(StringUtil.changeForLog("=======>>>>>Create Lic Request for Information reqInfoId "+licPremisesReqForInfoDto1.getReqInfoId()));
            try{
                gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto1,
                        signature.date(), signature.authorization(), signature2.date(), signature2.authorization());

                //send message to FE user.
                InterMessageDto interMessageDto = new InterMessageDto();
                interMessageDto.setMaskParams(mapPrem);
                interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
                List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceViewDto.getLicenceDto().getId()).getEntity();
                ApplicationDto applicationDto=applicationClient.getApplicationById(licAppCorrelationDtos.get(0).getApplicationId()).getEntity();
                String subject=rfiEmailTemplateDto.getSubject().replace("Application Number",applicationDto.getApplicationNo());
                interMessageDto.setSubject(subject);
                interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
                String mesNO = inboxMsgService.getMessageNo();
                interMessageDto.setRefNo(mesNO);
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
                interMessageDto.setService_id(svcDto.getId());
                interMessageDto.setMsgContent(mesContext);
                interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
                interMessageDto.setUserId(licenseeId);
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
                    log.error(e.getMessage(), e);
                }
            }catch (Exception e){
                requestForInformationService.deleteLicPremisesReqForInfo(licPremisesReqForInfoDto1.getReqInfoId());
            }
        }
//        if(lengths.length>=1){
//            requestForInformationService.clarificationApplicationUpdateByLicPremId(licPremId);
//        }

        // 		doCreateRequest->OnStepProcess
    }

    public void doCancel(BaseProcessClass bpc) {
        log.info("=======>>>>>doCancel>>>>>>>>>>>>>>>>requestForInformation");
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
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
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
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
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
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

    private Map<String, String> validate(HttpServletRequest request) throws ParseException {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        for (String len:lengths
             ) {
            String decision=ParamUtil.getString(request, "decision"+len);
            if(decision==null|| "Please Select".equals(decision)){
                errMap.put("rfiSelect"+len,"ERR0010");

            }
            String date=ParamUtil.getDate(request, "Due_date"+len);

            if(date==null){
                errMap.put("Due_date"+len,"ERR0010");
            }else {
                date= ParamUtil.getString(request, "Due_date"+len);
                String now=new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(new Date());
                if(date.compareTo(now) <0 ){
                    errMap.put("Due_date"+len,"Due Date should be a future Date.");
                }
            }
            String rfiTitle=ParamUtil.getString(request, "rfiTitle"+len);
            if(rfiTitle==null){
                errMap.put("rfiTitle"+len,"ERR0010");

            }
        }


        return errMap;
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

    @GetMapping(value = "/new-rfi-html")
    public @ResponseBody String genNewRfiHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String length = ParamUtil.getString(request,"Length");

        if(length==null){
            length="0";
        }

        String sql = SqlMap.INSTANCE.getSql("ReqForInfoQuery", "rfi-new").getSqlStr();

        List<SelectOption> salutationList= IaisCommonUtils.genNewArrayList();
        SelectOption selectOption=new SelectOption();
        selectOption.setValue("documents");
        selectOption.setText("Request for Supporting Documents");
        salutationList.add(selectOption);
        SelectOption selectOption1=new SelectOption();
        selectOption1.setValue("information");
        selectOption1.setText("Request for Information");
        salutationList.add(selectOption1);
        Map<String,String> rfiSelect = IaisCommonUtils.genNewHashMap();
        rfiSelect.put("name", "decision"+length);
        rfiSelect.put("style", "display: none;");
        String salutationSelectStr = generateDropDownHtml(rfiSelect, salutationList,"Please Select", null);
        sql = sql.replace("(rfiSelect)", salutationSelectStr);

        sql=sql.replaceAll("indexRfi",length);

        return sql;
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption, String checkedVal){
        StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
            sBuffer.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        sBuffer.append(" >");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<option value=\"\">").append(firestOption).append("</option>");
        }
        for(SelectOption sp:selectOptionList){
            if(!StringUtil.isEmpty(checkedVal)){
                if(checkedVal.equals(sp.getValue())){
                    sBuffer.append("<option selected=\"selected\" value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }else{
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }
            }else{
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
            }
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        if(!StringUtil.isEmpty(checkedVal)){
            sBuffer.append("<span selected=\"selected\" class=\"current\">").append(checkedVal).append("</span>");
        }else{
            if(!StringUtil.isEmpty(firestOption)){
                sBuffer.append("<span class=\"current\">").append(firestOption).append("</span>");
            }else{
                sBuffer.append("<span class=\"current\">").append(selectOptionList.get(0).getText()).append("</span>");
            }
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");

        if(!StringUtil.isEmpty(checkedVal)){
            for(SelectOption kv:selectOptionList){
                if(checkedVal.equals(kv.getValue())){
                    sBuffer.append("<li selected=\"selected\" data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }else if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"\" class=\"option selected\">").append(firestOption).append("</li>");
            for(SelectOption kv:selectOptionList){
                sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
            }
        }else{
            for(int i = 0;i<selectOptionList.size();i++){
                SelectOption kv = selectOptionList.get(i);
                if(i == 0){
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }

        sBuffer.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }
}
