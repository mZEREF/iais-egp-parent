package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.bsb.dto.enquiry.ApplicationInfoDto;
import sg.gov.moh.iais.bsb.dto.enquiry.FacilityInfoDto;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sop.webflow.rt.api.BaseProcessClass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * AUTHOR: YiMing
 * DATE:2021/7/14 14:15
 * DESCRIPTION: TODO
 **/
@Delegator(value = "biosafetyEnquiryDelegator")
@Slf4j
public class BiosafetyEnquiryDelegator {
    private final String SEARCH_CATALOG = "bsbBe";
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();
    FilterParameter appInfoParameter = new FilterParameter.Builder()
            .clz(ApplicationInfoDto.class)
            .searchAttr(BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_SEARCH)
            .resultAttr(BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_RESULT)
            .sortField("APPLICATION_DT").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter facInfoParameter = new FilterParameter.Builder()
            .clz(FacilityInfoDto.class)
            .searchAttr(BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_SEARCH)
            .resultAttr(BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_RESULT)
            .sortField("EXPIRYED_DT").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

   @Autowired
   private BiosafetyEnquiryClient biosafetyEnquiryClient;


    /**
     *  AutoStep: prepareBasicSearch
     * @param bpc
     */
    public void prepareBasicSearch(BaseProcessClass bpc){
        String count = ParamUtil.getString(bpc.request,"searchChk");
        if(StringUtil.isEmpty(count) || count == null){
            count = "0";
        }
        String searchNo = ParamUtil.getString(bpc.request, "searchNo");
        if(StringUtil.isEmpty(searchNo)){ searchNo = "null";}
            switch (count){
                case "1":
                    List<ApplicationInfoDto> applicationInfoDto= biosafetyEnquiryClient.queryApplicationByAppNo(searchNo).getEntity();
                    log.info(StringUtil.changeForLog("delegator applicationInfoDto----"+applicationInfoDto.toString()));
                    ParamUtil.setRequestAttr(bpc.request,"applicationInfoDto",applicationInfoDto);
                    break;
                case "2":
                    List<FacilityInfoDto> facilityInfoDto = biosafetyEnquiryClient.queryFacilityByFacName(searchNo).getEntity();
                    ParamUtil.setRequestAttr(bpc.request,"facilityInfoDto",facilityInfoDto);
                    break;
        }
        ParamUtil.setRequestAttr(bpc.request, "count",count);

    }


    /**
     * AutoStep: changePage
     * @param bpc
     */
    public void doPaging(BaseProcessClass bpc){

    }

    /**
     * AutoStep: prepareAdvSearch
     * @param bpc
     */
    public void prepareAdvSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String count=ParamUtil.getString(request,"searchChk");
        if(StringUtil.isEmpty(count)){
            count ="0";
        }
        ParamUtil.setRequestAttr(request, "count",count);
        preSelectOption(request,count);
    }

    /**
     * AutoStep: preAfterAdvSearch
     * @param bpc
     */
    public void preAfterAdvSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, "searchChk");
        ParamUtil.setRequestAttr(request, "count", count);
        preSelectOption(request, count);
        FilterParameter filterParameter = null;
        if("1".equals(count)){
            filterParameter = appInfoParameter;
            ParamUtil.setRequestAttr(request,"download","Application-information-file");
        }else if("2".equals(count)){
            filterParameter = facInfoParameter;
            ParamUtil.setRequestAttr(request,"download","Facility-information-file");
        }else{
            log.info(StringUtil.changeForLog("null point for filterParameter"));
        }
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        getResultAndAddFilter(request,searchParam,count,filterParameter);
    }


    /**
     * AutoStep: prepareDetail
     * @param bpc
     */
    public void  prepareDetail(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String count=ParamUtil.getString(request,"searchChk");
        ParamUtil.setRequestAttr(request, "count",count);
    }


    public void selectOption(HttpServletRequest request,String name,List<String> strings){
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        log.info(StringUtil.changeForLog("strings value"+strings.toString()));
        for (String string : strings) {
            selectModel.add(new SelectOption(string,string));
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request,String num){
        switch(num){
            case "2":
                List<String> approvals = biosafetyEnquiryClient.queryDistinctApproval().getEntity();
                selectOption(request,"AFC",approvals);
            case "1":
                List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
                selectOption(request,"facilityName",facNames);
                List<String> bioNames = biosafetyEnquiryClient.queryDistinctFA().getEntity();
                selectOption(request,"biologicalAgent",bioNames);
                break;
        }

    }

    public void getResultAndAddFilter(HttpServletRequest request,SearchParam searchParam,String count,FilterParameter filterParameter) throws ParseException {
        String applicationNo = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_STATUS);
        Date applicationSubmissionDateFrom = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_FROM));
        Date applicationSubmissionDateTo = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_TO));
        Date approvalDateFrom = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_FROM));
        Date approvalDateTo = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_TO));
        String facilityClassification = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_CLASSIFICATION);
        String[] facilityType = ParamUtil.getStrings(request,BioSafetyEnquiryConstants.PARAM_FACILITY_TYPE);
