package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
    private BeEicGatewayClient gatewayClient;
    @Autowired
    OrganizationClient organizationClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    private final String SEARCH_NO="searchNo";
    private final String RFI_QUERY="ReqForInfoQuery";
    FilterParameter licenceParameter = new FilterParameter.Builder()
            .clz(RfiLicenceQueryDto.class)
            .searchAttr("licParam")
            .resultAttr("licResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(0).pageSize(10).build();

    FilterParameter applicationParameter = new FilterParameter.Builder()
            .clz(RfiApplicationQueryDto.class)
            .searchAttr("appParam")
            .resultAttr("appResult")
            .sortField("application_no").pageNo(0).pageSize(10).sortType(SearchParam.ASCENDING).build();

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");

        // 		Start->OnStepProcess
    }


    public void preBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		preBasicSearch->OnStepProcess
    }

    public void doBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo=ParamUtil.getString(request,"search_no");
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request,SEARCH_NO,searchNo);

        // 		doBasicSearch->OnStepProcess
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


        String searchNo= (String) ParamUtil.getSessionAttr(request,SEARCH_NO);


        String applicationNo = ParamUtil.getString(bpc.request, "application_no");
        if(StringUtil.isEmpty(applicationNo)){
            applicationNo = searchNo;
        }
        String applicationType = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String subDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String toDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);

        Map<String,Object> filters=new HashMap<>();

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
        QueryHelp.setMainSql(RFI_QUERY,"applicationQuery",appParam);
        if (appParam != null) {
            SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

            if(!StringUtil.isEmpty(appResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                ) {
                    Map<String,Object> filter=new HashMap<>();
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
                            reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                            reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                            reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                            reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());
//                            reqForInfoSearchListDto.setCurrentRiskTagging(lic.getRiskLevel());
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
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		doSearchApplication->OnStepProcess
    }

    private void rfiApplicationQueryDtoToReqForInfoSearchListDto(RfiApplicationQueryDto rfiApplicationQueryDto,ReqForInfoSearchListDto reqForInfoSearchListDto){
        reqForInfoSearchListDto.setAppId(rfiApplicationQueryDto.getId());
        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiApplicationQueryDto.getApplicationType()}).get(0).getText();
        reqForInfoSearchListDto.setApplicationType(appType);
        reqForInfoSearchListDto.setApplicationNo(rfiApplicationQueryDto.getApplicationNo());
        reqForInfoSearchListDto.setApplicationStatus(rfiApplicationQueryDto.getApplicationStatus());
        reqForInfoSearchListDto.setHciCode(rfiApplicationQueryDto.getHciCode());
        reqForInfoSearchListDto.setHciName(rfiApplicationQueryDto.getHciName());
        reqForInfoSearchListDto.setBlkNo(rfiApplicationQueryDto.getBlkNo());
        reqForInfoSearchListDto.setBuildingName(rfiApplicationQueryDto.getBuildingName());
        reqForInfoSearchListDto.setUnitNo(rfiApplicationQueryDto.getUnitNo());
        reqForInfoSearchListDto.setStreetName(rfiApplicationQueryDto.getStreetName());
        reqForInfoSearchListDto.setFloorNo(rfiApplicationQueryDto.getFloorNo());
        reqForInfoSearchListDto.setLicenseeId(rfiApplicationQueryDto.getLicenseeId());
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
        reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
    }

    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        String searchNo= (String) ParamUtil.getSessionAttr(request,SEARCH_NO);

        String licence_no = ParamUtil.getString(bpc.request, "licence_no");
        if(StringUtil.isEmpty(licence_no)){
            licence_no = searchNo;
        }
        String serviceLicenceType = ParamUtil.getString(bpc.request, "service_licence_type");
        String licenceStatus = ParamUtil.getString(bpc.request, "licence_status");
        String subDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String toDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> filters=new HashMap<>(10);

        if(!StringUtil.isEmpty(licence_no)){
            filters.put("licence_no", licence_no);
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
        licenceParameter.setFilters(filters);

        SearchParam licParam = SearchResultHelper.getSearchParam(request, licenceParameter,true);
        QueryHelp.setMainSql(RFI_QUERY,"licenceQuery",licParam);
        if (licParam != null) {
            SearchResult<RfiLicenceQueryDto> licResult = requestForInformationService.licenceDoQuery(licParam);

            if(!StringUtil.isEmpty(licResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(licResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiLicenceQueryDto rfiLicenceQueryDto:licResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();
                    String licStatus= MasterCodeUtil.retrieveOptionsByCodes(new String[]{rfiLicenceQueryDto.getLicenceStatus()}).get(0).getText();
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
//                    reqForInfoSearchListDto.setCurrentRiskTagging(rfiLicenceQueryDto.getRiskLevel());
                    LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiLicenceQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());
                    if(!StringUtil.isEmpty(serviceLicenceType)){
                        boolean isAdd=false;
                        List<String> svcNames=requestForInformationService.getSvcNamesByType(serviceLicenceType);
                        for (String svcName:svcNames
                        ) {
                            if(svcName.equals(reqForInfoSearchListDto.getServiceName())){
                                isAdd=true;
                            }
                        }
                        if(isAdd){
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    else {
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        ParamUtil.setRequestAttr(request,"SearchParam", licParam);
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
        String licenseeId = (String) ParamUtil.getSessionAttr(request, "id");
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        OrganizationDto organizationDto= organizationClient.getOrganizationById(licenseeDto.getOrganizationId()).getEntity();
        ParamUtil.setRequestAttr(request,"licenseeDto",licenseeDto);
        ParamUtil.setRequestAttr(request,"organizationDto",organizationDto);
        // 		preAppInfo->OnStepProcess
    }

    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "id");
        List<LicPremisesReqForInfoDto>licPremisesReqForInfoDtoList= requestForInformationService.searchLicPremisesReqForInfo(licPremId);

        if(licPremisesReqForInfoDtoList.size()!=0) {
            ParamUtil.setSessionAttr(request, "licenceNo", licPremisesReqForInfoDtoList.get(0).getLicenceNo());
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
        if(!StringUtil.isEmpty(reqInfoId)) {
            ParamUtil.setSessionAttr(bpc.request, "reqInfoId", reqInfoId);
        }
        // 		doReqForInfo->OnStepProcess
    }

    public void preNewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preNewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
//        String licenceNo= (String) ParamUtil.getSessionAttr(request,"licenceNo");
//        ParamUtil.setRequestAttr(request,"licenceNo",licenceNo);
        // 		preNewRfi->OnStepProcess
    }
    public void doCreateRequest(BaseProcessClass bpc) throws ParseException, IOException, TemplateException {
        log.info("=======>>>>>doCreateRequest>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "id");
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
        officerRemarks.append(ParamUtil.getString(request,"rfiTitle")+" ");
        licPremisesReqForInfoDto.setLicPremId(licPremId);
        String reqType=ParamUtil.getString(request,"reqType");
        boolean isNeedDoc=false;
        officerRemarks.append(" |Information");
        if(!StringUtil.isEmpty(reqType)&&"on".equals(reqType)) {
            isNeedDoc = true;
            officerRemarks.append(" |Supporting documents");
        }
        licPremisesReqForInfoDto.setNeedDocument(isNeedDoc);
        licPremisesReqForInfoDto.setOfficerRemarks(officerRemarks.toString());

        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.createLicPremisesReqForInfo(licPremisesReqForInfoDto);

        String templateId="BEFC2AF0-250C-EA11-BE78-000C29D29DB0";
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto1.getReqInfoId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        Map<String,Object> map=new HashMap<>();
        map.put("APPLICANT_NAME",StringUtil.viewHtml(licenseeDto.getName()));
        map.put("DETAILS",StringUtil.viewHtml(licPremisesReqForInfoDto1.getOfficerRemarks()));
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        licPremisesReqForInfoDto1.setAction("create");
        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto1,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
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
}
