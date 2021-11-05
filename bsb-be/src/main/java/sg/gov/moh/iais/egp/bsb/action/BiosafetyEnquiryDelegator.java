package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.CommonConstants.*;


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
    private static final String CLONED_ENTITY_STATUS_IN_USE = "BSBCENS003";
    private static final String CHOOSE_MARK_APPLICATION = "app";
    private static final String CHOOSE_MARK_FACILITY = "fn";
    private static final String CHOOSE_MARK_APPROVAL = "an";
    private static final String CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY = "on";
    private static final String PARAM_RISK_LEVEL = "riskLevel";
    private static final String PARAM_BIOLOGICAL_NAME = "bioName";

    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;


    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY, FUNCTION_BIOSATETY_ENQUIRY);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ProcessContants.class);
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
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
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
        if ("app".equals(count)) {
            ParamUtil.setRequestAttr(request, KEY_DOWNLOAD, URL_APPLICATION_INFO_FILE);
        } else if ("fn".equals(count)) {
            ParamUtil.setRequestAttr(request, KEY_DOWNLOAD, URL_FACILITY_INFO_FILE);
        } else if ("an".equals(count)) {
            ParamUtil.setRequestAttr(request, KEY_DOWNLOAD, URL_APPROVAL_INFO_FILE);
        } else if ("on".equals(count)) {
            ParamUtil.setRequestAttr(request, KEY_DOWNLOAD, URL_APPROVED_CERTIFIER_INFO_FILE);
        }
        // get search DTO
        EnquiryDto searchDto = getSearchDto(request);
        getResultAndAddFilter(request, searchDto, count);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, searchDto);
    }


    public void prepareDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        String appId = ParamUtil.getMaskedString(request, "appId");
//        Application application = processClient.getApplicationById(appId).getEntity();
        FacilityActivity facilityActivity = biosafetyEnquiryClient.getFacilityActivityByApplicationId(appId).getEntity();
//        application.getFacility().setActiveType(facilityActivity.getActivityType());
//        List<Biological> biologicalList = JoinBiologicalName.getBioListByFacilityScheduleList(facilityActivity.getFacilitySchedules(),processClient);
//        application.setBiologicalList(biologicalList);
//        ParamUtil.setRequestAttr(request, "applicationInfo", application);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
    }


    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = new ArrayList<>();
        log.info(StringUtil.changeForLog("strings value" + strings.toString()));
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
        if (CHOOSE_MARK_APPLICATION.equals(count) && Boolean.TRUE.equals(validationParam(request, "app", enquiryDto))) {
            ApplicationResultDto applicationResultDto = biosafetyEnquiryClient.getApp(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_RESULT, applicationResultDto.getBsbApp());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, applicationResultDto.getPageInfo());
        } else if (CHOOSE_MARK_FACILITY.equals(count) && Boolean.TRUE.equals(validationParam(request, "fac", enquiryDto))) {
            FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFac(enquiryDto).getEntity();
            List<FacilityActivity> activities = facilityResultDto.getBsbFac();
            for (FacilityActivity activity : activities) {
                Map<String,String> infos = joinBioNamesAndRiskLevel(activity.getBiologicalAgents());
                activity.setBioName(infos.get(PARAM_BIOLOGICAL_NAME));
                activity.setRiskLevel(infos.get(PARAM_RISK_LEVEL));
            }
            joinAdmin(activities);
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_RESULT, facilityResultDto.getBsbFac());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, facilityResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(facilityResultDto.getBsbFac().toString() + "========="));
        } else if (CHOOSE_MARK_APPROVAL.equals(count) && Boolean.TRUE.equals(validationParam(request, "approval", enquiryDto))) {
            ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_RESULT, approvalResultDto.getBsbApproval());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, approvalResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(approvalResultDto.getBsbApproval().toString() + "==========facility"));
        } else if (CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY.equals(count) && Boolean.TRUE.equals(validationParam(request, "org", enquiryDto))) {
            ApprovedFacilityCerResultDto facilityCerResultDto = biosafetyEnquiryClient.getAFC(enquiryDto).getEntity();
            List<FacilityCertifierReg> certifierRegs = facilityCerResultDto.getBsbAFC();
            for (FacilityCertifierReg certifierReg : certifierRegs) {
                certifierReg.setAdminName(joinCertAdmin(certifierReg.getCertifierAdmins()));
            }
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVED_CERTIFIER_INFO_RESULT, facilityCerResultDto.getBsbAFC());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVED_CERTIFIER_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, facilityCerResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(facilityCerResultDto.getBsbAFC().toString() + "==========facility"));
        }

    }

    private Boolean validationParam(HttpServletRequest request, String type, EnquiryDto object) {
        ValidationResult vResult = WebValidationHelper.validateProperty(object, type);
        if (vResult != null && vResult.isHasErrors()) {
            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request, ProcessContants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return false;
        } else {
            return true;
        }
    }


