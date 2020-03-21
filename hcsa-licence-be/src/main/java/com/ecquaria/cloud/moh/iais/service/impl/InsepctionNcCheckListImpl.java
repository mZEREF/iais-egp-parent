package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.esotericsoftware.minlog.Log;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private FillupChklistService fillupChklistService;
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;
    @Override
    public InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto, AppPremisesPreInspectChklDto appPremDto, List<AppPremisesPreInspectionNcItemDto> itemDtoList, AppPremisesRecommendationDto appPremisesRecommendationDto) {
        List<InspectionCheckQuestionDto> fillCheckList = infillDto.getCheckList();
        List<InspectionCheckQuestionDto> ncCheckList = IaisCommonUtils.genNewArrayList();
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
                        if("1".equals(answerDtoList.get(i).getIsRec())&&"No".equals(temp.getChkanswer())){
                            temp.setRectified(true);
                        }else{
                            temp.setRectified(false);
                        }
                        ncCheckList.add(temp);
                    }
                }
            }
        }
        infillDto.setBestPractice(appPremisesRecommendationDto.getBestPractice());
        infillDto.setTuc(Formatter.formatDate(appPremisesRecommendationDto.getRecomInDate()));
        infillDto.setTcuRemark(appPremisesRecommendationDto.getRemarks());
        infillDto.setCheckList(ncCheckList);
        fillInspectionFillCheckListDto(infillDto);
        return infillDto;
    }


    public InspectionFillCheckListDto fillInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto) {
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        List<SectionDto> sectionDtoList = IaisCommonUtils.genNewArrayList();
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
            List<ItemDto> itemDtoList = IaisCommonUtils.genNewArrayList();
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
            taskId = "DF1C07EE-191E-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = null;
        if (taskDto != null) {
            appPremCorrId = taskDto.getRefNo();
        }
        Map<String, Object> appCklMap = IaisCommonUtils.genNewHashMap();
        appCklMap.put("appPremId", appPremCorrId);
        appCklMap.put("configId", configId);
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,configId).getEntity();
        return appPremisesPreInspectChklDto;
    }

    @Override
    public List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId) {
        AppPremPreInspectionNcDto ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appCorrId).getEntity();
        String ncId = ncDto.getId();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
        return ncItemDtoList;
    }

    @Override
    public AppPremisesRecommendationDto getAppRecomDtoByAppCorrId(String appCorrId, String recomType) {
        Map<String, Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("appCorrId", appCorrId);
        paramMap.put("recomType", recomType);
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorrId,recomType).getEntity();
        return appPremisesRecommendationDto;
    }

    @Override
    public void submit(InspectionFillCheckListDto commDto,AdCheckListShowDto showDto, InspectionFDtosDto serListDto,String appPremId) {
        if(commDto!=null){
            saveInspectionCheckListDto(commDto,appPremId);
        }
        saveSerListDto(serListDto,appPremId);
        saveAdhocDto(showDto,appPremId);
        saveInspectionDate(serListDto,appPremId);
        saveStartTime(serListDto,appPremId);
        saveEndTime(serListDto,appPremId);
        saveOtherInspection(serListDto,appPremId);
        saveRecommend(serListDto,appPremId);
        List<InspectionFillCheckListDto> fillcheckDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                fillcheckDtoList.add(temp);
            }
        }
        if(commDto!=null&&commDto.getCheckList()!=null&&!commDto.getCheckList().isEmpty()){
            fillcheckDtoList.add(commDto);
        }
        if(!fillcheckDtoList.isEmpty()){
            saveNcItem(fillcheckDtoList,appPremId);
        }
    }

    public void saveInspectionDate(InspectionFDtosDto serListDto, String appPremId) {
        String inspectionDate = null;
        if(serListDto!=null){
            inspectionDate = serListDto.getInspectionDate();
            if(!StringUtil.isEmpty(inspectionDate)){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setId(null);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                appPreRecommentdationDto.setRecomDecision(inspectionDate);
                Date insDate = null;
                try {
                    insDate = Formatter.parseDate(inspectionDate);
                }catch (Exception e){
                    Log.debug(e.toString());

                }
                appPreRecommentdationDto.setRecomInDate(insDate);
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveStartTime(InspectionFDtosDto serListDto, String appPremId) {
        String startTime = null;
        if(serListDto!=null){
            startTime = serListDto.getStartTime();
            if(!StringUtil.isEmpty(serListDto.getStartHour())&&!StringUtil.isEmpty(serListDto.getStartMin())){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME);
                appPreRecommentdationDto.setRecomDecision(startTime);
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }
    public void saveEndTime(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto!=null){
            String endTime = serListDto.getEndTime();
            if(!StringUtil.isEmpty(serListDto.getEndHour())&&!StringUtil.isEmpty(serListDto.getEndMin())){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME);
                appPreRecommentdationDto.setRecomDecision(endTime);
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveOtherInspection(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto!=null){
            if(!StringUtil.isEmpty(serListDto.getOtherinspectionofficer())){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);

                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS);
                appPreRecommentdationDto.setRemarks(serListDto.getOtherinspectionofficer());
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveNcItem(List<InspectionFillCheckListDto> fillcheckDtoList, String appPremId) {
        int ncflagNum = 0;
        for(InspectionFillCheckListDto temp:fillcheckDtoList){
            if(isHaveNc(temp)){
                ncflagNum++;
            }
        }
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = null;
        if(ncflagNum>0){
            AppPremPreInspectionNcDto appPremPreInspectionNcDto = getAppPremPreInspectionNcDto(appPremId);
            appPremPreInspectionNcDto = fillUpCheckListGetAppClient.saveAppPreNc(appPremPreInspectionNcDto).getEntity();
            appPremisesPreInspectionNcItemDtoList = getAppPremisesPreInspectionNcItemDto(fillcheckDtoList, appPremPreInspectionNcDto);
            fillUpCheckListGetAppClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtoList);
        }
    }


    private void saveRecommend(InspectionFDtosDto serListDto,String appPremId) {
        AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_TCU).getEntity();
        if(appPreRecommentdationDto!=null){
            appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
            appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
        }else{
            appPreRecommentdationDto = new AppPremisesRecommendationDto();
            appPreRecommentdationDto.setVersion(1);
        }
        appPreRecommentdationDto.setAppPremCorreId(appPremId);

        appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
        Date tcuDate = null;
        try {
            tcuDate = Formatter.parseDate(serListDto.getTuc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tcuDate != null) {
            appPreRecommentdationDto.setRecomInDate(tcuDate);
        }
        appPreRecommentdationDto.setId(null);
        appPreRecommentdationDto.setAppPremCorreId(appPremId);
        appPreRecommentdationDto.setBestPractice(serListDto.getBestPractice());
        appPreRecommentdationDto.setRemarks(serListDto.getTcuRemark());
        appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
        appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
    }

    private void saveSerListDto(InspectionFDtosDto serListDto,String appPremId) {
        List<InspectionFillCheckListDto> dtoList = serListDto.getFdtoList();
        if(dtoList!=null &&! dtoList.isEmpty()){
            for(InspectionFillCheckListDto temp:dtoList){
                saveInspectionCheckListDto(temp,appPremId);
            }
        }
    }

    public void saveAdhocDto(AdCheckListShowDto showDto,String appPremId){
        List<AdhocNcCheckItemDto>  itemDtoList = showDto.getAdItemList();
        List<AdhocChecklistItemDto> saveItemDtoList = IaisCommonUtils.genNewArrayList();
        AdhocCheckListConifgDto dto = applicationClient.getAdhocConfigByAppPremCorrId(appPremId).getEntity();
        if(dto!=null){
            dto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                applicationClient.updateAppAdhocConfig(dto);
                dto.setVersion(dto.getVersion()+1);
                dto.setId(null);
                dto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
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
    }

    public void saveInspectionCheckListDto(InspectionFillCheckListDto dto,String appPremId) {
        boolean ncflag = isHaveNc(dto);
        if (dto.getCheckList()!=null && !dto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
            String appPremCorrId = appPremId;
            String configId = icqDtoList.get(0).getConfigId();
            AppPremisesPreInspectChklDto appDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,configId).getEntity();
            if(appDto!=null ){
                appDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                fillUpCheckListGetAppClient.updateAppPreInspChkl(appDto);
                appDto.setVersion(1 + Integer.parseInt(appDto.getVersion())+"");
            }else{
                appDto= new AppPremisesPreInspectChklDto();
                appDto.setVersion(1+"");
            }
            //update
            appDto.setId(null);
            appDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
            appDto.setAppPremCorrId(appPremCorrId);
            appDto.setChkLstConfId(configId);
            appDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            try {
                List<InspectionCheckListAnswerDto> answerDtoList = IaisCommonUtils.genNewArrayList();
                InspectionCheckListAnswerDto answerDto = null;
                for(int i =0;i<icqDtoList.size();i++){
                    answerDto = new InspectionCheckListAnswerDto();
                    answerDto.setAnswer(icqDtoList.get(i).getChkanswer());
                    answerDto.setRemark(icqDtoList.get(i).getRemark());
                    answerDto.setItemId(icqDtoList.get(i).getItemId());
                    if("No".equals(icqDtoList.get(i).getChkanswer())&&ncflag&&icqDtoList.get(i).isRectified()){
                        answerDto.setIsRec("1");
                    }else{
                        answerDto.setIsRec("0");
                    }
                    answerDto.setSectionName(icqDtoList.get(i).getSectionName());
                    answerDtoList.add(answerDto);
                }
                String answerJson = JsonUtil.parseToJson(answerDtoList);
                appDto.setAnswer(answerJson);
                fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public List<AppPremisesPreInspectionNcItemDto> getAppPremisesPreInspectionNcItemDto(List<InspectionFillCheckListDto> fillcheckDtoList, AppPremPreInspectionNcDto ncDto) {
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList =  IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(fillcheckDtoList)){
            for(InspectionFillCheckListDto dto:fillcheckDtoList){
                List<InspectionCheckQuestionDto> insqDtoList = dto.getCheckList();
                for (InspectionCheckQuestionDto temp : insqDtoList) {
                    getAppNcByTemp(temp,ncDto,ncItemDtoList);
                }
            }
        }
        return ncItemDtoList;
    }

    public void getAppNcByTemp(InspectionCheckQuestionDto temp, AppPremPreInspectionNcDto ncDto,List<AppPremisesPreInspectionNcItemDto> ncItemDtoList){
        if("No".equals(temp.getChkanswer())){
            AppPremisesPreInspectionNcItemDto ncItemDto = null;
            ncItemDto = new AppPremisesPreInspectionNcItemDto();
            ncItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ncItemDto.setItemId(temp.getItemId());
            ncItemDto.setPreNcId(ncDto.getId());
            ncItemDto.setFeRectifiedFlag(0);
            if (temp.isRectified()) {
                ncItemDto.setIsRecitfied(1);
            } else {
                ncItemDto.setIsRecitfied(0);
            }
            ncItemDtoList.add(ncItemDto);
        }
    }

    public AppPremPreInspectionNcDto getAppPremPreInspectionNcDto(String appPremCorrId) {
        AppPremPreInspectionNcDto ncDto =  new AppPremPreInspectionNcDto();
        ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        if(ncDto!=null){
            ncDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            fillUpCheckListGetAppClient.updateAppPreNc(ncDto);
            ncDto.setVersion(1 + Integer.parseInt(ncDto.getVersion())+"");
        }else{
            ncDto =  new AppPremPreInspectionNcDto();
            ncDto.setVersion(1+"");
        }
        ncDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        ncDto.setId(null);
        ncDto.setAppPremCorrId(appPremCorrId);
        ncDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return ncDto;
    }
    @Override
    public List<NcAnswerDto> getNcAnswerDtoList(String appPremCorrId){
        List<NcAnswerDto> serviceNcDtoList = getServiceNcDtoList(appPremCorrId);
        List<NcAnswerDto> commonNcDtoList = getCommonNcDtoList(appPremCorrId);
        List<NcAnswerDto> ncAnswerDtoList = IaisCommonUtils.genNewArrayList();
        if(serviceNcDtoList!=null && !serviceNcDtoList.isEmpty()){
            for(NcAnswerDto temp:serviceNcDtoList){
                ncAnswerDtoList.add(temp);
            }
        }
        if(commonNcDtoList!=null&&!commonNcDtoList.isEmpty()){
            for(NcAnswerDto temp:commonNcDtoList){
                ncAnswerDtoList.add(temp);
            }
        }
        //adhoc
        getAdhocNcItem(appPremCorrId,ncAnswerDtoList);
        return ncAnswerDtoList;

    }

    private List<NcAnswerDto> getCommonNcDtoList(String appPremCorrId) {
        List<NcAnswerDto> ncAnswerDtoList = IaisCommonUtils.genNewArrayList();
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        List<AppPremisesPreInspectChklDto> commonChklList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(chkList)){
            for(AppPremisesPreInspectChklDto temp:chkList){
                ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(temp.getChkLstConfId()).getEntity();
                if(dto!=null&&dto.isCommon()){
                    commonChklList.add(temp);
                }
            }
        }
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto =null;
        if(!IaisCommonUtils.isEmpty(commonChklList)){
            appPremisesPreInspectChklDto = commonChklList.get(0);
        }
        if(appPremisesPreInspectChklDto!=null){
            String answerStr = appPremisesPreInspectChklDto.getAnswer();
            List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answerStr, InspectionCheckListAnswerDto.class);
            getCommNcDto(answerDtoList,ncAnswerDtoList);
        }
        return ncAnswerDtoList;
    }

    public void getCommNcDto( List<InspectionCheckListAnswerDto> answerDtoList,List<NcAnswerDto> ncAnswerDtoList){
        NcAnswerDto ncAnswerDto = null;
        for(InspectionCheckListAnswerDto temp:answerDtoList){
            if("No".equals(temp.getAnswer())){
                ncAnswerDto = new NcAnswerDto();
                ncAnswerDto.setItemId(temp.getItemId());
                ncAnswerDto.setRemark(temp.getRemark());
                ncAnswerDtoList.add(ncAnswerDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(ncAnswerDtoList)){
            for(NcAnswerDto temp:ncAnswerDtoList){
                getFillNcAnswerDto(temp);
            }
        }
    }



    private List<NcAnswerDto> getServiceNcDtoList(String appPremCorrId) {
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        List<AppPremisesPreInspectChklDto> serviceChklList = IaisCommonUtils.genNewArrayList();
        if(chkList!=null&&!chkList.isEmpty()){
            for(AppPremisesPreInspectChklDto temp:chkList){
                ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(temp.getChkLstConfId()).getEntity();
                if(dto!=null&&!dto.isCommon()){
                    serviceChklList.add(temp);
                }
            }
        }
        List<NcAnswerDto> ncList = null;
        if(!serviceChklList.isEmpty()){
            ncList = getNcList(serviceChklList);
        }
        return ncList;

    }

    private List<NcAnswerDto> getNcList(List<AppPremisesPreInspectChklDto> serviceChklList) {
        List<NcAnswerDto> ncAnswerDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesPreInspectChklDto achk:serviceChklList){
            if(achk!=null){
                String answerStr = achk.getAnswer();
                List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answerStr, InspectionCheckListAnswerDto.class);
                if(!IaisCommonUtils.isEmpty(answerDtoList)){
                    getServiceNc(answerDtoList,ncAnswerDtoList);
                }
            }
        }
        return ncAnswerDtoList;
    }

    public void getServiceNc(List<InspectionCheckListAnswerDto> answerDtoList,List<NcAnswerDto> ncAnswerDtoList){
        NcAnswerDto ncAnswerDto = null;
        for(InspectionCheckListAnswerDto temp:answerDtoList){
            if("No".equals(temp.getAnswer())){
                ncAnswerDto = new NcAnswerDto();
                ncAnswerDto.setItemId(temp.getItemId());
                ncAnswerDto.setRemark(temp.getRemark());
                ncAnswerDtoList.add(ncAnswerDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(ncAnswerDtoList)){
            for(NcAnswerDto temp:ncAnswerDtoList){
                getFillNcAnswerDto(temp);
            }
        }
    }

    public void getAdhocNcItem(String appPremId ,List<NcAnswerDto> ncAnswerDtoList){
        AdCheckListShowDto adchklDto =getAdhocCheckListDto(appPremId);
        NcAnswerDto ncDto = null;
        List<AdhocNcCheckItemDto> adItemList = adchklDto.getAdItemList();
        if(adItemList!=null && !adItemList.isEmpty()){
            for(AdhocNcCheckItemDto temp:adItemList){
                if("No".equals(temp.getAdAnswer())){
                    ncDto = new NcAnswerDto();
                    ncDto.setClause(temp.getQuestion());
                    ncDto.setRemark(temp.getRemark());
                    ncAnswerDtoList.add(ncDto);
                }
            }
        }
    }
    public void getFillNcAnswerDto(NcAnswerDto ncAnswerDto){
        String itemId = ncAnswerDto.getItemId();
        ChecklistItemDto cDto = hcsaChklClient.getChklItemById(itemId).getEntity();
        ncAnswerDto.setItemQuestion(cDto.getChecklistItem());
        HcsaChklSvcRegulationDto regDto = hcsaChklClient.getRegulationDtoById(cDto.getRegulationId()).getEntity();
        ncAnswerDto.setCaluseNo(regDto.getClauseNo());
        ncAnswerDto.setClause(regDto.getClause());
    }

    @Override
    public void updateTaskStatus(ApplicationDto applicationDto,String appPremCorrId) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    @Override
    public void getCommonDto(InspectionFillCheckListDto commonDto, AppPremisesPreInspectChklDto appPremDto) {
        List<InspectionCheckQuestionDto> fillCheckList = commonDto.getCheckList();
        List<InspectionCheckQuestionDto> ncCheckList = IaisCommonUtils.genNewArrayList();
        String answer = appPremDto.getAnswer();
        List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answer, InspectionCheckListAnswerDto.class);
        if (!IaisCommonUtils.isEmpty(fillCheckList)) {
            for (InspectionCheckQuestionDto temp : fillCheckList) {
                for (int i = 0; i < answerDtoList.size(); i++) {
                    getComAnswer(temp,answerDtoList,ncCheckList, i);
                }
            }
        }
        commonDto.setCheckList(ncCheckList);
        fillInspectionFillCheckListDto(commonDto);
    }
    public void getComAnswer(InspectionCheckQuestionDto temp,List<InspectionCheckListAnswerDto> answerDtoList,List<InspectionCheckQuestionDto> ncCheckList,int i){
        if (temp.getItemId().equals(answerDtoList.get(i).getItemId()) &&
                temp.getSectionName().equals(answerDtoList.get(i).getSectionName())) {
            temp.setChkanswer(answerDtoList.get(i).getAnswer());
            temp.setRemark(answerDtoList.get(i).getRemark());
            if(getComAnswerLogic(answerDtoList.get(i).getIsRec(),answerDtoList.get(i).getAnswer())){
                temp.setRectified(true);
            }else{
                temp.setRectified(false);
            }
            ncCheckList.add(temp);
        }
    }

    public boolean getComAnswerLogic(String recNum,String answer){
        if("1".equals(recNum)&&"No".equals(answer)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public AdCheckListShowDto getAdhocCheckListDto(String appPremCorrId) {
        AdCheckListShowDto adCheckListShowDto = fillupChklistService.getAdhoc(appPremCorrId);
        if(adCheckListShowDto!=null){
            List<AdhocNcCheckItemDto> adItemList = adCheckListShowDto.getAdItemList();
            if(adItemList!=null && !adItemList.isEmpty()){
                for(AdhocNcCheckItemDto temp:adItemList){
                    String answerStr = temp.getAnswer();
                    AdhocAnswerDto answerDto = JsonUtil.parseToObject(answerStr,AdhocAnswerDto.class);
                    temp.setAdAnswer(answerDto.getAnswer());
                    temp.setRemark(answerDto.getRemark());
                }
            }
        }
        return adCheckListShowDto;
    }

    public boolean isHaveNc(InspectionFillCheckListDto dto){
        if(dto!=null){
            List<InspectionCheckQuestionDto> dtoList = dto.getCheckList();
            if(!IaisCommonUtils.isEmpty(dtoList)){
                for(InspectionCheckQuestionDto temp:dtoList){
                    if("No".equals(temp.getChkanswer())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isHaveNcOrBestPractice(InspectionFDtosDto serListDto, InspectionFillCheckListDto comDto, AdCheckListShowDto showDto) {
        boolean serviceNcFlag = haveServiceNc(serListDto);
        boolean comNcFlag = haveComNc(comDto);
        boolean adhocNcFlag = haveAdhocDto(showDto);
        if(serListDto!=null){
            String bestPractice = serListDto.getBestPractice();
            if(serviceNcFlag||comNcFlag||adhocNcFlag||!StringUtil.isEmpty(bestPractice)){
                return true;
            }
        }
        return false;
    }

    private boolean haveAdhocDto(AdCheckListShowDto showDto) {
        if(showDto!=null){
            if(!IaisCommonUtils.isEmpty(showDto.getAdItemList())){
                for(AdhocNcCheckItemDto temp:showDto.getAdItemList()){
                    if("No".equals(temp.getAdAnswer())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean haveComNc(InspectionFillCheckListDto comDto) {
        return isHaveNc(comDto);
    }

    private boolean haveServiceNc(InspectionFDtosDto serListDto) {
        int ncflagNum = 0;
        if(serListDto!=null){
            if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
                for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                    if(isHaveNc(temp)){
                        ncflagNum++;
                    }
                }
            }
        }
        if(ncflagNum>0){
            return true;
        }
        return false;
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

    // only for show check list
    @Override
    public  void  getInspectionFillCheckListDtoForShow(InspectionFillCheckListDto inspectionFillCheckListDto){
        if(inspectionFillCheckListDto != null){
            inspectionFillCheckListDto.setSvcNameShow( StringUtil.getFilterSpecialCharacter( inspectionFillCheckListDto.getSubName()));
            List<SectionDto> sectionDtos = inspectionFillCheckListDto.getSectionDtoList();
            if(sectionDtos != null && sectionDtos.size() > 0){
                for(SectionDto sectionDto : sectionDtos){
                    if(sectionDto != null && sectionDto.getItemDtoList() != null && sectionDto.getItemDtoList().size() > 0){
                        List<ItemDto> itemDtoList = sectionDto.getItemDtoList();
                        for(ItemDto itemDto : itemDtoList){
                            if(itemDto.getIncqDto() != null)
                                itemDto.getIncqDto().setSectionNameShow(StringUtil.getFilterSpecialCharacter(itemDto.getIncqDto().getSectionNameSub()));
                        }
                    }
                }
            }
        }
    }
}
