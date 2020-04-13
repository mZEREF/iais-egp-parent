package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComplianceHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRectifiedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.EnquiryInspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
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
import java.util.Map;

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
    private TaskOrganizationClient taskOrganizationClient;


    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    ApplicationViewService applicationViewService;


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
        List<PersonnelsDto> personnelsDto= hcsaLicenceClient.getPersonnelDtoByLicId(licenceId).getEntity();

        for (LicenseeKeyApptPersonDto org:organizationLicDto.getLicenseeKeyApptPersonDtos()
        ) {
            org.setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{org.getSalutation()}).get(0).getText());
        }
        for (PersonnelsDto per:personnelsDto
        ) {
            try{
                per.getKeyPersonnelDto().setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelDto().getSalutation()}).get(0).getText());
                per.getKeyPersonnelDto().setDesignation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelDto().getDesignation()}).get(0).getText());
                per.getKeyPersonnelExtDto().setProfessionType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelExtDto().getProfessionType()}).get(0).getText());

            }catch (NullPointerException e){
                log.info(e.getMessage());
            }
        }

        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceId).getEntity();

        List<ComplianceHistoryDto> complianceHistoryDtos= IaisCommonUtils.genNewArrayList();
        for(LicAppCorrelationDto licAppCorrelationDto : licAppCorrelationDtos){
            ComplianceHistoryDto complianceHistoryDto=new ComplianceHistoryDto();
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(licAppCorrelationDto.getApplicationId()).getEntity();
            ApplicationDto applicationDto=applicationClient.getApplicationById(licAppCorrelationDto.getApplicationId()).getEntity();
            ApplicationGroupDto applicationGroupDto=applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
            complianceHistoryDto.setInspectionTypeName(applicationGroupDto.getIsPreInspection() == 0? "Post":"Pre");
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
                complianceHistoryDto.setRiskTag(adhocChecklistItemDtos.get(0).getRiskLvl());
            }catch (Exception e){
                log.info(e.getMessage());
            }
            try{
                List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = fillUpCheckListGetAppClient.getAppPremisesRecommendationHistoryDtosByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                Calendar c = Calendar.getInstance();
                for (AppPremisesRecommendationDto appPremisesRecommendationDto:appPremisesRecommendationDtos
                ) {
                    c.setTime(appPremisesRecommendationDto.getRecomInDate());
                    complianceHistoryDto.setInspectionDateYear(c.get(Calendar.YEAR));
                    complianceHistoryDtos.add(complianceHistoryDto);
                }

            }catch (Exception e){
                log.info(e.getMessage());
                // complianceHistoryDtos.add(complianceHistoryDto);
            }
        }

        ParamUtil.setSessionAttr(request,"complianceHistoryDtos", (Serializable) complianceHistoryDtos);
        ParamUtil.setSessionAttr(request,"organizationLicDto",organizationLicDto);
        ParamUtil.setSessionAttr(request,"personnelsDto", (Serializable) personnelsDto);
    }

    @Override
    public EnquiryInspectionReportDto getInsRepDto(ApplicationViewDto applicationViewDto) {
        EnquiryInspectionReportDto inspectionReportDto = new EnquiryInspectionReportDto();
        //inspection report application dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String applicationNo = applicationDto.getApplicationNo();
        String appGrpId = applicationDto.getAppGrpId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String status = applicationDto.getStatus();
        String appTypeCode = insRepClient.getAppType(appId).getEntity();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(appInsRepDto.getLicenseeId()).getEntity();
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


        List<String> inspectors = taskOrganizationClient.getInspectorByAppCorrId(appPremisesCorrelationId).getEntity();
        List<OrgUserDto> inspectorsNames = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
        List<String> nameList = IaisCommonUtils.genNewArrayList();
        for(OrgUserDto orgUserDto :inspectorsNames){
            nameList.add(orgUserDto.getDisplayName());
        }
        inspectionReportDto.setInspectOffices(nameList);


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
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "";
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcName = hcsaServiceDto.getSvcName();
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }

        List<HcsaSvcSubtypeOrSubsumedDto> subsumedDtos = hcsaConfigClient.listSubCorrelationFooReport(serviceId).getEntity();
        List<String> subsumedServices = IaisCommonUtils.genNewArrayList();
        for(HcsaSvcSubtypeOrSubsumedDto subsumedDto :subsumedDtos){
            subsumedServices.add(subsumedDto.getName());
        }
        inspectionReportDto.setSubsumedServices(subsumedServices);
        //Nc
        List<ChecklistQuestionDto> listChecklistQuestionDtos = hcsaChklClient.getcheckListQuestionDtoList(svcCode, "Inspection").getEntity();
        List<ReportNcRegulationDto> listReportNcRegulationDto = IaisCommonUtils.genNewArrayList();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = IaisCommonUtils.genNewArrayList();
        //add ReportNcRegulationDto and add ncItemId
        if (listChecklistQuestionDtos != null && !listChecklistQuestionDtos.isEmpty()) {
            String configId = listChecklistQuestionDtos.get(0).getConfigId();
            List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationId);
            if (ncAnswerDtoList != null && !ncAnswerDtoList.isEmpty()) {
                for (NcAnswerDto ncAnswerDto : ncAnswerDtoList) {
                    ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
                    reportNcRegulationDto.setNc(ncAnswerDto.getItemQuestion());
                    reportNcRegulationDto.setRegulation(ncAnswerDto.getClause());
                    listReportNcRegulationDto.add(reportNcRegulationDto);
                }
                inspectionReportDto.setStatus("Partial Compliance");
                inspectionReportDto.setNcRegulation(listReportNcRegulationDto);

            }
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
        AppPremisesRecommendationDto NcRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_TCU).getEntity();
        if(NcRecommendationDto==null){
            inspectionReportDto.setMarkedForAudit("No");
        }else if(NcRecommendationDto!=null&&NcRecommendationDto.getRecomInDate()!=null) {
            inspectionReportDto.setMarkedForAudit("Yes");
            Date recomInDate = NcRecommendationDto.getRecomInDate();
            inspectionReportDto.setTcuDate(recomInDate);
        }
        //checkList

        List<InspectionFillCheckListDto> cDtoList = getInspectionFillCheckListDtoListForReview(applicationViewDto.getAppPremisesCorrelationId(),"service");
        List<InspectionFillCheckListDto> commonList =getInspectionFillCheckListDtoListForReview(applicationViewDto.getAppPremisesCorrelationId(),"common");
        InspectionFillCheckListDto commonDto = null;
        if(commonList!=null && !commonList.isEmpty()){
            commonDto = commonList.get(0);
        }
        InspectionFDtosDto subType = new InspectionFDtosDto();
        subType.setFdtoList(cDtoList);
        inspectionReportDto.setCommonCheckList(commonDto);
        inspectionReportDto.setSubTypeCheckList(subType);
        inspectionReportDto.setRectifiedWithinKPI("Yes");
        Date inspectionDate = null;
        Date inspectionStartTime = null;
        Date inspectionEndTime = null;
        AppPremisesRecommendationDto appPreRecommentdationDtoStart = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
        AppPremisesRecommendationDto appPreRecommentdationDtoDate = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        AppPremisesRecommendationDto appPreRecommentdationDtoEnd = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
        if(appPreRecommentdationDtoDate!=null){
            inspectionDate = appPreRecommentdationDtoDate.getRecomInDate();
        }
        if(appPreRecommentdationDtoStart!=null){
            inspectionStartTime = appPreRecommentdationDtoStart.getRecomInDate();
        }
        if(appPreRecommentdationDtoEnd!=null){
            inspectionEndTime = appPreRecommentdationDtoEnd.getRecomInDate();
        }
        String bestPractice = null;
        String remarks = null;
        if (NcRecommendationDto != null) {
            bestPractice = NcRecommendationDto.getBestPractice();
            remarks = NcRecommendationDto.getRemarks();
        }
        AdCheckListShowDto adhocCheckListDto = insepctionNcCheckListService.getAdhocCheckListDto(appPremisesCorrelationId);
        if(adhocCheckListDto!=null){
            inspectionReportDto.setOtherCheckList(adhocCheckListDto);
        }


        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciCode(appInsRepDto.getHciCode());
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
        inspectionReportDto.setInspectionDate(inspectionDate);
