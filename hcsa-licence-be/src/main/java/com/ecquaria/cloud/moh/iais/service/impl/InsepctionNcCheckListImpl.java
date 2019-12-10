package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    private TaskService taskService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;


    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Override
    public InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto, AppPremisesPreInspectChklDto appPremDto, List<AppPremisesPreInspectionNcItemDto> itemDtoList, AppPremisesRecommendationDto appPremisesRecommendationDto) {
        List<InspectionCheckQuestionDto> fillCheckList = infillDto.getCheckList();
        List<InspectionCheckQuestionDto> ncCheckList = new ArrayList<>();
        InspectionCheckQuestionDto ncCheck = null;
        String answer = appPremDto.getAnswer();
        List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answer, InspectionCheckListAnswerDto.class);
        if (fillCheckList != null && !fillCheckList.isEmpty()) {
            for (InspectionCheckQuestionDto temp : fillCheckList) {
                for (int i = 0; i < answerDtoList.size(); i++) {
                    if (temp.getItemId().equals(answerDtoList.get(i).getItemId()) &&
                            temp.getSectionName().equals(answerDtoList.get(i).getSectionName())) {
                        //&&"No".equals(answerDtoList.get(i).getAnswer())){
                        temp.setChkanswer(answerDtoList.get(i).getAnswer());
                        temp.setRemark(answerDtoList.get(i).getRemark());
                        ncCheckList.add(temp);
                    }
                }
            }
        }
        infillDto.setBestPractice(appPremisesRecommendationDto.getBestPractice());
        infillDto.setTuc(appPremisesRecommendationDto.getRecomInDate() + "");
        for (InspectionCheckQuestionDto temp : ncCheckList) {
            if (itemDtoList != null && !itemDtoList.isEmpty()) {
                for (AppPremisesPreInspectionNcItemDto item : itemDtoList) {
                    if (item.getItemId().equals(temp.getItemId())) {
                        if (item.getIsRecitfied() == 0) {
                            temp.setRectified(false);
                        } else {
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


    public InspectionFillCheckListDto fillInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto) {
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        List<SectionDto> sectionDtoList = new ArrayList<>();
        for (InspectionCheckQuestionDto temp : iqdDtolist) {
            SectionDto sectionDto = new SectionDto();
            sectionDto.setSectionName(temp.getSectionName());
            if (isHaveSameSection(temp.getSectionName(), sectionDtoList)) {
                sectionDtoList.add(sectionDto);
            }
        }
        infillCheckListDto.setSectionDtoList(sectionDtoList);
        itemDto(infillCheckListDto);
        return infillCheckListDto;
    }

    public boolean isHaveSameSection(String sectionName, List<SectionDto> sectionDtoList) {
        if (sectionDtoList != null && !sectionDtoList.isEmpty()) {
            for (SectionDto temp : sectionDtoList) {
                if (temp.getSectionName().equals(sectionName)) {
                    return false;
                }
            }
        }
        return true;
    }

    public InspectionFillCheckListDto itemDto(InspectionFillCheckListDto infillCheckListDto) {
        List<SectionDto> sectionDtoList = infillCheckListDto.getSectionDtoList();
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        for (SectionDto temp : sectionDtoList) {
            List<ItemDto> itemDtoList = new ArrayList<>();
            for (InspectionCheckQuestionDto iq : iqdDtolist) {
                ItemDto itemDto = new ItemDto();
                if (temp.getSectionName().equals(iq.getSectionName())) {
                    itemDto.setItemId(iq.getItemId());
                    itemDtoList.add(itemDto);
                }
            }
            temp.setItemDtoList(itemDtoList);
        }
        getItemCheckListDto(infillCheckListDto);
        return infillCheckListDto;
    }

    public InspectionFillCheckListDto getItemCheckListDto(InspectionFillCheckListDto infillCheckListDto) {
        List<SectionDto> sectionDtoList = infillCheckListDto.getSectionDtoList();
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        for (InspectionCheckQuestionDto temp : iqdDtolist) {
            for (SectionDto section : sectionDtoList) {
                if (temp.getSectionName().equals(section.getSectionName())) {
                    List<ItemDto> itemDtoList = section.getItemDtoList();
                    for (ItemDto itemDto : itemDtoList) {
                        if (itemDto.getItemId().equals(temp.getItemId())) {
                            itemDto.setIncqDto(temp);
                        }
                    }
                }
            }
        }
        return infillCheckListDto;
    }


    @Override
    public AppPremisesPreInspectChklDto getAppPremChklDtoByTaskId(String taskId, String configId) {
        if (StringUtil.isEmpty(taskId)) {
            taskId = "7102C311-D10D-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        List<AppPremisesCorrelationDto> appCorrDtolist = null;
        String appPremCorrId = null;
        if (taskDto != null) {
            String refNo = taskDto.getRefNo();
            ApplicationDto appDto = applicationClient.getAppByNo(refNo).getEntity();
            String appId = appDto.getId();
            appCorrDtolist = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
            if (appCorrDtolist != null && !appCorrDtolist.isEmpty()) {
                appPremCorrId = appCorrDtolist.get(0).getId();
            }
        }
        Map<String, Object> appCklMap = new HashMap<>();
        appCklMap.put("appPremId", appPremCorrId);
        appCklMap.put("configId", configId);
        //AppPremisesPreInspectChklDto appPremisesPreInspectChklDto = RestApiUtil.getByReqParam("hcsa-config:8883/iais-apppreinschkl-be/AppPremissChklId/{appPremId}/{configId}", appCklMap, AppPremisesPreInspectChklDto.class);
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,configId).getEntity();
        return appPremisesPreInspectChklDto;
    }

    @Override
    public List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId) {
       // AppPremPreInspectionNcDto ncDto = RestApiUtil.getByPathParam("hcsa-config:8883/iais-apppreinsnc-be/AppPremNcByAppCorrId{appCorrId}", appCorrId, AppPremPreInspectionNcDto.class);
        AppPremPreInspectionNcDto ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appCorrId).getEntity();
        String ncId = ncDto.getId();
        //List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = RestApiUtil.getListByPathParam("hcsa-config:8883/iais-apppreinsncitem-be/AppPremNcItemByNcId{ncId}", ncId, AppPremisesPreInspectionNcItemDto.class);
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = fillUpCheckListGetAppClient.getAppNcItemByAppCorrId(ncId).getEntity();
        return ncItemDtoList;
    }

    @Override
    public AppPremisesRecommendationDto getAppRecomDtoByAppCorrId(String appCorrId, String recomType) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appCorrId", appCorrId);
        paramMap.put("recomType", recomType);
        //AppPremisesRecommendationDto appPremisesRecommendationDto = RestApiUtil.getByReqParam("hcsa-config:8883/application-be/RescomDto/{appPremId}/{recomType}", paramMap, AppPremisesRecommendationDto.class);
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorrId,recomType).getEntity();
        return appPremisesRecommendationDto;
    }

    @Override
    public void submit(InspectionFillCheckListDto infillDto) {
        saveInspectionCheckListDto(infillDto);
    }


    public void saveInspectionCheckListDto(InspectionFillCheckListDto dto) {
        List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
        List<InspectionCheckListAnswerDto> answerDtoList = new ArrayList<>();
        InspectionCheckListAnswerDto answerDto = null;
        for (InspectionCheckQuestionDto temp : icqDtoList) {
            answerDto = new InspectionCheckListAnswerDto();
            answerDto.setAnswer(temp.getChkanswer());
            answerDto.setRemark(temp.getRemark());
            answerDto.setItemId(temp.getItemId());
            answerDto.setSectionName(temp.getSectionName());
            answerDtoList.add(answerDto);
        }
        String answerJson = JsonUtil.parseToJson(answerDtoList);
        String appPremCorrId = icqDtoList.get(0).getAppPreCorreId();
        String configId = icqDtoList.get(0).getConfigId();
        //AppPremisesPreInspectChklDto appDto = new AppPremisesPreInspectChklDto();
        AppPremisesPreInspectChklDto appDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,configId).getEntity();
        appDto.setAnswer(answerJson);
        appDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        appDto.setAppPremCorrId(appPremCorrId);
        appDto.setVersion(1 + appDto.getVersion());
        appDto.setChkLstConfId(configId);
        appDto.setStatus("CMSTAT001");
        appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId,"tcu").getEntity();
        //AppPremisesRecommendationDto appPreRecommentdationDto = new AppPremisesRecommendationDto();
        appPreRecommentdationDto.setAppPremCorreId(appPremCorrId);
        appPreRecommentdationDto.setRecomType("tcu");
        Date tcuDate = null;
        try {
            tcuDate = Formatter.parseDate(dto.getTuc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tcuDate != null) {
            appPreRecommentdationDto.setRecomInDate(tcuDate);
        }
        appPreRecommentdationDto.setAppPremCorreId(appPremCorrId);
        appPreRecommentdationDto.setBestPractice(dto.getBestPractice());
        appPreRecommentdationDto.setRemarks(dto.getTcuRemark());
        appPreRecommentdationDto.setStatus("CMSTAT001");
        appPreRecommentdationDto.setRecomType("tcu");
        appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
        appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        try {
            //RestApiUtil.postGetObject("hcsa-config:8883/iais-apppreinschkl-be/AppPremissChkl",appDto,AppPremisesPreInspectChklDto.class);
            fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
            //RestApiUtil.postGetObject("hcsa-config:8883/application-be/RescomDtoStorage",appPreRecommentdationDto,AppPremisesRecommendationDto.class);
            fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            AppPremPreInspectionNcDto appPremPreInspectionNcDto = getAppPremPreInspectionNcDto(dto,appPremCorrId);
            //appPremPreInspectionNcDto = RestApiUtil.postGetObject("hcsa-config:8883/iais-apppreinsnc-be/AppPremNcResult",appPremPreInspectionNcDto,AppPremPreInspectionNcDto.class);
            appPremPreInspectionNcDto = fillUpCheckListGetAppClient.saveAppPreNc(appPremPreInspectionNcDto).getEntity();
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = getAppPremisesPreInspectionNcItemDto(dto, appPremPreInspectionNcDto);
            //RestApiUtil.postGetList("hcsa-config:8883/iais-apppreinsncitem-be/AppPremNcItemResult",appPremisesPreInspectionNcItemDtoList,AppPremisesPreInspectionNcItemDto.class);
            fillUpCheckListGetAppClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<AppPremisesPreInspectionNcItemDto> getAppPremisesPreInspectionNcItemDto(InspectionFillCheckListDto dto, AppPremPreInspectionNcDto ncDto) {
        List<InspectionCheckQuestionDto> insqDtoList = dto.getCheckList();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = new ArrayList<>();
        for (InspectionCheckQuestionDto temp : insqDtoList) {
            AppPremisesPreInspectionNcItemDto ncItemDto = null;
            ncItemDto = new AppPremisesPreInspectionNcItemDto();
            ncItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ncItemDto.setItemId(temp.getItemId());
            ncItemDto.setPreNcId(ncDto.getId());
            if (temp.isRectified()) {
                ncItemDto.setIsRecitfied(1);
            } else {
                ncItemDto.setIsRecitfied(0);
            }
            ncItemDtoList.add(ncItemDto);
        }
        return ncItemDtoList;
    }

    public AppPremPreInspectionNcDto getAppPremPreInspectionNcDto(InspectionFillCheckListDto dto,String appPremCorrId) {
        AppPremPreInspectionNcDto ncDto =  fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        ncDto.setStatus("CMSTAT001");
        ncDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        ncDto.setVersion(1 + ncDto.getVersion());
        ncDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
        ncDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return ncDto;
    }

    public List<NcAnswerDto> getNcAnswerDtoList(String configId,String appPremCorrId){
        List<NcAnswerDto> ncAnswerDtoList = new ArrayList<>();
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,configId).getEntity();
        String answerStr = appPremisesPreInspectChklDto.getAnswer();
        List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answerStr, InspectionCheckListAnswerDto.class);
        NcAnswerDto ncAnswerDto = null;
        for(InspectionCheckListAnswerDto temp:answerDtoList){
            ncAnswerDto = new NcAnswerDto();
            ncAnswerDto.setItemId(temp.getItemId());
            ncAnswerDto.setRemark(temp.getRemark());
            ncAnswerDtoList.add(ncAnswerDto);
        }
        if(ncAnswerDtoList!=null &&!ncAnswerDtoList.isEmpty()){
            for(NcAnswerDto temp:ncAnswerDtoList){

                getFillNcAnswerDto(temp);
            }
        }
        return ncAnswerDtoList;
    }

    public void getFillNcAnswerDto(NcAnswerDto ncAnswerDto){
        String itemId = ncAnswerDto.getItemId();
        ChecklistItemDto cDto = hcsaChklClient.getChklItemById(itemId).getEntity();
        ncAnswerDto.setItemQuestion(cDto.getChecklistItem());
        HcsaChklSvcRegulationDto regDto = hcsaChklClient.getRegulationDtoById(cDto.getRegulationId()).getEntity();
        ncAnswerDto.setCaluseNo(regDto.getClauseNo());
        ncAnswerDto.setClause(regDto.getClause());
        //todo:adhoc
    }



}
