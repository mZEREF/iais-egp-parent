package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListCilent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FillupChklistServiceImpl implements FillupChklistService {
    @Autowired
    private FillUpCheckListCilent fillUpCheckListCilent;
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
        //List<ChecklistQuestionDto> cDtoList = fillUpCheckListCilent.getcheckListQuestionDtoList("CLB","Self-Assessment").getEntity();
        Map<String,Object> paramMap= new HashMap<> ();
        paramMap.put("svcCode",svcCode);
        paramMap.put("svcType",svcType);
        List<ChecklistQuestionDto> cDtoList = RestApiUtil.getListByReqParam("hcsa-config:8878/iais-hcsa-checklist/config/results/{svcCode}/{svcType}",paramMap,ChecklistQuestionDto.class);
        InspectionFillCheckListDto infillCheckListDto = new InspectionFillCheckListDto();
        if(cDtoList!=null && !cDtoList.isEmpty()){
            List<InspectionCheckQuestionDto> cqDtoList = new ArrayList<>();
            for(ChecklistQuestionDto temp:cDtoList){
                InspectionCheckQuestionDto inspectionCheckQuestionDto = null;
                inspectionCheckQuestionDto = transferQuestionDtotoInDto(temp);
                cqDtoList.add(inspectionCheckQuestionDto);
            }
            infillCheckListDto.setCheckList(cqDtoList);
            getInspectionFillCheckListDto(infillCheckListDto);
            return infillCheckListDto;
        }
        return null;
    }
    public InspectionFillCheckListDto getInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto){
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
        return icDto;
    }
}
