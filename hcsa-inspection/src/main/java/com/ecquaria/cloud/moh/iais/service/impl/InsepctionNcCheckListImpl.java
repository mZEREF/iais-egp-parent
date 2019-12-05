package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListCilent;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetTaskCilent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/5 9:48
 */
@Slf4j
@Service
public class InsepctionNcCheckListImpl implements InsepctionNcCheckListService {
    @Autowired
    private FillUpCheckListCilent fillUpCheckListCilent;
    @Autowired
    private FillUpCheckListGetTaskCilent fillUpCheckListGetTaskCilent;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Override
    public InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto, AppPremisesPreInspectChklDto appPremDto,List<AppPremisesPreInspectionNcItemDto> itemDtoList) {
        List<InspectionCheckQuestionDto> fillCheckList = infillDto.getCheckList();
        List<InspectionCheckQuestionDto> ncCheckList = new ArrayList<>();
        InspectionCheckQuestionDto ncCheck = null;
        String answer = appPremDto.getAnswer();
        List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answer, InspectionCheckListAnswerDto.class);
        if(fillCheckList!=null && !fillCheckList.isEmpty()){
            for(InspectionCheckQuestionDto temp:fillCheckList){
                for(int i=0;i<answerDtoList.size();i++){
                    if(temp.getItemId().equals(answerDtoList.get(i).getItemId())&&
                            temp.getSectionName().equals(answerDtoList.get(i).getSectionName())&&"No".equals(answerDtoList.get(i).getAnswer())){
                        temp.setChkanswer(answerDtoList.get(i).getAnswer());
                        ncCheckList.add(temp);
                    }
                }
            }
        }
        for(InspectionCheckQuestionDto temp:ncCheckList){
            if(itemDtoList!=null &&itemDtoList.isEmpty()) {
                for (AppPremisesPreInspectionNcItemDto item : itemDtoList) {
                    if(item.getItemId().equals(temp.getItemId())){
                        if(item.getIsRecitfied()==0){
                            temp.setRectified(false);
                        }else{
                            temp.setRectified(true);
                        }

                    }
                }
            }
        }
        infillDto.setCheckList(ncCheckList);
        fillInspectionFillCheckListDto(infillDto);
        return infillDto;
    }


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


    @Override
    public AppPremisesPreInspectChklDto getAppPremChklDtoByTaskId(String taskId,String configId) {
        if(StringUtil.isEmpty(taskId)){
            taskId = "7102C311-D10D-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = fillUpCheckListGetTaskCilent.getTaskDtoByTaskId(taskId).getEntity();
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        if(taskDto!=null){
            String refNo = taskDto.getRefNo();
            ApplicationDto appDto = fillUpCheckListGetAppClient.getAppViewDtoByRefNo(refNo).getEntity();
            String appId = appDto.getId();
            appCorrDtolist = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
            if(appCorrDtolist!=null && !appCorrDtolist.isEmpty()){
                appPremCorrId = appCorrDtolist.get(0).getId();
            }
        }
        Map<String,Object> appCklMap = new HashMap<>();
        appCklMap.put("appPremId",appPremCorrId);
        appCklMap.put("configId",configId);
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto = RestApiUtil.getByReqParam("hcsa-config:8883/iais-apppreinschkl-be/AppPremissChklId/{appPremId}/{configId}",appCklMap, AppPremisesPreInspectChklDto.class);
        //  `                                                       RestApiUtil.getListByReqParam("hcsa-config:8878/iais-hcsa-checklist/config/results/{svcCode}/{svcType}",paramMap, ChecklistQuestionDto.class);
        return appPremisesPreInspectChklDto;
    }

    @Override
    public List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId) {
        AppPremPreInspectionNcDto ncDto = RestApiUtil.getByPathParam("hcsa-config:8883/iais-apppreinsnc-be//AppPremNcByAppCorrId{appCorrId}",appCorrId, AppPremPreInspectionNcDto.class);
        String appPremId = ncDto.getAppPremCorrId();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = RestApiUtil.getListByPathParam("hcsa-config:8883/iais-apppreinsncitem-be//AppPremNcItemByNcId{ncId}",appPremId, AppPremisesPreInspectionNcItemDto.class);
        return ncItemDtoList;
    }
}
