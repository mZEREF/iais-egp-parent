package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
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
        ParamUtil.setSessionAttr(request,"searchNo",searchNo);

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


        String searchNo= (String) ParamUtil.getSessionAttr(request,"searchNo");


        String application_no = ParamUtil.getString(bpc.request, "application_no");
        if(StringUtil.isEmpty(application_no)){
            application_no = searchNo;
        }
        String application_type = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String sub_date = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                "yyyy-MM-dd");
        String to_date = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                "yyyy-MM-dd");

        Map<String,Object> filters=new HashMap<>();

        if(!StringUtil.isEmpty(application_no)){
            filters.put("appNo", application_no);
        }
        if(!StringUtil.isEmpty(application_type)){
            filters.put("appType", application_type);
        }
        if(!StringUtil.isEmpty(status)){
            filters.put("appStatus", status);
        }
        if(!StringUtil.isEmpty(sub_date)){
            filters.put("subDate", sub_date);
        }
        if(!StringUtil.isEmpty(to_date)){
            filters.put("toDate",to_date);
        }
        applicationParameter.setFilters(filters);
        SearchParam appParam = SearchResultHelper.getSearchParam(request, true,applicationParameter);
        QueryHelp.setMainSql("ReqForInfoQuery","applicationQuery",appParam);
        if (appParam != null) {
            SearchResult<RfiApplicationQueryDto> appResult = requestForInformationService.appDoQuery(appParam);

            if(!StringUtil.isEmpty(appResult)){
                SearchResult<ReqForInfoSearchListDto> searchListDtoSearchResult=new SearchResult<>();
                searchListDtoSearchResult.setRowCount(appResult.getRowCount());
                List<ReqForInfoSearchListDto> reqForInfoSearchListDtos=new ArrayList<>();
                for (RfiApplicationQueryDto rfiApplicationQueryDto:appResult.getRows()
                ) {
                    ReqForInfoSearchListDto reqForInfoSearchListDto=new ReqForInfoSearchListDto();

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
                    LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(rfiApplicationQueryDto.getLicenseeId());
                    reqForInfoSearchListDto.setLicenseeName(licenseeDto.getName());

                    licenceParameter.setClz(RfiLicenceQueryDto.class);
                    licenceParameter.setSearchAttr("licParam");
                    licenceParameter.setResultAttr("licResult");
                    Map<String,Object> filter=new HashMap<>();
                    if(!StringUtil.isEmpty(rfiApplicationQueryDto.getId())){
                        filter.put("app_id", rfiApplicationQueryDto.getId());
                    }
                    licenceParameter.setFilters(filter);
                    SearchParam licParam = SearchResultHelper.getSearchParam(request, true,licenceParameter);
                    QueryHelp.setMainSql("ReqForInfoQuery","licenceQuery",licParam);
                    SearchResult<RfiLicenceQueryDto> licResult =requestForInformationService.licenceDoQuery(licParam);
                    if(licResult.getRowCount()!=0) {
                        for (RfiLicenceQueryDto lic:licResult.getRows()
                             ) {
                            String licStatus = MasterCodeUtil.retrieveOptionsByCodes(new String[]{lic.getLicenceStatus()}).get(0).getText();
                            reqForInfoSearchListDto.setLicenceStatus(licStatus);
                            reqForInfoSearchListDto.setLicenceNo(lic.getLicenceNo());
                            reqForInfoSearchListDto.setServiceName(lic.getServiceName());
                            reqForInfoSearchListDto.setStartDate(lic.getStartDate());
                            reqForInfoSearchListDto.setExpiryDate(lic.getExpiryDate());
                            reqForInfoSearchListDto.setLicPremId(lic.getLicPremId());
                            reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                        }
                    }
                    else {
                        reqForInfoSearchListDtos.add(reqForInfoSearchListDto);
                    }
                }
                searchListDtoSearchResult.setRows(reqForInfoSearchListDtos);
                ParamUtil.setSessionAttr(request,"SearchParam", appParam);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }



        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		doSearchApplication->OnStepProcess
    }

    public void doSearchLicence(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        String searchNo= (String) ParamUtil.getSessionAttr(request,"searchNo");

        String licence_no = ParamUtil.getString(bpc.request, "licence_no");
        if(StringUtil.isEmpty(licence_no)){
            licence_no = searchNo;
        }
        String service_licence_type = ParamUtil.getString(bpc.request, "service_licence_type");
        String licence_status = ParamUtil.getString(bpc.request, "licence_status");
        String sub_date = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "sub_date")),
                "yyyy-MM-dd");
        String to_date = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "to_date")),
                "yyyy-MM-dd");
        Map<String,Object> filters=new HashMap<>(10);

        if(!StringUtil.isEmpty(licence_no)){
            filters.put("licence_no", licence_no);
        }
        if(!StringUtil.isEmpty(licence_status)){
            filters.put("licence_status", licence_status);
        }
        if(!StringUtil.isEmpty(sub_date)){
            filters.put("start_date", sub_date);
        }
        if(!StringUtil.isEmpty(to_date)){
            filters.put("expiry_date", to_date);
        }
        licenceParameter.setFilters(filters);

        SearchParam licParam = SearchResultHelper.getSearchParam(request, true,licenceParameter);
        QueryHelp.setMainSql("ReqForInfoQuery","licenceQuery",licParam);
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
                    reqForInfoSearchListDto.setLicPremId(rfiLicenceQueryDto.getLicPremId());

                    if(!StringUtil.isEmpty(service_licence_type)){
                        boolean isAdd=false;
                        List<String> svcNames=requestForInformationService.getSvcNamesByType(service_licence_type);
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
                ParamUtil.setSessionAttr(request,"SearchParam", licParam);
                ParamUtil.setRequestAttr(request,"SearchResult", searchListDtoSearchResult);
            }
        }
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		doSearchLicence->OnStepProcess
    }

    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"licPremId",licPremId);
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void doSearchApplicationAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplicationAfter>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request,"licPremId",licPremId);
// 		doSearchApplicationAfter->OnStepProcess
    }

    public void preAppInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String appNo = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        // 		preAppInfo->OnStepProcess
    }

    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "licPremId");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoList=new ArrayList<>();
        licPremisesReqForInfoDtoList= requestForInformationService.searchLicPremisesReqForInfo(licPremId);

        List<SelectOption> selectOptions=MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.REQUEST_FOR_SUPPORTING_DOCUMENTS,RequestForInformationConstants.REQUEST_FOR_INFORMATION,RequestForInformationConstants.OTHERS});

        ParamUtil.setRequestAttr(request,"selectOptions", selectOptions);
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDtoList", licPremisesReqForInfoDtoList);


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
    public void doCreateRequest(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>doCreateRequest>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "licPremId");
        StringBuilder officerRemarks=new StringBuilder();
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();
        licPremisesReqForInfoDto.setReqType(RequestForInformationConstants.AD_HOC);
        licPremisesReqForInfoDto.setDueDateSubmission(Formatter.parseDate(ParamUtil.getString(request, "Due_date")));
        officerRemarks.append(ParamUtil.getString(request,"rfiTitle")+" ");
        licPremisesReqForInfoDto.setLicPremId(licPremId);
        String[] reqType=ParamUtil.getStrings(request,"reqType");
        boolean isNeedDoc;
        if("on".equals(reqType[0])){
            officerRemarks.append(" :Information");
        }
        if( "on".equals(reqType[1])){
            isNeedDoc=true;
            officerRemarks.append(" :Supporting documents");

        }
        else {
            isNeedDoc=false;
        }
        licPremisesReqForInfoDto.setNeedDocument(isNeedDoc);
        licPremisesReqForInfoDto.setOfficerRemarks(officerRemarks.toString());
        if("on".equals(reqType[0])||"on".equals(reqType[1])) {
            LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.createLicPremisesReqForInfo(licPremisesReqForInfoDto);
            //requestForInformationService.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto1);
        }
        // 		doCreateRequest->OnStepProcess
    }
    public void preNewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preNewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "licPremId");

        // 		preNewRfi->OnStepProcess
    }
    public void doCancel(BaseProcessClass bpc) {
        log.info("=======>>>>>doCancel>>>>>>>>>>>>>>>>requestForInformation");
        String id = (String) ParamUtil.getRequestAttr(bpc.request, "reqInfoId");
        requestForInformationService.acceptLicPremisesReqForInfo(id);
        // 		doCancel->OnStepProcess
    }
    public void doAccept(BaseProcessClass bpc) {
        log.info("=======>>>>>doAccept>>>>>>>>>>>>>>>>requestForInformation");
        String id = (String) ParamUtil.getRequestAttr(bpc.request, "reqInfoId");
        requestForInformationService.deleteLicPremisesReqForInfo(id);
        // 		doAccept->OnStepProcess
    }
    public void preViewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preViewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = (String) ParamUtil.getSessionAttr(bpc.request, "reqInfoId");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(id);
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
        // 		preViewRfi->OnStepProcess
    }
    public void preCancel(BaseProcessClass bpc) {
        log.info("=======>>>>>preCancel>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String licPremId = (String) ParamUtil.getSessionAttr(request, "licPremId");

        // 		preCancel->OnStepProcess
    }
}