//
//    @GetMapping(value = "/Application-information-file")
//    public @ResponseBody
//    void appFileHandler(HttpServletRequest request, HttpServletResponse response) {
//        log.debug(StringUtil.changeForLog("application fileHandler start ...."));
//        File file = null;
//        log.debug("indicates that a app record has been selected ");
//        EnquiryDto enquiryDto = getSearchDto(request);
//        ApplicationResultDto results = biosafetyEnquiryClient.getApp(enquiryDto).getEntity();
//        if (!Objects.isNull(results)) {
//            List<Application> queryList = results.getBsbApp();
//            List<ApplicationInfoDto> applicationInfoDtos = new ArrayList<>();
//            for (Application application : queryList) {
//                application.setBioName(JoinBiologicalName.joinBiologicalName(application.getFacility().getFacilitySchedules(), processClient));
//                application.setRiskLevel(JoinBiologicalName.joinRiskLevel(application.getFacility().getFacilitySchedules(), processClient));
//                ApplicationInfoDto applicationInfoDto = new ApplicationInfoDto();
//                applicationInfoDto.setApplicationNo(application.getApplicationNo());
//                applicationInfoDto.setRiskLevel(application.getRiskLevel());
//                applicationInfoDto.setAppType(application.getAppType());
//                applicationInfoDto.setAppStatus(application.getStatus());
//                applicationInfoDto.setProcessType(application.getProcessType());
//                applicationInfoDto.setFacilityName(application.getFacility().getFacilityName());
//                applicationInfoDto.setFacilityClassification(application.getFacility().getFacilityClassification());
//                applicationInfoDto.setFacilityType(application.getFacility().getFacilityType());
//                applicationInfoDto.setApplicationDt(application.getApplicationDt());
//                applicationInfoDto.setApprovalDate(application.getApprovalDate());
//                applicationInfoDto.setBioName(application.getRiskLevel());
//                applicationInfoDto.setDoVerifiedDt(application.getDoVerifiedDt());
//                applicationInfoDto.setHmVerifiedDt(application.getHmVerifiedDt());
//                applicationInfoDto.setAoVerifiedDt(application.getAoVerifiedDt());
//                applicationInfoDtos.add(applicationInfoDto);
//            }
//            applicationInfoDtos.forEach(i -> i.setAppStatus(MasterCodeUtil.getCodeDesc(i.getAppStatus())));
//            applicationInfoDtos.forEach(i -> i.setAppType(MasterCodeUtil.getCodeDesc(i.getAppType())));
//            applicationInfoDtos.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
//            applicationInfoDtos.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
//            applicationInfoDtos.forEach(i -> i.setProcessType(MasterCodeUtil.getCodeDesc(i.getProcessType())));
//            try {
//                file = ExcelWriter.writerToExcel(applicationInfoDtos, ApplicationInfoDto.class, "Application Information_Search_Template");
//            } catch (Exception e) {
//                log.error("=======>app fileHandler  error >>>>>", e);
//            }
//        }
//        try {
//            FileUtils.writeFileResponseContent(response, file);
//            FileUtils.deleteTempFile(file);
//        } catch (IOException e) {
//            log.debug(e.getMessage());
//        }
//        log.debug(StringUtil.changeForLog("app fileHandler end ...."));
//    }

