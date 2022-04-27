package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AnswerForDifDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MODULE_VIEW_NEW_FACILITY;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_FOLLOW_UP_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;

@Service
@Slf4j
public class InspectionService {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;


    public InspectionService(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;

    }

    public void getInitFollowUpData(HttpServletRequest request,ReviewInsFollowUpDto dto){
        if (!StringUtils.hasLength(dto.getAppId())) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            // facility info
            dto = inspectionClient.getInitInsFollowUpData(appId);
            dto.setAppId(appId);
            ParamUtil.setSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO, dto);

        }
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        //support doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, dto.getSupportDocDisplayDtoList());
        // facility details
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, dto.getFacilityDetailsInfo());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, dto.getProcessHistoryDtoList());
        // view application need appId and moduleType
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, dto.getInsAppId());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, MODULE_VIEW_NEW_FACILITY);
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(dto.getSupportDocDisplayDtoList());
        repoIdDocNameMap.putAll(DocDisplayDtoUtil.getRepoIdDocNameMap(dto.getFollowUpDocDisplayDtoList()));
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        // display internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(dto.getAppId());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public List<InspectionFillCheckListDto> getServiceChkDtoListByAppPremId(String appId, String conifgType, boolean needVehicleSeparation) {
        List<InspectionFillCheckListDto> chkDtoList = IaisCommonUtils.genNewArrayList();

        ChecklistConfigDto dto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);

        InspectionFillCheckListDto fDto;
        if("service".equals(conifgType)&&!dto.isCommon()) {
            if (!((!needVehicleSeparation && StringUtil.isEmpty(dto.getInspectionEntity())) || (needVehicleSeparation && HcsaChecklistConstants.INSPECTION_ENTITY_VEHICLE.equalsIgnoreCase(dto.getInspectionEntity())))) {
                return null;
            }
            fDto = transferToInspectionCheckListDto(dto, appId);

            if (!StringUtil.isEmpty(dto.getSvcName())) {
                fDto.setSvcName(dto.getSvcName());
            }
            fDto.setConfigId(dto.getId());
            fDto.setSvcCode(dto.getSvcCode());
            if (dto.getSvcSubType() != null) {
                fDto.setSubName(dto.getSvcSubType().replace(" ", ""));
                fDto.setSubType(dto.getSvcSubType());
            } else {
                fDto.setSubName(dto.getSvcCode());
            }
            chkDtoList.add(fDto);
        }
        return chkDtoList;
    }

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

    public void getInspectionFillCheckListDtoByInspectionFillCheckListDto(InspectionFillCheckListDto inspectionFillCheckListDto, List<OrgUserDto> orgUserDtos) {
        if(inspectionFillCheckListDto == null){
            return ;
        }
        Map<String, String> draftRemarkMaps = IaisCommonUtils.genNewHashMap();
        inspectionFillCheckListDto.setDraftRemarkMaps(draftRemarkMaps);
        int userNum = orgUserDtos.size();
        if(userNum > 1){
            inspectionFillCheckListDto.setMoreOneDraft(true);
        }

        List<InspectionCheckQuestionDto>  inspectionCheckQuestionDtos = inspectionFillCheckListDto.getCheckList();
        if(IaisCommonUtils.isEmpty( inspectionCheckQuestionDtos)){
            return ;
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


    }

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

    private InspectionCheckQuestionDto getInspectionCheckQuestionDtoByAnswerForDifDto(InspectionCheckQuestionDto inspectionCheckQuestionDto,AnswerForDifDto answerForDifDto){
        inspectionCheckQuestionDto.setRemark(answerForDifDto.getRemark());
        inspectionCheckQuestionDto.setChkanswer(answerForDifDto.getAnswer());
        inspectionCheckQuestionDto.setRectified("1".equalsIgnoreCase(answerForDifDto.getIsRec()));
        inspectionCheckQuestionDto.setNcs(answerForDifDto.getNcs());
        return  inspectionCheckQuestionDto;
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
    private Boolean isSameByStrings(String s1,String s2,String answer){
        if("No".equalsIgnoreCase(answer)){
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
    public void getRateOfCheckList(InspectionFDtosDto serListDto,  InspectionFillCheckListDto commonDto) {
        if(serListDto == null) return;
        if(serListDto.getFdtoList()!=null){
            getServiceTotalAndNc(serListDto);
        }
        if(commonDto!=null){
            getGeneralTotalAndNc(commonDto,serListDto);
        }

        int totalNcNum = serListDto.getGeneralNc()+serListDto.getServiceNc()+serListDto.getAdhocNc();

        serListDto.setTotalNcNum(totalNcNum);
    }

    private void getGeneralTotalAndNc(InspectionFillCheckListDto commonDto, InspectionFDtosDto serListDto) {
        int totalNum = 0;
        int ncNum = 0;
        int doNum = 0;
        for(InspectionCheckQuestionDto cqDto : commonDto.getCheckList()){
            totalNum++;
            if(!StringUtil.isEmpty(cqDto.getChkanswer())){
                if( "No".equalsIgnoreCase(cqDto.getChkanswer())){
                    if(StringUtil.isNotEmpty(cqDto.getRemark()) && StringUtil.isNotEmpty(cqDto.getNcs())){
                        ncNum++;
                    }
                }
                doNum++;
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
                        if( "No".equalsIgnoreCase(cqDto.getChkanswer())){
                            if(StringUtil.isNotEmpty(cqDto.getRemark()) && StringUtil.isNotEmpty(cqDto.getNcs())){
                                ncNum++;
                            }
                        }
                        doNum++;
                    }
                }
            }
        }
        serListDto.setServiceDo(doNum);
        serListDto.setServiceTotal(totalNum);
        serListDto.setServiceNc(ncNum);
    }
}