//        log.info(StringUtil.changeForLog(facilityType.length+"-------------------facilityType"));
        String facilityName = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_NAME);
        String scheduleType = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_SCHEDULE_TYPE);
        String riskLevelOfTheBiologicalAgent = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT);
        String processType = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_PROCESS_TYPE);
        Date facilityExpiryDateFrom = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_FROM));
        Date facilityExpiryDateTo = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_TO));
        String gazettedArea = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_GAZETTED_AREA);
        String facilityOperator = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_OPERATOR);
        String facilityAdmin = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_ADMIN);
        String authorisedPersonnelWorkingInFacility = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_AUTHORISED_PERSONNEL_WORKING_IN_FACILITY);
        String biosafetyCommitteePersonnel = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_BIOSAFETY_COMMITTEE_PERSONNEL);
        String facilityStatus = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_STATUS);
        String approvedFacilityCertifier = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVED_FACILITY_CERTIFIER);
        if (StringUtil.isNotEmpty(applicationNo)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_NO, applicationNo, true);
        }
        if (StringUtil.isNotEmpty(applicationType)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_TYPE, applicationType, true);
        }
        if (StringUtil.isNotEmpty(applicationStatus)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_STATUS, applicationStatus, true);
        }
        if (applicationSubmissionDateFrom != null ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_FROM, applicationSubmissionDateFrom, true);
        }else if("1".equals(count)){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-1);
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_FROM, calendar, true);
        }
        if (applicationSubmissionDateTo != null ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_TO, applicationSubmissionDateTo, true);
        }else if("1".equals(count)){
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_TO, new Date(), true);
        }
        if (approvalDateFrom != null ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_FROM, approvalDateFrom, true);
        }
        if (approvalDateTo != null ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_TO, approvalDateTo, true);
        }
        if (StringUtil.isNotEmpty(facilityClassification)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_CLASSIFICATION, facilityClassification, true);
        }
        if (facilityType != null && facilityType.length>0 ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_TYPE, facilityType, true);
        }
        if (StringUtil.isNotEmpty(facilityName)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_NAME, facilityName, true);
        }
        if (StringUtil.isNotEmpty(scheduleType)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_SCHEDULE_TYPE, scheduleType, true);
        }
        if (StringUtil.isNotEmpty(riskLevelOfTheBiologicalAgent)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT, riskLevelOfTheBiologicalAgent, true);
        }
        if (StringUtil.isNotEmpty(processType)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_PROCESS_TYPE, processType, true);
        }
        if (facilityExpiryDateFrom != null ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_FROM, facilityExpiryDateFrom, true);
        }else if(StringUtil.stringEqual("2",count)){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-1);
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_FROM, calendar, true);
        }
        if (facilityExpiryDateTo != null ) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_TO, facilityExpiryDateTo, true);
        } else if (StringUtil.stringEqual("2", count)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_TO,new Date(), true);
        }
        if (StringUtil.isNotEmpty(gazettedArea)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_GAZETTED_AREA, gazettedArea, true);
        }
        if (StringUtil.isNotEmpty(facilityOperator)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_OPERATOR, facilityOperator, true);
        }
        if (StringUtil.isNotEmpty(facilityAdmin)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_ADMIN, facilityAdmin, true);
        }
        if (StringUtil.isNotEmpty(authorisedPersonnelWorkingInFacility)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_AUTHORISED_PERSONNEL_WORKING_IN_FACILITY, authorisedPersonnelWorkingInFacility, true);
        }
        if (StringUtil.isNotEmpty(biosafetyCommitteePersonnel)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_BIOSAFETY_COMMITTEE_PERSONNEL, biosafetyCommitteePersonnel, true);
        }
        if (StringUtil.isNotEmpty(facilityStatus)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_FACILITY_STATUS, facilityStatus, true);
        }
        if (StringUtil.isNotEmpty(facilityAdmin)) {
            searchParam.addFilter(BioSafetyEnquiryConstants.PARAM_APPROVED_FACILITY_CERTIFIER, approvedFacilityCertifier, true);
        }
        if("1".equals(count)){
            QueryHelp.setMainSql(SEARCH_CATALOG, "queryApplicationInfo", searchParam);
            SearchResult<ApplicationInfoDto> searchResult = biosafetyEnquiryClient.queryAppInfo(searchParam).getEntity();
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_RESULT, searchResult);
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_SEARCH, searchParam);
            log.info(StringUtil.changeForLog(searchResult.getRows().toString()+"===================application"));
        }
        if("2".equals(count)){
            QueryHelp.setMainSql(SEARCH_CATALOG, "queryFacilityInfo", searchParam);
            SearchResult<FacilityInfoDto> searchResult = biosafetyEnquiryClient.queryFacilityInfo(searchParam).getEntity();
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_RESULT, searchResult);
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_SEARCH, searchResult);
            log.info(StringUtil.changeForLog(searchResult.getRows().toString()+"==================="));
        }

    }

    @GetMapping(value = "Application-information-file")
    public @ResponseBody
    void appFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, appInfoParameter);
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql(SEARCH_CATALOG, "queryApplicationInfo", searchParam);
        SearchResult<ApplicationInfoDto> results = biosafetyEnquiryClient.queryAppInfo(searchParam).getEntity();
        if (!Objects.isNull(results)){
            List<ApplicationInfoDto> queryList = results.getRows();
            queryList.forEach(i -> i.setApplicationStatus(MasterCodeUtil.getCodeDesc(i.getApplicationStatus())));
            queryList.forEach(i -> i.setApplicationType(MasterCodeUtil.getCodeDesc(i.getApplicationType())));
            queryList.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
            queryList.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
            queryList.forEach(i -> i.setRiskLevelOfTheBiologicalAgent(MasterCodeUtil.getCodeDesc(i.getRiskLevelOfTheBiologicalAgent())));
            queryList.forEach(i -> i.setProcessType(MasterCodeUtil.getCodeDesc(i.getProcessType())));
            try {
                file = ExcelWriter.writerToExcel(queryList, ApplicationInfoDto.class, "Application Information_Search_Template");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }
        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "Facility-information-file")
    public @ResponseBody
    void facFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, facInfoParameter);
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql(SEARCH_CATALOG, "queryFacilityInfo", searchParam);
        SearchResult<FacilityInfoDto> results = biosafetyEnquiryClient.queryFacilityInfo(searchParam).getEntity();
        if (!Objects.isNull(results)){
            List<FacilityInfoDto> queryList = results.getRows();
            queryList.forEach(i -> i.setGazettedArea(MasterCodeUtil.getCodeDesc(i.getGazettedArea())));
            queryList.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
            queryList.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
            queryList.forEach(i -> i.setRiskLevelOfTheBiologicalAgent(MasterCodeUtil.getCodeDesc(i.getRiskLevelOfTheBiologicalAgent())));
            try {
                file = ExcelWriter.writerToExcel(queryList, FacilityInfoDto.class, "Facility Information_Search_Template");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
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
