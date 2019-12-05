package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListCilent;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetTaskCilent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FillupChklistServiceImpl implements FillupChklistService {
    @Autowired
    private FillUpCheckListCilent fillUpCheckListCilent;
    @Autowired
    private FillUpCheckListGetTaskCilent fillUpCheckListGetTaskCilent;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Override
    public SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.CHECKLIST_CONFIG_RESULTS, searchParam);
    }

    @Override
    public SearchResult<CheckItemQueryDto> listChklItem(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.CHECKLIST_ITEM_RESULTS, searchParam);
    }

    @Override
    public InspectionFillCheckListDto getInspectionFillCheckListDto(String taskId,String svcCode,String svcType) {
        List<ChecklistQuestionDto> cDtoList = fillUpCheckListCilent.getcheckListQuestionDtoList("BLB","Inspection").getEntity();
        Map<String,Object> paramMap= new HashMap<> ();
        paramMap.put("svcCode",svcCode);
        paramMap.put("svcType",svcType);
        taskId = "7102C311-D10D-EA11-BE7D-000C29F371DC";
        //List<ChecklistQuestionDto> cDtoList = RestApiUtil.getListByReqParam("hcsa-config:8878/iais-hcsa-checklist/config/results/{svcCode}/{svcType}",paramMap,ChecklistQuestionDto.class);
        //List<ChecklistQuestionDto> cDtoList = fillUpCheckListCilent.getcheckListQuestionDtoList(svcCode,svcType).getEntity();
        //TaskDto taskDto = RestApiUtil.getByPathParam("hcsa-config:8879/iais-task/{taskId}",taskId, TaskDto.class);
        TaskDto taskDto = fillUpCheckListGetTaskCilent.getTaskDtoByTaskId(taskId).getEntity();
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        if(taskDto!=null){
            String refNo = taskDto.getRefNo();
            //ApplicationViewDto appDto = RestApiUtil.getByPathParam("hcsa-config:8883/iais-application/application/{AppNo}",refNo, ApplicationViewDto.class);
            ApplicationDto appDto = fillUpCheckListGetAppClient.getAppViewDtoByRefNo(refNo).getEntity();
            String appId = appDto.getId();
            //appCorrDtolist = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
            //appCorrDtolist = RestApiUtil.getListByPathParam("hcsa-config:8883/iais-application/application/correlations/{appid}",appId,AppPremisesCorrelationDto.class);

            if(appCorrDtolist!=null && !appCorrDtolist.isEmpty()){
                appPremCorrId = appCorrDtolist.get(0).getId();
            }
        }
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
        return null;
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
        List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
        List<InspectionCheckListAnswerDto> answerDtoList = new ArrayList<>();
        InspectionCheckListAnswerDto answerDto = null;
        for (InspectionCheckQuestionDto temp : icqDtoList) {
            answerDto = new InspectionCheckListAnswerDto();
            answerDto.setAnswer(temp.getChkanswer());
            answerDto.setRemark(temp.getRemark());
            answerDto.setItemId(temp.getItemId());
            answerDtoList.add(answerDto);
        }
        String answerJson = JsonUtil.parseToJson(answerDtoList);
        String appPremCorrId = icqDtoList.get(0).getAppPreCorreId();
        String configId = icqDtoList.get(0).getConfigId();
        AppPremisesPreInspectChklDto appDto = new AppPremisesPreInspectChklDto();
        appDto.setAnswer(answerJson);
        appDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        appDto.setAppPremCorrId("4B7DB578-A1B4-4836-A43E-53450A58F078");
        appDto.setVersion(1+"");
        appDto.setChkLstConfId(configId);
        appDto.setStatus("CMSTAT001");
        appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppPremisesRecommendationDto appPreRecommentdationDto = new AppPremisesRecommendationDto();
        appPreRecommentdationDto.setAppPremCorreId("4B7DB578-A1B4-4836-A43E-53450A58F078");
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
        appPreRecommentdationDto.setAppPremCorreId("4B7DB578-A1B4-4836-A43E-53450A58F078");
        appPreRecommentdationDto.setBestPractice(dto.getBestPractice());
        appPreRecommentdationDto.setRemarks("haha");
        appPreRecommentdationDto.setStatus("CMSTAT001");
        appPreRecommentdationDto.setRecomType("tcu");
        appPreRecommentdationDto.setVersion(1);
        appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        try {
            RestApiUtil.postGetObject("hcsa-config:8883/iais-apppreinschkl-be/AppPremissChkl",appDto,AppPremisesPreInspectChklDto.class);
            RestApiUtil.postGetObject("hcsa-config:8883/application-be/RescomDtoStorage",appPreRecommentdationDto,AppPremisesRecommendationDto.class);
            AppPremPreInspectionNcDto AppPremPreInspectionNcDto = getAppPremPreInspectionNcDto(dto);
            AppPremPreInspectionNcDto = RestApiUtil.postGetObject("hcsa-config:8883/iais-apppreinsnc-be/AppPremNcResult",AppPremPreInspectionNcDto,AppPremPreInspectionNcDto.class);
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = getAppPremisesPreInspectionNcItemDto(dto,AppPremPreInspectionNcDto);
            RestApiUtil.postGetList("hcsa-config:8883/iais-apppreinsncitem-be/AppPremNcItemResult",appPremisesPreInspectionNcItemDtoList,AppPremisesPreInspectionNcItemDto.class);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
     }
    public List<AppPremisesPreInspectionNcItemDto> getAppPremisesPreInspectionNcItemDto(InspectionFillCheckListDto dto,AppPremPreInspectionNcDto ncDto){
        List<InspectionCheckQuestionDto> insqDtoList =  dto.getCheckList();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = new ArrayList<>();
        for(InspectionCheckQuestionDto temp:insqDtoList){
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
        return ncItemDtoList;
    }
    public AppPremPreInspectionNcDto getAppPremPreInspectionNcDto(InspectionFillCheckListDto dto){
        AppPremPreInspectionNcDto ncDto = new AppPremPreInspectionNcDto();
        ncDto.setStatus("CMSTAT001");
        ncDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        ncDto.setVersion(1+"");
        ncDto.setAppPremCorrId("4B7DB578-A1B4-4836-A43E-53450A58F078");
        ncDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return ncDto;
    }
}
