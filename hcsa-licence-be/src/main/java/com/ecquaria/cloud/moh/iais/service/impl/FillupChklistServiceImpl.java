package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocSaveAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AnswerForDifDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSpecServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.BeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.ChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FillupChklistServiceImpl implements FillupChklistService {
    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private  AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;
    @Autowired
    private  InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private  ApplicationService applicationService;
    @Autowired
    private InspectionTaskClient inspectionTaskClient;
    @Autowired
    private AdhocChecklistService adhocChecklistService;
    @Autowired
    private AppointmentClient appointmentClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Override
    public ApplicationViewDto getAppViewDto(String taskId){
        TaskDto taskDto = taskService.getTaskById(taskId);
        String refNo = taskDto.getRefNo();
        ApplicationViewDto viewDto = applicationViewService.getApplicationViewDtoByCorrId(refNo);
        return viewDto;
    }

    @Override
    public InspectionFillCheckListDto getInspectionFillCheckListDto(String taskId,String svcType) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        String serviceCode = null;
        if(taskDto!=null){
            appPremCorrId = taskDto.getRefNo();
            ApplicationViewDto appViewDto = applicationClient.getAppViewByCorrelationId(appPremCorrId).getEntity();
            String svcId = appViewDto.getApplicationDto().getServiceId();
            HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
            serviceCode = svcDto.getSvcCode();
        }
        List<ChecklistQuestionDto> cDtoList = hcsaChklClient.getcheckListQuestionDtoList(serviceCode,"Inspection").getEntity();
        InspectionFillCheckListDto infillCheckListDto = new InspectionFillCheckListDto();
        if(cDtoList!=null && !cDtoList.isEmpty()){
            List<InspectionCheckQuestionDto> cqDtoList = IaisCommonUtils.genNewArrayList();
            for(ChecklistQuestionDto temp:cDtoList){
                InspectionCheckQuestionDto inspectionCheckQuestionDto  = transferQuestionDtotoInDto(temp);
                inspectionCheckQuestionDto.setAppPreCorreId(appPremCorrId);
                cqDtoList.add(inspectionCheckQuestionDto);
            }
            infillCheckListDto.setCheckList(cqDtoList);
            fillInspectionFillCheckListDto(infillCheckListDto);
            return infillCheckListDto;
        }
        return infillCheckListDto;
    }
    @Override
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
    public void saveDto(InspectionFillCheckListDto dto) {
        boolean ncflag = isHaveNc(dto);
        List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
        List<InspectionCheckListAnswerDto> answerDtoList = IaisCommonUtils.genNewArrayList();
        InspectionCheckListAnswerDto answerDto = null;

        String appPremCorrId = icqDtoList.get(0).getAppPreCorreId();
        String configId = icqDtoList.get(0).getConfigId();

        AppPremisesRecommendationDto appPreRecommentdationDto = new AppPremisesRecommendationDto();
        appPreRecommentdationDto.setAppPremCorreId(appPremCorrId);
        appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
        Date tcuDate = null;
        try {
            tcuDate = Formatter.parseDate(dto.getTuc());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        if(tcuDate!=null){
            appPreRecommentdationDto.setRecomInDate(tcuDate);
        }
        appPreRecommentdationDto.setAppPremCorreId(appPremCorrId);
        appPreRecommentdationDto.setBestPractice(dto.getBestPractice());
        appPreRecommentdationDto.setRemarks(dto.getTcuRemark());
        appPreRecommentdationDto.setStatus("CMSTAT001");
        appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
        appPreRecommentdationDto.setVersion(1);
        appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        try {
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = null;
            if(ncflag){
                AppPremPreInspectionNcDto appPremPreInspectionNcDto = getAppPremPreInspectionNcDto(dto);
                appPremPreInspectionNcDto = fillUpCheckListGetAppClient.saveAppPreNc(appPremPreInspectionNcDto).getEntity();
                appPremisesPreInspectionNcItemDtoList = getAppPremisesPreInspectionNcItemDto(dto,appPremPreInspectionNcDto);
                appPremisesPreInspectionNcItemDtoList = fillUpCheckListGetAppClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtoList).getEntity();
            }
            int j=0;
            for (InspectionCheckQuestionDto inspectionCheckQuestionDto : icqDtoList) {
                answerDto = new InspectionCheckListAnswerDto();
                answerDto.setAnswer(inspectionCheckQuestionDto.getChkanswer());
                answerDto.setRemark(inspectionCheckQuestionDto.getRemark());
                answerDto.setItemId(inspectionCheckQuestionDto.getItemId());
                if ( ncflag&&"No".equals(inspectionCheckQuestionDto.getChkanswer()) ) {
                    answerDto.setIsRec(appPremisesPreInspectionNcItemDtoList.get(j).getIsRecitfied() + "");
                    j++;
                }
                answerDto.setSectionName(inspectionCheckQuestionDto.getSectionName());
                answerDtoList.add(answerDto);
            }
            String answerJson = JsonUtil.parseToJson(answerDtoList);
            //AppPremisesPreInspectChklDto appDto = new AppPremisesPreInspectChklDto();
            AppPremisesPreInspectChklDto appDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(dto.getCheckList().get(0).getAppPreCorreId(),dto.getCheckList().get(0).getConfigId()).getEntity();
            if(appDto==null){
                appDto = new AppPremisesPreInspectChklDto();
            }
            appDto.setAnswer(answerJson);
            appDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
            appDto.setAppPremCorrId(appPremCorrId);
            appDto.setVersion(1+"");
            appDto.setChkLstConfId(configId);
            appDto.setStatus("CMSTAT001");
            appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
            fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    public List<AppPremisesPreInspectionNcItemDto> getAppPremisesPreInspectionNcItemDto(InspectionFillCheckListDto dto,AppPremPreInspectionNcDto ncDto){
        List<InspectionCheckQuestionDto> insqDtoList =  dto.getCheckList();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = IaisCommonUtils.genNewArrayList();
        for(InspectionCheckQuestionDto temp:insqDtoList){
            if("No".equals(temp.getChkanswer())){
                AppPremisesPreInspectionNcItemDto ncItemDto = null;
                ncItemDto = new AppPremisesPreInspectionNcItemDto();
                ncItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                ncItemDto.setItemId(temp.getItemId());
                ncItemDto.setPreNcId(ncDto.getId());
                if(temp.isRectified()){
                    ncItemDto.setIsRecitfied(1);
                }else{
                    ncItemDto.setIsRecitfied(0);
                }
                ncItemDtoList.add(ncItemDto);
            }
        }
        return ncItemDtoList;
    }
    public AppPremPreInspectionNcDto getAppPremPreInspectionNcDto(InspectionFillCheckListDto dto){
        AppPremPreInspectionNcDto ncDto = new AppPremPreInspectionNcDto();
        ncDto.setStatus("CMSTAT001");
        ncDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        ncDto.setVersion(1+"");
        ncDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        ncDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return ncDto;
    }
    @Override
    public TaskDto getTaskDtoById(String taskId){
        return organizationClient.getTaskById(taskId).getEntity();
    }

    @Override
    public ChecklistConfigDto getcommonCheckListDto(String configType, String module) {
        ChecklistConfigDto commonckDto = hcsaChklClient.getMaxVersionCommonConfig().getEntity();
        return commonckDto ;
    }

    @Override
    public void merge(InspectionFillCheckListDto comDto, InspectionFillCheckListDto icDto) {
        List<InspectionCheckQuestionDto> comDtoList = comDto.getCheckList();
        List<InspectionCheckQuestionDto> icDtoList = icDto.getCheckList();
        List<InspectionCheckQuestionDto> mergeList = IaisCommonUtils.genNewArrayList();
        if(icDtoList!=null && !icDtoList.isEmpty()){
            for(InspectionCheckQuestionDto temp:icDtoList){
                mergeList.add(temp);
            }
        }
        if(comDtoList!=null && !comDtoList.isEmpty()){
            for(InspectionCheckQuestionDto temp:comDtoList){
                mergeList.add(temp);
            }
        }
        icDto.setCheckList(mergeList);
    }

    @Override
    public InspectionFillCheckListDto transferToInspectionCheckListDto(ChecklistConfigDto commonCheckListDto, String appPremCorrId) {
        InspectionFillCheckListDto dto = new InspectionFillCheckListDto();
        List<ChecklistSectionDto> sectionDtos = commonCheckListDto.getSectionDtos();
        List<InspectionCheckQuestionDto> checkList = IaisCommonUtils.genNewArrayList();
        InspectionCheckQuestionDto inquest;
        if(sectionDtos!=null && !sectionDtos.isEmpty()){
            for(ChecklistSectionDto temp:sectionDtos){
                for(ChecklistItemDto item: temp.getChecklistItemDtos()){
                    inquest= new InspectionCheckQuestionDto();
                    inquest.setItemId(item.getItemId());
                    inquest.setAppPreCorreId(appPremCorrId);
                    inquest.setSectionName(temp.getSection());
                    inquest.setSectionId(temp.getId());
                    inquest.setConfigId(temp.getConfigId());
                    inquest.setRegClauseNo(item.getRegulationClauseNo());
                    inquest.setRegClause(item.getRegulationClause());;
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
                InspectionCheckQuestionDto inspectionCheckQuestionDto = transferQuestionDtotoInDto(temp);
                inspectionCheckQuestionDto.setAppPreCorreId(appPremCorrId);
                cqDtoList.add(inspectionCheckQuestionDto);
            }
            dto.setCheckList(checkList);
            fillInspectionFillCheckListDto(dto);
            return dto;
        }
        return dto;
    }
    @Override
    public AdCheckListShowDto getAdhoc(String appremCorrId){
        List<AdhocChecklistItemDto> adhocItemList = applicationClient.getAdhocByAppPremCorrId(appremCorrId).getEntity();
        AdCheckListShowDto adShowDto = new AdCheckListShowDto();
        List<AdhocNcCheckItemDto> adhocNcCheckItemDtoList = IaisCommonUtils.genNewArrayList();
        if(adhocItemList!=null&&!adhocItemList.isEmpty()){
            for(AdhocChecklistItemDto temp:adhocItemList){
                AdhocNcCheckItemDto addto = transfertoadNcItemDto(temp);
                addto.setRectified(temp.getRectified());
                adhocNcCheckItemDtoList.add(addto);
            }
            adShowDto.setAdItemList(adhocNcCheckItemDtoList);
        }
        return adShowDto;
    }

    public AdhocNcCheckItemDto transfertoadNcItemDto(AdhocChecklistItemDto dto){
        AdhocNcCheckItemDto adto  = new AdhocNcCheckItemDto();
        adto.setId(dto.getId());
        adto.setAnswer(dto.getAnswer());
        adto.setAnswerType(dto.getAnswerType());
        adto.setNonCompliant(dto.getNonCompliant());
        adto.setOrder(dto.getOrder());
        adto.setQuestion(dto.getQuestion());
        adto.setRectified(dto.getRectified());
        adto.setRiskLvl(dto.getRiskLvl());
        adto.setItemId(dto.getItemId());
        return adto;
    }
    @Override
    public void saveAdhocDto(AdCheckListShowDto showDto,String appPremId){
        List<AdhocNcCheckItemDto>  itemDtoList = showDto.getAdItemList();
        List<AdhocChecklistItemDto> saveItemDtoList = IaisCommonUtils.genNewArrayList();
        AdhocCheckListConifgDto dto = applicationClient.getAdhocConfigByAppPremCorrId(appPremId).getEntity();
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if(itemDtoList!=null && !itemDtoList.isEmpty()){
            dto.setVersion(dto.getVersion()+1);
            dto.setId(null);
            dto = applicationClient.saveAppAdhocConfig(dto).getEntity();
            for(AdhocNcCheckItemDto temp:itemDtoList){
                temp.setAdhocConfId(dto.getId());
                temp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                temp.setId(null);
                AdhocAnswerDto adhocAnswerDto = new AdhocAnswerDto();
                adhocAnswerDto.setRemark(temp.getRemark());
                adhocAnswerDto.setAnswer(temp.getAdAnswer());
                String saveAnswer = JsonUtil.parseToJson(adhocAnswerDto);
                temp.setAnswer(saveAnswer);
                saveItemDtoList.add(temp);
            }
            applicationClient.saveAdhocItems(saveItemDtoList).getEntity();
        }
    }

    @Override
    public void saveDraft( InspectionFillCheckListDto comDto, AdCheckListShowDto adDto,InspectionFDtosDto serListDto,String refNo) {
        //create new appckl
        if(comDto!=null){
            saveCommDraft(comDto,refNo);
        }

        if(serListDto!=null){
            saveSerListDtoDraft(serListDto,refNo);
        }

        if(adDto!=null){
            saveAdhocDraft(adDto,refNo);
        }
        /*List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(refNo).getEntity();
        if(!IaisCommonUtils.isEmpty(chkList)){
            for(AppPremisesPreInspectChklDto temp:chkList){
                String id = temp.getId();
                String conifgId = temp.getChkLstConfId();

            }
        }*/
        /*CheckListDraftDto checkListDraftDto = new CheckListDraftDto();
        //checkListDraftDto.setGeneralDto(icDto);
        checkListDraftDto.setComDto(comDto);
        String insDraft =JsonUtil.parseToJson(checkListDraftDto);
        AppPremInsDraftDto insDraftDto = new AppPremInsDraftDto();
        insDraftDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        insDraftDto.setAnswer(insDraft);
        AppPremisesPreInspectChklDto appDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(icDto.getCheckList().get(0).getAppPreCorreId(),icDto.getCheckList().get(0).getConfigId()).getEntity();
        if (appDto == null) {
            appDto = new AppPremisesPreInspectChklDto();
            appDto.setVersion(1+"");
            appDto.setStatus("CMSTAT001");
            appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appDto.setAppPremCorrId(icDto.getCheckList().get(0).getAppPreCorreId());
            appDto.setChkLstConfId(icDto.getCheckList().get(0).getConfigId());
            appDto = fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto).getEntity();
        }
        insDraftDto.setPreInsChklId(appDto.getId());
        //Date date = newDate()
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY,9);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        insDraftDto.setClockin(c.getTime());
        c.set(Calendar.HOUR_OF_DAY,17);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        insDraftDto.setClockout(c.getTime());
        List<AdhocNcCheckItemDto> adhocItemList = adDto.getAdItemList();
        List<AdhocDraftDto> adhocSaveList = IaisCommonUtils.genNewArrayList();
        if(adhocItemList!=null &&!adhocItemList.isEmpty()){
            AdhocDraftDto saveDto = null;
            for(AdhocNcCheckItemDto temp:adhocItemList){
                saveDto = new AdhocDraftDto();
                AdhocSaveAnswerDto anwAnswerDto = new AdhocSaveAnswerDto();
                anwAnswerDto.setAnswer(temp.getAdAnswer());
                anwAnswerDto.setRemark(temp.getRemark());
                if(temp.getRectified()){
                    anwAnswerDto.setIsRec(1);
                }else{
                    anwAnswerDto.setIsRec(0);
                }
                String answerStr = JsonUtil.parseToJson(anwAnswerDto);
                saveDto.setAdhocItemId(temp.getId());
                saveDto.setAnswer(answerStr);
                saveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                adhocSaveList.add(saveDto);
            }
        }
        fillUpCheckListGetAppClient.saveAppInsDraft(insDraftDto);
        fillUpCheckListGetAppClient.saveAdhocDraft(adhocSaveList);*/

    }



    private void saveAdhocDraft(AdCheckListShowDto adDto, String refNo) {
        if(adhocFillUpVad(adDto)) {
            List<AdhocNcCheckItemDto> adhocItemList = adDto.getAdItemList();
            List<AdhocDraftDto> adhocSaveList = IaisCommonUtils.genNewArrayList();
            if (adhocItemList != null && !adhocItemList.isEmpty()) {
                AdhocDraftDto saveDto;
                for (AdhocNcCheckItemDto temp : adhocItemList) {
                    saveDto = new AdhocDraftDto();
                    AdhocSaveAnswerDto anwAnswerDto = new AdhocSaveAnswerDto();
                    anwAnswerDto.setAnswer(temp.getAdAnswer());
                    anwAnswerDto.setRemark(temp.getRemark());
                    if (temp.getRectified()) {
                        anwAnswerDto.setIsRec(1);
                    } else {
                        anwAnswerDto.setIsRec(0);
                    }
                    String answerStr = JsonUtil.parseToJson(anwAnswerDto);
                    saveDto.setAdhocItemId(temp.getId());
                    saveDto.setAnswer(answerStr);
                    saveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    adhocSaveList.add(saveDto);
                }
                fillUpCheckListGetAppClient.saveAdhocDraft(adhocSaveList);
            }
        }
    }

    private void saveSerListDtoDraft(InspectionFDtosDto serListDto, String refNo) {
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                saveCommDraft(temp,refNo);
            }
        }
    }
    private boolean commFillUpVad(InspectionFillCheckListDto icDto) {
        if (icDto != null) {
            List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
            if(cqDtoList!=null && !cqDtoList.isEmpty()){
                for(InspectionCheckQuestionDto temp:cqDtoList){
                    if(!StringUtil.isEmpty(temp.getChkanswer())){
                        return true;
                    }
                }
            }
        }
        return false;
    }



    private boolean adhocFillUpVad(AdCheckListShowDto showDto) {
        if(showDto!=null){
            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    if(!StringUtil.isEmpty(temp.getAdAnswer())){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void saveCommDraft(InspectionFillCheckListDto comDto, String refNo) {
        if(commFillUpVad(comDto)) {
            if (!IaisCommonUtils.isEmpty(comDto.getCheckList())) {
                //update status
                AppPremisesPreInspectChklDto appChklDto = getAppChkIdBycConfigIdAndRefNo(comDto.getConfigId(), refNo);
                //create new appChkl
                appChklDto.setChkLstConfId(comDto.getConfigId());
                appChklDto.setAppPremCorrId(refNo);
                appChklDto.setVersion(Integer.parseInt(appChklDto.getVersion())+1+"");
                appChklDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appChklDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appChklDto.setId(null);
                String appChkId = fillUpCheckListGetAppClient.saveAppPreInspChkl(appChklDto).getEntity().getId();
                AppPremInsDraftDto insDraftDto = new AppPremInsDraftDto();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.set(Calendar.HOUR_OF_DAY, 9);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                insDraftDto.setClockin(c.getTime());
                c.set(Calendar.HOUR_OF_DAY, 17);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                insDraftDto.setClockout(c.getTime());
                String insDraft = JsonUtil.parseToJson(comDto);
                insDraftDto.setAnswer(insDraft);
                insDraftDto.setPreInsChklId(appChkId);
                insDraftDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                fillUpCheckListGetAppClient.saveAppInsDraft(insDraftDto);
            }
        }
    }

    private AppPremisesPreInspectChklDto getAppChkIdBycConfigIdAndRefNo(String configId, String refNo) {
        return fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(refNo,configId).getEntity();
    }

    @Override
    public CheckListDraftDto getDraftByTaskId(String taskId, String svcType) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        String serviceCode = null;
        if(taskDto!=null){
            String refNo = taskDto.getRefNo();
            appPremCorrId = taskDto.getRefNo();
            ApplicationViewDto appViewDto = applicationClient.getAppViewByCorrelationId(appPremCorrId).getEntity();
            String svcId = appViewDto.getApplicationDto().getServiceId();
            HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
            serviceCode = svcDto.getSvcCode();
        }
        List<ChecklistQuestionDto> cDtoList = hcsaChklClient.getcheckListQuestionDtoList(serviceCode,"Inspection").getEntity();
        CheckListDraftDto draft = null;
        if(cDtoList!=null){
            AppPremisesPreInspectChklDto chklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,cDtoList.get(0).getConfigId()).getEntity();
            if(chklDto!=null){
                AppPremInsDraftDto draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(chklDto.getId()).getEntity();
                if(draftDto!=null){
                    String answerStr = draftDto.getAnswer();
                    draft = JsonUtil.parseToObject(answerStr,CheckListDraftDto.class);
                }
            }
        }
        return draft;
    }

    @Override
    public AdCheckListShowDto getAdhocDraftByappCorrId(String appremCorrId) {
        AdCheckListShowDto adShowDto = getAdhoc(appremCorrId);
        if(adShowDto != null){
            List<AdhocNcCheckItemDto> adhocItemList = adShowDto.getAdItemList();
            List<String> itemIdList = IaisCommonUtils.genNewArrayList();
            if(adhocItemList!=null&&!adhocItemList.isEmpty()){
                for(AdhocNcCheckItemDto temp:adhocItemList){
                    itemIdList.add(temp.getId());
                }
            }else{
                return null;
            }
        }
        return adShowDto;
    }

    public boolean isHaveNc(InspectionFillCheckListDto dto){
        List<InspectionCheckQuestionDto> dtoList = dto.getCheckList();
        for(InspectionCheckQuestionDto temp:dtoList){
            if("No".equals(temp.getChkanswer())){
                return true;
            }
        }
        return false;
    }


    @Override
    public void routingTask(TaskDto taskDto, String preInspecRemarks, LoginContext loginContext, boolean flag) {
        if(flag){
            routingToNcEmail(taskDto,preInspecRemarks,loginContext); // false
        }else{
            routingForToReport(taskDto,preInspecRemarks,loginContext);// false
        }
        saveAppInspCorreByAppNo(taskDto.getApplicationNo());
    }

    private  void saveAppInspCorreByAppNo(String appNo){
        try{
            List<AppPremInspCorrelationDto> appPremInspCorrelationDtos =   inspectionTaskClient.getAppInspCorreByAppNoStatus(appNo,AppConsts.COMMON_STATUS_ACTIVE).getEntity();
           if( !IaisCommonUtils.isEmpty(appPremInspCorrelationDtos)){
               for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtos){
                   appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                   inspectionTaskClient.updateAppPremInspCorrelationDto(appPremInspCorrelationDto);
               }
           }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void routingToNcEmail(TaskDto taskDto, String preInspecRemarks,LoginContext loginContext){
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String svcId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
        String appType = applicationDto.getApplicationType();
        String stgId = taskDto.getTaskKey();
       /* List<TaskDto> dtos = organizationClient.getTasksByRefNo(taskDto.getRefNo()).getEntity();
        removeOtherTask(dtos,taskDto.getId());*/
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_EMAIL);
        HcsaSvcStageWorkingGroupDto dto = new HcsaSvcStageWorkingGroupDto();
        dto.setStageId(stgId);
        dto.setServiceId(svcId);
        dto.setOrder(1);
        dto.setType(appType);
        //call api to get workId
        dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(dto).getEntity();
        String workGrp = dto.getGroupId();
        String subStage = HcsaConsts.ROUTING_STAGE_POT;
        if(StringUtil.isEmpty(workGrp))  workGrp = taskDto.getWkGrpId();
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.PROCESS_DECI_PENDING_MYSELF_FOR_CHECKLIST_VERIFY, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        //create
        TaskDto updatedtaskDto = completedTask(taskDto);
        updatedtaskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        updatedtaskDto.setApplicationNo(applicationDto.getApplicationNo());
        updatedtaskDto.setId(null);
        updatedtaskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_NCEMAIL);
        updatedtaskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        updatedtaskDto.setSlaDateCompleted(null);
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0) {
            updatedtaskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        }
        createAppPremisesRoutingHistory(updateApplicationDto.getApplicationNo(),updateApplicationDto.getStatus(),taskDto.getTaskKey(),null, null, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createTaskDtoList.add(updatedtaskDto);
        taskService.createTasks(createTaskDtoList);
        updateInspectionStatus(applicationDto,InspectionConstants.INSPECTION_STATUS_PENDING_EMAIL_VERIFY);
    }

    public void routingForToReport(TaskDto taskDto, String preInspecRemarks,LoginContext loginContext){
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
       /* List<TaskDto> dtos = organizationClient.getTasksByRefNo(taskDto.getRefNo()).getEntity();
        removeOtherTask(dtos,taskDto.getId());*/
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT );
        updateInspectionStatus(applicationDto,InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        //create createAppPremisesRoutingHistory
        String svcId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
        String stgId = taskDto.getTaskKey();
        HcsaSvcStageWorkingGroupDto dto = new HcsaSvcStageWorkingGroupDto();
        dto.setType(updateApplicationDto.getApplicationType());
        dto.setStageId(stgId);
        dto.setServiceId(svcId);
        dto.setOrder(1);
        dto.setType(applicationDto.getApplicationType());
        dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(dto).getEntity();
        String workGrp = dto.getGroupId();
        String subStage = HcsaConsts.ROUTING_STAGE_POT;
        if(StringUtil.isEmpty(workGrp))  workGrp = taskDto.getWkGrpId();
        createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        //create task
        TaskDto updatedtaskDto = completedTask(taskDto);
        updatedtaskDto.setId(null);
        updatedtaskDto.setApplicationNo(applicationDto.getApplicationNo());
        updatedtaskDto.setUserId(loginContext.getUserId());
        updatedtaskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        updatedtaskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        updatedtaskDto.setSlaDateCompleted(null);
        updatedtaskDto.setRoleId( RoleConsts.USER_ROLE_INSPECTIOR);
        updatedtaskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0) {
            updatedtaskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        }
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createAppPremisesRoutingHistory(updateApplicationDto.getApplicationNo(),updateApplicationDto.getStatus(),taskDto.getTaskKey(),null, null, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        createTaskDtoList.add(updatedtaskDto);
        taskService.createTasks(createTaskDtoList);
    }

    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setUpdateCount(1);
        TaskDto updatedtaskDto = taskService.updateTask(taskDto);
        if(updatedtaskDto == null){
            updatedtaskDto = taskDto;
            updatedtaskDto.setUpdateCount(0);
        }else {
            updatedtaskDto.setUpdateCount(0);
        }
        return updatedtaskDto;
    }

    private ApplicationDto updateApplicaitonStatus(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationService.updateFEApplicaiton(applicationDto);
        return updateApplicaiton(applicationDto);
    }

    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return applicationClient.updateApplication(applicationDto).getEntity();
    }

  /*  private void removeOtherTask(List<TaskDto> dtos, String taskId) {
        if(!IaisCommonUtils.isEmpty(dtos)){
            for(TaskDto temp:dtos){
                if(removeOtherTaskLogic(temp,taskId)){
                    temp.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                    temp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    taskService.updateTask(temp);
                }
            }
        }
    }*/

    public boolean removeOtherTaskLogic(TaskDto temp,String taskId){
        if(!taskId.equals(temp.getId())&&TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY.equals(temp.getProcessUrl())&&!temp.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED)){
            return true;
        }else{
            return false;
        }
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if(appGrpPremisesEntityDto != null){
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            }else{
                log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :"+applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec,String role,String wrkGroupId,String subStageId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setRoleId(role);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setWorkingGroup(wrkGroupId);
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGroupId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    private void updateInspectionStatus(ApplicationDto applicationDto,String inspectionStatus) {
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationDtos.get(0).getId()).getEntity();
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusDto.setStatus(inspectionStatus);
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    @Override
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String taskId,String conifgType,boolean needVehicleSeparation) {
        List<InspectionFillCheckListDto> fillChkDtoList = null;
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = null;
        if(taskDto!=null){
            appPremCorrId = taskDto.getRefNo();
        }
        if(appPremCorrId!=null){
            List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
            if(chkList!=null && !chkList.isEmpty()){
                fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,conifgType,needVehicleSeparation);
            }
        }
        return fillChkDtoList;
    }

    private List<InspectionFillCheckListDto> getServiceChkDtoListByAppPremId(List<AppPremisesPreInspectChklDto> chkList,String appPremCorrId,String conifgType,boolean needVehicleSeparation) {
        List<InspectionFillCheckListDto> chkDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesPreInspectChklDto temp:chkList){
            String configId  = temp.getChkLstConfId();
            ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(configId).getEntity();
            InspectionFillCheckListDto fDto;
            if("common".equals(conifgType)&&dto.isCommon()){
                fDto = transferToInspectionCheckListDto(dto,appPremCorrId);
                fDto.setConfigId(temp.getChkLstConfId());
                fDto.setPreCheckId(temp.getId());
                chkDtoList.add(fDto);
            }else if("service".equals(conifgType)&&!dto.isCommon()){
                if(!((!needVehicleSeparation && StringUtil.isEmpty(dto.getInspectionEntity()))||(needVehicleSeparation && HcsaChecklistConstants.INSPECTION_ENTITY_VEHICLE.equalsIgnoreCase(dto.getInspectionEntity())))){
                    continue;
                }
                fDto = transferToInspectionCheckListDto(dto,appPremCorrId);
                if(StringUtil.isNotEmpty(temp.getAnswer())){
                    List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(temp.getAnswer(), InspectionCheckListAnswerDto.class);
                    if(IaisCommonUtils.isNotEmpty(answerDtoList) ){
                        for(InspectionCheckListAnswerDto inspectionCheckListAnswerDto : answerDtoList){
                            String vehicleName = inspectionCheckListAnswerDto.getIdentify();
                            if(StringUtil.isNotEmpty( vehicleName)){
                                fDto.setVehicleName(vehicleName);
                                break;
                            }
                        }
                    }
                }
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
                fDto.setPreCheckId(temp.getId());
                chkDtoList.add(fDto);
            }
        }
        return chkDtoList;
    }

    @Override
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoListForReview(String taskId, String service,boolean needVehicleSeparation) {
        List<InspectionFillCheckListDto> fillCheckDtoList = getInspectionFillCheckListDtoList(taskId,service,needVehicleSeparation);
        if(fillCheckDtoList!=null&&!fillCheckDtoList.isEmpty()){
            for(InspectionFillCheckListDto temp:fillCheckDtoList){
                AppPremisesPreInspectChklDto appPremPreCklDto =  getAppPremChklDtoByTaskIdAndVehicleName(taskId,temp.getConfigId(),temp.getVehicleName());
                if(appPremPreCklDto != null){
                    insepctionNcCheckListService.getCommonDto(temp,appPremPreCklDto);
                }
            }
        }
        return fillCheckDtoList;
    }

    private AppPremisesPreInspectChklDto getAppPremChklDtoByTaskIdAndVehicleName(String taskId,String configId,String vehicleName){
        if(StringUtil.isNotEmpty(vehicleName)){
            List<AppPremisesPreInspectChklDto> appPremisesPreInspectChklDtos =  getAppPremChklDtsByTaskId(taskId, configId);
            return getAppPremisesPreInspectChklDtoByVehicleName( vehicleName,appPremisesPreInspectChklDtos);
        }else {
          return   insepctionNcCheckListService.getAppPremChklDtoByTaskId(taskId,configId);
        }
    }

    private List<AppPremisesPreInspectChklDto> getAppPremChklDtsByTaskId(String taskId, String configId){
        if (StringUtil.isEmpty(taskId)) {
            log.info("-----------getAppPremChklDtoByTaskId TASKID is null -------------------------- ");
            return null;
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = null;
        if (taskDto != null) {
            appPremCorrId = taskDto.getRefNo();
        }
        List<AppPremisesPreInspectChklDto> appPremisesPreInspectChklDto = fillUpCheckListGetAppClient.getPremInsChkls(appPremCorrId,configId).getEntity();
        return appPremisesPreInspectChklDto;
    }

    @Override
    public AppPremisesPreInspectChklDto getAppPremChklDtoByCorrIdAndVehicleName(String corrId, String configId, String vehicleName) {
        if(StringUtil.isNotEmpty(vehicleName)){
            List<AppPremisesPreInspectChklDto> appPremisesPreInspectChklDtos = fillUpCheckListGetAppClient.getPremInsChkls(corrId,configId).getEntity();
            return getAppPremisesPreInspectChklDtoByVehicleNameAndMaxVersion( vehicleName,appPremisesPreInspectChklDtos);
        } else {
            return  fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(corrId,configId).getEntity();
        }
    }

    private  AppPremisesPreInspectChklDto getAppPremisesPreInspectChklDtoByVehicleNameAndMaxVersion(String vehicleName, List<AppPremisesPreInspectChklDto> appPremisesPreInspectChklDtos){
        if(IaisCommonUtils.isNotEmpty(appPremisesPreInspectChklDtos)){
            AppPremisesPreInspectChklDto appPremisesPreInspectChklDtoOriginalVersion = null;
            int maxVersion = 1;
            for(AppPremisesPreInspectChklDto appPremisesPreInspectChklDto : appPremisesPreInspectChklDtos){
                if(StringUtil.isNotEmpty(appPremisesPreInspectChklDto.getAnswer())){
                    List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(appPremisesPreInspectChklDto.getAnswer(), InspectionCheckListAnswerDto.class);
                    if(IaisCommonUtils.isNotEmpty(answerDtoList) && vehicleName.equalsIgnoreCase(answerDtoList.get(0).getIdentify())){
                        return appPremisesPreInspectChklDto;
                    }
                    int nowVerison = StringUtil.isNotEmpty(appPremisesPreInspectChklDto.getVersion()) ? Integer.parseInt(appPremisesPreInspectChklDto.getVersion()) : 1;
                    maxVersion =  nowVerison > maxVersion ? nowVerison : maxVersion;
                }else {
                    appPremisesPreInspectChklDtoOriginalVersion = appPremisesPreInspectChklDto;
                }
            }
            if(appPremisesPreInspectChklDtoOriginalVersion != null){
                return  appPremisesPreInspectChklDtoOriginalVersion;
            }
            appPremisesPreInspectChklDtoOriginalVersion = new AppPremisesPreInspectChklDto();
            appPremisesPreInspectChklDtoOriginalVersion.setVersion(String.valueOf(maxVersion-1));
            return appPremisesPreInspectChklDtoOriginalVersion;
        }
        return null;
    }

    private  AppPremisesPreInspectChklDto getAppPremisesPreInspectChklDtoByVehicleName(String vehicleName, List<AppPremisesPreInspectChklDto> appPremisesPreInspectChklDtos){
        if(IaisCommonUtils.isNotEmpty(appPremisesPreInspectChklDtos)){
            AppPremisesPreInspectChklDto appPremisesPreInspectChklDtoOriginalVersion = null;
            for(AppPremisesPreInspectChklDto appPremisesPreInspectChklDto : appPremisesPreInspectChklDtos){
                if(StringUtil.isNotEmpty(appPremisesPreInspectChklDto.getAnswer())){
                    List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(appPremisesPreInspectChklDto.getAnswer(), InspectionCheckListAnswerDto.class);
                    if(IaisCommonUtils.isNotEmpty(answerDtoList) && vehicleName.equalsIgnoreCase(answerDtoList.get(0).getIdentify())){
                        return appPremisesPreInspectChklDto;
                    }
                }else {
                    appPremisesPreInspectChklDtoOriginalVersion = appPremisesPreInspectChklDto;
                }
            }
            return  appPremisesPreInspectChklDtoOriginalVersion;
        }
        return null;
    }

    @Override
    public void getTcuInfo(InspectionFDtosDto serListDto, String appPremCorrId) {
        AppPremisesRecommendationDto dto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId,InspectionConstants.RECOM_TYPE_TCU).getEntity();
        try {
            serListDto.setTuc(Formatter.formatDate(dto.getRecomInDate()));
        }catch (Exception e){
            log.debug("date formatter error");
        }
        if( dto != null){
            serListDto.setBestPractice(dto.getBestPractice());
            serListDto.setTcuRemark(dto.getRemarks());
        }
        if(! StringUtil.isEmpty(serListDto.getTuc())){
            serListDto.setTcuFlag(true);
        }
    }

    @Override
    public String getInspectionDate(String appPremCorrId) {
        AppPremisesRecommendationDto dto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        String inspectionDate = null;
        if(dto!=null){
            try {
                inspectionDate = Formatter.formatDate(dto.getRecomInDate());
            }catch (Exception e){
                log.debug(e.toString());
            }
        }
        return inspectionDate;
    }

    @Override
    public String getStringByRecomType(String appPremCorrId,String recomType) {
        AppPremisesRecommendationDto dto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId,recomType).getEntity();
        if(InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS.equalsIgnoreCase(recomType))
        return  dto == null ? null : dto.getRemarks();
        if(InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME.equalsIgnoreCase(recomType) || InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME.equalsIgnoreCase(recomType))
            return  dto == null ? null : dto.getRecomDecision();
        return null;
    }

    @Override
    public List<String> getInspectiors(TaskDto taskDto) {
        List<String> inspectiors = IaisCommonUtils.genNewArrayList();
        List<TaskDto> taskDtos  = organizationClient.getCurrTaskByRefNo(taskDto.getRefNo()).getEntity();
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            List<String> userIds = new ArrayList<>(taskDtos.size());
            for(TaskDto taskDto1 : taskDtos){
                if( !userIds.contains(taskDto1.getUserId()))
                    userIds.add(taskDto1.getUserId());
            }
            List< OrgUserDto> orgDtos =  organizationClient.retrieveOrgUserAccount( userIds).getEntity();
            if(!IaisCommonUtils.isEmpty(orgDtos)){
                for(OrgUserDto temp:orgDtos){
                    inspectiors.add(temp.getDisplayName());
                }
            }

        }
        return inspectiors;
    }

    @Override
    public String getInspectionLeader(TaskDto taskDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType( taskDto.getRefNo(),InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
        if(appPremisesRecommendationDto != null){
            return appPremisesRecommendationDto.getRecomDecision().replace(",",", ");
        }
        List<TaskDto> taskDtos  = organizationClient.getTaskByAppNoStatus(taskDto.getApplicationNo(),TaskConsts.TASK_STATUS_COMPLETED,TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
        if( taskDtos  != null && taskDtos.size() >0){
             StringBuilder stringBuilder = new StringBuilder();
             List<String> userNames = IaisCommonUtils.genNewArrayList();
            for(TaskDto taskDto1 : taskDtos){
                List<String> leaders =  organizationClient.getInspectionLead( taskDto1.getWkGrpId()).getEntity();
                if(!IaisCommonUtils.isEmpty(leaders)){
                    for(String temp:leaders){
                        OrgUserDto userDto = organizationClient.retrieveOrgUserAccountById(temp).getEntity();
                        if(userDto.getAvailable() != null && userDto.getAvailable() && ! userNames.contains(userDto.getDisplayName())){
                            userNames.add(userDto.getDisplayName());
                        }
                    }
                }
            }

            if(!IaisCommonUtils.isEmpty(userNames) && userNames.size() >1){
                userNames.sort(String::compareTo);
                for(String userName :  userNames){
                    stringBuilder.append(userName).append(", ");
                }
            }

            String leaderStr = stringBuilder.toString();
            return StringUtil.isEmpty(leaderStr) ? "" : leaderStr.substring(0,leaderStr.length()-2);
        }
         return  "";
    }

    @Override
    public void getRateOfCheckList(InspectionFDtosDto serListDto, AdCheckListShowDto adchklDto, InspectionFillCheckListDto commonDto) {
        if(serListDto == null) return;
        if(serListDto.getFdtoList()!=null){
            getServiceTotalAndNc(serListDto);
        }
        if(commonDto!=null){
            getGeneralTotalAndNc(commonDto,serListDto);
        }
        if(adchklDto!=null&&!IaisCommonUtils.isEmpty(adchklDto.getAdItemList())){
            getAdhocTotalAndNc(adchklDto,serListDto);
        }
        int totalNcNum = serListDto.getGeneralNc()+serListDto.getServiceNc()+serListDto.getAdhocNc();

        serListDto.setTotalNcNum(totalNcNum);
    }

    @Override
    public void getRateOfSpecCheckList(List<InspectionSpecServiceDto> inspectionSpecServiceDtos, InspectionFillCheckListDto commonDto,InspectionFDtosDto serListDto, AdCheckListShowDto adchklDto) {
        getRateOfCheckList(serListDto,adchklDto,commonDto);
        if(serListDto == null) return;
        int totalNcNum = serListDto.getTotalNcNum();
       if(IaisCommonUtils.isNotEmpty(inspectionSpecServiceDtos)){
            for(InspectionSpecServiceDto inspectionSpecServiceDto : inspectionSpecServiceDtos){
                InspectionFDtosDto inspectionFDtosDto = MiscUtil.transferEntityDto(inspectionSpecServiceDto,InspectionFDtosDto.class);
                if(IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getFdtoList())){
                    inspectionFDtosDto.setFdtoList(inspectionSpecServiceDto.getFdtoList());
                    getServiceTotalAndNc(inspectionFDtosDto);
                    totalNcNum += inspectionFDtosDto.getServiceNc();
                }
                if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
                    getAdhocTotalAndNc(inspectionSpecServiceDto.getAdchklDto(),inspectionFDtosDto);
                    totalNcNum += inspectionFDtosDto.getAdhocNc();
                }
                setInspectionSpecServiceDtoByInspectionFDtosDto(inspectionSpecServiceDto,inspectionFDtosDto);
            }
       }
        serListDto.setTotalNcNum(totalNcNum);
    }

    private void setInspectionSpecServiceDtoByInspectionFDtosDto(InspectionSpecServiceDto inspectionSpecServiceDto,InspectionFDtosDto inspectionFDtosDto ){
        inspectionSpecServiceDto.setAdhocDo(inspectionFDtosDto.getAdhocDo());
        inspectionSpecServiceDto.setAdhocNc(inspectionFDtosDto.getAdhocNc());
        inspectionSpecServiceDto.setAdhocTotal(inspectionFDtosDto.getAdhocTotal());
        inspectionSpecServiceDto.setServiceDo(inspectionFDtosDto.getServiceDo());
        inspectionSpecServiceDto.setServiceNc(inspectionFDtosDto.getServiceNc());
        inspectionSpecServiceDto.setServiceTotal(inspectionFDtosDto.getServiceTotal());
    }
    private void getAdhocTotalAndNc(AdCheckListShowDto adchklDto, InspectionFDtosDto serListDto) {
        int totalNum = 0;
        int ncNum = 0;
        int doNum = 0;
        for(AdhocNcCheckItemDto aditem : adchklDto.getAdItemList()){
            totalNum++;
            if(!StringUtil.isEmpty(aditem.getAdAnswer())){
                if( !"Yes".equalsIgnoreCase(aditem.getAdAnswer())){
                    if(!StringUtil.isEmpty(aditem.getRemark())){
                        doNum++;
                        if("No".equals(aditem.getAdAnswer())){
                            ncNum++;
                        }
                    }
                }else {
                    doNum++;
                }
            }
        }
        serListDto.setAdhocTotal(totalNum);
        serListDto.setAdhocNc(ncNum);
        serListDto.setAdhocDo(doNum);
    }

    @Override
    public void getSvcName(InspectionFDtosDto serListDto) {
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                if(!StringUtil.isEmpty(temp.getSvcName())){
                    serListDto.setServiceName(temp.getSvcName());
                }
            }
        }
    }

    private void getGeneralTotalAndNc(InspectionFillCheckListDto commonDto, InspectionFDtosDto serListDto) {
        int totalNum = 0;
        int ncNum = 0;
        int doNum = 0;
        for(InspectionCheckQuestionDto cqDto : commonDto.getCheckList()){
            totalNum++;
            if(!StringUtil.isEmpty(cqDto.getChkanswer())){
                if( !"Yes".equalsIgnoreCase(cqDto.getChkanswer())){
                    if(!StringUtil.isEmpty(cqDto.getRemark())){
                        doNum++;
                        if("No".equals(cqDto.getChkanswer())){
                            ncNum++;
                        }
                    }
                }else {
                    doNum++;
                }
            }
        }
        serListDto.setGeneralTotal(totalNum);
        serListDto.setGeneralDo(doNum);
        serListDto.setGeneralNc(ncNum);
    }

    private void getServiceTotalAndNc(InspectionFDtosDto serListDto) {
        List<InspectionFillCheckListDto> dtoList = serListDto.getFdtoList();
        int totalNum = 0;
        int doNum = 0;
        int ncNum = 0;
        for(InspectionFillCheckListDto temp:dtoList){
            if(!IaisCommonUtils.isEmpty(temp.getCheckList())){
                for(InspectionCheckQuestionDto cqDto : temp.getCheckList()){
                    totalNum++;
                    if(!StringUtil.isEmpty(cqDto.getChkanswer())){
                        if( !"Yes".equalsIgnoreCase(cqDto.getChkanswer())){
                            if(!StringUtil.isEmpty(cqDto.getRemark())){
                                doNum++;
                                if("No".equals(cqDto.getChkanswer())){
                                    ncNum++;
                                }
                            }
                        }else {
                            doNum++;
                        }

                    }
                }
            }
        }
        serListDto.setServiceDo(doNum);
        serListDto.setServiceTotal(totalNum);
        serListDto.setServiceNc(ncNum);
    }





    private List<InspectionFillCheckListDto> getOtherVersionCommList(List<AppPremisesPreInspectChklDto> chkList,InspectionFillCheckListDto maxVersionChkl, String appPremCorrId) {
        List<InspectionFillCheckListDto> otherVersionChkList = IaisCommonUtils.genNewArrayList();

        if(!IaisCommonUtils.isEmpty(chkList)){
            String maxVersion = chkList.get(0).getVersion();
            String comFigId = maxVersionChkl.getConfigId();
            try {
                int versionNum = Integer.parseInt(maxVersion);
                for(int i = versionNum-2;i>0;i--){
                    List<AppPremisesPreInspectChklDto> versionCkList =  fillUpCheckListGetAppClient.getPremInsChklListByPremIdAndVersion(appPremCorrId,versionNum+"").getEntity();
                    List<InspectionFillCheckListDto> otherverList = getOtherVersionChkList(versionCkList,comFigId);
                    if(!IaisCommonUtils.isEmpty(otherverList)){
                        otherVersionChkList.add(otherverList.get(0));
                    }
                }
            }catch (Exception e){
                log.info("Integer formatter error");
            }
        }
        return otherVersionChkList;
    }

    private List<InspectionFillCheckListDto> getOtherVersionChkList(List<AppPremisesPreInspectChklDto> versionCkList, String comConfigId) {
        InspectionFillCheckListDto comDto;
        AppPremInsDraftDto draftDto;
        List<InspectionFillCheckListDto> comDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(versionCkList)){
            for(AppPremisesPreInspectChklDto temp:versionCkList){
                if(temp.getChkLstConfId().equals(comConfigId)){
                    draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(temp.getId()).getEntity();
                    if(draftDto!=null && !StringUtil.isEmpty(draftDto.getAnswer())){
                        comDto = JsonUtil.parseToObject(draftDto.getAnswer(),InspectionFillCheckListDto.class);
                        comDtoList.add(comDto);
                    }
                }
            }
        }
        return comDtoList;
    }

    @Override
    public  InspectionFDtosDto  getInspectionFDtosDto(String appPremCorrId,TaskDto taskDto,List<InspectionFillCheckListDto> cDtoList){
        InspectionFDtosDto serListDto = new InspectionFDtosDto();
        String inspectionDate = getInspectionDate(appPremCorrId);
        List<String> inspeciotnOfficers  = getInspectiors(taskDto);
        String inspectionleader =  getInspectionLeader(taskDto);
        serListDto.setInspectionDate(inspectionDate);
        serListDto.setInspectionofficer(inspeciotnOfficers);
        serListDto.setInspectionLeader(inspectionleader);
        serListDto.setFdtoList(cDtoList);

        AppPremisesSpecialDocDto appPremisesSpecialDocDto = getAppPremisesSpecialDocDtoByRefNo(taskDto.getRefNo());
       if(appPremisesSpecialDocDto != null){
           serListDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto );
           serListDto.setOldFileGuid(appPremisesSpecialDocDto.getFileRepoId());
           serListDto.setCopyAppPremisesSpecialDocDto(getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(appPremisesSpecialDocDto));
       }
        String startDate = getStringByRecomType(appPremCorrId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME);
        String endDate = getStringByRecomType(appPremCorrId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME);
        String otherinspectionofficer = getStringByRecomType(appPremCorrId,InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS);
        String observation = getObservationByAppPremCorrId(appPremCorrId);
        serListDto.setOtherinspectionofficer(otherinspectionofficer);
        serListDto.setStartTime(startDate);
        serListDto.setEndTime(endDate);
        serListDto.setObservation(observation);
       String [] startDateHHMM = getStringsByHHDD(startDate);
       if(startDateHHMM != null && startDateHHMM.length == 2){
           serListDto.setStartHour(startDateHHMM[0]);
           Iterator<String> iterator= Arrays.stream(startDateHHMM).iterator();
           iterator.next();
           if(iterator.hasNext()){
               serListDto.setStartMin(iterator.next());
           }
       }
        String [] endDateHHMM = getStringsByHHDD(endDate);
        if(endDateHHMM != null && endDateHHMM.length == 2){
            serListDto.setEndHour(endDateHHMM[0]);
            Iterator<String> iterator= Arrays.stream(endDateHHMM).iterator();
            iterator.next();
            if(iterator.hasNext()){
                serListDto.setEndMin(iterator.next());
            }
        }
        if( !StringUtil.isEmpty(appPremCorrId)){
          getTcuInfo(serListDto,appPremCorrId);
        }
        getSvcName(serListDto);
        return serListDto;
    }

    @Override
    public String getObservationByAppPremCorrId(String appPremCorrId) {
        String observation = "";
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType
                (appPremCorrId, InspectionConstants.RECOM_TYPE_INSP_CHECKLIST_OBSERVATION).getEntity();
        if(appPremisesRecommendationDto != null) {
            observation = appPremisesRecommendationDto.getRemarks();
        }
        return observation;
    }

    @Override
    public AppPremisesSpecialDocDto getAppPremisesSpecialDocDtoByRefNo(String RefNo){
       AppIntranetDocDto appIntranetDocDto = fillUpCheckListGetAppClient.getAppIntranetDocByPremIdAndStatusAndAppDocType(RefNo,AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.APP_DOC_TYPE_CHECK_LIST).getEntity();
       if(StringUtil.isEmpty(appIntranetDocDto.getId()))
           return null;
        AppPremisesSpecialDocDto appPremisesSpecialDocDto =  new AppPremisesSpecialDocDto();
        appPremisesSpecialDocDto.setId(appIntranetDocDto.getId());
        appPremisesSpecialDocDto.setAppPremCorreId(appIntranetDocDto.getAppPremCorrId());
        appPremisesSpecialDocDto.setSubmitBy(appIntranetDocDto.getSubmitBy());
        appPremisesSpecialDocDto.setSubmitDt(appIntranetDocDto.getSubmitDt());
        appPremisesSpecialDocDto.setDocSize(Integer.valueOf(appIntranetDocDto.getDocSize()));
        appPremisesSpecialDocDto.setFileRepoId(appIntranetDocDto.getFileRepoId());
        appPremisesSpecialDocDto.setDocName(appIntranetDocDto.getDocName()+"."+appIntranetDocDto.getDocType());
        return appPremisesSpecialDocDto;
    }

    @Override
    public  AppIntranetDocDto getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(AppPremisesSpecialDocDto appPremisesSpecialDocDto){
        AppIntranetDocDto copyAppPremisesSpecialDocDto = new AppIntranetDocDto();
        String[] docNameSpec = appPremisesSpecialDocDto.getDocName().split("[.]");
        if(docNameSpec.length == 2){
            copyAppPremisesSpecialDocDto.setDocName(docNameSpec[0]);
            copyAppPremisesSpecialDocDto.setDocDesc(docNameSpec[0]);
            copyAppPremisesSpecialDocDto.setDocType(docNameSpec[1]);
        } else {
            return null;
        }
        copyAppPremisesSpecialDocDto.setId(appPremisesSpecialDocDto.getId());
        copyAppPremisesSpecialDocDto.setFileRepoId(appPremisesSpecialDocDto.getFileRepoId());
        copyAppPremisesSpecialDocDto.setDocSize(appPremisesSpecialDocDto.getDocSize()+"KB");
        OrgUserDto user = applicationViewService.getUserById(appPremisesSpecialDocDto.getSubmitBy());
        copyAppPremisesSpecialDocDto.setSubmitByName(user.getDisplayName());
        copyAppPremisesSpecialDocDto.setSubmitDtString(Formatter.formatDateTime(appPremisesSpecialDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
        return  copyAppPremisesSpecialDocDto;
    }


    // only HH : DD
    public  String[]  getStringsByHHDD(String dateString){
        return StringUtil.isEmpty(dateString) ? null : dateString.split(" : ");
    }
    @Override
    public  InspectionFDtosDto getInspectionFDtosDtoOnlyForChecklistLetter(String refNo){
        InspectionFDtosDto serListDto = new InspectionFDtosDto();
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = getAppPremisesSpecialDocDtoByRefNo(refNo);
        if(appPremisesSpecialDocDto != null){
            serListDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto );
            serListDto.setOldFileGuid(appPremisesSpecialDocDto.getFileRepoId());
            serListDto.setCopyAppPremisesSpecialDocDto(getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(appPremisesSpecialDocDto));
        }
        return serListDto;
    }


    @Override
    public InspectionFillCheckListDto getInspectionFillCheckListDtoByInspectionFillCheckListDto(InspectionFillCheckListDto inspectionFillCheckListDto, List<OrgUserDto> orgUserDtos){
        if(inspectionFillCheckListDto == null){
            return inspectionFillCheckListDto;
        }
        Map<String, String> draftRemarkMaps = IaisCommonUtils.genNewHashMap();
        inspectionFillCheckListDto.setDraftRemarkMaps(draftRemarkMaps);
        int userNum = orgUserDtos.size();
        if(userNum > 1){
            inspectionFillCheckListDto.setMoreOneDraft(true);
        }

        List<InspectionCheckQuestionDto>  inspectionCheckQuestionDtos = inspectionFillCheckListDto.getCheckList();
        if(IaisCommonUtils.isEmpty( inspectionCheckQuestionDtos)){
            return inspectionFillCheckListDto;
        }

        for(InspectionCheckQuestionDto inspectionCheckQuestionDto : inspectionCheckQuestionDtos){
            List<AnswerForDifDto> answerForDifDtos = new ArrayList<>(userNum);
            Map<String, AnswerForDifDto> answerForDifDtoMaps = Maps.newHashMapWithExpectedSize(userNum);
            for (OrgUserDto entry : orgUserDtos) {
                AnswerForDifDto answerForDifDto = new AnswerForDifDto();
                answerForDifDto.setSubmitId(entry.getId());
                answerForDifDto.setSubmitName(entry.getUserId());
                answerForDifDtos.add(answerForDifDto);
                answerForDifDtoMaps.put(entry.getId(),answerForDifDto);
            }
            inspectionCheckQuestionDto.setAnswerForDifDtos(answerForDifDtos);
            inspectionCheckQuestionDto.setAnswerForDifDtoMaps(answerForDifDtoMaps);
        }

        List<AppPremInsDraftDto> appPremInsDraftDtos = getAppPremInsDraftDtos(inspectionFillCheckListDto);
        if(IaisCommonUtils.isEmpty(appPremInsDraftDtos)){
            return inspectionFillCheckListDto;
        }else {
            setDraftRemarkMaps(draftRemarkMaps,appPremInsDraftDtos);
            inspectionFillCheckListDto.setStringInspectionCheckQuestionDtoMap( getStringInspectionCheckQuestionDtoMapByList( inspectionCheckQuestionDtos));
            inspectionFillCheckListDto.setOtherInspectionOfficer(getOtherOffs(appPremInsDraftDtos));
            List<InspectionCheckListAnswerDto> answerDtos = getInspectionCheckListAnswerDtosByAppPremInsDraftDtos(appPremInsDraftDtos,inspectionFillCheckListDto.getVehicleName());
            for(InspectionCheckQuestionDto inspectionCheckQuestionDto :  inspectionCheckQuestionDtos ){
                 List<InspectionCheckListAnswerDto> answerDtosOne = new ArrayList<>(1);
                  for(InspectionCheckListAnswerDto inspectionCheckListAnswerDto : answerDtos){
                     if(inspectionCheckQuestionDto.getItemId().equalsIgnoreCase(inspectionCheckListAnswerDto.getItemId())){
                         answerDtosOne.add(inspectionCheckListAnswerDto);
                     }
                  }
                if(IaisCommonUtils.isEmpty(answerDtosOne)){
                    log.info(StringUtil.changeForLog(" inspectionCheckQuestionDto id is " + inspectionCheckQuestionDto.getId() +" no draft."));
                }else {
                    setDraftAnswerByAnswerDtosOne(answerDtosOne,inspectionCheckQuestionDto.getAnswerForDifDtoMaps());
                    if(userNum == 1 && answerDtosOne .size() == 1){
                        getInspectionCheckQuestionDtoByInspectionCheckQuestionDto(inspectionCheckQuestionDto,answerDtosOne.get(0));
                    }else if(userNum == appPremInsDraftDtos .size()){
                        List<AnswerForDifDto> answerForDifDtos =  inspectionCheckQuestionDto.getAnswerForDifDtos();
                        AnswerForDifDto  answerForSame =  getAnswerForDifDtoByAnswerForDifDtos(answerForDifDtos);
                        inspectionCheckQuestionDto.setSameAnswer(answerForSame.isSameAnswer());
                        getInspectionCheckQuestionDtoByAnswerForDifDto(inspectionCheckQuestionDto, answerForSame);
                    }
                }

              }
        }

        return inspectionFillCheckListDto;
    }

    private   List<AppPremInsDraftDto> getAppPremInsDraftDtos(InspectionFillCheckListDto inspectionFillCheckListDto){
        List<AppPremInsDraftDto> appPremInsDraftDtos =   fillUpCheckListGetAppClient.getInspDraftAnswer(getCheckListIdsByInspectionCheckQuestionDtos(inspectionFillCheckListDto.getPreCheckId())).getEntity();
        if(IaisCommonUtils.isNotEmpty(appPremInsDraftDtos) && StringUtil.isNotEmpty(inspectionFillCheckListDto.getVehicleName())){
            List<AppPremInsDraftDto> appPremInsDraftDtosOneVeh = IaisCommonUtils.genNewArrayList();
            for(AppPremInsDraftDto appPremInsDraftDto : appPremInsDraftDtos){
                if(!StringUtil.isEmpty(appPremInsDraftDto.getAnswer())){
                    List<InspectionCheckListAnswerDto> inspectionCheckListAnswerDto = JsonUtil.parseToList(appPremInsDraftDto.getAnswer(),InspectionCheckListAnswerDto.class);
                    if(IaisCommonUtils.isNotEmpty(inspectionCheckListAnswerDto) && inspectionFillCheckListDto.getVehicleName().equalsIgnoreCase( inspectionCheckListAnswerDto.get(0).getIdentify())){
                        appPremInsDraftDtosOneVeh.add(appPremInsDraftDto);
                    }
                }
            }
            return appPremInsDraftDtosOneVeh;
        }
        return appPremInsDraftDtos;
    }
    private  void setDraftRemarkMaps( Map<String, String> draftRemarkMaps, List<AppPremInsDraftDto> appPremInsDraftDtos){
        for(AppPremInsDraftDto appPremInsDraftDto : appPremInsDraftDtos){
            if(!StringUtil.isEmpty(appPremInsDraftDto.getRemarks())){
                draftRemarkMaps.put(appPremInsDraftDto.getCreatedBy(),appPremInsDraftDto.getRemarks());
            }
        }
    }

    private  void  setDraftAnswerByAnswerDtosOne( List<InspectionCheckListAnswerDto> answerDtosOne ,  Map<String, AnswerForDifDto> answerForDifDtoMaps ){
        for (Map.Entry<String, AnswerForDifDto> entry : answerForDifDtoMaps.entrySet()) {
            AnswerForDifDto answerForDifDto = entry.getValue();
            for(InspectionCheckListAnswerDto inspectionCheckListAnswerDto : answerDtosOne){
                if( entry.getKey().equalsIgnoreCase(inspectionCheckListAnswerDto.getSubBy())){
                    answerForDifDto.setRemark(inspectionCheckListAnswerDto.getRemark());
                    answerForDifDto.setAnswer(inspectionCheckListAnswerDto.getAnswer());
                    answerForDifDto.setIsRec(inspectionCheckListAnswerDto.getIsRec());
                    answerForDifDto.setNcs(inspectionCheckListAnswerDto.getNcs());
                    break;
                }
            }
        }
    }

    @Override
    public String getOtherOffGropByInspectionFillCheckListDtos(List<InspectionFillCheckListDto> inspectionFillCheckListDtos){
        if( !IaisCommonUtils.isEmpty(inspectionFillCheckListDtos)){
            List<String> otherInspectionOfficers = new ArrayList<>(inspectionFillCheckListDtos.size());
            for(InspectionFillCheckListDto inspectionFillCheckListDto : inspectionFillCheckListDtos){
                if( !IaisCommonUtils.isEmpty(inspectionFillCheckListDto.getOtherInspectionOfficer())){
                    otherInspectionOfficers.addAll(inspectionFillCheckListDto.getOtherInspectionOfficer());
                }
            }
            if( !IaisCommonUtils.isEmpty(otherInspectionOfficers)){
                StringBuffer stringBuffer = new StringBuffer();
                for(String otherOff : otherInspectionOfficers){
                     String otherOffs =  stringBuffer.toString();
                    if(StringUtil.isEmpty(otherOffs)){
                        stringBuffer.append(otherOff);
                    }else if((!StringUtil.isEmpty(otherOffs) && !otherOffs.contains(otherOff) )){
                        stringBuffer.append(',');
                        stringBuffer.append(otherOff);
                    }
                }
                return stringBuffer.toString();
            }
        }
        return null;
    }
    private  List<String> getOtherOffs(List<AppPremInsDraftDto> appPremInsDraftDtos ){
        List<String> otherOffs = new ArrayList<>(appPremInsDraftDtos.size());
        for(AppPremInsDraftDto appPremInsDraftDto : appPremInsDraftDtos){
            String otherOff = appPremInsDraftDto.getOtherInspectors();
            if( !StringUtil.isEmpty(otherOff) && !otherOffs.contains(otherOff)){
                otherOffs.add(otherOff);
           }
        }
        return  otherOffs;
    }
    private  List<InspectionCheckListAnswerDto>  getInspectionCheckListAnswerDtosByAppPremInsDraftDtos(List<AppPremInsDraftDto> appPremInsDraftDtos,String identify){
        List<InspectionCheckListAnswerDto> answerDtos = new ArrayList<>(3);
        for(AppPremInsDraftDto appPremInsDraftDto : appPremInsDraftDtos){
            if( !StringUtil.isEmpty (appPremInsDraftDto.getAnswer())){
                List<InspectionCheckListAnswerDto> inspectionCheckListAnswerDto = JsonUtil.parseToList(appPremInsDraftDto.getAnswer(),InspectionCheckListAnswerDto.class);
                if(StringUtil.isEmpty(identify)){
                    for(InspectionCheckListAnswerDto a :  inspectionCheckListAnswerDto){
                        a.setSubBy(appPremInsDraftDto.getCreatedBy());
                    }
                    answerDtos.addAll(inspectionCheckListAnswerDto);
                }else {
                    //set spec check list
                    for(InspectionCheckListAnswerDto a :  inspectionCheckListAnswerDto){
                        if(identify.equalsIgnoreCase(a.getIdentify())){
                            a.setSubBy(appPremInsDraftDto.getCreatedBy());
                            answerDtos.add(a);
                        }
                    }
                }
            }
        }
        return   answerDtos;
    }

    private   List<String> getCheckListIdsByInspectionCheckQuestionDtos( String  preCheId){
        List<String> checkListIds =  new ArrayList<>(1);
            checkListIds.add(preCheId);
        return checkListIds;
    }
    private Map<String,InspectionCheckQuestionDto> getStringInspectionCheckQuestionDtoMapByList(List<InspectionCheckQuestionDto>  inspectionCheckQuestionDtos){
        Map<String,InspectionCheckQuestionDto> map = Maps.newHashMapWithExpectedSize(inspectionCheckQuestionDtos.size());
        for(InspectionCheckQuestionDto inspectionCheckQuestionDto : inspectionCheckQuestionDtos){
            map.put(inspectionCheckQuestionDto.getItemId(),inspectionCheckQuestionDto);
        }
        return map;
    }

    private InspectionCheckQuestionDto getInspectionCheckQuestionDtoByAnswerForDifDto(InspectionCheckQuestionDto inspectionCheckQuestionDto,AnswerForDifDto answerForDifDto){
        inspectionCheckQuestionDto.setRemark(answerForDifDto.getRemark());
        inspectionCheckQuestionDto.setChkanswer(answerForDifDto.getAnswer());
        inspectionCheckQuestionDto.setRectified("1".equalsIgnoreCase(answerForDifDto.getIsRec()));
        inspectionCheckQuestionDto.setNcs(answerForDifDto.getNcs());
        return  inspectionCheckQuestionDto;
    }

    private InspectionCheckQuestionDto getInspectionCheckQuestionDtoByInspectionCheckQuestionDto(InspectionCheckQuestionDto inspectionCheckQuestionDto,InspectionCheckListAnswerDto inspectionCheckListAnswerDto){
        inspectionCheckQuestionDto.setRemark(inspectionCheckListAnswerDto.getRemark());
        inspectionCheckQuestionDto.setChkanswer(inspectionCheckListAnswerDto.getAnswer());
        inspectionCheckQuestionDto.setRectified("1".equalsIgnoreCase(inspectionCheckListAnswerDto.getIsRec()));
        inspectionCheckQuestionDto.setNcs(inspectionCheckListAnswerDto.getNcs());
        return  inspectionCheckQuestionDto;
    }
    @Override
    public  AdCheckListShowDto getAdCheckListShowDtoByAdCheckListShowDto(AdCheckListShowDto adCheckListShowDto,  List<OrgUserDto> orgUserDtos){
        if(adCheckListShowDto == null){
            return adCheckListShowDto;
        }

        int userNum = orgUserDtos.size();
        if(userNum > 1){
            adCheckListShowDto.setMoreOneDraft(true);
        }

        List<AdhocNcCheckItemDto> adItemList =  adCheckListShowDto.getAdItemList();

        if(IaisCommonUtils.isEmpty(adItemList)){
            return adCheckListShowDto;
        }

        for(AdhocNcCheckItemDto adhocNcCheckItemDto :  adItemList){
            List<AnswerForDifDto> answerForDifDtos = new ArrayList<>(userNum);
            Map<String, AnswerForDifDto> answerForDifDtoMaps = Maps.newHashMapWithExpectedSize(userNum);
            for (OrgUserDto entry : orgUserDtos) {
                AnswerForDifDto answerForDifDto = new AnswerForDifDto();
                answerForDifDto.setSubmitId(entry.getId());
                answerForDifDto.setSubmitName(entry.getUserId());
                answerForDifDtos.add(answerForDifDto);
                answerForDifDtoMaps.put(entry.getId(),answerForDifDto);
            }
            adhocNcCheckItemDto.setAdhocAnswerForDifDtos(answerForDifDtos);
            adhocNcCheckItemDto.setAnswerForDifDtoMaps(answerForDifDtoMaps);
        }

        List<String> itemList = new ArrayList<>(adItemList.size());
        String identify = null;
        for(AdhocNcCheckItemDto adhocNcCheckItemDto : adItemList){
            if(StringUtil.isEmpty(identify)){
                identify = adhocNcCheckItemDto.getIdentify();
            }
            itemList.add(adhocNcCheckItemDto.getId());
        }
        List<AdhocDraftDto>  adhocDraftDtos  = getAdhocDraftDtosByIdentify(identify,itemList);
        if(IaisCommonUtils.isEmpty(adhocDraftDtos)){
            return  adCheckListShowDto;
        }else {

            // Distinguish between different answers
            for(AdhocNcCheckItemDto adhocNcCheckItemDto : adItemList) {
                List<AdhocDraftDto> adhocDraftDtosOne = new ArrayList<>(1);
                for (AdhocDraftDto adhocDraftDto : adhocDraftDtos) {
                     if(adhocNcCheckItemDto.getId().equalsIgnoreCase(adhocDraftDto.getAdhocItemId())){
                         adhocDraftDtosOne.add(adhocDraftDto);
                     }
                }

               if(IaisCommonUtils.isEmpty(adhocDraftDtosOne)){
                  log.info(StringUtil.changeForLog(" adhocNcCheckItemDto id is " + adhocNcCheckItemDto.getId() +" no draft."));
               }else {
                   setAnswerForDifDtoMapsByAdhocDraftDtosOne(adhocDraftDtosOne,adhocNcCheckItemDto.getAnswerForDifDtoMaps());
                   if(userNum == 1 && adhocDraftDtosOne .size() == 1){
                       if( !StringUtil.isEmpty(adhocDraftDtosOne.get(0).getAnswer())){
                           AdhocAnswerDto adhocAnswerDto = JsonUtil.parseToObject(adhocDraftDtosOne.get(0).getAnswer(),AdhocAnswerDto.class);
                           getAdhocNcCheckItemDtoByAdhocAnswerDto(adhocNcCheckItemDto,adhocAnswerDto);
                       }
                   }else if(userNum == adhocDraftDtosOne .size()){
                       List<AnswerForDifDto> answerForDifDtos = adhocNcCheckItemDto.getAdhocAnswerForDifDtos();
                       AnswerForDifDto  answerForSame =  getAnswerForDifDtoByAnswerForDifDtos(answerForDifDtos);
                       adhocNcCheckItemDto.setSameAnswer(answerForSame.isSameAnswer());
                       getAdhocNcCheckItemDtoByAnswerForDifDto(adhocNcCheckItemDto, answerForSame);
                   }
               }
            }
        }
        return adCheckListShowDto;
    }

    private  List<AdhocDraftDto> getAdhocDraftDtosByIdentify(String identify, List<String> itemList){
        List<AdhocDraftDto>  adhocDraftDtos = fillUpCheckListGetAppClient.getAdhocChecklistDraftsByAdhocItemIdIn(itemList).getEntity();
        if(IaisCommonUtils.isEmpty(adhocDraftDtos)){
            return adhocDraftDtos;
        }
        if(StringUtil.isEmpty(identify)){
            List<AdhocDraftDto> serviceAdhocDraftDtos = IaisCommonUtils.genNewArrayList();
            for(AdhocDraftDto adhocDraftDto : adhocDraftDtos){
                if(!StringUtil.isEmpty(adhocDraftDto.getAnswer())){
                    AdhocAnswerDto adhocAnswerDto = JsonUtil.parseToObject(adhocDraftDto.getAnswer(),AdhocAnswerDto.class);
                    if(StringUtil.isEmpty(adhocAnswerDto.getIdentify())){
                        serviceAdhocDraftDtos.add(adhocDraftDto);
                    }
                }
            }
            return serviceAdhocDraftDtos;
        }
        List<AdhocDraftDto> identifyAdhocDraftDtos = IaisCommonUtils.genNewArrayList();
        for(AdhocDraftDto adhocDraftDto : adhocDraftDtos){
            if(!StringUtil.isEmpty(adhocDraftDto.getAnswer())){
                AdhocAnswerDto adhocAnswerDto = JsonUtil.parseToObject(adhocDraftDto.getAnswer(),AdhocAnswerDto.class);
                if(identify.equalsIgnoreCase(adhocAnswerDto.getIdentify())){
                    identifyAdhocDraftDtos.add(adhocDraftDto);
                }
            }
        }
        return identifyAdhocDraftDtos;
    }

    private  void setAnswerForDifDtoMapsByAdhocDraftDtosOne(List<AdhocDraftDto> adhocDraftDtosOne,  Map<String, AnswerForDifDto> answerForDifDtoMaps ){
        for (Map.Entry<String, AnswerForDifDto> entry : answerForDifDtoMaps.entrySet()) {
            AnswerForDifDto answerForDifDto = entry.getValue();
            for(AdhocDraftDto adhocDraftDto : adhocDraftDtosOne){
                if( entry.getKey().equalsIgnoreCase( adhocDraftDto.getCreatedBy())){
                    if(!StringUtil.isEmpty(adhocDraftDto.getAnswer())){
                        AdhocAnswerDto adhocAnswerDto = JsonUtil.parseToObject(adhocDraftDto.getAnswer(),AdhocAnswerDto.class);
                        answerForDifDto.setRemark(adhocAnswerDto.getRemark());
                        answerForDifDto.setAnswer(adhocAnswerDto.getAnswer());
                        answerForDifDto.setNcs(adhocAnswerDto.getNcs());
                        answerForDifDto.setIsRec(adhocAnswerDto.getIsRec());
                    }
                    break;
                }
            }
        }
    }
    private AdhocNcCheckItemDto getAdhocNcCheckItemDtoByAdhocAnswerDto(AdhocNcCheckItemDto adhocNcCheckItemDto, AdhocAnswerDto adhocAnswerDto){
        adhocNcCheckItemDto.setRemark(adhocAnswerDto.getRemark());
        adhocNcCheckItemDto.setAdAnswer(adhocAnswerDto.getAnswer());
        adhocNcCheckItemDto.setRectified("1".equalsIgnoreCase(adhocAnswerDto.getIsRec()));
        adhocNcCheckItemDto.setNcs(adhocAnswerDto.getNcs());
        return  adhocNcCheckItemDto;
    }

    private AdhocNcCheckItemDto getAdhocNcCheckItemDtoByAnswerForDifDto(AdhocNcCheckItemDto adhocNcCheckItemDto, AnswerForDifDto  adhocAnswerDto){
        adhocNcCheckItemDto.setRemark(adhocAnswerDto.getRemark());
        adhocNcCheckItemDto.setAdAnswer(adhocAnswerDto.getAnswer());
        adhocNcCheckItemDto.setRectified("1".equalsIgnoreCase(adhocAnswerDto.getIsRec()));
        adhocNcCheckItemDto.setNcs(adhocAnswerDto.getNcs());
        return  adhocNcCheckItemDto;
    }

    private AnswerForDifDto getAnswerForDifDtoByAnswerForDifDtos( List<AnswerForDifDto> adhocAnswerForDifDtos){
        List<AnswerForDifDto> answerForDifDtoCopys = copyAnswerForDifDtos(adhocAnswerForDifDtos);
         AnswerForDifDto  answerForSame = new AnswerForDifDto();
         AnswerForDifDto answerForDifDto = adhocAnswerForDifDtos.get(0);
            Boolean recSame = Boolean.TRUE;
            Boolean answerSame = Boolean.TRUE;
            Boolean remarkSame = Boolean.TRUE;
            Boolean ncsSame = Boolean.TRUE;
           for(AnswerForDifDto answerForDifDtoCopy :  answerForDifDtoCopys){
               Boolean isSameSubmit = isSameByStrings(answerForDifDtoCopy.getSubmitName(),answerForDifDto.getSubmitName());
                if(StringUtil.isEmpty( answerForDifDtoCopy.getAnswer()) || (answerSame && !isSameSubmit && !isSameByStrings( answerForDifDtoCopy.getAnswer(),answerForDifDto.getAnswer(),answerForDifDto.getAnswer()))){
                    answerSame = Boolean.FALSE;
                }
               if( recSame && !isSameSubmit &&  !isSameByStrings( answerForDifDtoCopy.getIsRec(),answerForDifDto.getIsRec())){
                   recSame = Boolean.FALSE;
               }
               if( remarkSame && !isSameSubmit && !isSameByStrings( answerForDifDtoCopy.getRemark(),answerForDifDto.getRemark(),answerForDifDto.getAnswer())){
                   remarkSame = Boolean.FALSE;
               }
               if(  ncsSame && !isSameSubmit && !isSameByStrings( answerForDifDtoCopy.getNcs(),answerForDifDto.getNcs(),answerForDifDto.getAnswer())){
                   ncsSame = Boolean.FALSE;
               }
           }

           if(answerSame && recSame && remarkSame && ncsSame){
               answerForSame.setIsRec(answerForDifDto.getIsRec());
               answerForSame.setAnswer( answerForDifDto.getAnswer());
               answerForSame.setRemark(answerForDifDto.getRemark());
               answerForSame.setNcs(answerForDifDto.getNcs());
               answerForSame.setSameAnswer(true);
           }else {
               answerForSame.setIsRec( null);
               answerForSame.setAnswer( null);
               answerForSame.setRemark(null);
               answerForSame.setSameAnswer(false);
           }

        return answerForSame;
    }
    private Boolean isSameByStrings(String s1,String s2,String answer){
        if(!"Yes".equalsIgnoreCase(answer)){
            if(StringUtil.isEmpty(s1)&& StringUtil.isEmpty(s2)){
                return Boolean.FALSE;
            }
        }
        return isSameByStrings(s1, s2);
    }
    private Boolean isSameByStrings(String s1,String s2){
        if(StringUtil.isEmpty(s1)&& StringUtil.isEmpty(s2)){
            return Boolean.TRUE;
        }
        if(!StringUtil.isEmpty(s1)){
            if( StringUtil.isEmpty(s2)){
                return Boolean.FALSE;
            }else {
                if(s1.equalsIgnoreCase(s2)){
                    return Boolean.TRUE;
                }else {
                    return Boolean.FALSE;
                }
            }

        }

        return Boolean.FALSE;
    }
    private List<AnswerForDifDto> copyAnswerForDifDtos(List<AnswerForDifDto> adhocAnswerForDifDtos){
        List<AnswerForDifDto> answerForDifDtoCopys = new ArrayList<>(adhocAnswerForDifDtos.size());
        for(AnswerForDifDto answerForDifDto : adhocAnswerForDifDtos){
            AnswerForDifDto answerForDifDtoCopy = new AnswerForDifDto();
            answerForDifDtoCopy .setRemark( answerForDifDto.getRemark());
            answerForDifDtoCopy .setAnswer( answerForDifDto.getAnswer());
            answerForDifDtoCopy .setIsRec( answerForDifDto.getIsRec());
            answerForDifDtoCopy .setSubmitName( answerForDifDto.getSubmitName());
            answerForDifDtoCopy.setNcs(answerForDifDto.getNcs());
            answerForDifDtoCopys.add(answerForDifDtoCopy);
        }
        return answerForDifDtoCopys;
    }

    @Override
    public List<TaskDto> getCurrTaskByRefNo(TaskDto taskDto) {
        return organizationClient.getCurrTaskByRefNo(taskDto.getRefNo()).getEntity();
    }

    @Override
    public  List<OrgUserDto> getOrgUserDtosByTaskDatos(List<TaskDto> taskDtos){
        List<String> strings = new ArrayList<>(taskDtos.size());
        for(TaskDto taskDto : taskDtos){
            if(!StringUtil.isEmpty(taskDto.getUserId())){
                strings.add(taskDto.getUserId());
            }
        }
        return organizationClient.retrieveOrgUserAccount(strings).getEntity();
    }
    @Override
    public void saveOtherTasks(List<TaskDto> taskDtos,TaskDto taskDto){
        if(IaisCommonUtils.isEmpty(taskDtos)){
            return;
        }
        List<TaskDto> taskDtoList = new ArrayList<>(taskDtos.size()-1);
        Date date = new Date();
        for(TaskDto taskDto1 : taskDtos){
            if( !taskDto.getId().equalsIgnoreCase( taskDto1.getId())){
                taskDto1.setSlaDateCompleted(date);
                taskDto1.setSlaRemainInDays(taskService.remainDays(taskDto1));
                taskDto1.setUpdateCount(1);
                taskDto1.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                taskDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                taskDtoList.add(taskDto1);
            }
        }
        if( !IaisCommonUtils.isEmpty( taskDtoList)){
            taskService.createTasks(taskDtoList);
        }
    }

    @Override
    public  AdhocCheckListConifgDto getAdhocCheckListConifgDtoByCorId(String corId){
        AdhocCheckListConifgDto adhocCheckListConifgDto = applicationClient.getAdhocConfigByAppPremCorrId(corId).getEntity();
        if(adhocCheckListConifgDto == null){
            return adhocCheckListConifgDto;
        }
        List<AdhocChecklistItemDto> adhocItemList = applicationClient.getAdhocByAppPremCorrId(corId).getEntity();
        if(adhocItemList == null){
            adhocCheckListConifgDto.setAllAdhocItem(new ArrayList<>(1));
        }else {
            adhocCheckListConifgDto.setAllAdhocItem(adhocItemList);
        }
        return adhocCheckListConifgDto;
    }

    @Override
    public boolean editAhocByAdhocCheckListConifgDtoAndOldAdhocCheckListConifgDto(AdhocCheckListConifgDto adhocCheckListConifgDto, AdhocCheckListConifgDto adhocCheckListConifgDtoOld) {
        if(adhocCheckListConifgDto == null && adhocCheckListConifgDtoOld == null) {
            return false;
        }

        if((adhocCheckListConifgDto != null && adhocCheckListConifgDtoOld == null)
                ||  (adhocCheckListConifgDto == null && adhocCheckListConifgDtoOld != null)){
            return true;
        }

        if(adhocCheckListConifgDto != null && adhocCheckListConifgDtoOld != null){
            List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();
            List<AdhocChecklistItemDto> oldAdhocItems = adhocCheckListConifgDtoOld.getAllAdhocItem();
            if(IaisCommonUtils.isEmpty(allAdhocItem) && IaisCommonUtils.isEmpty(oldAdhocItems)){
                return false;
            }

            if((IaisCommonUtils.isEmpty(allAdhocItem) && !IaisCommonUtils.isEmpty(oldAdhocItems))
                    || (!IaisCommonUtils.isEmpty(allAdhocItem) && IaisCommonUtils.isEmpty(oldAdhocItems))){
                return true;
            }

            if( !IaisCommonUtils.isEmpty(allAdhocItem) && !IaisCommonUtils.isEmpty(oldAdhocItems)){
              if(allAdhocItem.size() != oldAdhocItems.size()){
                  return true;
              }
              for(AdhocChecklistItemDto adhocChecklistItemDto : allAdhocItem){
                  boolean haveAhoc = false;
                  for(AdhocChecklistItemDto adhocChecklistItemDtoOld : oldAdhocItems){
                      if(adhocChecklistItemDtoOld.getId().equalsIgnoreCase(adhocChecklistItemDto.getId())){
                          haveAhoc = true;
                          break;
                      }
                  }
                  if(!haveAhoc){
                      return true;
                  }
              }
            }
        }
        return false;
    }

    @Override
    public void sendModifiedChecklistEmailToAOStage(ApplicationViewDto appViewDto){
        try{
            ChecklistHelper.sendModifiedChecklistEmailToAOStage(appViewDto, mailSender);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }

    @Override
    public void saveAdhocDto(AdhocCheckListConifgDto adhocCheckListConifgDto) {
        if(adhocCheckListConifgDto != null){
            if(adhocCheckListConifgDto.getVersion() != null){
                AdhocCheckListConifgDto adhocCheckListConifgDtoDb = applicationClient.getAdhocConfigByAppPremCorrId(adhocCheckListConifgDto.getPremCorreId()).getEntity();
                if(adhocCheckListConifgDtoDb != null){
                    adhocCheckListConifgDto.setVersion(adhocCheckListConifgDtoDb.getVersion()+1);
                    adhocCheckListConifgDto.setOldId(adhocCheckListConifgDtoDb.getId());
                }
                adhocCheckListConifgDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                adhocCheckListConifgDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                adhocCheckListConifgDto.setId(null);
                adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
            }else {
                adhocCheckListConifgDto.setId(null);
                adhocCheckListConifgDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                adhocCheckListConifgDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                adhocCheckListConifgDto.setVersion(1);
                adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
            }
        }
    }

    @Override
    public void changeDataForNc( List<InspectionFillCheckListDto> inspectionFillCheckListDtos,String refNo){
        if(IaisCommonUtils.isEmpty(inspectionFillCheckListDtos)){
            return;
        }
        List<SelfAssessment> selfAssessments = BeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(refNo);
        if(IaisCommonUtils.isEmpty(selfAssessments)){
            return;
        }

        List<SelfAssessmentConfig> selfAssessmentConfigs = selfAssessments.get(0).getSelfAssessmentConfig();
        if(!IaisCommonUtils.isEmpty(selfAssessmentConfigs)) {
            for(InspectionFillCheckListDto inspectionFillCheckListDto : inspectionFillCheckListDtos){
                for(SelfAssessmentConfig selfAssessmentConfig : selfAssessmentConfigs){
                    if(selfAssessmentConfig != null){
                        if( StringUtil.isEmpty(inspectionFillCheckListDto.getVehicleName()) && inspectionFillCheckListDto.isCommonConfig() == selfAssessmentConfig.isCommon()){
                            List<PremCheckItem> question =  selfAssessmentConfig.getQuestion();
                            if(!IaisCommonUtils.isEmpty(question)){
                                List<PremCheckItem> ncQs = getAllSelfQs(question);
                               if( !IaisCommonUtils.isEmpty(ncQs)){
                                   if(IaisCommonUtils.isEmpty(inspectionFillCheckListDto.getCheckList())){
                                       setSefNcAnswerToSevCheckList(ncQs,inspectionFillCheckListDto.getSectionDtoList());
                                   }else {
                                       setSefNcAnswerToCheckList(ncQs,inspectionFillCheckListDto.getCheckList());
                                   }
                               }
                            }

                        }
                    }
                }
            }

        }
    }

    // now no use
   private  List<PremCheckItem> getNcQs( List<PremCheckItem> question){
       List<PremCheckItem> ncQs = new ArrayList<>(2);
       for(PremCheckItem premCheckItem : question){
           if("NO".equalsIgnoreCase(premCheckItem.getAnswer())){
               ncQs.add(premCheckItem);
           }
       }
       return ncQs;
   }

    private  List<PremCheckItem> getAllSelfQs( List<PremCheckItem> question){
        List<PremCheckItem> ncQs = new ArrayList<>(2);
        for(PremCheckItem premCheckItem : question){
            if("NO".equalsIgnoreCase(premCheckItem.getAnswer())){
                premCheckItem.setAnswer("No");
                ncQs.add(premCheckItem);
            }else if("YES".equalsIgnoreCase(premCheckItem.getAnswer())){
                premCheckItem.setAnswer("Yes");
                ncQs.add(premCheckItem);
            }else if("NA".equalsIgnoreCase(premCheckItem.getAnswer())){
                premCheckItem.setAnswer("N/A");
                ncQs.add(premCheckItem);
            }
        }
        return ncQs;
    }
    private void setSefNcAnswerToSevCheckList( List<PremCheckItem> ncQs,List<SectionDto> sectionDtoList){
        if(IaisCommonUtils.isEmpty(sectionDtoList)){
            return;
        }
        for(SectionDto sectionDto : sectionDtoList){
            List<ItemDto> itemDtos =  sectionDto.getItemDtoList();
            if(!IaisCommonUtils.isEmpty(itemDtos)){
                for(ItemDto itemDto :itemDtos){
                    for(PremCheckItem premCheckItem : ncQs){
                        InspectionCheckQuestionDto incqDto =  itemDto.getIncqDto();
                       if( itemDto.getItemId().equalsIgnoreCase(premCheckItem.getChecklistItemId())){
                           if("No".equalsIgnoreCase(premCheckItem.getAnswer())){
                               incqDto.setNcSelfAnswer(true);
                           }
                           incqDto.setSelfAnswer(premCheckItem.getAnswer());
                       }
                    }
                }
            }
        }
    }
    private void setSefNcAnswerToCheckList( List<PremCheckItem> ncQs, List<InspectionCheckQuestionDto> checkQuestionDtos){
               for(InspectionCheckQuestionDto itemDto : checkQuestionDtos){
                    for(PremCheckItem premCheckItem : ncQs){
                        if(itemDto.getItemId().equalsIgnoreCase(premCheckItem.getChecklistItemId())){
                            if("No".equalsIgnoreCase(premCheckItem.getAnswer())){
                                itemDto.setNcSelfAnswer(true);
                            }
                            itemDto.setSelfAnswer(premCheckItem.getAnswer());
                        }
                    }
            }
        }

    @Override
    public void setInspectionCheckQuestionDtoByAnswerForDifDtosAndDeconflict(InspectionCheckQuestionDto inspectionCheckQuestionDto, List<AnswerForDifDto> answerForDifDtos, String deconflict) {
        AnswerForDifDto  answerForSame =  getAnswerForDifDtoByAnswerForDifDtos(answerForDifDtos);
        inspectionCheckQuestionDto.setSameAnswer(answerForSame.isSameAnswer());
        getInspectionCheckQuestionDtoByAnswerForDifDto(inspectionCheckQuestionDto, answerForSame);
        if(!inspectionCheckQuestionDto.isSameAnswer()){
            inspectionCheckQuestionDto.setDeconflict(deconflict);
            if( !StringUtil.isEmpty(deconflict)){
                for(AnswerForDifDto answerForDifDto : answerForDifDtos){
                    if(deconflict.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                       getInspectionCheckQuestionDtoByAnswerForDifDto(inspectionCheckQuestionDto, answerForDifDto);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void setAdhocNcCheckItemDtoByAnswerForDifDtosAndDeconflict(AdhocNcCheckItemDto adhocNcCheckItemDto, List<AnswerForDifDto> answerForDifDtos, String deconflict) {
        AnswerForDifDto  answerForSame =  getAnswerForDifDtoByAnswerForDifDtos(answerForDifDtos);
        adhocNcCheckItemDto.setSameAnswer(answerForSame.isSameAnswer());
        getAdhocNcCheckItemDtoByAnswerForDifDto(adhocNcCheckItemDto, answerForSame);
        if( !adhocNcCheckItemDto.isSameAnswer()){
            adhocNcCheckItemDto.setDeconflict(deconflict);
            if( !StringUtil.isEmpty(deconflict)) {
                for (AnswerForDifDto answerForDifDto : answerForDifDtos) {
                    if (deconflict.equalsIgnoreCase(answerForDifDto.getSubmitId())) {
                        getAdhocNcCheckItemDtoByAnswerForDifDto(adhocNcCheckItemDto, answerForDifDto);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String setRemarksAndStartTimeAndEndTimeForCheckList(InspectionFDtosDto serListDto, InspectionFillCheckListDto commonDto, String refNo) {
        log.info("------------setRemarksAndStartTimeAndEndTimeForCheckList from mobile ------------------");
         setRemark(serListDto,commonDto);
         setStartTimeAndEndTime(serListDto,refNo);
         log.info("------------setRemarksAndStartTimeAndEndTimeForCheckList from mobile  end------------------");
         return  serListDto.getTcuRemark();
    }

    private void setRemark(InspectionFDtosDto serListDto, InspectionFillCheckListDto commonDto){
        if(StringUtil.isEmpty(serListDto.getTcuRemark())){
            log.info("---------get Remarks From mobile -------");
            Map<String, String> draftRemarkMaps  = commonDto.getDraftRemarkMaps();
            List<InspectionFillCheckListDto> fdtoList = serListDto.getFdtoList();
            Map<String, String> remarkMap = IaisCommonUtils.genNewHashMap();
            if(!IaisCommonUtils.isEmpty(fdtoList)){
                for(InspectionFillCheckListDto inspectionFillCheckListDto : fdtoList){
                    if(IaisCommonUtils.isEmpty(draftRemarkMaps) && !IaisCommonUtils.isEmpty(inspectionFillCheckListDto.getDraftRemarkMaps())){
                        draftRemarkMaps = inspectionFillCheckListDto.getDraftRemarkMaps();
                    }else if ( !IaisCommonUtils.isEmpty(draftRemarkMaps) && !IaisCommonUtils.isEmpty(inspectionFillCheckListDto.getDraftRemarkMaps())){
                        for(Map.Entry<String, String> entry: draftRemarkMaps.entrySet()){
                            String key =  entry.getKey();
                            String value = entry.getValue();
                            if(StringUtil.isEmpty(value)){
                                value = "";
                            }
                            StringBuilder  stringBuilderValue = new StringBuilder();
                            stringBuilderValue.append(value);
                            for(Map.Entry<String, String> entryIns: inspectionFillCheckListDto.getDraftRemarkMaps().entrySet()){
                                if(key.equalsIgnoreCase(entryIns.getKey())){
                                    stringBuilderValue.append(" \n") .append(entryIns.getValue());
                                }
                            }
                            remarkMap.put(key,stringBuilderValue.toString());
                        }
                    }
                }
            }
            StringBuilder  stringBuilderRemarks = new StringBuilder();
            if(!IaisCommonUtils.isEmpty(remarkMap) ){
                for (String value : remarkMap.values()) {
                    stringBuilderRemarks.append(value).append(" \n");
                }
            } else if(IaisCommonUtils.isEmpty(remarkMap) && !IaisCommonUtils.isEmpty(draftRemarkMaps)){
                for (String value : draftRemarkMaps.values()) {
                    stringBuilderRemarks.append(value).append(" \n");
                }
            }
            serListDto.setTcuRemark(stringBuilderRemarks.toString());
            log.info("---------get Remarks From mobile -------");
        }
    }

    private void  setStartTimeAndEndTime(InspectionFDtosDto serListDto,String refNo){
        if(StringUtil.isEmpty(serListDto.getStartTime()) || StringUtil.isEmpty(serListDto.getEndTime())){
            AppPremisesInspecApptDto appPremisesInspecApptDto = inspectionTaskClient.getSpecificDtoByAppPremCorrId(refNo).getEntity();
            if(appPremisesInspecApptDto != null){
                ApptUserCalendarDto cancelCalendarDto = new ApptUserCalendarDto();
                cancelCalendarDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                cancelCalendarDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                cancelCalendarDto.setStatus(AppointmentConstants.CALENDAR_STATUS_RESERVED);
                List<ApptUserCalendarDto> apptUserCalendarDtos=  appointmentClient.getCalenderByApptRefNoAndStatusOrderByTimeSlot(cancelCalendarDto).getEntity();
                if(!IaisCommonUtils.isEmpty(apptUserCalendarDtos)){
                    Date startDate = apptUserCalendarDtos.get(0).getTimeSlot();
                    Date endDate =  apptUserCalendarDtos.get(apptUserCalendarDtos.size()-1).getTimeSlot();
                    String startDateTime = Formatter.formatDateTime( startDate,"HH : mm");
                    String endDateDateTime = Formatter.formatDateTime(endDate,"HH : mm");
                    String [] startDateHHMM = getStringsByHHDD(startDateTime);
                    if(startDateHHMM != null && startDateHHMM.length == 2){
                        serListDto.setStartHour(startDateHHMM[0]);
                        Iterator<String> iterator= Arrays.stream(startDateHHMM).iterator();
                        iterator.next();
                        if(iterator.hasNext()){
                            serListDto.setStartMin(iterator.next());
                        }
                        serListDto.setStartTime(startDateTime);
                    }
                    String [] endDateHHMM = getStringsByHHDD(endDateDateTime);
                    if(endDateHHMM != null && endDateHHMM.length == 2){
                        String endHour = String.valueOf(Integer.parseInt(endDateHHMM[0]) + 1);
                        serListDto.setEndHour(endHour);
                        Iterator<String> iterator= Arrays.stream(endDateHHMM).iterator();
                        iterator.next();
                        if(iterator.hasNext()){
                            serListDto.setEndMin(iterator.next());
                        }
                        serListDto.setEndTime( endHour+" : " + serListDto.getEndMin());
                    }
                }
            }
        }
    }

    @Override
    public boolean isBeforeFinishCheckList(String refNo) {
        AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(refNo,InspectionConstants.RECOM_TYPE_INSP_FINISH_CHECKLIST_BEFORE).getEntity();
        if(appPreRecommentdationDto != null){
            return true;
        }
        return false;
    }

    @Override
    public AdCheckListShowDto getSpecAhocData(AdCheckListShowDto adCheckListShowDto, String identify, boolean beforeFinishList,List<OrgUserDto>  orgUserDtos) {
        if(adCheckListShowDto == null || IaisCommonUtils.isEmpty(adCheckListShowDto.getAdItemList())){
            return null;
        }
        AdCheckListShowDto adCheckListShowDtoCopy = (AdCheckListShowDto)com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(adCheckListShowDto);
        if(beforeFinishList){
            List<AdhocNcCheckItemDto> adItemList = IaisCommonUtils.genNewArrayList();
           for(AdhocNcCheckItemDto adhocNcCheckItemDto :adCheckListShowDto.getAdItemList()){
               if(identify.equalsIgnoreCase(adhocNcCheckItemDto.getIdentify())){
                   adItemList.add(adhocNcCheckItemDto);
               }
           }
            adCheckListShowDtoCopy.setAdItemList(adItemList);
        }else {
            for(AdhocNcCheckItemDto adhocNcCheckItemDto : adCheckListShowDtoCopy.getAdItemList()){
                adhocNcCheckItemDto.setIdentify(identify);
            }
            //get draft ahoc
            adCheckListShowDtoCopy = getAdCheckListShowDtoByAdCheckListShowDto(adCheckListShowDtoCopy,orgUserDtos);
        }
        return adCheckListShowDtoCopy;
    }

    @Override
    public boolean checklistNeedVehicleSeparation(ApplicationViewDto appViewDto){
        List<AppSvcVehicleDto> appSvcVehicleDtos =  appViewDto.getAppSvcVehicleDtos();
        if(IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
            return  false;
        }
        if( ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appViewDto.getApplicationDto().getApplicationType())){
            //get vehicleNoList for edit
            List<String> vehicleNoList = applicationService.getVehicleNoByFlag(InspectionConstants.SWITCH_ACTION_EDIT, appViewDto);
            //sort AppSvcVehicleDto List
            applicationService.sortAppSvcVehicleListToShow(vehicleNoList, appViewDto);
            appSvcVehicleDtos = appViewDto.getVehicleRfcShowDtos();
        }
        return (IaisCommonUtils.isEmpty(appSvcVehicleDtos) || hcsaChklClient.getMaxVersionInspectionEntityConfig(appViewDto.getSvcCode(), AdhocChecklistServiceImpl.compareType(appViewDto.getApplicationDto().getApplicationType()), AdhocChecklistServiceImpl.compareModule(appViewDto.getApplicationDto().getApplicationType()),HcsaChecklistConstants.INSPECTION_ENTITY_VEHICLE ).getEntity() == null) ? false : true;
    }

    @Override
    public InspectionSpecServiceDto getOriginalInspectionSpecServiceDtoByTaskId(boolean needVehicleSeparation, boolean beforeFinishList, String taskId) {
       if(needVehicleSeparation){
           InspectionSpecServiceDto inspectionSpecServiceDto = new InspectionSpecServiceDto();
           inspectionSpecServiceDto.setFdtoList(beforeFinishList ? getInspectionFillCheckListDtoListForReview(taskId,"service",true) : getInspectionFillCheckListDtoList(taskId,"service",true));
           return inspectionSpecServiceDto;
       }
       return null;
    }

    @Override
    public AdCheckListShowDto getNoVehicleAdhoc(AdCheckListShowDto adCheckListShowDto) {
        if(adCheckListShowDto != null && IaisCommonUtils.isNotEmpty(adCheckListShowDto.getAdItemList())){
            Iterator<AdhocNcCheckItemDto> itemDtoIterator = adCheckListShowDto.getAdItemList().iterator();
            while(itemDtoIterator.hasNext()){
                AdhocNcCheckItemDto adhocNcCheckItemDto = itemDtoIterator.next();
                if(StringUtil.isNotEmpty(adhocNcCheckItemDto.getIdentify())){
                    itemDtoIterator.remove();
                }
            }
        }
        return adCheckListShowDto;
    }
}