//        inspectionReportDto.setInspectionStartTime(inspectionStartTime.toString());
//        inspectionReportDto.setInspectionEndTime(inspectionEndTime.toString());
        inspectionReportDto.setBestPractice(bestPractice);
        inspectionReportDto.setTaskRemarks(remarks);
        inspectionReportDto.setCurrentStatus(status);
        return inspectionReportDto;
    }
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoListForReview(String appCorrId, String service) {
        List<InspectionFillCheckListDto> fillCheckDtoList = getInspectionFillCheckListDtoList(appCorrId,service);
        if(fillCheckDtoList!=null&&!fillCheckDtoList.isEmpty()){
            for(InspectionFillCheckListDto temp:fillCheckDtoList){
                AppPremisesPreInspectChklDto appPremPreCklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appCorrId,temp.getConfigId()).getEntity();
                insepctionNcCheckListService.getCommonDto(temp,appPremPreCklDto);
            }
        }
        return fillCheckDtoList;
    }
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String appCorrId,String conifgType) {
        List<InspectionFillCheckListDto> fillChkDtoList = null;

        if(appCorrId!=null){
            List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appCorrId).getEntity();
            if(chkList!=null && !chkList.isEmpty()){
                fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appCorrId,conifgType);
            }
        }
        return fillChkDtoList;
    }
    private List<InspectionFillCheckListDto> getServiceChkDtoListByAppPremId(List<AppPremisesPreInspectChklDto> chkList,String appPremCorrId,String conifgType) {
        List<InspectionFillCheckListDto> chkDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesPreInspectChklDto temp:chkList){
            String configId  = temp.getChkLstConfId();
            ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(configId).getEntity();
            InspectionFillCheckListDto fDto =null;
            if("common".equals(conifgType)&&dto.isCommon()){
                fDto = transferToInspectionCheckListDto(dto,appPremCorrId);
                fDto.setConfigId(temp.getChkLstConfId());
                chkDtoList.add(fDto);
            }else if("service".equals(conifgType)&&!dto.isCommon()){
                fDto = transferToInspectionCheckListDto(dto,appPremCorrId);
                if(!StringUtil.isEmpty(dto.getSvcName())){
                    fDto.setSvcName(dto.getSvcName());
                }
                fDto.setConfigId(temp.getChkLstConfId());
                fDto.setSvcCode(dto.getSvcCode());
                if(dto.getSvcSubType()!=null){
                    fDto.setSubName(dto.getSvcSubType().replace(" ",""));
                    fDto.setSubType(dto.getSvcSubType());
                }else{
                    fDto.setSubName(dto.getSvcCode());
                }
                chkDtoList.add(fDto);
            }
        }
        return chkDtoList;
    }

    public InspectionFillCheckListDto transferToInspectionCheckListDto(ChecklistConfigDto commonCheckListDto, String appPremCorrId) {
        InspectionFillCheckListDto dto = new InspectionFillCheckListDto();
        List<ChecklistSectionDto> sectionDtos = commonCheckListDto.getSectionDtos();
        List<InspectionCheckQuestionDto> checkList = IaisCommonUtils.genNewArrayList();
        InspectionCheckQuestionDto inquest = null;
        if(sectionDtos!=null && !sectionDtos.isEmpty()){
            for(ChecklistSectionDto temp:sectionDtos){
                for(ChecklistItemDto item: temp.getChecklistItemDtos()){
                    inquest= new InspectionCheckQuestionDto();
                    inquest.setItemId(item.getItemId());
                    inquest.setAppPreCorreId(appPremCorrId);
                    inquest.setSectionName(temp.getSection());
                    inquest.setConfigId(temp.getConfigId());
                    inquest.setRegClauseNo(item.getRegulationClauseNo());
                    inquest.setRegClause(item.getRegulationClause());
                    if(temp.getSection()!=null){
                        inquest.setSectionNameSub(temp.getSection().replace(" ",""));
                    }
                    inquest.setChecklistItem(item.getChecklistItem());
                    checkList.add(inquest);
                }
            }
        }
        dto.setCheckList(checkList);
        if(checkList!=null && !checkList.isEmpty()){
            List<InspectionCheckQuestionDto> cqDtoList = IaisCommonUtils.genNewArrayList();
            for(ChecklistQuestionDto temp:checkList){
                InspectionCheckQuestionDto inspectionCheckQuestionDto = null;
                inspectionCheckQuestionDto = transferQuestionDtotoInDto(temp);
                inspectionCheckQuestionDto.setAppPreCorreId(appPremCorrId);
                cqDtoList.add(inspectionCheckQuestionDto);
            }
            dto.setCheckList(checkList);
            fillInspectionFillCheckListDto(dto);
            return dto;
        }
        return dto;
    }
    public InspectionFillCheckListDto fillInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto){
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        List<SectionDto> sectionDtoList = IaisCommonUtils.genNewArrayList();
        for(InspectionCheckQuestionDto temp:iqdDtolist){
            SectionDto sectionDto = new SectionDto();
            sectionDto.setSectionName(temp.getSectionName());
            if(isHaveSameSection(temp.getSectionName(),sectionDtoList)){
                sectionDtoList.add(sectionDto);
            }
        }
        infillCheckListDto.setSectionDtoList(sectionDtoList);
        itemDto(infillCheckListDto);
        return infillCheckListDto;
    }
    public boolean isHaveSameSection(String sectionName,List<SectionDto> sectionDtoList){
        if(sectionDtoList!=null && !sectionDtoList.isEmpty()){
            for(SectionDto temp:sectionDtoList){
                if(temp.getSectionName().equals(sectionName)){
                    return false;
                }
            }
        }
        return true;
    }
    public InspectionFillCheckListDto itemDto(InspectionFillCheckListDto infillCheckListDto){
        List<SectionDto> sectionDtoList = infillCheckListDto.getSectionDtoList();
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        for(SectionDto temp:sectionDtoList){
            List<ItemDto> itemDtoList = IaisCommonUtils.genNewArrayList();
            for(InspectionCheckQuestionDto iq:iqdDtolist){
                ItemDto itemDto = new ItemDto();
                if(temp.getSectionName().equals(iq.getSectionName())){
                    itemDto.setItemId(iq.getItemId());
                    itemDtoList.add(itemDto);
                }
            }
            temp.setItemDtoList(itemDtoList);
        }
        getItemCheckListDto(infillCheckListDto);
        return infillCheckListDto;
    }
    public InspectionFillCheckListDto getItemCheckListDto(InspectionFillCheckListDto infillCheckListDto){
        List<SectionDto> sectionDtoList = infillCheckListDto.getSectionDtoList();
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        for(InspectionCheckQuestionDto temp:iqdDtolist){
            for(SectionDto section :sectionDtoList){
                if(temp.getSectionName().equals(section.getSectionName())){
                    List<ItemDto> itemDtoList = section.getItemDtoList();
                    for(ItemDto itemDto :itemDtoList){
                        if(itemDto.getItemId().equals(temp.getItemId())){
                            itemDto.setIncqDto(temp);
                        }
                    }
                }
            }
        }
        return infillCheckListDto;
    }
    public InspectionCheckQuestionDto transferQuestionDtotoInDto(ChecklistQuestionDto cdto){
        InspectionCheckQuestionDto icDto = new InspectionCheckQuestionDto();
        icDto.setAnswerType(cdto.getAnswerType());
        icDto.setAnswer(cdto.getAnswer());
        icDto.setChecklistItem(cdto.getChecklistItem());
        icDto.setCommon(cdto.getCommon());
        icDto.setConfigId(cdto.getConfigId());
        icDto.setHciCode(cdto.getHciCode());
        icDto.setId(cdto.getId());
        icDto.setItemId(cdto.getItemId());
        icDto.setModule(cdto.getModule());
        icDto.setSvcName(cdto.getSvcName());
        icDto.setSvcType(cdto.getSvcType());
        icDto.setRegClause(cdto.getRegClause());
        icDto.setRegClauseNo(cdto.getRegClauseNo());
        icDto.setRiskLvl(cdto.getRiskLvl());
        icDto.setSecOrder(cdto.getSecOrder());
        icDto.setSectionDesc(cdto.getSectionDesc());
        icDto.setSectionName(cdto.getSectionName());
        icDto.setSubTypeName(cdto.getSubTypeName());
        icDto.setSvcCode(cdto.getSvcCode());
        icDto.setSvcId(cdto.getSvcId());
        icDto.setRectified(false);
        return icDto;
    }
    @Override
    public void preInspReport(HttpServletRequest request) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>requestForInformation");
        String appPremCorrId = ParamUtil.getMaskedString(request, IaisEGPConstant.CRUD_ACTION_VALUE);

        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appPremCorrId);
        EnquiryInspectionReportDto insRepDto = getInsRepDto(applicationViewDto);
        ParamUtil.setSessionAttr(request, "insRepDto", insRepDto);
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        ParamUtil.setSessionAttr(request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        // 		preAppInfo->OnStepProcess
    }

    @Override
    public void setAppInfo(HttpServletRequest request) {
        String appCorrId = (String) ParamUtil.getSessionAttr(request, "id");
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(appCorrId);
        AppInsRepDto appInsRepDto=insRepClient.getAppInsRepDto(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationViewDto.getApplicationDto().getId());
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(appInsRepDto.getLicenseeId());
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =  appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto=null;
        if(oldAppSubmissionDto!=null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto,request);
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        String serviceId = appSvcRelatedInfoDto.getServiceId();
        hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
        allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
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
        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationType()}).get(0).getText();
        applicationViewDto.setApplicationType(appType);
        applicationViewDto.getApplicationDto().setApplicationType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos=hcsaConfigClient.getServiceStepsByServiceIds(new ArrayList<>(Collections.singleton(applicationViewDto.getApplicationDto().getServiceId()))).getEntity();
        for (HcsaServiceStepSchemeDto h :hcsaServiceStepSchemeDtos
        ) {
            h.setStepCode(MasterCodeUtil.retrieveOptionsByCodes(new String[]{h.getStepCode()}).get(0).getText());
        }
        ParamUtil.setSessionAttr(request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationList);
        ParamUtil.setRequestAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setRequestAttr(request,"authorisedPersonList",appInsRepDto.getPrincipalOfficer());
        ParamUtil.setRequestAttr(request,"licenseeDto",licenseeDto);
        ParamUtil.setRequestAttr(request,"serviceStep",hcsaServiceStepSchemeDtos);
        // 		preAppInfo->OnStepProcess
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
                                    HcsaSvcSubtypeOrSubsumedDto  hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
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
        String[] personnelRoles={"DES001","DES002","DES003","DES004"};
        return MasterCodeUtil.retrieveOptionsByCodes(personnelRoles);
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