//    @GetMapping(value = "/Facility-information-file")
//    public @ResponseBody
//    void facFileHandler(HttpServletRequest request, HttpServletResponse response) {
//        log.debug(StringUtil.changeForLog("facility fileHandler start ...."));
//        File file = null;
//        EnquiryDto enquiryDto = getSearchDto(request);
//        FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFac(enquiryDto).getEntity();
//        log.debug("indicates that a facility record has been selected ");
//        if (!Objects.isNull(facilityResultDto)) {
//            List<FacilityActivity> queryList = facilityResultDto.getBsbFac();
//            List<FacilityInfoDto> facilityInfoDtos = new ArrayList<>();
//
//            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
//            for (FacilityActivity facility : queryList) {
//                StringBuilder stringBuilder = new StringBuilder();
//                FacilityInfoDto facilityInfoDto = new FacilityInfoDto();
//                Biological biological = biosafetyEnquiryClient.getBiologicalById(facility.getBiologicalId()).getEntity();
//                facilityInfoDto.setFacilityAddress(facility.getFacilitySchedule().getFacilityActivity().getFacility().getBlkNo() + "" + facility.getFacilitySchedule().getFacilityActivity().getFacility().getStreetName() + "" + facility.getFacilitySchedule().getFacilityActivity().getFacility().getFloorNo() + "-" + facility.getFacilitySchedule().getFacilityActivity().getFacility().getUnitNo() + "" + facility.getFacilitySchedule().getFacilityActivity().getFacility().getPostalCode());
//                for (int i = 0; i < facility.getFacilitySchedule().getFacilityActivity().getFacility().getAdmins().size(); i++) {
//                    if (i + 1 <= facility.getFacilitySchedule().getFacilityActivity().getFacility().getAdmins().size()) {
//                        stringBuilder.append(facility.getFacilitySchedule().getFacilityActivity().getFacility().getAdmins().get(i).getName()).append(",");
//                    } else {
//                        stringBuilder.append(facility.getFacilitySchedule().getFacilityActivity().getFacility().getAdmins().get(i).getName());
//                    }
//                }
//                facilityInfoDto.setFacilityAdmin(stringBuilder.toString());
//                facilityInfoDto.setFacilityType(facility.getFacilitySchedule().getFacilityActivity().getActivityType());
//                facilityInfoDto.setFacilityName(facility.getFacilitySchedule().getFacilityActivity().getFacility().getFacilityName());
//                facilityInfoDto.setFacilityExpiryDate(sdf.format(facility.getFacilitySchedule().getFacilityActivity().getFacility().getExpiryDt()));
//                facilityInfoDto.setFacilityClassification(facility.getFacilitySchedule().getFacilityActivity().getFacility().getFacilityClassification());
//                facilityInfoDto.setApprovedFacilityCertifier(facility.getFacilitySchedule().getFacilityActivity().getFacility().getApproval());
//                facilityInfoDto.setFacilityOperator(facility.getFacilitySchedule().getFacilityActivity().getFacility().getOperator().getFacOperator());
//                facilityInfoDto.setCurrentFacilityStatus(facility.getFacilitySchedule().getFacilityActivity().getFacility().getFacilityStatus());
//                facilityInfoDto.setBiologicalAgent(biological.getName());
//                facilityInfoDto.setGazettedArea(facility.getFacilitySchedule().getFacilityActivity().getFacility().getIsProtected());
//                facilityInfoDto.setRiskLevelOfTheBiologicalAgent(biological.getRiskLevel());
//                facilityInfoDtos.add(facilityInfoDto);
//            }
//            facilityInfoDtos.forEach(i -> i.setCurrentFacilityStatus(MasterCodeUtil.getCodeDesc(i.getCurrentFacilityStatus())));
//            facilityInfoDtos.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
//            facilityInfoDtos.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
//            facilityInfoDtos.forEach(i -> i.setRiskLevelOfTheBiologicalAgent(MasterCodeUtil.getCodeDesc(i.getRiskLevelOfTheBiologicalAgent())));
//            try {
//                file = ExcelWriter.writerToExcel(facilityInfoDtos, FacilityInfoDto.class, "Facility Information_Search_Template");
//            } catch (Exception e) {
//                log.error("=======>facility fileHandler error >>>>>", e);
//            }
//        }
//        try {
//            FileUtils.writeFileResponseContent(response, file);
//            FileUtils.deleteTempFile(file);
//        } catch (IOException e) {
//            log.debug(e.getMessage());
//        }
//        log.debug(StringUtil.changeForLog("facility fileHandler end ...."));
//    }

