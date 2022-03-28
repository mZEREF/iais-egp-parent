package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.dto.entity.BiologicalDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;


/**
 * @author YiMing
 * @version 2021/10/15 14:16
 **/
@Delegator(value = "biosafetyEnquiryDelegator")
@Slf4j
public class BiosafetyEnquiryDelegator {
    private static final String PARAM_SEARCH_CHK = "searchChk";
    private static final String PARAM_COUNT = "count";
    private static final String FUNCTION_BIOSATETY_ENQUIRY = "Biosafety Enquiry";
    private static final String CHOOSE_MARK_APPLICATION = "app";
    private static final String CHOOSE_MARK_FACILITY = "fn";
    private static final String CHOOSE_MARK_APPROVAL = "an";
    private static final String CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY = "on";
    private static final String PARAM_RISK_LEVEL = "riskLevel";
    private static final String PARAM_BIOLOGICAL_NAME = "bioName";
    private static final String PARAM_BIO_SAFETY_ENQUIRY = "bioSafetyDto";

    private final BiosafetyEnquiryClient biosafetyEnquiryClient;

    public BiosafetyEnquiryDelegator(BiosafetyEnquiryClient biosafetyEnquiryClient) {
        this.biosafetyEnquiryClient = biosafetyEnquiryClient;
    }

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY, FUNCTION_BIOSATETY_ENQUIRY);
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_ENQUIRY_SEARCH_DTO,null);
    }


    public void prepareBasicSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        String searchNo = ParamUtil.getString(request, "searchNo");
        EnquiryDto enquiryDto = getSearchDto(request);
        enquiryDto = choose(count,searchNo,enquiryDto);
        if(enquiryDto != null){
            getResultAndAddFilter(request, enquiryDto, count);
        }
        preSelectOption(request,null);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
    }


    public EnquiryDto choose(String count,String searchNo,EnquiryDto enquiryDto){
        if(StringUtils.hasLength(count)){
            switch (count) {
                case CHOOSE_MARK_APPLICATION:
                    enquiryDto.setApplicationNo(searchNo);
                    break;
                case CHOOSE_MARK_FACILITY:
                    enquiryDto.setFacilityName(searchNo);
                    break;
                case CHOOSE_MARK_APPROVAL:
                    enquiryDto.setApprovalNo(searchNo);
                    break;
                case CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY:
                    enquiryDto.setOrganisationName(searchNo);
                    break;
                default:
                    log.info("no such option");
                    break;
            }
            return enquiryDto;
        }else{
            return null;
        }

       }



    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto enquiryDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                enquiryDto.setPage(0);
                enquiryDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                enquiryDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, enquiryDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto enquiryDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        enquiryDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, enquiryDto);
    }


    public void prepareAdvSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        if (StringUtils.isEmpty(count)) {
            count = "0";
        }
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
        preSelectOption(request, count);
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ENQUIRY_SEARCH_DTO);
    }


    public void preAfterAdvSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
        preSelectOption(request, count);
        ParamUtil.setRequestAttr(request,KEY_DOWNLOAD,URL_BIO_SAFETY_INFO_FILE);
        // get search DTO
        EnquiryDto searchDto = getSearchDto(request);
        getResultAndAddFilter(request, searchDto, count);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, searchDto);
    }


    public void prepareDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
    }


    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = new ArrayList<>(strings.size());
        for (String string : strings) {
            selectModel.add(new SelectOption(string, string));
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request, String num) {
        //add action
        List<String> action = new ArrayList<>(3);
        action.add("Revoke");
        action.add("Suspend");
        action.add("Reinstate");
        selectOption(request, "action", action);
        if ("app".equals(num) || "fn".equals(num) || "an".equals(num)) {
            List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
            selectOption(request, "facilityName", facNames);
            List<String> bioNames = biosafetyEnquiryClient.queryDistinctFA().getEntity();
            selectOption(request, "biologicalAgent", bioNames);
        }
        if("on".equals(num)){
           List<String> orgNames =  biosafetyEnquiryClient.queryDistinctOrgName().getEntity();
           selectOption(request,"orgName",orgNames);
        }
    }

    private void addFilter(HttpServletRequest request, EnquiryDto enquiryDto, String count) throws ParseException {
        enquiryDto.clearAllFields();
        commonFilter(request, enquiryDto);
        if ("app".equals(count)) {
            addAppFilter(request, enquiryDto);
        }

        if ("fn".equals(count)) {
            addFacFilter(request, enquiryDto);
        }

        if ("an".equals(count)) {
            addApprovalFilter(request, enquiryDto);
        }

        if ("on".equals(count)) {
            addOrgFilter(request, enquiryDto);
        }
    }

    public void commonFilter(HttpServletRequest request, EnquiryDto enquiryDto) throws ParseException {
        String facilityClassification = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_CLASSIFICATION);
        String[] facilityType = ParamUtil.getStrings(request, BioSafetyEnquiryConstants.PARAM_FACILITY_TYPE);
        String facilityName = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_NAME);
        String biologicalAgent = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_BIOLOGICAL_AGENT);
        String scheduleType = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_SCHEDULE_TYPE);
        String riskLevelOfTheBiologicalAgent = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT);
        Date approvedDateFrom = Formatter.parseDate(ParamUtil.getString(request, ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVED_DATE_FROM)));
        Date approvedDateTo = Formatter.parseDate(ParamUtil.getString(request, ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVED_DATE_TO)));
        if (StringUtil.isNotEmpty(facilityClassification)) {
            enquiryDto.setFacilityClassification(facilityClassification);
        }
        if (facilityType != null && facilityType.length > 0) {
            enquiryDto.setFacilityType(Arrays.asList(facilityType));
        }
        if (StringUtil.isNotEmpty(facilityName)) {
            enquiryDto.setFacilityName(facilityName);
        }
        if (StringUtil.isNotEmpty(biologicalAgent)) {
            enquiryDto.setBiologicalAgent(biologicalAgent);
        }
        if (StringUtil.isNotEmpty(scheduleType)) {
            enquiryDto.setScheduleType(scheduleType);
        }
        if (StringUtil.isNotEmpty(riskLevelOfTheBiologicalAgent)) {
            enquiryDto.setRiskLevelOfTheBiologicalAgent(riskLevelOfTheBiologicalAgent);
        }
        if (approvedDateFrom != null) {
            enquiryDto.setApprovedDateFrom(approvedDateFrom);
        }
        if (approvedDateTo != null) {
            enquiryDto.setApprovedDateTo(approvedDateTo);
        }
    }

    private void addAppFilter(HttpServletRequest request, EnquiryDto enquiryDto) throws ParseException {
        String applicationNo = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_STATUS);
        Date applicationSubmissionDateFrom = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_FROM));
        Date applicationSubmissionDateTo = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_TO));
        Date approvalDateFrom = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_FROM));
        Date approvalDateTo = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_DATE_TO));
        String processType = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_PROCESS_TYPE);
        if (StringUtil.isNotEmpty(applicationNo)) {
            enquiryDto.setApplicationNo(applicationNo);
        }
        if (StringUtil.isNotEmpty(applicationType)) {
            enquiryDto.setApplicationType(applicationType);
        }
        if (StringUtil.isNotEmpty(applicationStatus)) {
            enquiryDto.setApplicationStatus(applicationStatus);
        }
        if (applicationSubmissionDateFrom != null) {
            enquiryDto.setApplicationSubmissionDateFrom(applicationSubmissionDateFrom);
        }
        if (applicationSubmissionDateTo != null) {
            enquiryDto.setApplicationSubmissionDateTo(applicationSubmissionDateTo);
        }
        if (approvalDateFrom != null) {
            enquiryDto.setApprovalDateFrom(approvalDateFrom);
        }
        if (approvalDateTo != null) {
            enquiryDto.setApprovalDateTo(approvalDateTo);
        }
        if (StringUtil.isNotEmpty(processType)) {
            enquiryDto.setProcessType(processType);
        }
    }

    private void addFacFilter(HttpServletRequest request, EnquiryDto enquiryDto) throws ParseException {
        Date facilityExpiryDateFrom = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_FROM));
        Date facilityExpiryDateTo = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_EXPIRY_DATE_TO));
        String gazettedArea = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_GAZETTED_AREA);
        String facilityOperator = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_OPERATOR);
        String facilityAdmin = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_ADMIN);
        String authorisedPersonnelWorkingInFacility = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_AUTHORISED_PERSONNEL_WORKING_IN_FACILITY);
        String biosafetyCommitteePersonnel = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_BIOSAFETY_COMMITTEE_PERSONNEL);
        String facilityStatus = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_STATUS);
        String approvedFacilityCertifier = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVED_FACILITY_CERTIFIER);
        if (facilityExpiryDateFrom != null) {
            enquiryDto.setFacilityExpiryDateFrom(facilityExpiryDateFrom);
        }
        if (facilityExpiryDateTo != null) {
            enquiryDto.setFacilityExpiryDateTo(facilityExpiryDateTo);
        }
        if (StringUtil.isNotEmpty(gazettedArea)) {
            enquiryDto.setGazettedArea(gazettedArea);
        }
        if (StringUtil.isNotEmpty(facilityOperator)) {
            enquiryDto.setFacilityOperator(facilityOperator);
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
        if (StringUtil.isNotEmpty(approvedFacilityCertifier)) {
            enquiryDto.setApprovedFacilityCertifier(approvedFacilityCertifier);
        }
    }

    private void addApprovalFilter(HttpServletRequest request, EnquiryDto enquiryDto) throws ParseException {
        String[] natureOfTheSample = ParamUtil.getStrings(request, BioSafetyEnquiryConstants.PARAM_NATURE_OF_THE_SAMPLE);
        String approvalStatus = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_STATUS);
        String approvalType = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_TYPE);
        Date approvalSubmissionDateFrom = Formatter.parseDate(ParamUtil.getString(request, ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_SUBMISSION_DATE_FROM)));
        Date approvalSubmissionDateTo = Formatter.parseDate(ParamUtil.getString(request, ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_SUBMISSION_DATE_TO)));
        if (StringUtil.isNotEmpty(approvalStatus)) {
            enquiryDto.setApprovalStatus(approvalStatus);
        }
        if (natureOfTheSample != null && natureOfTheSample.length > 0) {
            enquiryDto.setNatureOfTheSamples(Arrays.asList(natureOfTheSample));
        }
        if (StringUtil.isNotEmpty(approvalType)) {
            enquiryDto.setApprovalType(approvalType);
        }
        if (approvalSubmissionDateFrom != null) {
            enquiryDto.setApprovalSubmissionDateFrom(approvalSubmissionDateFrom);
        }
        if (approvalSubmissionDateTo != null) {
            enquiryDto.setApprovalSubmissionDateTo(approvalSubmissionDateTo);
        }
    }

    private void addOrgFilter(HttpServletRequest request, EnquiryDto enquiryDto) {
        String organisationName = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_ORGANISATION_NAME);
        String facilityAdministrator = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_ADMINISTRATOR);
        String afcStatus = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_AFC_STATUS);
        String teamMemberName = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_TEAM_MEMBER_NAME);
        String teamMemberID = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_TEAM_MEMBER_ID);
        if (StringUtil.isNotEmpty(organisationName)) {
            enquiryDto.setOrganisationName(organisationName);
        }
        if (StringUtil.isNotEmpty(facilityAdministrator)) {
            enquiryDto.setFacilityAdministrator(facilityAdministrator);
        }
        if (StringUtil.isNotEmpty(afcStatus)) {
            enquiryDto.setAfcStatus(afcStatus);
        }
        if (StringUtil.isNotEmpty(teamMemberName)) {
            enquiryDto.setTeamMemberName(teamMemberName);
        }
        if (StringUtil.isNotEmpty(teamMemberID)) {
            enquiryDto.setTeamMemberID(teamMemberID);
        }
    }

    private void getResultAndAddFilter(HttpServletRequest request, EnquiryDto enquiryDto, String count) throws ParseException {
        addFilter(request, enquiryDto, count);
        ParamUtil.setSessionAttr(request,PARAM_COUNT,count);
        if (CHOOSE_MARK_APPLICATION.equals(count) && Boolean.TRUE.equals(validationParam(request, "app", enquiryDto))) {
            ApplicationResultDto applicationResultDto = biosafetyEnquiryClient.getApplication(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_RESULT, applicationResultDto.getBsbApp());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_SEARCH, enquiryDto);
            ParamUtil.setSessionAttr(request,PARAM_BIO_SAFETY_ENQUIRY,new ArrayList<>(applicationResultDto.getBsbApp()));
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, applicationResultDto.getPageInfo());
        } else if (CHOOSE_MARK_FACILITY.equals(count) && Boolean.TRUE.equals(validationParam(request, "fac", enquiryDto))) {
            FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFacility(enquiryDto).getEntity();
            List<FacilityActivity> activities = facilityResultDto.getBsbFac();
            for (FacilityActivity activity : activities) {
                Map<String,String> infos = joinBioNameAndRiskLevelFromAgents(activity.getBiologicalAgents());
//                activity.setBioName(infos.get(PARAM_BIOLOGICAL_NAME));
//                activity.setRiskLevel(infos.get(PARAM_RISK_LEVEL));
//                activity.setAdmin(joinFacilityActivityAdmin(activity.getFacility()));
            }
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_RESULT, facilityResultDto.getBsbFac());
            ParamUtil.setSessionAttr(request,PARAM_BIO_SAFETY_ENQUIRY,new ArrayList<>(facilityResultDto.getBsbFac()));
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, facilityResultDto.getPageInfo());
        } else if (CHOOSE_MARK_APPROVAL.equals(count) && Boolean.TRUE.equals(validationParam(request, "approval", enquiryDto))) {
            ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_RESULT, approvalResultDto.getBsbApproval());
            ParamUtil.setSessionAttr(request,PARAM_BIO_SAFETY_ENQUIRY,new ArrayList<>(approvalResultDto.getBsbApproval()));
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, approvalResultDto.getPageInfo());
        } else if (CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY.equals(count) && Boolean.TRUE.equals(validationParam(request, "org", enquiryDto))) {
            ApprovedFacilityCerResultDto facilityCerResultDto = biosafetyEnquiryClient.getApprovedFacilityCertifier(enquiryDto).getEntity();
            List<FacilityCertifierReg> certifierRegs = facilityCerResultDto.getBsbAFC();
            for (FacilityCertifierReg certifierReg : certifierRegs) {
                certifierReg.setAdminName(joinFacilityCertifierAdmin(certifierReg.getCertifierAdmins()));
                certifierReg.setAddress(TableDisplayUtil.getOneLineAddress("12",certifierReg.getStreetName(),certifierReg.getFloorNo(),certifierReg.getUnitNo(),certifierReg.getPostalCode()));
            }
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVED_CERTIFIER_INFO_RESULT, facilityCerResultDto.getBsbAFC());
            ParamUtil.setSessionAttr(request,PARAM_BIO_SAFETY_ENQUIRY,new ArrayList<>(facilityCerResultDto.getBsbAFC()));
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVED_CERTIFIER_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, facilityCerResultDto.getPageInfo());
        }

    }

    private Boolean validationParam(HttpServletRequest request, String type, EnquiryDto object) {
        ValidationResult vResult = WebValidationHelper.validateProperty(object, type);
        if (vResult != null && vResult.isHasErrors()) {
            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, WebValidationHelper.generateJsonStr(errorMap));
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }



    @GetMapping(value = "/bioSafety-information-file")
    public @ResponseBody
    void appFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("application fileHandler start ...."));
        File file = null;
        log.debug("indicates that a app record has been selected ");
        String count = (String) ParamUtil.getSessionAttr(request,PARAM_COUNT);
        if(StringUtils.hasLength(count)){
            switch (count) {
                case CHOOSE_MARK_APPLICATION: {
                    List<ApplicationInfoDto> bioSafetyDto = (List<ApplicationInfoDto>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
                    try {
                        file = ExcelWriter.writerToExcel(filedApplicationInfo(bioSafetyDto), ApplicationInfoDto.class, "Application Information_Search_Template");
                    } catch (Exception e) {
                        log.error("=======>app fileHandler  error >>>>>", e);
                    }
                    break;
                }
                case CHOOSE_MARK_APPROVAL: {
                    List<ApprovalInfoDto> bioSafetyDto = (List<ApprovalInfoDto>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
                    try {
                        file = ExcelWriter.writerToExcel(filedApprovalInfo(bioSafetyDto), ApprovalInfoDto.class, "Approval Information_Search_Template");
                    } catch (Exception e) {
                        log.error("=======>approval fileHandler  error >>>>>", e);
                    }
                    break;
                }
                case CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY: {
                    List<FacilityCertifierReg> bioSafetyDto = (List<FacilityCertifierReg>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
                    try {
                        file = ExcelWriter.writerToExcel(filedFacilityCertifierInfo(bioSafetyDto), ApprovedFacilityCertifierInfoDto.class, "Approved Facility Certifier Information_Search_Template");
                    } catch (Exception e) {
                        log.error("=======>facility certifier fileHandler  error >>>>>", e);
                    }
                    break;
                }
                case CHOOSE_MARK_FACILITY: {
                    List<FacilityActivity> bioSafetyDto = (List<FacilityActivity>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
                    try {
                        file = ExcelWriter.writerToExcel(filedFacilityInfo(bioSafetyDto), FacilityInfoDto.class, "Facility Information_Search_Template");
                    } catch (Exception e) {
                        log.error("=======>facility fileHandler  error >>>>>", e);
                    }
                    break;
                }
                default:
                    if(log.isInfoEnabled()){
                        log.info("error count{}", LogUtil.escapeCrlf(count));
                    }
                    break;
            }
        }
        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("app fileHandler end ...."));
    }

    public List<ApplicationInfoDto> filedApplicationInfo(List<ApplicationInfoDto> bsbApp){
        if(!CollectionUtils.isEmpty(bsbApp)){
            log.info("empty list applicationInfo");
        }
        for (ApplicationInfoDto info : bsbApp) {
            info.setAppStatus(MasterCodeUtil.getCodeDesc(info.getAppStatus()));
            info.setAppType(MasterCodeUtil.getCodeDesc(info.getAppType()));
            info.setFacilityClassification(MasterCodeUtil.getCodeDesc(info.getFacilityClassification()));
            info.setFacilityType(MasterCodeUtil.getCodeDesc(info.getFacilityType()));
            info.setProcessType(MasterCodeUtil.getCodeDesc(info.getProcessType()));
        }
        return bsbApp;
    }

    public List<ApprovalInfoDto> filedApprovalInfo(List<ApprovalInfoDto> bsbAppr){
        if(CollectionUtils.isEmpty(bsbAppr)){
            log.info("empty list approvalInfo");
            return Collections.emptyList();
        }
        for (ApprovalInfoDto dto : bsbAppr) {
            dto.setType(MasterCodeUtil.getCodeDesc(dto.getType()));
            dto.setFacStatus(MasterCodeUtil.getCodeDesc(dto.getFacStatus()));
            dto.setStatus(MasterCodeUtil.getCodeDesc(dto.getStatus()));
        }

        return bsbAppr;
    }

    public List<ApprovedFacilityCertifierInfoDto> filedFacilityCertifierInfo(List<FacilityCertifierReg> regs){
        if(CollectionUtils.isEmpty(regs)){
            log.info("empty list facilityCertifier");
            return Collections.emptyList();
        }
        List<ApprovedFacilityCertifierInfoDto> infos = new ArrayList<>(regs.size());
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
        for (FacilityCertifierReg reg : regs) {
            ApprovedFacilityCertifierInfoDto info = new ApprovedFacilityCertifierInfoDto();
            Approval approval = reg.getApproval();
            info.setAdministrator(reg.getAdminName());
            info.setApprovedDate(sdf.format(approval.getApprovalDate()));
            info.setExpiryDate(sdf.format(approval.getApprovalExpiryDate()));
            info.setOrganisationAddress(reg.getAddress());
            info.setOrganisationName(reg.getOrgName());
            info.setAfcStatus(approval.getStatus());
            infos.add(info);
        }
        return infos;
    }

    public List<FacilityInfoDto> filedFacilityInfo(List<FacilityActivity> fac){
        if(CollectionUtils.isEmpty(fac)){
            log.info("empty list facilityInfo");
            return Collections.emptyList();
        }
        List<FacilityInfoDto> infos = new ArrayList<>(fac.size());
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
        for (FacilityActivity activity : fac) {
            FacilityInfoDto info = new FacilityInfoDto();
            Facility facility = activity.getFacility();
            Approval approval = activity.getApproval();
            info.setFacilityAddress(TableDisplayUtil.getOneLineAddress(facility.getBlkNo(),facility.getStreetName(),
                    facility.getFloorNo(),facility.getUnitNo(),facility.getPostalCode()));
//            info.setFacilityAdmin(activity.getAdmin());
            info.setFacilityType(activity.getActivityType());
            info.setFacilityName(facility.getFacilityName());
            info.setFacilityExpiryDate(sdf.format(approval.getApprovalExpiryDate()));
            info.setFacilityClassification(facility.getFacilityClassification());
            info.setFacilityOperator(facility.getOperator().getFacOperator());
//            info.setBiologicalAgent(activity.getBioName());
            info.setGazettedArea(facility.getIsProtected());
//            info.setRiskLevelOfTheBiologicalAgent(activity.getRiskLevel());
            infos.add(info);
        }
        return infos;
    }



    private EnquiryDto getSearchDto(HttpServletRequest request) {
        EnquiryDto searchDto = (EnquiryDto) ParamUtil.getSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private EnquiryDto getDefaultSearchDto() {
        return new EnquiryDto();
    }


    public Map<String,String> joinBioNameAndRiskLevelFromAgents(List<FacilityBiologicalAgent> agents){
        List<String> newBioNameList = new ArrayList<>(agents.size());
        List<String> newRiskLevelList = new ArrayList<>(agents.size());
        for (FacilityBiologicalAgent agent : agents) {
            Map<String,String> newMap = getRiskLevelAndBioName(agent);
            newBioNameList.add(newMap.get(PARAM_BIOLOGICAL_NAME));
            newRiskLevelList.add(newMap.get(PARAM_RISK_LEVEL));
        }
        Map<String,String> bioNameAndRiskMap = Maps.newHashMapWithExpectedSize(2);
        bioNameAndRiskMap.put(PARAM_RISK_LEVEL,TableDisplayUtil.getOneLineRiskLevel(newRiskLevelList));
        bioNameAndRiskMap.put(PARAM_BIOLOGICAL_NAME,TableDisplayUtil.getOneLineBiologicalName(newBioNameList));
        return bioNameAndRiskMap;
    }

    private Map<String,String> getRiskLevelAndBioName(FacilityBiologicalAgent agent){
        Map<String,String> riskLevelAndBioNamesMap = Maps.newHashMapWithExpectedSize(2);
        if(agent != null){
            BiologicalDto biological = biosafetyEnquiryClient.getBiologicalById(agent.getBiologicalId());
            riskLevelAndBioNamesMap.put(PARAM_BIOLOGICAL_NAME,biological.getName());
            riskLevelAndBioNamesMap.put(PARAM_RISK_LEVEL,biological.getRiskLevel());
        }
        return riskLevelAndBioNamesMap;
    }

    public String joinFacilityActivityAdmin(Facility facility){
        if(facility == null){
            log.info("empty entity facility");
            return null;
        }
        List<String> admins = facility.getAdmins().stream().map(FacilityAdmin::getName).collect(Collectors.toList());
        return joinAdmin(admins);
    }

    public String joinFacilityCertifierAdmin(List<FacilityCertifierAdmin> admins){
        if(CollectionUtils.isEmpty(admins)){
            log.info("empty activity list");
            return null;
        }
        List<String> adminNames = admins.stream().map(FacilityCertifierAdmin::getAdminName).distinct().collect(Collectors.toList());
        return joinAdmin(adminNames);
    }


    private String joinAdmin(List<String> adminNames){
        if(CollectionUtils.isEmpty(adminNames)){
            log.info("empty admin name list");
            return null;
        }
        return TableDisplayUtil.getOneLineAdmin(adminNames);
    }
}
