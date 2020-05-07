package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
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
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.esotericsoftware.minlog.Log;
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
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    FileRepoClient fileRepoClient;
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
    @Transactional(rollbackFor = Exception.class)
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
    @Override
    public AdCheckListShowDto getAdhoc(String appremCorrId){
        List<AdhocChecklistItemDto> adhocItemList = applicationClient.getAdhocByAppPremCorrId(appremCorrId).getEntity();
        AdCheckListShowDto adShowDto = new AdCheckListShowDto();
        List<AdhocNcCheckItemDto> adhocNcCheckItemDtoList = IaisCommonUtils.genNewArrayList();
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
                AdhocDraftDto saveDto = null;
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
    public List<AdhocDraftDto> getAdhocDraftByAppPremId(String appPremId) {
        List<AdhocChecklistItemDto> adhocItemList = applicationClient.getAdhocByAppPremCorrId(appPremId).getEntity();

        return null;
    }

    @Override
    public AdCheckListShowDto getAdhocDraftByappCorrId(String appremCorrId) {
        AdCheckListShowDto adShowDto = getAdhoc(appremCorrId);
        if(adShowDto!=null){
            List<AdhocNcCheckItemDto> adhocItemList = adShowDto.getAdItemList();
            List<String> itemIdList = IaisCommonUtils.genNewArrayList();
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
    public void routingTask(TaskDto taskDto, String preInspecRemarks, LoginContext loginContext, boolean flag) {
        if(flag){
            routingToNcEmail(taskDto,preInspecRemarks,loginContext); // false
        }else{
            routingForToReport(taskDto,preInspecRemarks,loginContext);// false
        }
    }

    public void routingToNcEmail(TaskDto taskDto, String preInspecRemarks,LoginContext loginContext){
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String svcId = applicationDto.getServiceId();
        String appType = applicationDto.getApplicationType();
        String stgId = taskDto.getTaskKey();
        List<TaskDto> dtos = organizationClient.getTaskByAppNo(taskDto.getRefNo()).getEntity();
        removeOtherTask(dtos,taskDto.getId());
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
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.PROCESS_DECI_PENDING_MYSELF_FOR_CHECKLIST_VERIFY, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //create
        TaskDto updatedtaskDto = taskService.updateTask(taskDto);
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
        createAppPremisesRoutingHistory(updateApplicationDto.getApplicationNo(),updateApplicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, null, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createTaskDtoList.add(updatedtaskDto);
        taskService.createTasks(createTaskDtoList);
        updateInspectionStatus(applicationDto,InspectionConstants.INSPECTION_STATUS_PENDING_EMAIL_VERIFY);
    }

    public void routingForToReport(TaskDto taskDto, String preInspecRemarks,LoginContext loginContext){
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<TaskDto> dtos = organizationClient.getTaskByAppNo(taskDto.getRefNo()).getEntity();
        removeOtherTask(dtos,taskDto.getId());
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT );
        updateInspectionStatus(applicationDto,InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        completedTask(taskDto);
        //create createAppPremisesRoutingHistory
        String svcId = applicationDto.getServiceId();
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
        createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        //create task
        TaskDto updatedtaskDto = taskService.updateTask(taskDto);
        updatedtaskDto.setId(null);
        updatedtaskDto.setUserId(loginContext.getUserId());
        updatedtaskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        updatedtaskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        updatedtaskDto.setSlaDateCompleted(null);
        updatedtaskDto.setRoleId( RoleConsts.USER_ROLE_INSPECTIOR);
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0) {
            updatedtaskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        }
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createAppPremisesRoutingHistory(updateApplicationDto.getApplicationNo(),updateApplicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks, null, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        createTaskDtoList.add(updatedtaskDto);
        taskService.createTasks(createTaskDtoList);
    }

    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }

    private ApplicationDto updateApplicaitonStatus(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationService.updateFEApplicaiton(applicationDto);
        return updateApplicaiton(applicationDto);
    }

    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return applicationClient.updateApplication(applicationDto).getEntity();
    }

    private void removeOtherTask(List<TaskDto> dtos, String taskId) {
        if(!IaisCommonUtils.isEmpty(dtos)){
            for(TaskDto temp:dtos){
                if(removeOtherTaskLogic(temp,taskId)){
                    temp.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                    taskService.updateTask(temp);
                }
            }
        }
    }

    public boolean removeOtherTaskLogic(TaskDto temp,String taskId){
        if(!taskId.equals(temp.getId())&&TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY.equals(temp.getProcessUrl())&&!temp.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED)){
            return true;
        }else{
            return false;
        }
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
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String taskId,String conifgType) {
        List<InspectionFillCheckListDto> fillChkDtoList = null;
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = null;
        if(taskDto!=null){
            appPremCorrId = taskDto.getRefNo();
        }
        if(appPremCorrId!=null){
            List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
            if(chkList!=null && !chkList.isEmpty()){
                fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,conifgType);
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

    @Override
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoListForReview(String taskId, String service) {
        List<InspectionFillCheckListDto> fillCheckDtoList = getInspectionFillCheckListDtoList(taskId,service);
        if(fillCheckDtoList!=null&&!fillCheckDtoList.isEmpty()){
            for(InspectionFillCheckListDto temp:fillCheckDtoList){
                AppPremisesPreInspectChklDto appPremPreCklDto = insepctionNcCheckListService.getAppPremChklDtoByTaskId(taskId,temp.getConfigId());
                insepctionNcCheckListService.getCommonDto(temp,appPremPreCklDto);
            }
        }
        return fillCheckDtoList;
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
        List<TaskDto> taskDtos  = organizationClient.getTaskByRefNoStatus(taskDto.getRefNo(),TaskConsts.TASK_STATUS_COMPLETED,TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
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
        List<TaskDto> taskDtos  = organizationClient.getTaskByRefNoStatus(taskDto.getRefNo(),TaskConsts.TASK_STATUS_COMPLETED,TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
        String workGrpId = "";
        if( taskDtos  != null && taskDtos.size() >0)
         workGrpId = taskDtos.get(0).getWkGrpId();
        String leaderStr = null;
        List<String> leaders =  organizationClient.getInspectionLead(workGrpId).getEntity();
        if(!IaisCommonUtils.isEmpty(leaders)){
            for(String temp:leaders){
                OrgUserDto userDto = organizationClient.retrieveOrgUserAccountById(temp).getEntity();
                leaderStr = userDto.getDisplayName()+" ";
            }
        }
        return leaderStr;
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

    private void getAdhocTotalAndNc(AdCheckListShowDto adchklDto, InspectionFDtosDto serListDto) {
        int totalNum = 0;
        int ncNum = 0;
        int doNum = 0;
        for(AdhocNcCheckItemDto aditem : adchklDto.getAdItemList()){
            totalNum++;
            if(!StringUtil.isEmpty(aditem)){
                doNum++;
                if("No".equals(aditem.getAdAnswer())){
                    ncNum++;
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
                doNum++;
            }
            if("No".equals(cqDto.getChkanswer())){
                ncNum++;
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
                        doNum++;
                        if("No".equals(cqDto.getChkanswer())){
                            ncNum++;
                        }
                    }
                }
            }
        }
        serListDto.setServiceDo(doNum);
        serListDto.setServiceTotal(totalNum);
        serListDto.setServiceNc(ncNum);
    }

    @Override
    public InspectionFillCheckListDto getMaxVersionComAppChklDraft(String appPremCorrId) {
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklListFOrDraft(appPremCorrId).getEntity();
        InspectionFillCheckListDto maxVersionAppDto = getComAppChklDraft(appPremCorrId);
        InspectionFillCheckListDto comDto = null;
        AppPremInsDraftDto draftDto = null;
        if(!IaisCommonUtils.isEmpty(chkList)){
            for(AppPremisesPreInspectChklDto temp:chkList){
                if(maxVersionAppDto != null && temp.getChkLstConfId().equals(maxVersionAppDto.getConfigId())){
                    draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(temp.getId()).getEntity();
                    if(draftDto!=null){
                        comDto = JsonUtil.parseToObject(draftDto.getAnswer(),InspectionFillCheckListDto.class);
                    }
                }
            }
        }
        return comDto;
    }

    @Override
    public List<InspectionFillCheckListDto> getAllVersionComAppChklDraft(String appPremCorrId) {
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        List<InspectionFillCheckListDto> fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,"common");
        List<InspectionFillCheckListDto> otherVersionCommList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(fillChkDtoList)){
            InspectionFillCheckListDto maxVersionChklList = fillChkDtoList.get(0);
            otherVersionCommList = getOtherVersionCommList(chkList,maxVersionChklList,appPremCorrId);

        }
        return otherVersionCommList;
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
        InspectionFDtosDto fDtosDto = new InspectionFDtosDto();
        InspectionFillCheckListDto comDto = null;
        AppPremInsDraftDto draftDto = null;
        List<InspectionFillCheckListDto> comDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(versionCkList)){
            for(AppPremisesPreInspectChklDto temp:versionCkList){
                if(temp.getChkLstConfId().equals(comConfigId)){
                    draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(temp.getId()).getEntity();
                    if(draftDto!=null){
                        comDto = JsonUtil.parseToObject(draftDto.getAnswer(),InspectionFillCheckListDto.class);
                        comDtoList.add(comDto);
                    }
                }
            }
        }
        return comDtoList;
    }

    private List<InspectionFillCheckListDto> getAllVersionDraftList(String configId, List<AppPremisesPreInspectChklDto> chkList) {
        InspectionFillCheckListDto comDto = null;
        AppPremInsDraftDto draftDto = null;
        List<InspectionFillCheckListDto> comDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesPreInspectChklDto temp: chkList){
            if(temp.getChkLstConfId().equals(configId)){
                draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(temp.getId()).getEntity();
                if(draftDto!=null){
                    comDto = JsonUtil.parseToObject(draftDto.getAnswer(),InspectionFillCheckListDto.class);
                    comDtoList.add(comDto);
                }
            }
        }
        return comDtoList;
    }

    public InspectionFillCheckListDto getComAppChklDraft(String appPremCorrId){
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        if(chkList!=null && !chkList.isEmpty()){
            List<InspectionFillCheckListDto> fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,"common");
            if(!IaisCommonUtils.isEmpty(fillChkDtoList)){
               return  fillChkDtoList.get(0);
            }
        }
        return null;
    }

    public InspectionFDtosDto getMaxVersionServiceList(String appPremCorrId){
        InspectionFDtosDto fdto = null;
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        List<InspectionFillCheckListDto> fillChkDtoList = null;
        if(chkList!=null && !chkList.isEmpty()){
            fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,"service");
            if(!IaisCommonUtils.isEmpty(fillChkDtoList)){
                fdto = getFdtoDraft(fillChkDtoList,chkList,appPremCorrId);
            }
        }
        return fdto;
    }

    private InspectionFDtosDto getFdtoDraft(List<InspectionFillCheckListDto> fillChkDtoList, List<AppPremisesPreInspectChklDto> chkList,String appPremCorrId) {
        InspectionFDtosDto fdto = new InspectionFDtosDto();
        InspectionFillCheckListDto comDto = null;
        List<InspectionFillCheckListDto> comDtoList = IaisCommonUtils.genNewArrayList();
        AppPremInsDraftDto draftDto = null;
        for(InspectionFillCheckListDto temp:fillChkDtoList){
            AppPremisesPreInspectChklDto appchklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(temp.getConfigId(),appPremCorrId).getEntity();
            draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(appchklDto.getId()).getEntity();
            if(draftDto!=null){
                comDto = JsonUtil.parseToObject(draftDto.getAnswer(),InspectionFillCheckListDto.class);
                comDtoList.add(comDto);
            }
        }
        fdto.setFdtoList(comDtoList);
        return fdto;
    }

    @Override
    public List<InspectionFDtosDto> geAllVersionServiceDraftList(String appPremCorrId){
        List<InspectionFDtosDto> fdtoList = IaisCommonUtils.genNewArrayList();
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        List<InspectionFillCheckListDto> fillChkDtoList = null;
        if(chkList!=null && !chkList.isEmpty()){
            fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,"service");
            if(!IaisCommonUtils.isEmpty(fillChkDtoList)){
                fdtoList = getAllVersionfdtoList(fillChkDtoList,chkList,appPremCorrId);
            }
        }
        return fdtoList;
    }

    private List<InspectionFDtosDto> getAllVersionfdtoList(List<InspectionFillCheckListDto> fillChkDtoList, List<AppPremisesPreInspectChklDto> chkList,String appPremCorrId) {
        String version = chkList.get(0).getVersion();
        List<InspectionFDtosDto> fdtoList = IaisCommonUtils.genNewArrayList();
        InspectionFDtosDto fdto= null;
        List<InspectionFillCheckListDto> comList = getServiceChkDtoListByAppPremId(chkList,appPremCorrId,"common");
        String comfigId = null;
        if(!IaisCommonUtils.isEmpty(comList)){
            comfigId = comList.get(0).getConfigId();
        }
        try {
            int versionNum = Integer.parseInt(version);
            if(versionNum>0){
                for(int i=versionNum;i>0;i--){
                    List<AppPremisesPreInspectChklDto> versionCkList =  fillUpCheckListGetAppClient.getPremInsChklListByPremIdAndVersion(appPremCorrId,versionNum+"").getEntity();
                    fdto = getadhocByVersionCkList(versionCkList,comfigId);
                    fdtoList.add(fdto);
                }
            }
        }catch (Exception e){
            Log.debug(e.toString());
        }
        return fdtoList;
    }

    private InspectionFDtosDto getadhocByVersionCkList(List<AppPremisesPreInspectChklDto> versionCkList,String comConfigId) {
        InspectionFDtosDto fDtosDto = new InspectionFDtosDto();
        InspectionFillCheckListDto comDto = null;
        AppPremInsDraftDto draftDto = null;
        List<InspectionFillCheckListDto> comDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(versionCkList)){
            for(AppPremisesPreInspectChklDto temp:versionCkList){
                if(!temp.getChkLstConfId().equals(comConfigId)){
                    draftDto = fillUpCheckListGetAppClient.getAppInsDraftByChkId(temp.getId()).getEntity();
                    if(draftDto!=null){
                        comDto = JsonUtil.parseToObject(draftDto.getAnswer(),InspectionFillCheckListDto.class);
                        comDtoList.add(comDto);
                    }
                }
            }
        }
        fDtosDto.setFdtoList(comDtoList);
        return fDtosDto;
    }

    @Override
    public InspectionFDtosDto getMaxVersionServiceDraft(List<InspectionFDtosDto> fdtosdraftList) {
        if(!IaisCommonUtils.isEmpty(fdtosdraftList)){
           return fdtosdraftList.get(0);
        }
        return null;
    }

    @Override
    public List<InspectionFDtosDto> getOtherVersionfdtos(List<InspectionFDtosDto> fdtosdraft) {
        List<InspectionFDtosDto> otherVersionList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(fdtosdraft)){
            for(int i = fdtosdraft.size()-2;i>0;i--){
                otherVersionList.add(fdtosdraft.get(i));
            }
        }
        return otherVersionList;
    }

    @Override
    public List<AdCheckListShowDto> getOtherAdhocList(String appPremCorrId) {
        return applicationClient.getAllVersionAdhocList(appPremCorrId).getEntity();
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
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(taskDto.getRefNo()).getEntity();
       if(appPremisesSpecialDocDto != null){
           serListDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto );
           serListDto.setOldFileGuid(appPremisesSpecialDocDto.getFileRepoId());
           serListDto.setCopyAppPremisesSpecialDocDto(getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(appPremisesSpecialDocDto));
       }
        String startDate = getStringByRecomType(appPremCorrId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME);
        String endDate = getStringByRecomType(appPremCorrId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME);
        String otherinspectionofficer = getStringByRecomType(appPremCorrId,InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS);
        serListDto.setOtherinspectionofficer(otherinspectionofficer);
        serListDto.setStartMin(startDate);
        serListDto.setEndTime(endDate);
       String [] startDateHHMM = getStringsByHHDD(startDate);
       if(startDateHHMM != null && startDateHHMM.length == 2){
           serListDto.setStartHour(startDateHHMM[0]);
           serListDto.setStartMin(startDateHHMM[1]);
       }
        String [] endDateHHMM = getStringsByHHDD(endDate);
        if(endDateHHMM != null && endDateHHMM.length == 2){
            serListDto.setEndHour(endDateHHMM[0]);
            serListDto.setEndMin(endDateHHMM[1]);
        }
        if( !StringUtil.isEmpty(appPremCorrId)){
          getTcuInfo(serListDto,appPremCorrId);
        }
        getSvcName(serListDto);
        return serListDto;
    }

    @Override
    public  AppIntranetDocDto getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(AppPremisesSpecialDocDto appPremisesSpecialDocDto){
        AppIntranetDocDto copyAppPremisesSpecialDocDto = new AppIntranetDocDto();
        String[] docNameSpec = appPremisesSpecialDocDto.getDocName().split("[.]");
        if(docNameSpec.length == 2){
            copyAppPremisesSpecialDocDto.setDocName(docNameSpec[0]);
            copyAppPremisesSpecialDocDto.setDocDesc(docNameSpec[0]);
            copyAppPremisesSpecialDocDto.setDocType(docNameSpec[1]);
        }else
            return null;
        copyAppPremisesSpecialDocDto.setId(appPremisesSpecialDocDto.getId());
        copyAppPremisesSpecialDocDto.setFileRepoId(appPremisesSpecialDocDto.getFileRepoId());
        copyAppPremisesSpecialDocDto.setDocSize(String.valueOf(appPremisesSpecialDocDto.getDocSize()+"KB"));
        OrgUserDto user = applicationViewService.getUserById(appPremisesSpecialDocDto.getSubmitBy());
        copyAppPremisesSpecialDocDto.setSubmitByName(user.getDisplayName());
        copyAppPremisesSpecialDocDto.setSubmitDtString(Formatter.formatDateTime(appPremisesSpecialDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
        return  copyAppPremisesSpecialDocDto;
    }


    // only HH : DD
    public  String[]  getStringsByHHDD(String dateString){
        return StringUtil.isEmpty(dateString) ? null : dateString.split(" : ");
    }

}
