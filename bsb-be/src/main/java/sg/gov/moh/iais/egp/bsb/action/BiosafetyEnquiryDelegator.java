package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.util.Make;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sop.webflow.rt.api.BaseProcessClass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private static final String KEY_ENQUIRY_SEARCH_DTO = "enquiryDto";
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();
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
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ENQUIRY_SEARCH_DTO);
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
        if("1".equals(count)){
            ParamUtil.setRequestAttr(request,"download","Application-information-file");
        }else if("2".equals(count)) {
            ParamUtil.setRequestAttr(request, "download", "Facility-information-file");
        }
        // get search DTO
        EnquiryDto searchDto = getSearchDto(request);
        getResultAndAddFilter(request,searchDto,count);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, searchDto);
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
            case "3":
            case "1":
                List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
                selectOption(request,"facilityName",facNames);
                List<String> bioNames = biosafetyEnquiryClient.queryDistinctFA().getEntity();
                selectOption(request,"biologicalAgent",bioNames);
                break;
            case "4":
                break;
        }

    }

    public void getResultAndAddFilter(HttpServletRequest request,EnquiryDto enquiryDto,String count) throws ParseException {
        String applicationNo = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_STATUS);
        Date applicationSubmissionDateFrom = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_FROM));
        Date applicationSubmissionDateTo = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_TO));
        Date approvalDateFrom = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_FROM));
        Date approvalDateTo = Formatter.parseDate(ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_TO));
        String facilityClassification = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_CLASSIFICATION);
        String[] facilityType = ParamUtil.getStrings(request,BioSafetyEnquiryConstants.PARAM_FACILITY_TYPE);
        String facilityName = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_FACILITY_NAME);
        String biologicalAgent = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_BIOLOGICAL_AGENT);
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
        String[] natureOfTheSample = ParamUtil.getStrings(request,BioSafetyEnquiryConstants.PARAM_NATURE_OF_THE_SAMPLE);
        String approvalStatus = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_STATUS);
        String approvalType = ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_TYPE);
        Date approvalSubmissionDateFrom = Formatter.parseDate(ParamUtil.getString(request,ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_SUBMISSION_DATE_FROM)));
        Date approvalSubmissionDateTo = Formatter.parseDate(ParamUtil.getString(request,ParamUtil.getString(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_SUBMISSION_DATE_TO)));

        if (StringUtil.isNotEmpty(applicationNo)) {
            enquiryDto.setApplicationNo(applicationNo);
        }
        if (StringUtil.isNotEmpty(applicationType)) {
            enquiryDto.setApplicationType(applicationType);
        }
        if (StringUtil.isNotEmpty(applicationStatus)) {
            enquiryDto.setApplicationStatus(applicationStatus);
        }
        if (applicationSubmissionDateFrom != null ) {
            enquiryDto.setApplicationSubmissionDateFrom(applicationSubmissionDateFrom);
        }else if("1".equals(count)){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-1);
            enquiryDto.setApplicationSubmissionDateFrom(calendar.getTime());
        }
        if (applicationSubmissionDateTo != null ) {
            enquiryDto.setApplicationSubmissionDateTo(applicationSubmissionDateTo);
        }else if("1".equals(count)){
            enquiryDto.setApplicationSubmissionDateTo(new Date());
        }
        if (approvalDateFrom != null ) {
            enquiryDto.setApprovalDateFrom(approvalDateFrom);
        }else if ("2".equals(count) || "3".equals(count)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-1);
            enquiryDto.setApprovalDateFrom(calendar.getTime());
        }
        if (approvalDateTo != null ) {
            enquiryDto.setApprovalDateTo(approvalDateTo);
        }else if ("2".equals(count) || "3".equals(count)){
            enquiryDto.setApprovalDateTo(new Date());
        }
        if (StringUtil.isNotEmpty(facilityClassification)) {
            enquiryDto.setFacilityClassification(facilityClassification);
        }
        if (facilityType != null && facilityType.length>0 ) {
            List<String> facType = IaisCommonUtils.genNewArrayList();
            for (String s : facilityType) {
                facType.add(s);
            }
            enquiryDto.setFacilityType(facType);
        }
        if (StringUtil.isNotEmpty(facilityName)) {
            enquiryDto.setFacilityName(facilityName);
        }
        if (StringUtil.isNotEmpty(facilityName)) {
            enquiryDto.setFacilityName(biologicalAgent);
        }
        if (StringUtil.isNotEmpty(scheduleType)) {
            enquiryDto.setScheduleType(scheduleType);
        }
        if (StringUtil.isNotEmpty(riskLevelOfTheBiologicalAgent)) {
            enquiryDto.setRiskLevelOfTheBiologicalAgent(riskLevelOfTheBiologicalAgent);
        }
        if (StringUtil.isNotEmpty(processType)) {
            enquiryDto.setProcessType(processType);
        }
        if (facilityExpiryDateFrom != null ) {
            enquiryDto.setFacilityExpiryDateFrom(facilityExpiryDateFrom);
        }else if(StringUtil.stringEqual("2",count)){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-1);
            enquiryDto.setFacilityExpiryDateFrom(calendar.getTime());
        }
        if (facilityExpiryDateTo != null ) {
            enquiryDto.setFacilityExpiryDateTo(facilityExpiryDateTo);
        } else if (StringUtil.stringEqual("2", count)) {
            enquiryDto.setFacilityExpiryDateTo(new Date());
        }
        if (StringUtil.isNotEmpty(gazettedArea)) {
            enquiryDto.setGazettedArea(gazettedArea);
        }
        if (StringUtil.isNotEmpty(facilityOperator)) {
            enquiryDto.setFacilityOperator(facilityOperator);
        }
        if (StringUtil.isNotEmpty(facilityAdmin)) {
            enquiryDto.setFacilityName(facilityName);
        }
        if (StringUtil.isNotEmpty(authorisedPersonnelWorkingInFacility)) {
            enquiryDto.setAuthorisedPersonnelWorkingInFacility(authorisedPersonnelWorkingInFacility);
        }
        if (StringUtil.isNotEmpty(biosafetyCommitteePersonnel)) {
            enquiryDto.setBiosafetyCommitteePersonnel(biosafetyCommitteePersonnel);
        }
        if (StringUtil.isNotEmpty(facilityStatus)) {
            enquiryDto.setFacilityStatus(facilityStatus);
        }
        if (StringUtil.isNotEmpty(facilityAdmin)) {
            enquiryDto.setFacilityAdmin(facilityAdmin);
        }
        if(StringUtil.isNotEmpty(approvedFacilityCertifier)){
            enquiryDto.setApprovedFacilityCertifier(approvedFacilityCertifier);
        }
        if(StringUtil.isNotEmpty(approvalStatus)){
            enquiryDto.setApprovalStatus(approvalStatus);
        }
        if(natureOfTheSample != null && natureOfTheSample.length>0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < natureOfTheSample.length; i++) {
                if ((i + 1 == natureOfTheSample.length)) {
                    stringBuilder.append(natureOfTheSample[i]);
                } else {
                    stringBuilder.append(natureOfTheSample[i]).append(",");
                }
            }
            enquiryDto.setNatureOfTheSample(stringBuilder.toString());
        }
        if(StringUtil.isNotEmpty(approvalType)){
            enquiryDto.setApprovalType(approvalType);
        }
        if(approvalSubmissionDateFrom != null){
            enquiryDto.setApprovalSubmissionDateFrom(approvalSubmissionDateFrom);
        } else if ("3".equals(count)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-1);
            enquiryDto.setApprovalSubmissionDateFrom(calendar.getTime());
        }
        if(approvalSubmissionDateTo != null){
            enquiryDto.setApprovalSubmissionDateTo(approvalSubmissionDateTo);
        }else if("3".equals(count)){
            enquiryDto.setApprovalSubmissionDateTo(new Date());
        }
        if("1".equals(count)){
            ApplicationResultDto applicationResultDto = biosafetyEnquiryClient.getApp(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_RESULT, applicationResultDto.getBsbApp());
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_SEARCH, enquiryDto);
            log.info(StringUtil.changeForLog(applicationResultDto.getBsbApp().toString()+"===================application"));
        }
        if("2".equals(count)){
            FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFac(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_RESULT,facilityResultDto.getBsbFac());
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_SEARCH,enquiryDto);
            log.info(StringUtil.changeForLog(facilityResultDto.getBsbFac().toString()+"==========facility"));
        }
        if("3".equals(count)){
            ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_RESULT,approvalResultDto.getBsbApproval());
            ParamUtil.setRequestAttr(request,BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_SEARCH,enquiryDto);
            log.info(StringUtil.changeForLog(approvalResultDto.getBsbApproval().toString()+"==========facility"));
        }


    }

    @GetMapping(value = "Application-information-file")
    public @ResponseBody
    void appFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        log.debug("indicates that a record has been selected ");
        EnquiryDto enquiryDto = getSearchDto(request);
        ApplicationResultDto results = biosafetyEnquiryClient.getApp(enquiryDto).getEntity();
        if (!Objects.isNull(results)){
            List<ApplicationInfoDto> queryList = results.getBsbApp();
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
        EnquiryDto enquiryDto = getSearchDto(request);
        FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFac(enquiryDto).getEntity();
        log.debug("indicates that a record has been selected ");
        if (!Objects.isNull(facilityResultDto)){
            List<FacilityInfoDto> queryList = facilityResultDto.getBsbFac();
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
    private EnquiryDto getSearchDto(HttpServletRequest request) {
        EnquiryDto searchDto = (EnquiryDto) ParamUtil.getSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private EnquiryDto getDefaultSearchDto() {
        EnquiryDto dto = new EnquiryDto();
        return dto;
    }
}
