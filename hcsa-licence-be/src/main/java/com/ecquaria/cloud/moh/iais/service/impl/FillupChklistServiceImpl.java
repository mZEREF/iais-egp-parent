package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocSaveAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;
    @Override
    public ApplicationViewDto getAppViewDto(String taskId){
        TaskDto taskDto = taskService.getTaskById(taskId);
        String refNo = taskDto.getRefNo();
        ApplicationViewDto viewDto = applicationClient.getAppViewByCorrelationId(refNo).getEntity();
        return viewDto;
    }

    @Override
    public InspectionFillCheckListDto getInspectionFillCheckListDto(String taskId,String svcType) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        String serviceCode = null;
        if(taskDto!=null){
            /*String refNo = taskDto.getRefNo();
            ApplicationDto appDto = applicationClient.getAppByNo(refNo).getEntity();
            String serviceId = appDto.getServiceId();
            HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
            serviceCode = svcDto.getSvcCode();
            String appId = appDto.getId();
            appCorrDtolist = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
            if(appCorrDtolist!=null && !appCorrDtolist.isEmpty()){
                appPremCorrId = appCorrDtolist.get(0).getId();
            }*/
            appPremCorrId = taskDto.getRefNo();
        }
        List<ChecklistQuestionDto> cDtoList = hcsaChklClient.getcheckListQuestionDtoList(serviceCode,"Inspection").getEntity();
        InspectionFillCheckListDto infillCheckListDto = new InspectionFillCheckListDto();
        if(cDtoList!=null && !cDtoList.isEmpty()){
            List<InspectionCheckQuestionDto> cqDtoList = new ArrayList<>();
            for(ChecklistQuestionDto temp:cDtoList){
                InspectionCheckQuestionDto inspectionCheckQuestionDto = null;
                inspectionCheckQuestionDto = transferQuestionDtotoInDto(temp);
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
        List<SectionDto> sectionDtoList = new ArrayList<>();
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
            List<ItemDto> itemDtoList = new ArrayList<>();
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
        //icDto.setAppPreCorreId();
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
    @Transactional(rollbackFor = Exception.class)
    public void saveDto(InspectionFillCheckListDto dto) {
        boolean ncflag = isHaveNc(dto);
        List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
        List<InspectionCheckListAnswerDto> answerDtoList = new ArrayList<>();
        InspectionCheckListAnswerDto answerDto = null;

        String appPremCorrId = icqDtoList.get(0).getAppPreCorreId();
        String configId = icqDtoList.get(0).getConfigId();

        AppPremisesRecommendationDto appPreRecommentdationDto = new AppPremisesRecommendationDto();
        appPreRecommentdationDto.setAppPremCorreId(appPremCorrId);
        appPreRecommentdationDto.setRecomType("tcu");
        Date tcuDate = null;
        try {
            tcuDate = Formatter.parseDate(dto.getTuc());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(tcuDate!=null){
            appPreRecommentdationDto.setRecomInDate(tcuDate);
        }
        appPreRecommentdationDto.setAppPremCorreId(appPremCorrId);
        appPreRecommentdationDto.setBestPractice(dto.getBestPractice());
        appPreRecommentdationDto.setRemarks(dto.getTcuRemark());
        appPreRecommentdationDto.setStatus("CMSTAT001");
        appPreRecommentdationDto.setRecomType("tcu");
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
            for(int i =0;i<icqDtoList.size();i++){
                answerDto = new InspectionCheckListAnswerDto();
                answerDto.setAnswer(icqDtoList.get(i).getChkanswer());
                answerDto.setRemark(icqDtoList.get(i).getRemark());
                answerDto.setItemId(icqDtoList.get(i).getItemId());
                if("No".equals(icqDtoList.get(i).getChkanswer())&&ncflag){
                   answerDto.setIsRec(appPremisesPreInspectionNcItemDtoList.get(j).getIsRecitfied()+"");
                   j++;
                }
                answerDto.setSectionName(icqDtoList.get(i).getSectionName());
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
            e.printStackTrace();
            throw e;
        }
     }


    public List<AppPremisesPreInspectionNcItemDto> getAppPremisesPreInspectionNcItemDto(InspectionFillCheckListDto dto,AppPremPreInspectionNcDto ncDto){
        List<InspectionCheckQuestionDto> insqDtoList =  dto.getCheckList();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = new ArrayList<>();
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
        List<InspectionCheckQuestionDto> mergeList = new ArrayList<>();
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
        List<InspectionCheckQuestionDto> checkList = new ArrayList<>();
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
                    inquest.setChecklistItem(item.getChecklistItem());
                    checkList.add(inquest);
                }
            }
        }
        dto.setCheckList(checkList);
        if(checkList!=null && !checkList.isEmpty()){
            List<InspectionCheckQuestionDto> cqDtoList = new ArrayList<>();
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
    @Override
    public AdCheckListShowDto getAdhoc(String appremCorrId){
        List<AdhocChecklistItemDto> adhocItemList = applicationClient.getAdhocByAppPremCorrId(appremCorrId).getEntity();
        AdCheckListShowDto adShowDto = new AdCheckListShowDto();
        List<AdhocNcCheckItemDto> adhocNcCheckItemDtoList = new ArrayList<>();
        if(adhocItemList!=null&&!adhocItemList.isEmpty()){
            AdhocNcCheckItemDto addto = null;
            for(AdhocChecklistItemDto temp:adhocItemList){
                addto = transfertoadNcItemDto(temp);
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
        return adto;
    }
    @Override
    public void saveAdhocDto(AdCheckListShowDto showDto,String appPremId){
        List<AdhocNcCheckItemDto>  itemDtoList = showDto.getAdItemList();
        List<AdhocChecklistItemDto> saveItemDtoList = new ArrayList<>();
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
    public void saveDraft(InspectionFillCheckListDto icDto, InspectionFillCheckListDto comDto, AdCheckListShowDto adDto) {
        CheckListDraftDto checkListDraftDto = new CheckListDraftDto();
        checkListDraftDto.setGeneralDto(icDto);
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
        List<AdhocDraftDto> adhocSaveList = new ArrayList<>();
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
        fillUpCheckListGetAppClient.saveAdhocDraft(adhocSaveList);

    }

    @Override
    public CheckListDraftDto getDraftByTaskId(String taskId, String svcType) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        String serviceCode = null;
        if(taskDto!=null){
            String refNo = taskDto.getRefNo();
            /*ApplicationDto appDto = applicationClient.getAppByNo(refNo).getEntity();
            String serviceId = appDto.getServiceId();
            HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
            serviceCode = svcDto.getSvcCode();
            String appId = appDto.getId();
            appCorrDtolist = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
            if(appCorrDtolist!=null && !appCorrDtolist.isEmpty()){
                appPremCorrId = appCorrDtolist.get(0).getId();
            }*/
            appPremCorrId = taskDto.getRefNo();
        }
        List<ChecklistQuestionDto> cDtoList = hcsaChklClient.getcheckListQuestionDtoList(serviceCode,"Inspection").getEntity();
        AppPremisesPreInspectChklDto chklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,cDtoList.get(0).getConfigId()).getEntity();
        CheckListDraftDto draft = null;
        if(chklDto!=null){
            AppPremInsDraftDto draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(chklDto.getId()).getEntity();
            if(draftDto!=null){
                String answerStr = draftDto.getAnswer();
                draft = JsonUtil.parseToObject(answerStr,CheckListDraftDto.class);
            }
        }
        return draft;
    }

    @Override
    public List<AdhocDraftDto> getAdhocDraftByAppPremId(String appPremId) {
        List<AdhocChecklistItemDto> adhocItemList = applicationClient.getAdhocByAppPremCorrId(appPremId).getEntity();

        return null;
    }

    @Override
    public AdCheckListShowDto getAdhocDraftByappCorrId(String appremCorrId) {
        AdCheckListShowDto adShowDto = getAdhoc(appremCorrId);
        if(adShowDto!=null){
            List<AdhocNcCheckItemDto> adhocItemList = adShowDto.getAdItemList();
            List<String> itemIdList = new ArrayList<>();
            if(adhocItemList!=null&&!adhocItemList.isEmpty()){
                for(AdhocNcCheckItemDto temp:adhocItemList){
                    itemIdList.add(temp.getId());
                }
            }else{
                return null;
            }
            List<AdhocDraftDto> adhocDraftDtoList = fillUpCheckListGetAppClient.getAdhocDraftItems(itemIdList).getEntity();
            if(adhocDraftDtoList!=null && !adhocDraftDtoList.isEmpty()){
                for(AdhocNcCheckItemDto temp:adhocItemList){
                    if(adhocDraftDtoList!=null&& !adhocDraftDtoList.isEmpty()){
                        for(AdhocDraftDto draft:adhocDraftDtoList){
                            if(draft.getAdhocItemId().equals(temp.getId())){
                                getAdhocDraft(temp,draft);
                            }
                        }
                    }
                }
            }
        }
        return adShowDto;
    }

    private void getAdhocDraft(AdhocNcCheckItemDto itemDto,AdhocDraftDto draftDto){
        String answerStr = draftDto.getAnswer();
        AdhocSaveAnswerDto answerDto = JsonUtil.parseToObject(answerStr,AdhocSaveAnswerDto.class);
        itemDto.setAdAnswer(answerDto.getAnswer());
        if(answerDto.getIsRec()==1){
            itemDto.setRectified(true);
        }else{
            itemDto.setRectified(false);
        }
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
    public void routingTask(TaskDto taskDto, String preInspecRemarks) {
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.PROCESS_DECI_PENDING_MYSELF_FOR_CHECKLIST_VERIFY, RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setSlaDateCompleted(new Date());
        //create
        TaskDto updatedtaskDto = taskService.updateTask(taskDto);
        updatedtaskDto.setId(null);
        updatedtaskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_NCEMAIL);
        updatedtaskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        updatedtaskDto.setSlaDateCompleted(null);
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
        String workGroupId = null;
        if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0) {
            workGroupId = hcsaSvcStageWorkingGroupDtos.get(0).getGroupId();
        }
        updatedtaskDto.setWkGrpId(workGroupId);
        List<TaskDto> createTaskDtoList = new ArrayList<>();
        createTaskDtoList.add(updatedtaskDto);
        taskService.createTasks(createTaskDtoList);
        updateInspectionStatus(applicationDto);
    }
    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec,String role){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setRoleId(role);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    private void updateInspectionStatus(ApplicationDto applicationDto) {
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationDtos.get(0).getId()).getEntity();
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_INSPECTION);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }
}
