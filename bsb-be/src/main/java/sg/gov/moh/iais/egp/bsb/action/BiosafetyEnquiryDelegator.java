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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sg.gov.moh.iais.egp.bsb.util.JoinBiologicalName;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.CommonConstants.*;


/**
 * AUTHOR: YiMing
 * DATE:2021/7/14 14:15
 * DESCRIPTION: TODO
 **/
@Delegator(value = "biosafetyEnquiryDelegator")
@Slf4j
public class BiosafetyEnquiryDelegator {
    private static final String PARAM_SEARCH_CHK = "searchChk";
    private static final String PARAM_COUNT = "count";

    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;
    @Autowired
    private ProcessClient processClient;

    /**
     * AutoStep: prepareBasicSearch
     *
     * @param bpc
     */
    public void prepareBasicSearch(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, PARAM_SEARCH_CHK);
        if (StringUtil.isEmpty(count)) {
            count = "0";
        }
        String searchNo = ParamUtil.getString(bpc.request, "searchNo");
        if (StringUtil.isEmpty(searchNo)) {
            searchNo = "null";
        }
        if ("app".equals(count)) {
            ApplicationResultDto applicationResultDto = biosafetyEnquiryClient.queryApplicationByAppNo(searchNo).getEntity();
            for (Application application : applicationResultDto.getBsbApp()) {
                application.setBioName(JoinBiologicalName.joinBiologicalName(application.getFacility().getFacilitySchedules(), processClient));
                application.setRiskLevel(JoinBiologicalName.joinRiskLevel(application.getFacility().getFacilitySchedules(), processClient));
            }
            ParamUtil.setRequestAttr(bpc.request, "applicationInfoDto", applicationResultDto.getBsbApp());
            ParamUtil.setRequestAttr(bpc.request, KEY_PAGE_INFO, applicationResultDto.getPageInfo());
        } else if ("fn".equals(count)) {
            FacilityResultDto facilityInfoDto = biosafetyEnquiryClient.queryFacilityByFacName(searchNo).getEntity();
            for (FacilityBiologicalAgent agent : facilityInfoDto.getBsbFac()) {
                agent.setBioName(biosafetyEnquiryClient.getBiologicalById(agent.getBiologicalId()).getEntity().getName());
            }
            ParamUtil.setRequestAttr(bpc.request, "facilityInfoDto", facilityInfoDto.getBsbFac());
            ParamUtil.setRequestAttr(bpc.request, KEY_PAGE_INFO, facilityInfoDto.getPageInfo());
        } else if ("on".equals(count)) {
            ApprovedFacilityCerResultDto approvedFacilityCerResultDto = biosafetyEnquiryClient.getAfcByOrgName(searchNo).getEntity();
            ParamUtil.setRequestAttr(bpc.request, "afcInfoDto", approvedFacilityCerResultDto.getBsbAFC());
            ParamUtil.setRequestAttr(bpc.request, KEY_PAGE_INFO, approvedFacilityCerResultDto.getPageInfo());
        }
        ParamUtil.setRequestAttr(bpc.request, PARAM_COUNT, count);
    }


    /**
     * AutoStep: changePage
     *
     * @param bpc
     */
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

    /**
     * AutoStep: prepareAdvSearch
     *
     * @param bpc
     */
    public void prepareAdvSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        if (StringUtil.isEmpty(count)) {
            count = "0";
        }
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
        preSelectOption(request, count);
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ENQUIRY_SEARCH_DTO);
    }

    /**
     * AutoStep: preAfterAdvSearch
     *
     * @param bpc
     */
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


    /**
     * AutoStep: prepareDetail
     *
     * @param bpc
     */
    public void prepareDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request,"searchChk");
        String appId = ParamUtil.getMaskedString(request,"appId");
        Application application = processClient.getApplicationById(appId).getEntity();
        List<Biological> biologicalList = JoinBiologicalName.getBioListByFacilityScheduleList(application.getFacility().getFacilitySchedules(),processClient);
        application.setBiologicalList(biologicalList);
        ParamUtil.setRequestAttr(request,"applicationInfo",application);
        ParamUtil.setRequestAttr(request,PARAM_COUNT,count);
    }


    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        log.info(StringUtil.changeForLog("strings value" + strings.toString()));
        for (String string : strings) {
            if (!"biologicalAgent".equals(name)) {
                selectModel.add(new SelectOption(string, string));
            } else {
                selectModel.add(new SelectOption(biosafetyEnquiryClient.getBiologicalByName(string).getEntity().getId(), string));
            }
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request, String num) {
        if ("app".equals(num) || "fn".equals(num) || "an".equals(num)) {
            if ("fn".equals(num)) {
                List<String> approvals = biosafetyEnquiryClient.queryDistinctApproval().getEntity();
                selectOption(request, "AFC", approvals);
            }
            List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
            selectOption(request, "facilityName", facNames);
            List<String> bioNames = biosafetyEnquiryClient.queryDistinctFA().getEntity();
            selectOption(request, "biologicalAgent", bioNames);
        }
    }

    private void addFilter(HttpServletRequest request, EnquiryDto enquiryDto, String count) throws ParseException {
        enquiryDto.clearAllFields();
        commonFilter(request, enquiryDto, count);
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

    public void commonFilter(HttpServletRequest request, EnquiryDto enquiryDto, String count) throws ParseException {
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
        } else if ("on".equals(count) || "an".equals(count)) {
            enquiryDto.setApprovedDateFrom(DateUtil.yearAgoDt());
        }
        if (approvedDateTo != null) {
            enquiryDto.setApprovedDateTo(approvedDateTo);
        } else if ("on".equals(count) || "an".equals(count)) {
            enquiryDto.setApprovedDateTo(new Date());
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
        } else {
            enquiryDto.setApplicationSubmissionDateFrom(DateUtil.yearAgoDt());
        }
        if (applicationSubmissionDateTo != null) {
            enquiryDto.setApplicationSubmissionDateTo(applicationSubmissionDateTo);
        } else {
            enquiryDto.setApplicationSubmissionDateTo(new Date());
        }
        if (approvalDateFrom != null) {
            enquiryDto.setApprovalDateFrom(approvalDateFrom);
        } else {
            enquiryDto.setApprovalDateFrom(DateUtil.yearAgoDt());
        }
        if (approvalDateTo != null) {
            enquiryDto.setApprovalDateTo(approvalDateTo);
        } else {
            enquiryDto.setApprovalDateTo(new Date());
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
        } else {
            enquiryDto.setFacilityExpiryDateFrom(DateUtil.yearAgoDt());
        }
        if (facilityExpiryDateTo != null) {
            enquiryDto.setFacilityExpiryDateTo(facilityExpiryDateTo);
        } else {
            enquiryDto.setFacilityExpiryDateTo(new Date());
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
            enquiryDto.setNatureOfTheSample(StringUtils.join(natureOfTheSample, ","));
        }
        if (StringUtil.isNotEmpty(approvalType)) {
            enquiryDto.setApprovalType(approvalType);
        }
        if (approvalSubmissionDateFrom != null) {
            enquiryDto.setApprovalSubmissionDateFrom(approvalSubmissionDateFrom);
        } else {
            enquiryDto.setApprovalSubmissionDateFrom(DateUtil.yearAgoDt());
        }
        if (approvalSubmissionDateTo != null) {
            enquiryDto.setApprovalSubmissionDateTo(approvalSubmissionDateTo);
        } else {
            enquiryDto.setApprovalSubmissionDateTo(new Date());
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
        if ("app".equals(count)) {
            ApplicationResultDto applicationResultDto = biosafetyEnquiryClient.getApp(enquiryDto).getEntity();
            for (Application application : applicationResultDto.getBsbApp()) {
                application.setBioName(JoinBiologicalName.joinBiologicalName(application.getFacility().getFacilitySchedules(), processClient));
                application.setRiskLevel(JoinBiologicalName.joinRiskLevel(application.getFacility().getFacilitySchedules(), processClient));
            }
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_RESULT, applicationResultDto.getBsbApp());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPLICATION_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, applicationResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(applicationResultDto.getBsbApp().toString() + "===================application"));
        }
        if ("fn".equals(count)) {
            FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFac(enquiryDto).getEntity();
            for (FacilityBiologicalAgent agent : facilityResultDto.getBsbFac()) {
                agent.setBioName(biosafetyEnquiryClient.getBiologicalById(agent.getBiologicalId()).getEntity().getName());
            }
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_RESULT, facilityResultDto.getBsbFac());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_FACILITY_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, facilityResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(facilityResultDto.getBsbFac().toString() + "==========facility"));
        }
        if ("an".equals(count)) {
            ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(enquiryDto).getEntity();
            List<FacilityAgentSample> list = approvalResultDto.getBsbApproval();
            for (FacilityAgentSample agentSample : list) {
                agentSample.setBioName(biosafetyEnquiryClient.getBiologicalById(agentSample.getFacilityBiologicalAgent().getBiologicalId()).getEntity().getName());
            }
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_RESULT, approvalResultDto.getBsbApproval());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, approvalResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(approvalResultDto.getBsbApproval().toString() + "==========facility"));
        }

        if ("on".equals(count)) {
            ApprovedFacilityCerResultDto facilityCerResultDto = biosafetyEnquiryClient.getAFC(enquiryDto).getEntity();
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVED_CERTIFIER_INFO_RESULT, facilityCerResultDto.getBsbAFC());
            ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVED_CERTIFIER_INFO_SEARCH, enquiryDto);
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, facilityCerResultDto.getPageInfo());
            log.info(StringUtil.changeForLog(facilityCerResultDto.getBsbAFC().toString() + "==========facility"));
        }

    }

    @GetMapping(value = "/Application-information-file")
    public @ResponseBody
    void appFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        log.debug("indicates that a record has been selected ");
        EnquiryDto enquiryDto = getSearchDto(request);
        ApplicationResultDto results = biosafetyEnquiryClient.getApp(enquiryDto).getEntity();
        if (!Objects.isNull(results)) {
            List<Application> queryList = results.getBsbApp();
            List<ApplicationInfoDto> applicationInfoDtos = new ArrayList<>();
            for (Application application : queryList) {
                application.setBioName(JoinBiologicalName.joinBiologicalName(application.getFacility().getFacilitySchedules(), processClient));
                application.setRiskLevel(JoinBiologicalName.joinRiskLevel(application.getFacility().getFacilitySchedules(), processClient));
                ApplicationInfoDto applicationInfoDto = new ApplicationInfoDto();
                applicationInfoDto.setApplicationNo(application.getApplicationNo());
                applicationInfoDto.setRiskLevel(application.getRiskLevel());
                applicationInfoDto.setAppType(application.getAppType());
                applicationInfoDto.setAppStatus(application.getStatus());
                applicationInfoDto.setProcessType(application.getProcessType());
                applicationInfoDto.setFacilityName(application.getFacility().getFacilityName());
                applicationInfoDto.setFacilityClassification(application.getFacility().getFacilityClassification());
                applicationInfoDto.setFacilityType(application.getFacility().getFacilityType());
                applicationInfoDto.setApplicationDt(application.getApplicationDt());
                applicationInfoDto.setApprovalDate(application.getApprovalDate());
                applicationInfoDto.setBioName(application.getRiskLevel());
                applicationInfoDto.setDoVerifiedDt(application.getDoVerifiedDt());
                applicationInfoDto.setHmVerifiedDt(application.getHmVerifiedDt());
                applicationInfoDto.setAoVerifiedDt(application.getAoVerifiedDt());
                applicationInfoDtos.add(applicationInfoDto);
            }
            applicationInfoDtos.forEach(i -> i.setAppStatus(MasterCodeUtil.getCodeDesc(i.getAppStatus())));
            applicationInfoDtos.forEach(i -> i.setAppType(MasterCodeUtil.getCodeDesc(i.getAppType())));
            applicationInfoDtos.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
            applicationInfoDtos.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
            applicationInfoDtos.forEach(i -> i.setProcessType(MasterCodeUtil.getCodeDesc(i.getProcessType())));
            try {
                file = ExcelWriter.writerToExcel(applicationInfoDtos, ApplicationInfoDto.class, "Application Information_Search_Template");
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

    @GetMapping(value = "/Facility-information-file")
    public @ResponseBody
    void facFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        EnquiryDto enquiryDto = getSearchDto(request);
        FacilityResultDto facilityResultDto = biosafetyEnquiryClient.getFac(enquiryDto).getEntity();
        log.debug("indicates that a record has been selected ");
        if (!Objects.isNull(facilityResultDto)) {
            List<FacilityBiologicalAgent> queryList = facilityResultDto.getBsbFac();
            List<FacilityInfoDto> facilityInfoDtos = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
            for (FacilityBiologicalAgent facility : queryList) {
                StringBuilder stringBuilder = new StringBuilder();
                FacilityInfoDto facilityInfoDto = new FacilityInfoDto();
                facilityInfoDto.setFacilityAddress(facility.getFacilitySchedule().getFacility().getBlkNo() + "" + facility.getFacilitySchedule().getFacility().getStreetName() + "" + facility.getFacilitySchedule().getFacility().getFloorNo() + "-" + facility.getFacilitySchedule().getFacility().getUnitNo() + "" + facility.getFacilitySchedule().getFacility().getPostalCode());
                for (int i = 0; i < facility.getFacilitySchedule().getFacility().getAdmins().size(); i++) {
                    if (i + 1 <= facility.getFacilitySchedule().getFacility().getAdmins().size()) {
                        stringBuilder.append(facility.getFacilitySchedule().getFacility().getAdmins().get(i).getName()).append(",");
                    } else {
                        stringBuilder.append(facility.getFacilitySchedule().getFacility().getAdmins().get(i).getName());
                    }
                }
                facilityInfoDto.setFacilityAdmin(stringBuilder.toString());
                facilityInfoDto.setFacilityType(facility.getFacilitySchedule().getFacility().getFacilityType());
                facilityInfoDto.setFacilityName(facility.getFacilitySchedule().getFacility().getFacilityName());
                facilityInfoDto.setFacilityExpiryDate(sdf.format(facility.getFacilitySchedule().getFacility().getExpiryDt()));
                facilityInfoDto.setFacilityClassification(facility.getFacilitySchedule().getFacility().getFacilityClassification());
                facilityInfoDto.setApprovedFacilityCertifier(facility.getFacilitySchedule().getFacility().getApproval());
                facilityInfoDto.setFacilityOperator(facility.getFacilitySchedule().getFacility().getOperatorName());
                facilityInfoDto.setCurrentFacilityStatus(facility.getFacilitySchedule().getFacility().getFacilityStatus());
                facilityInfoDto.setBiologicalAgent(biosafetyEnquiryClient.getBiologicalById(facility.getBiologicalId()).getEntity().getName());
                facilityInfoDto.setGazettedArea(facility.getFacilitySchedule().getFacility().getIsProtected());
                facilityInfoDto.setRiskLevelOfTheBiologicalAgent(facility.getRiskLevel());
                facilityInfoDtos.add(facilityInfoDto);
            }
            facilityInfoDtos.forEach(i -> i.setCurrentFacilityStatus(MasterCodeUtil.getCodeDesc(i.getCurrentFacilityStatus())));
            facilityInfoDtos.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
            facilityInfoDtos.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
            facilityInfoDtos.forEach(i -> i.setRiskLevelOfTheBiologicalAgent(MasterCodeUtil.getCodeDesc(i.getRiskLevelOfTheBiologicalAgent())));
            try {
                file = ExcelWriter.writerToExcel(facilityInfoDtos, FacilityInfoDto.class, "Facility Information_Search_Template");
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

    @GetMapping(value = "/Approval-information-file")
    public @ResponseBody
    void apprFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        EnquiryDto enquiryDto = getSearchDto(request);
        ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(enquiryDto).getEntity();
        log.debug("indicates that a record has been selected ");
        if (!Objects.isNull(approvalResultDto)) {
            List<FacilityAgentSample> queryList = approvalResultDto.getBsbApproval();
            List<ApprovalInfoDto> approvalInfoDtos = new ArrayList<>();

            for (FacilityAgentSample facilityAgentSample : queryList) {
                ApprovalInfoDto approvalInfoDto = new ApprovalInfoDto();
                approvalInfoDto.setFacilityAddress(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getBlkNo() + "" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getStreetName() + "" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFloorNo() + "-" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getUnitNo() + "" + facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getPostalCode());
                approvalInfoDto.setApprovalType(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getApprovalType());
                approvalInfoDto.setApprovalStatus(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getApprovalStatus());
                approvalInfoDto.setAgent(biosafetyEnquiryClient.getBiologicalById(facilityAgentSample.getFacilityBiologicalAgent().getBiologicalId()).getEntity().getName());
                approvalInfoDto.setFacilityName(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityName());
                approvalInfoDto.setFacilityClassification(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityClassification());
                approvalInfoDto.setFacilityStatus(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityStatus());
                approvalInfoDto.setFacilityType(facilityAgentSample.getFacilityBiologicalAgent().getFacilitySchedule().getFacility().getFacilityType());
                approvalInfoDto.setNatureOfTheSample(facilityAgentSample.getSampleNature());
                approvalInfoDto.setRiskLevelOfTheBiologicalAgent(facilityAgentSample.getFacilityBiologicalAgent().getRiskLevel());
                approvalInfoDtos.add(approvalInfoDto);
            }
            approvalInfoDtos.forEach(i -> i.setFacilityClassification(MasterCodeUtil.getCodeDesc(i.getFacilityClassification())));
            approvalInfoDtos.forEach(i -> i.setFacilityType(MasterCodeUtil.getCodeDesc(i.getFacilityType())));
            approvalInfoDtos.forEach(i -> i.setRiskLevelOfTheBiologicalAgent(MasterCodeUtil.getCodeDesc(i.getRiskLevelOfTheBiologicalAgent())));
            approvalInfoDtos.forEach(i -> i.setApprovalType(MasterCodeUtil.getCodeDesc(i.getApprovalType())));
            approvalInfoDtos.forEach(i -> i.setApprovalStatus(MasterCodeUtil.getCodeDesc(i.getApprovalStatus())));
            approvalInfoDtos.forEach(i -> i.setNatureOfTheSample(MasterCodeUtil.getCodeDesc(i.getNatureOfTheSample())));
            try {
                file = ExcelWriter.writerToExcel(approvalInfoDtos, ApprovalInfoDto.class, "Approval Information_Search_Template");
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

    @GetMapping(value = "/Approved-certifier-information-file")
    public @ResponseBody
    void afcrFileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;
        EnquiryDto enquiryDto = getSearchDto(request);
        ApprovedFacilityCerResultDto approvedFacilityCerResultDto = biosafetyEnquiryClient.getAFC(enquiryDto).getEntity();
        log.debug("indicates that a record has been selected ");
        if (!Objects.isNull(approvedFacilityCerResultDto)) {
            List<Facility> queryList = approvedFacilityCerResultDto.getBsbAFC();
            List<ApprovedFacilityCertifierInfoDto> facilityCertifierInfoDtos = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
            for (Facility facility : queryList) {
                StringBuilder stringBuilder = new StringBuilder();
                ApprovedFacilityCertifierInfoDto facilityCertifierInfoDto = new ApprovedFacilityCertifierInfoDto();
                facilityCertifierInfoDto.setAfcStatus(facility.getFacilityStatus());
                for (int i = 0; i < facility.getAdmins().size(); i++) {
                    if (i + 1 <= facility.getAdmins().size()) {
                        stringBuilder.append(facility.getAdmins().get(i).getName()).append(",");
                    } else {
                        stringBuilder.append(facility.getAdmins().get(i).getName());
                    }
                }
                facilityCertifierInfoDto.setAdministrator(stringBuilder.toString());
                facilityCertifierInfoDto.setApprovedDate(sdf.format(facility.getApprovalDate()));
                facilityCertifierInfoDto.setExpiryDate(sdf.format(facility.getExpiryDt()));
                facilityCertifierInfoDto.setOrganisationAddress(facility.getOrganization().getBlkNo() + "" + facility.getOrganization().getStreetName() + "" + facility.getOrganization().getFloorNo() + "-" + facility.getOrganization().getUnitNo() + "" + facility.getOrganization().getPostalCode());
                facilityCertifierInfoDto.setOrganisationName(facility.getOrganization().getName());
                facilityCertifierInfoDtos.add(facilityCertifierInfoDto);
            }
            facilityCertifierInfoDtos.forEach(i -> i.setAfcStatus(MasterCodeUtil.getCodeDesc(i.getAfcStatus())));
            try {
                file = ExcelWriter.writerToExcel(facilityCertifierInfoDtos, ApprovedFacilityCertifierInfoDto.class, "Approved_Facility_Certifier_Information_Search_Template");
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