//    @GetMapping(value = "/Approval-information-file")
//    public @ResponseBody
//    void approvalFileHandler(HttpServletRequest request, HttpServletResponse response) {
//        log.debug(StringUtil.changeForLog("fileHandler start ...."));
//        File file = null;
//        EnquiryDto enquiryDto = getSearchDto(request);
//        ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(enquiryDto).getEntity();
//        log.debug("indicates that a approval record has been selected ");
//        if (!Objects.isNull(approvalResultDto)) {
//            List<FacilityAgentSample> queryList = approvalResultDto.getBsbApproval();
//            List<ApprovalInfoDto> approvalInfoDtos = new ArrayList<>();
//
//            for (FacilityAgentSample facilityAgentSample : queryList) {
//                ApprovalInfoDto approvalInfoDto = new ApprovalInfoDto();
//                approvalInfoDto.setFacilityAddress(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getBlkNo() + "" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getStreetName() + "" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFloorNo() + "-" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getUnitNo() + "" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getPostalCode());
//                approvalInfoDto.setApprovalType(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getApprovalType());
//                approvalInfoDto.setApprovalStatus(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getApprovalStatus());
//                approvalInfoDto.setAgent(biosafetyEnquiryClient.getBiologicalById(facilityAgentSample.getFacilityBiologicalAgent().getBiologicalId()).getEntity().getName());
//                approvalInfoDto.setFacilityName(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityName());
//                approvalInfoDto.setFacilityClassification(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityClassification());
//                approvalInfoDto.setFacilityStatus(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityStatus());
//                approvalInfoDto.setFacilityType(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityType());
//                approvalInfoDto.setNatureOfTheSample(facilityAgentSample.getSampleNature());
//                approvalInfoDto.setRiskLevelOfTheBiologicalAgent(facilityAgentSample.getFacilityBiologicalAgent().getRiskLevel());
//                approvalInfoDtos.add(approvalInfoDto);
//            }
//            approvalInfoDtos.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
//            approvalInfoDtos.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
//            approvalInfoDtos.forEach(i -> i.setRiskLevelOfTheBiologicalAgent(MasterCodeUtil.getCodeDesc(i.getRiskLevelOfTheBiologicalAgent())));
//            approvalInfoDtos.forEach(i -> i.setApprovalType(MasterCodeUtil.getCodeDesc(i.getApprovalType())));
//            approvalInfoDtos.forEach(i -> i.setApprovalStatus(MasterCodeUtil.getCodeDesc(i.getApprovalStatus())));
//            approvalInfoDtos.forEach(i -> i.setNatureOfTheSample(MasterCodeUtil.getCodeDesc(i.getNatureOfTheSample())));
//            try {
//                file = ExcelWriter.writerToExcel(approvalInfoDtos, ApprovalInfoDto.class, "Approval Information_Search_Template");
//            } catch (Exception e) {
//                log.error("=======>approval fileHandler error >>>>>", e);
//            }
//        }
//        try {
//            FileUtils.writeFileResponseContent(response, file);
//            FileUtils.deleteTempFile(file);
//        } catch (IOException e) {
//            log.debug(e.getMessage());
//        }
//        log.debug(StringUtil.changeForLog("approval fileHandler end ...."));
//    }

