package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditAdhocCheckShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditAdhocItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditAdhocNcCehckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspectiNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremPreInspNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisemPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAdhocChklConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AuditCHeckListInspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.esotericsoftware.minlog.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/14 8:56
 */
@Service
@Slf4j
public class AuditCHeckListInspectionServiceImpl implements AuditCHeckListInspectionService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Override
    public List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String taskId, String configType) {
        List<InspectionFillCheckListDto> fillChkDtoList = null;
        TaskDto taskDto = taskService.getTaskById(taskId);
        String licPremId = null;
        if(taskDto!=null){
            licPremId = taskDto.getRefNo();
        }
        if(licPremId!=null){
            List<LicPremisemPreInspectChklDto> chkList = hcsaLicenceClient.getPremInsChklList(licPremId).getEntity();
            if(chkList!=null && !chkList.isEmpty()){
                fillChkDtoList = getServiceChkDtoListByAppPremId(chkList,licPremId,configType);
            }
        }
        return fillChkDtoList;
    }

    private List<InspectionFillCheckListDto> getServiceChkDtoListByAppPremId(List<LicPremisemPreInspectChklDto> chkList, String licPremCorrId, String conifgType) {
        List<InspectionFillCheckListDto> chkDtoList = new ArrayList<>();
        for(LicPremisemPreInspectChklDto temp:chkList){
            String configId  = temp.getChkListConifId();
            ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(configId).getEntity();
            InspectionFillCheckListDto fDto =null;
            if("common".equals(conifgType)&&dto.isCommon()){
                fDto = transferToInspectionCheckListDto(dto,licPremCorrId);
                fDto.setConfigId(temp.getChkListConifId());
                chkDtoList.add(fDto);
            }else if("service".equals(conifgType)&&!dto.isCommon()){
                fDto = transferToInspectionCheckListDto(dto,licPremCorrId);
                if(!StringUtil.isEmpty(dto.getSvcName())){
                    fDto.setSvcName(dto.getSvcName());
                }
                fDto.setConfigId(temp.getChkListConifId());
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
    public AuditAdhocCheckShowDto getAdhocDraftByLicPremId(String licPremid) {
        List<AuditAdhocItemDto> adhocItemList = hcsaLicenceClient.getAdhocByPremId(licPremid).getEntity();
        AuditAdhocCheckShowDto adShowDto = new AuditAdhocCheckShowDto();
        List<AuditAdhocNcCehckItemDto> adhocNcCheckItemDtoList = new ArrayList<>();
        if(adhocItemList!=null&&!adhocItemList.isEmpty()){
            AuditAdhocNcCehckItemDto addto = null;
            for(AuditAdhocItemDto temp:adhocItemList){
                addto = transfertoadNcItemDto(temp);
                adhocNcCheckItemDtoList.add(addto);
            }
            adShowDto.setAdItemList(adhocNcCheckItemDtoList);
        }
        return adShowDto;
    }

    public AuditAdhocNcCehckItemDto transfertoadNcItemDto(AuditAdhocItemDto dto){
        AuditAdhocNcCehckItemDto adto  = new AuditAdhocNcCehckItemDto();
        adto.setId(dto.getId());
        adto.setAnswer(dto.getAnswer());
        adto.setAnswerType(dto.getAnswerType());
        adto.setNonCompliant(dto.getNonCompliant());
        adto.setOrder(dto.getOrder());
        adto.setQuestion(dto.getQuestion());
        adto.setRectified(dto.getRectified());
        adto.setRiskLevel(dto.getRiskLevel());
        return adto;

    }

    @Override
    public String getInspectionDate(String licPremId) {
        LicPremisesRecommendationDto dto = hcsaLicenceClient.getLicPremRecordByIdAndType(licPremId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        String inspectionDate = null;
        if(dto!=null){
            try {
                inspectionDate = Formatter.formatDate(dto.getRecomInDate());
            }catch (Exception e){
                Log.debug(e.toString());
            }
        }
        return inspectionDate;
    }

    @Override
    public List<String> getInspectiors(TaskDto taskDto) {
        List<String> inspectiors = new ArrayList<>();
        String workGrpId = taskDto.getWkGrpId();
        List<OrgUserDto> orgDtos = organizationClient.getUsersByWorkGroupName(workGrpId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        if(!IaisCommonUtils.isEmpty(orgDtos)){
            for(OrgUserDto temp:orgDtos){
                inspectiors.add(temp.getDisplayName());
            }
        }
        return inspectiors;
    }

    @Override
    public String getInspectionLeader(TaskDto taskDto) {
        String workGrpId = taskDto.getWkGrpId();
        List<String> leaders = null;
        String leaderStr = null;
        leaders =  organizationClient.getInspectionLead(workGrpId).getEntity();
        if(!IaisCommonUtils.isEmpty(leaders)){
            for(String temp:leaders){
                OrgUserDto userDto = organizationClient.retrieveOrgUserAccountById(temp).getEntity();
                leaderStr = userDto.getDisplayName()+" ";
            }
        }
        return leaderStr;
    }

    @Override
    public boolean isHaveNcOrBestPractice(InspectionFDtosDto serListDto, InspectionFillCheckListDto comDto, AuditAdhocCheckShowDto showDto) {
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

    private boolean haveAdhocDto(AuditAdhocCheckShowDto showDto) {
        if(showDto!=null){
            if(!IaisCommonUtils.isEmpty(showDto.getAdItemList())){
                for(AuditAdhocNcCehckItemDto temp:showDto.getAdItemList()){
                    if("No".equals(temp.getAdAnswer())){
                        return true;
                    }
                }
            }
        }
        return false;
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
    public void submit(InspectionFillCheckListDto commDto, AuditAdhocCheckShowDto showDto, InspectionFDtosDto serListDto, String premId) {
        if(commDto!=null){
            saveInspectionCheckListDto(commDto,premId);
        }
        saveSerListDto(serListDto,premId);
        saveAdhocDto(showDto,premId);
        saveInspectionDate(serListDto,premId);
        saveStartTime(serListDto,premId);
        saveEndTime(serListDto,premId);
        saveOtherInspection(serListDto,premId);
        saveRecommend(serListDto,premId);
        List<InspectionFillCheckListDto> fillcheckDtoList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                fillcheckDtoList.add(temp);
            }
        }
        if(commDto!=null&&commDto.getCheckList()!=null&&!commDto.getCheckList().isEmpty()){
            fillcheckDtoList.add(commDto);
        }
        if(!fillcheckDtoList.isEmpty()){
            saveNcItem(fillcheckDtoList,premId);
        }
    }

    private void saveAdhocDto(AuditAdhocCheckShowDto showDto, String premId) {
        List<AuditAdhocNcCehckItemDto>  itemDtoList = showDto.getAdItemList();
        List<AuditAdhocItemDto> saveItemDtoList = new ArrayList<>();
        LicPremisesAdhocChklConfigDto dto = hcsaLicenceClient.getAdhocConfigByPremCorrId(premId).getEntity();
        if(dto!=null){
            dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                dto.setVersion(dto.getVersion()+1);
                dto.setId(null);
                dto = hcsaLicenceClient.saveAuditAdhocConfig(dto).getEntity();
                for(AuditAdhocNcCehckItemDto temp:itemDtoList){
                    temp.setLicPremCorreId(dto.getId());
                    temp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    temp.setId(null);
                    AdhocAnswerDto adhocAnswerDto = new AdhocAnswerDto();
                    adhocAnswerDto.setRemark(temp.getRemark());
                    adhocAnswerDto.setAnswer(temp.getAdAnswer());
                    String saveAnswer = JsonUtil.parseToJson(adhocAnswerDto);
                    temp.setAnswer(saveAnswer);
                    saveItemDtoList.add(temp);
                }
                hcsaLicenceClient.saveAdhocItems(saveItemDtoList).getEntity();
            }
        }
    }

    private void saveSerListDto(InspectionFDtosDto serListDto, String appPremId) {
        List<InspectionFillCheckListDto> dtoList = serListDto.getFdtoList();
        if (dtoList != null && !dtoList.isEmpty()) {
            for (InspectionFillCheckListDto temp : dtoList) {
                saveInspectionCheckListDto(temp, appPremId);
            }
        }
    }

    public void saveInspectionCheckListDto(InspectionFillCheckListDto dto,String appPremId) {
        boolean ncflag = isHaveNc(dto);
        if (dto.getCheckList()!=null && !dto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
            String premId = appPremId;
            String configId = icqDtoList.get(0).getConfigId();
            LicPremisemPreInspectChklDto licDto = hcsaLicenceClient.getLicPremInspeChlkBypremIdAndConfigId(appPremId,configId).getEntity();

            if(licDto!=null ){
                licDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                hcsaLicenceClient.updateLicPreInspChkl(licDto);
                licDto.setVersion(1 + Integer.parseInt(licDto.getVersion())+"");
            }else{
                licDto= new LicPremisemPreInspectChklDto();
                licDto.setVersion(1+"");
            }
            //update
            licDto.setId(null);
            licDto.setLicPremId(appPremId);
            licDto.setChkListConifId(configId);
            licDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            licDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            try {
                List<InspectionCheckListAnswerDto> answerDtoList = new ArrayList<>();
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
                licDto.setAnswer(answerJson);
                hcsaLicenceClient.saveLicPreInspChkl(licDto);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public void saveInspectionDate(InspectionFDtosDto serListDto, String appPremId) {
        String inspectionDate = null;
        if(serListDto!=null){
            inspectionDate = serListDto.getInspectionDate();
            if(!StringUtil.isEmpty(inspectionDate)){
                LicPremisesRecommendationDto appPreRecommentdationDto = hcsaLicenceClient.getLicPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    hcsaLicenceClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setId(null);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                }else{
                    appPreRecommentdationDto = new LicPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setLicPremId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                appPreRecommentdationDto.setRecomDecision(inspectionDate);
                Date insDate = null;
                try {
                    insDate = Formatter.parseDate(inspectionDate);
                }catch (Exception e){
                    Log.debug(e.toString());

                }
                appPreRecommentdationDto.setRecomInDate(insDate);
                hcsaLicenceClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveStartTime(InspectionFDtosDto serListDto, String appPremId) {
        String startTime = null;
        if(serListDto!=null){
            startTime = serListDto.getStartTime();
            if(!StringUtil.isEmpty(serListDto.getStartHour())&&!StringUtil.isEmpty(serListDto.getStartMin())){
                LicPremisesRecommendationDto appPreRecommentdationDto = hcsaLicenceClient.getLicPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    hcsaLicenceClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new LicPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setLicPremId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME);
                appPreRecommentdationDto.setRecomDecision(startTime);
                hcsaLicenceClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    public void saveEndTime(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto!=null){
            String endTime = serListDto.getEndTime();
            if(!StringUtil.isEmpty(serListDto.getEndHour())&&!StringUtil.isEmpty(serListDto.getEndMin())){
                LicPremisesRecommendationDto appPreRecommentdationDto = hcsaLicenceClient.getLicPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    hcsaLicenceClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new LicPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setLicPremId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME);
                appPreRecommentdationDto.setRecomDecision(endTime);
                hcsaLicenceClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveOtherInspection(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto!=null){
            if(!StringUtil.isEmpty(serListDto.getOtherinspectionofficer())){
                LicPremisesRecommendationDto appPreRecommentdationDto = hcsaLicenceClient.getLicPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    hcsaLicenceClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new LicPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);

                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setLicPremId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS);
                appPreRecommentdationDto.setRemarks(serListDto.getOtherinspectionofficer());
                hcsaLicenceClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveRecommend(InspectionFDtosDto serListDto,String appPremId) {
        LicPremisesRecommendationDto appPreRecommentdationDto = hcsaLicenceClient.getLicPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_TCU).getEntity();
        if(appPreRecommentdationDto!=null){
            appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            hcsaLicenceClient.updateAppRecom(appPreRecommentdationDto);
            appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
        }else{
            appPreRecommentdationDto = new LicPremisesRecommendationDto();
            appPreRecommentdationDto.setVersion(1);
        }
        appPreRecommentdationDto.setLicPremId(appPremId);
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
        appPreRecommentdationDto.setLicPremId(appPremId);
        appPreRecommentdationDto.setBestPractice(serListDto.getBestPractice());
        appPreRecommentdationDto.setRemarks(serListDto.getTcuRemark());
        appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
        appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        hcsaLicenceClient.saveAppRecom(appPreRecommentdationDto);
    }


    private void saveNcItem(List<InspectionFillCheckListDto> fillcheckDtoList, String appPremId) {
        int ncflagNum = 0;
        for(InspectionFillCheckListDto temp:fillcheckDtoList){
            if(isHaveNc(temp)){
                ncflagNum++;
            }
        }
        List<LicPremPreInspNcItemDto> appPremisesPreInspectionNcItemDtoList = null;
        if(ncflagNum>0){
            LicPremInspectiNcDto appPremPreInspectionNcDto = getAppPremPreInspectionNcDto(appPremId);
            appPremPreInspectionNcDto = hcsaLicenceClient.saveAppPreNc(appPremPreInspectionNcDto).getEntity();
            appPremisesPreInspectionNcItemDtoList = getAppPremisesPreInspectionNcItemDto(fillcheckDtoList, appPremPreInspectionNcDto);
            hcsaLicenceClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtoList);
        }
    }

    public LicPremInspectiNcDto getAppPremPreInspectionNcDto(String appPremCorrId) {
        LicPremInspectiNcDto ncDto =  new LicPremInspectiNcDto();
        ncDto = hcsaLicenceClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        if(ncDto!=null){
            ncDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            hcsaLicenceClient.updateAppPreNc(ncDto);
            ncDto.setVersion(1 + Integer.parseInt(ncDto.getVersion())+"");
        }else{
            ncDto =  new LicPremInspectiNcDto();
            ncDto.setVersion(1+"");
        }
        ncDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        ncDto.setId(null);
        ncDto.setLicPremId(appPremCorrId);
        ncDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return ncDto;
    }

    public List<LicPremPreInspNcItemDto> getAppPremisesPreInspectionNcItemDto(List<InspectionFillCheckListDto> fillcheckDtoList, LicPremInspectiNcDto ncDto) {
        List<LicPremPreInspNcItemDto> ncItemDtoList =  new ArrayList<>();
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

    public void getAppNcByTemp(InspectionCheckQuestionDto temp, LicPremInspectiNcDto ncDto,List<LicPremPreInspNcItemDto> ncItemDtoList){
        if("No".equals(temp.getChkanswer())){
            LicPremPreInspNcItemDto ncItemDto = null;
            ncItemDto = new LicPremPreInspNcItemDto();
            ncItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ncItemDto.setItemId(temp.getItemId());
            ncItemDto.setPreNcId(ncDto.getId());
            if (temp.isRectified()) {
                ncItemDto.setIsRectified(1);
            } else {
                ncItemDto.setIsRectified(0);
            }
            ncItemDtoList.add(ncItemDto);
        }
    }


}
