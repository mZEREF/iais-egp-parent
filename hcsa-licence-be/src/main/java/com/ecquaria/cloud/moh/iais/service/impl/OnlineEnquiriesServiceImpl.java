package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComplianceHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRectifiedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.EnquiryInspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OnlineEnquiriesServiceImpl
 *
 * @author junyu
 * @date 2020/2/11
 */
@Service
@Slf4j
public class OnlineEnquiriesServiceImpl implements OnlineEnquiriesService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    InspectionRectificationProService inspectionRectificationProService;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private InsRepClient insRepClient;
    @Autowired
    LicenceService licenceService;
    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    private InsRepService insRepService;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    TaskService taskService;
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private LicenceViewService licenceViewService;


    @Override
    public SearchResult<LicenseeQueryDto> searchLicenseeIdsParam(SearchParam searchParam) {
        return organizationClient.searchLicenseeIdsParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<HcsaSvcQueryDto> searchSvcNamesParam(SearchParam searchParam) {
        return hcsaConfigClient.searchSvcNamesParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<ProfessionalInformationQueryDto> searchProfessionalInformation(SearchParam searchParam) {
        return hcsaLicenceClient.searchProfessionalInformation(searchParam).getEntity();
    }

    @Override
    public void setLicInfo(HttpServletRequest request) {
        String licenceId = (String) ParamUtil.getSessionAttr(request, "id");

        LicenceDto licenceDto=licenceService.getLicenceDto(licenceId);
        OrganizationLicDto organizationLicDto= organizationClient.getOrganizationLicDtoByLicenseeId(licenceDto.getLicenseeId()).getEntity();
        organizationLicDto.getLicenseeIndividualDto().setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{organizationLicDto.getLicenseeIndividualDto().getSalutation()}).get(0).getText());
        try{
            organizationLicDto.setDoMain(IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId()).get(0));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            organizationLicDto.setDoMain("-");
        }
        List<PersonnelsDto> personnelsDto= hcsaLicenceClient.getPersonnelDtoByLicId(licenceId).getEntity();

        for (LicenseeKeyApptPersonDto org:organizationLicDto.getLicenseeKeyApptPersonDtos()
        ) {
            org.setDesignation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{org.getDesignation()}).get(0).getText());
            org.setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{org.getSalutation()}).get(0).getText());
        }
        for (PersonnelsDto per:personnelsDto
        ) {
            try{
                per.getLicKeyPersonnelDto().setPsnType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getLicKeyPersonnelDto().getPsnType()}).get(0).getText());
                per.getKeyPersonnelDto().setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelDto().getSalutation()}).get(0).getText());
                per.getKeyPersonnelDto().setDesignation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelDto().getDesignation()}).get(0).getText());
                per.getKeyPersonnelExtDto().setProfessionType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelExtDto().getProfessionType()}).get(0).getText());
                switch (per.getKeyPersonnelExtDto().getPreferredMode()){
                    case "1":per.getKeyPersonnelExtDto().setPreferredMode("Email");break;
                    case "2":per.getKeyPersonnelExtDto().setPreferredMode("SMS");break;
                    case "3":per.getKeyPersonnelExtDto().setPreferredMode("Email  SMS");break;
                    default:per.getKeyPersonnelExtDto().setPreferredMode("-");break;
                }
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
        }

        List<ComplianceHistoryDto> complianceHistoryDtos= getComplianceHistoryDtosByLicId(licenceId);

        ParamUtil.setSessionAttr(request,"complianceHistoryDtos", (Serializable) complianceHistoryDtos);
        ParamUtil.setSessionAttr(request,"organizationLicDto",organizationLicDto);
        ParamUtil.setSessionAttr(request,"personnelsDto", (Serializable) personnelsDto);
    }

    @Override
    public List<ComplianceHistoryDto> getComplianceHistoryDtosByLicId(String licenceId){
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceId).getEntity();
        List<ComplianceHistoryDto> complianceHistoryDtos= IaisCommonUtils.genNewArrayList();
        for(LicAppCorrelationDto appCorrelationDto:licAppCorrelationDtos){
            ComplianceHistoryDto complianceHistoryDto=new ComplianceHistoryDto();
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appCorrelationDto.getApplicationId()).getEntity();
            ApplicationDto applicationDto=applicationClient.getApplicationById(appCorrelationDto.getApplicationId()).getEntity();
            ApplicationGroupDto applicationGroupDto=applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
            complianceHistoryDto.setInspectionTypeName(applicationGroupDto.getIsPreInspection() == 0? "Off-Site Inspection":InspectionConstants.INSPECTION_TYPE_ONSITE);
            complianceHistoryDto.setAppPremCorrId(appPremisesCorrelationDto.getId());
            complianceHistoryDto.setComplianceTag("Full");
            try{
                List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = insepctionNcCheckListService.getNcItemDtoByAppCorrId(appPremisesCorrelationDto.getId());
                if(appPremisesPreInspectionNcItemDtos.size()!=0){
                    for (AppPremisesPreInspectionNcItemDto nc:appPremisesPreInspectionNcItemDtos
                    ) {
                        if(nc.getIsRecitfied()==0){
                            complianceHistoryDto.setComplianceTag("Partial");
                        }
                    }
                }
                AdCheckListShowDto adCheckListShowDto = fillupChklistService.getAdhoc(appPremisesCorrelationDto.getId());
                if(adCheckListShowDto!=null){
                    List<AdhocNcCheckItemDto> adItemList = adCheckListShowDto.getAdItemList();
                    if(adItemList!=null && !adItemList.isEmpty()){
                        for(AdhocNcCheckItemDto temp:adItemList){
                            if(!temp.getRectified()){
                                complianceHistoryDto.setComplianceTag("Partial");
                            }
                        }
                    }
                }

                List<AdhocChecklistItemDto> adhocChecklistItemDtos=applicationClient.getAdhocByAppPremCorrId(appPremisesCorrelationDto.getId()).getEntity();
                List<ChecklistItemDto> checklistItemDtos=inspectionRectificationProService.getQuesAndClause( appPremisesCorrelationDto.getId());
                String riskLvl = MasterCodeUtil.retrieveOptionsByCodes(new String[]{checklistItemDtos.get(0).getRiskLevel()}).get(0).getText();
                if(adhocChecklistItemDtos.size()!=0){
                    riskLvl = MasterCodeUtil.retrieveOptionsByCodes(new String[]{adhocChecklistItemDtos.get(0).getRiskLvl()}).get(0).getText();
                }
                complianceHistoryDto.setRiskTag(riskLvl);
            }catch (Exception e){
                log.error(e.getMessage(), e);
                complianceHistoryDto.setRiskTag("-");
            }
            try{
                List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                Calendar c = Calendar.getInstance();
                for (AppPremisesRecommendationDto appPremisesRecommendationDto:appPremisesRecommendationDtos
                ) {
                    c.setTime(appPremisesRecommendationDto.getRecomInDate());
                    complianceHistoryDto.setRemarks(appPremisesRecommendationDto.getRemarks());
                    complianceHistoryDto.setInspectionDateYear(c.get(Calendar.YEAR));
                    complianceHistoryDtos.add(complianceHistoryDto);
                }

            }catch (Exception e){
                log.error(e.getMessage(), e);
                // complianceHistoryDtos.add(complianceHistoryDto);
            }
        }
        return complianceHistoryDtos;
    }

    @Override
    public EnquiryInspectionReportDto getInsRepDto(ApplicationViewDto applicationViewDto,String licenceId) {
        EnquiryInspectionReportDto inspectionReportDto = new EnquiryInspectionReportDto();
        List<PremisesDto> licPremisesDto=hcsaLicenceClient.getPremisess(licenceId).getEntity();
        inspectionReportDto.setHciCode(licPremisesDto.get(0).getHciCode());
        //inspection report application dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(applicationViewDto.getAppPremisesCorrelationId()).getEntity();

        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String appGrpId = applicationDto.getAppGrpId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String status = applicationDto.getStatus();
        String appTypeCode = insRepClient.getAppType(appId).getEntity();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(appInsRepDto.getLicenseeId()).getEntity();
        if(StringUtil.isEmpty(licenceId)){
            inspectionReportDto.setLicenceNo("-");
        }else{
            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licenceId).getEntity();
            if(licenceDto!=null){
                String licenceNo = licenceDto.getLicenceNo();
                inspectionReportDto.setLicenceNo(licenceNo);
            }
        }
        if(licenseeDto!=null){
            String name = licenseeDto.getName();
            inspectionReportDto.setLicenseeName(name);
        }
        List<AppGrpPersonnelDto> principalOfficer = appInsRepDto.getPrincipalOfficer();
        List<String> poNames = IaisCommonUtils.genNewArrayList();
        for(AppGrpPersonnelDto appGrpPersonnelDto : principalOfficer){
            String name = appGrpPersonnelDto.getName();
            poNames.add(name);
        }
        inspectionReportDto.setPrincipalOfficers(poNames);


        List<String> nameList = IaisCommonUtils.genNewArrayList();
        AppPremisesRecommendationDto otherOfficesDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS).getEntity();
        if(otherOfficesDto!=null){
            String otherOffices = otherOfficesDto.getRemarks();
            nameList.add(otherOffices);
            inspectionReportDto.setInspectOffices(nameList);
        }else {
            String s = "-";
            nameList.add(s);
            inspectionReportDto.setInspectOffices(nameList);
        }
        //get application type (pre/post)
        Integer isPre = applicationGroupDto.getIsPreInspection();
        String appType = MasterCodeUtil.getCodeDesc(appTypeCode);
        String reasonForVisit;
        if (isPre == 1) {
            reasonForVisit = "Pre-licensing inspection for " + appType ;
        } else {
            reasonForVisit = "Post-licensing inspection for " + appType ;
        }

        //serviceId transform serviceCode
        List<String> list = IaisCommonUtils.genNewArrayList();
        String serviceId = appInsRepDto.getServiceId();
        list.add(serviceId) ;
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "" ;
        String svcCode = "" ;
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcName = hcsaServiceDto.getSvcName();
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }

        List<HcsaSvcSubtypeOrSubsumedDto> subsumedDtos = hcsaConfigClient.listSubCorrelationFooReport(serviceId).getEntity();
        List<String> subsumedServices = IaisCommonUtils.genNewArrayList();
        if (subsumedDtos != null && !subsumedDtos.isEmpty()) {
            for(HcsaSvcSubtypeOrSubsumedDto subsumedDto :subsumedDtos){
                subsumedServices.add(subsumedDto.getName());
            }
        }else {
            subsumedServices.add("-");
        }
        inspectionReportDto.setSubsumedServices(subsumedServices);
        //Nc
        List<ChecklistQuestionDto> listChecklistQuestionDtos = hcsaChklClient.getcheckListQuestionDtoList(svcCode, "Inspection").getEntity();
        List<ReportNcRegulationDto> listReportNcRegulationDto = IaisCommonUtils.genNewArrayList();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = IaisCommonUtils.genNewArrayList();
        //add ReportNcRegulationDto and add ncItemId
        if (listChecklistQuestionDtos != null && !listChecklistQuestionDtos.isEmpty()) {
            List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationId);
            if (ncAnswerDtoList != null && !ncAnswerDtoList.isEmpty()) {
                for (NcAnswerDto ncAnswerDto : ncAnswerDtoList) {
                    ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
                    reportNcRegulationDto.setNc(ncAnswerDto.getItemQuestion());
                    String clause = ncAnswerDto.getClause();
                    if(StringUtil.isEmpty(clause)){
                        reportNcRegulationDto.setRegulation("-");
                    }else {
                        reportNcRegulationDto.setRegulation(clause);
                    }
                    listReportNcRegulationDto.add(reportNcRegulationDto);
                }
            }
            inspectionReportDto.setStatus("Partial Compliance");
            inspectionReportDto.setNcRegulation(listReportNcRegulationDto);
        }else {
            inspectionReportDto.setStatus("Full Compliance");
            inspectionReportDto.setNcRegulation(null);
        }
        //add listReportNcRectifiedDto and add ncItemId
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremisesCorrelationId).getEntity();
        if (appPremPreInspectionNcDto != null) {
            String ncId = appPremPreInspectionNcDto.getId();
            List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
            if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                for (AppPremisesPreInspectionNcItemDto preInspNc : listAppPremisesPreInspectionNcItemDtos) {
                    ChecklistItemDto cDto = hcsaChklClient.getChklItemById(preInspNc.getItemId()).getEntity();
                    ReportNcRectifiedDto reportNcRectifiedDto = new ReportNcRectifiedDto();
                    reportNcRectifiedDto.setNc(cDto.getChecklistItem());
                    reportNcRectifiedDto.setRectified(preInspNc.getIsRecitfied() == 1 ? "Yes" : "No");
                    listReportNcRectifiedDto.add(reportNcRectifiedDto);
                }
                inspectionReportDto.setNcRectification(listReportNcRectifiedDto);
            }
        }else {
            inspectionReportDto.setNcRectification(null);
        }
        List<AppPremisesRecommendationDto>  appPremisesRecommendationDtos  = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();

        AppPremisesRecommendationDto ncRecommendationDto =appPremisesRecommendationDtos.get(0);
        //best practice remarks
        String bestPractice = "-";
        String remarks = "-";
        if(ncRecommendationDto==null){
            inspectionReportDto.setMarkedForAudit("No");
        }else if(ncRecommendationDto!=null) {
            Date recomInDate = ncRecommendationDto.getRecomInDate();
            if(recomInDate==null){
                inspectionReportDto.setMarkedForAudit("No");
            }else {
                inspectionReportDto.setMarkedForAudit("Yes");
                inspectionReportDto.setTcuDate(recomInDate);
            }
            String ncBestPractice = ncRecommendationDto.getBestPractice();
            String ncRemarks = ncRecommendationDto.getRemarks();
            if(!StringUtil.isEmpty(ncBestPractice)){
                bestPractice = ncBestPractice ;
            }
            if(!StringUtil.isEmpty(ncRemarks)){
                remarks = ncRemarks ;
            }
        }
        inspectionReportDto.setRectifiedWithinKPI("Yes");
        //Date time
        Date inspectionDate = null;
        String inspectionStartTime = "-";
        String inspectionEndTime = "-";
        try{
            List<AppPremisesRecommendationDto> appPreRecommentdationDtoStart = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
            List<AppPremisesRecommendationDto> appPreRecommentdationDtoDate = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
            List<AppPremisesRecommendationDto> appPreRecommentdationDtoEnd = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
            if(appPreRecommentdationDtoDate!=null){
                inspectionDate = appPreRecommentdationDtoDate.get(0).getRecomInDate();
            }
            if(appPreRecommentdationDtoStart!=null){
                inspectionStartTime = appPreRecommentdationDtoStart.get(0).getRecomDecision();
            }
            if(appPreRecommentdationDtoEnd!=null){
                inspectionEndTime = appPreRecommentdationDtoEnd.get(0).getRecomDecision();
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        AppPremisesRecommendationDto rectiDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSPECTYPE).getEntity();
        if(rectiDto!=null){
            String inspectypeRemarks = rectiDto.getRemarks();
            inspectionReportDto.setInspectypeRemarks(inspectypeRemarks);
        }else {
            inspectionReportDto.setInspectypeRemarks("-");
        }

        //get reported By
        List<String> listUserId = IaisCommonUtils.genNewArrayList();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationViewDto.getApplicationDto().getApplicationNo());
        String userId ="";
        String wkGrpId = appPremisesRoutingHistoryDtos.get(0).getWrkGrpId();
        for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto:appPremisesRoutingHistoryDtos ){
            if(appPremisesRoutingHistoryDto.getProcessDecision().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW)){
                userId=appPremisesRoutingHistoryDto.getActionby();
            }
            if(appPremisesRoutingHistoryDto.getWrkGrpId()!=null){
                wkGrpId = appPremisesRoutingHistoryDto.getWrkGrpId();
            }
        }
        listUserId.add(userId);
        List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
        String reportBy = userList.get(0).getDisplayName();
        listUserId.clear();
        //get inspection lead
        List<String> leadId = organizationClient.getInspectionLead(wkGrpId).getEntity();
        listUserId.addAll(leadId);
        List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
        String leadName = leadList.get(0).getDisplayName();
        inspectionReportDto.setReportedBy(reportBy);
        inspectionReportDto.setReportNoteBy(leadName);
        Set<String> inspectiors = taskService.getInspectiors(appPremisesCorrelationId, "TSTATUS003", "INSPECTOR");
        //get inspectiors
        List<String> inspectors = IaisCommonUtils.genNewArrayList();
        for (String inspector : inspectiors) {
            inspectors.add(inspector);
        }
        List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
        List<String> inspectorsName = IaisCommonUtils.genNewArrayList();
        for (OrgUserDto orgUserDto : inspectorList) {
            String displayName = orgUserDto.getDisplayName();
            inspectorsName.add(displayName);
        }
        inspectionReportDto.setInspectors(inspectorsName);

        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
        inspectionReportDto.setInspectionDate(inspectionDate);
        inspectionReportDto.setInspectionStartTime(inspectionStartTime);
        inspectionReportDto.setInspectionEndTime(inspectionEndTime);
        inspectionReportDto.setBestPractice(bestPractice);
        inspectionReportDto.setTaskRemarks(remarks);
        inspectionReportDto.setCurrentStatus(status);
        return inspectionReportDto;
    }

    @Override
    public void preInspReport(HttpServletRequest request) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>requestForInformation");
        String index=ParamUtil.getString(request, "crud_action_additional");
        int indexNo=Integer.parseInt(index);
        String licenceId = (String) ParamUtil.getSessionAttr(request, "id");
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceId).getEntity();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=IaisCommonUtils.genNewArrayList();
        for(LicAppCorrelationDto appCorrelationDto:licAppCorrelationDtos) {
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appCorrelationDto.getApplicationId()).getEntity();
            appPremisesCorrelationDtos.add(appPremisesCorrelationDto);
        }
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appPremisesCorrelationDtos.get(indexNo).getId());
        EnquiryInspectionReportDto insRepDto = getInsRepDto(applicationViewDto,licenceId);
        ParamUtil.setSessionAttr(request, "insRepDto", insRepDto);
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDtos.get(indexNo).getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        appPremisesRecommendationDto.setRecomDecision(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRecommendationDto.getRecomDecision()}).get(0).getText());
        ParamUtil.setSessionAttr(request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        // 		preInspReport->OnStepProcess
    }

    @Override
    public void setAppInfo(HttpServletRequest request) {
        String appCorrId = (String) ParamUtil.getSessionAttr(request, "id");
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(appCorrId);
        if(applicationViewDto.getHciCode()==null){
            List<String> appIds= new ArrayList<>(Collections.singleton(applicationViewDto.getApplicationDto().getId()));
            List<String> licIds=getLicIdsByappIds(appIds);
            if(licIds.size()!=0){
                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(licIds.get(0)).getEntity();
                applicationViewDto.setHciCode(premisesDtoList.get(0).getHciCode());
            }
        }
        AppInsRepDto appInsRepDto=insRepClient.getAppInsRepDto(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationViewDto.getApplicationDto().getId());
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(appInsRepDto.getLicenseeId());
        try{
            licenseeDto.setEmilAddr(IaisEGPHelper.getLicenseeEmailAddrs(appInsRepDto.getLicenseeId()).get(0));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =  appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return;
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        if(appSvcRelatedInfoDto != null){
            String serviceId = appSvcRelatedInfoDto.getServiceId();
            hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        if( allocationDto !=null && allocationDto.size()>0 ){
            for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){

                appSvcDisciplineAllocationDto.setPremiseVal(applicationViewDto.getHciAddress());
                List<AppSvcChckListDto> appSvcChckListDtoList = null;
                String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                //set chkLstName
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                    for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
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
            }
        }

        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationType()}).get(0).getText();
        applicationViewDto.setApplicationType(appType);
        applicationViewDto.getApplicationDto().setApplicationType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
        List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos=applicationClient.getAppSvcPremisesScopeListByCorreId(appCorrId).getEntity();
        for (AppSvcPremisesScopeDto a:appSvcPremisesScopeDtos
             ) {
            a.setScopeName(hcsaConfigClient.getHcsaServiceSubTypeById(a.getScopeName()).getEntity().getSubtypeName());
        }
        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtos=organizationClient.getLicenseeKeyApptPersonDtoListByLicenseeId(licenseeDto.getId()).getEntity();
        ParamUtil.setRequestAttr(request,"licenseeKeyApptPersonDtos",licenseeKeyApptPersonDtos);
        ParamUtil.setRequestAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setRequestAttr(request,"appSvcRelatedInfoDto",appSvcRelatedInfoDto);
        ParamUtil.setRequestAttr(request,"licenseeDto",licenseeDto);
        ParamUtil.setRequestAttr(request,"serviceStep",appSvcPremisesScopeDtos);
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
    @Override
    public List<SelectOption> getServicePersonnelRoleOption() {
        List<SelectOption> svcPerRoleOption=IaisCommonUtils.genNewArrayList(4);
        SelectOption selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        svcPerRoleOption.add(selectOption);
        selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        svcPerRoleOption.add(selectOption);
        selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        svcPerRoleOption.add(selectOption);
        selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        svcPerRoleOption.add(selectOption);
        return svcPerRoleOption;
    }

    @Override
    public List<String> getLicIdsByappIds(List<String> appIds) {
        List<String> licIds =IaisCommonUtils.genNewArrayList();
        List<LicAppCorrelationDto> appCorrelationDtoList=IaisCommonUtils.genNewArrayList();
        for (String appId:appIds
        ) {
            List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrByappId(appId).getEntity();
            if(licAppCorrelationDtos.size()>0){
                appCorrelationDtoList.addAll(licAppCorrelationDtos);
            }
        }
        for (LicAppCorrelationDto licAppCorrelationDto:appCorrelationDtoList
        ) {
            licIds.add(licAppCorrelationDto.getLicenceId());
        }
        HashSet<String> set = new HashSet<>(licIds);
        licIds.clear();
        licIds.addAll(set);
        return licIds;
    }
}