//    @GetMapping(value = "/Approved-certifier-information-file")
//    public @ResponseBody
//    void afcFileHandler(HttpServletRequest request, HttpServletResponse response) {
//        log.debug(StringUtil.changeForLog("fileHandler start ...."));
//        File file = null;
//        EnquiryDto enquiryDto = getSearchDto(request);
//        ApprovedFacilityCerResultDto approvedFacilityCerResultDto = biosafetyEnquiryClient.getAFC(enquiryDto).getEntity();
//        log.debug("indicates that a approved record has been selected ");
//        if (!Objects.isNull(approvedFacilityCerResultDto)) {
//            List<Facility> queryList = approvedFacilityCerResultDto.getBsbAFC();
//            List<ApprovedFacilityCertifierInfoDto> facilityCertifierInfoDtos = new ArrayList<>();
//
//            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
//            for (Facility facility : queryList) {
//                StringBuilder stringBuilder = new StringBuilder();
//                ApprovedFacilityCertifierInfoDto facilityCertifierInfoDto = new ApprovedFacilityCertifierInfoDto();
//                facilityCertifierInfoDto.setAfcStatus(facility.getFacilityStatus());
//                for (int i = 0; i < facility.getAdmins().size(); i++) {
//                    if (i + 1 <= facility.getAdmins().size()) {
//                        stringBuilder.append(facility.getAdmins().get(i).getName()).append(",");
//                    } else {
//                        stringBuilder.append(facility.getAdmins().get(i).getName());
//                    }
//                }
//                facilityCertifierInfoDto.setAdministrator(stringBuilder.toString());
//                facilityCertifierInfoDto.setApprovedDate(sdf.format(facility.getApprovalDate()));
//                facilityCertifierInfoDto.setExpiryDate(sdf.format(facility.getExpiryDt()));
//                facilityCertifierInfoDto.setOrganisationAddress("0915 xxxx tech 4-168");
//                facilityCertifierInfoDto.setOrganisationName(facility.getFacilityName());
//                facilityCertifierInfoDtos.add(facilityCertifierInfoDto);
//            }
//            facilityCertifierInfoDtos.forEach(i -> i.setAfcStatus(MasterCodeUtil.getCodeDesc(i.getAfcStatus())));
//            try {
//                file = ExcelWriter.writerToExcel(facilityCertifierInfoDtos, ApprovedFacilityCertifierInfoDto.class, "Approved_Facility_Certifier_Information_Search_Template");
//            } catch (Exception e) {
//                log.error("=======>approved fileHandler error >>>>>", e);
//            }
//        }
//        try {
//            FileUtils.writeFileResponseContent(response, file);
//            FileUtils.deleteTempFile(file);
//        } catch (IOException e) {
//            log.debug(e.getMessage());
//        }
//        log.debug(StringUtil.changeForLog("approved fileHandler end ...."));
//    }

    private EnquiryDto getSearchDto(HttpServletRequest request) {
        EnquiryDto searchDto = (EnquiryDto) ParamUtil.getSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private EnquiryDto getDefaultSearchDto() {
        return new EnquiryDto();
    }

    //join biological name according to risk level
    private Map<String,String> joinBioNamesAndRiskLevel(List<FacilityBiologicalAgent> agents) {
        StringBuilder nameSb = new StringBuilder();
        StringBuilder rlSb = new StringBuilder();
        Map<String, StringBuilder> bios = new HashMap<>();
        Map<String,String> infos = new HashMap<>();
        Set<String> riskLists = new HashSet<>();
            for (FacilityBiologicalAgent agent : agents) {
                if (agent != null && CLONED_ENTITY_STATUS_IN_USE.equals(agent.getUseStatus())) {
                    //get each biological by bioId and put value into hashMap
                    Biological biological = biosafetyEnquiryClient.getBiologicalById(agent.getBiologicalId()).getEntity();
                    riskLists.add(biological.getRiskLevel());
                    String key = biological.getRiskLevel();
                    if (bios.containsKey(key)) {
                        StringBuilder sb1 = bios.get(key);
                        sb1.append(biological.getName()).append(",");
                    } else {
                        bios.put(biological.getRiskLevel(), new StringBuilder().append(biological.getName()).append(","));
                    }
                }
            }
            for (String str : riskLists) {
                StringBuilder name = bios.get(str);
                //Remove extra punctuation
                name.deleteCharAt(name.length() - 1);
                nameSb.append(name).append(";");
                rlSb.append(str).append(";");
            }
            //save value of biological name and risk level ,clear stringBuilder as well
            nameSb.deleteCharAt(nameSb.length() - 1);
            rlSb.deleteCharAt(rlSb.length() - 1);
            infos.put(PARAM_BIOLOGICAL_NAME,nameSb.toString());
            nameSb.delete(0, nameSb.length());
            infos.put(PARAM_RISK_LEVEL,rlSb.toString());
            rlSb.delete(0, rlSb.length());
            //clear set
            riskLists.clear();
            //clear hashMap
            bios.clear();
            return infos;
    }

    //join admin name by admin or alter for show
    public void joinAdmin(List<FacilityActivity> activities) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sn = new StringBuilder();
        HashMap<String, List<String>> info = new HashMap<>();
        for (FacilityActivity activity : activities) {
            List<FacilityAdmin> admins = activity.getFacility().getAdmins();
            //step1:Get information about different roles through loops
            for (FacilityAdmin admin : admins) {
                String name = admin.getName();
                String type = admin.getType();
                if (StringUtils.hasLength(type) && info.containsKey(type)) {
                    List<String> names = info.get(type);
                    if (names != null && !names.isEmpty() && !names.contains(name)) {
                        names.add(name);
                    }
                } else {
                    if (StringUtils.hasLength(type)) {
                        List<String> aName = new ArrayList<>();
                        aName.add(name);
                        info.put(type, aName);
                    }
                }
            }
            if (!info.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : info.entrySet()) {
                    String key = entry.getKey();
                    List<String> value = entry.getValue();
                    for (String s : value) {
                        sn.append(s).append(",");
                    }
                    sn.deleteCharAt(sn.length() - 1);
                    sb.append(key).append(": ").append(sn).append(";");
                    sn.delete(0, sn.length());
                }
                //Remove the end; number
                sb.deleteCharAt(sb.length() - 1);
                activity.setAdmin(sb.toString());
                //empty hashMap and StringBuilder
                sb.delete(0, sb.length());
                info.clear();
            } else {
                log.debug("....");
            }
        }
    }

    //join admin name by admin or alter for show
    public String joinCertAdmin(List<FacilityCertifierAdmin> admins) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sn = new StringBuilder();
        HashMap<String, List<String>> info = new HashMap<>();

        //step1:Get information about different roles through loops
        for (FacilityCertifierAdmin admin : admins) {
            String name = admin.getAdminName();
            String type = admin.getAdminType();
            if (StringUtils.hasLength(type) && info.containsKey(type)) {
                List<String> names = info.get(type);
                if (names != null && !names.isEmpty() && !names.contains(name)) {
                    names.add(name);
                }
            } else {
                if (StringUtils.hasLength(type)) {
                    List<String> aName = new ArrayList<>();
                    aName.add(name);
                    info.put(type, aName);
                }
            }
        }
        if (!info.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : info.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                for (String s : value) {
                    sn.append(s).append(",");
                }
                sn.deleteCharAt(sn.length() - 1);
                sb.append(key).append(": ").append(sn).append(";");
                sn.delete(0, sn.length());
            }
            //Remove the end; number
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
            //empty hashMap and StringBuilder
        } else {
            log.debug("....");
            return null;
        }
    }


}
