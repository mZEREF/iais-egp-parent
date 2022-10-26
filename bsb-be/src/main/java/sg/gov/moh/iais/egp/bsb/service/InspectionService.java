package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.dto.chklst.BsbAdCheckListShowDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.BsbAdhocNcCheckItemDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.BsbAnswerForDifDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InspectionCheckQuestionDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InspectionFDtosDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InspectionFillCheckListDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ItemDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.SectionDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistItemDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_SUBMIT_REPORT_DATE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_FOLLOW_UP_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_REMARK_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;

@Service
@Slf4j
@RequiredArgsConstructor
public class InspectionService {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;
    private final DocSettingService docSettingService;

    public InsSubmitReportDataDto getInsSubmitReportDataDto(HttpServletRequest request, String appId, String roleId) {
        InsSubmitReportDataDto dto = (InsSubmitReportDataDto) ParamUtil.getSessionAttr(request, KEY_INS_SUBMIT_REPORT_DATE_DTO);
        if (dto == null) {
            dto = inspectionClient.getInitInsSubmitReportData(appId, roleId);
        }
        return dto;
    }

    /**
     * Gets init data for follow-up BE modules (Review extension, DO item verification and AO item verification).
     * @param roleId nullable, officer list of the specific role you need
     */
    public void getInitFollowUpData(HttpServletRequest request, String appId, String roleId){
        ReviewInsFollowUpDto dto = (ReviewInsFollowUpDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO);
        if (dto == null) {
            // facility info
            dto = inspectionClient.getInitInsFollowUpData(appId, roleId);
            dto.setAppId(appId);
            ParamUtil.setSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO, dto);
        }
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        // facility details
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, dto.getFacilityDetailsInfo());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, dto.getProcessHistoryDtoList());
        // show remark history
        ParamUtil.setRequestAttr(request, KEY_REMARK_HISTORY_LIST, dto.getRemarksHistoryList());
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());
        // show route to moh selection list
        if (!CollectionUtils.isEmpty(dto.getSelectRouteToMoh())) {
            ParamUtil.setSessionAttr(request, KEY_SELECT_ROUTE_TO_MOH, new ArrayList<>(dto.getSelectRouteToMoh()));
        }
    }

    public List<InspectionFillCheckListDto> getServiceChkDtoListByAppPremId(String appId, String conifgType, boolean needVehicleSeparation) {
        List<InspectionFillCheckListDto> chkDtoList = IaisCommonUtils.genNewArrayList();

        ChecklistConfigDto dto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);

        InspectionFillCheckListDto fDto;
        if("service".equals(conifgType)&&!dto.isCommon()) {
            if (!((!needVehicleSeparation && StringUtils.isEmpty(dto.getInspectionEntity())) || (needVehicleSeparation && HcsaChecklistConstants.INSPECTION_ENTITY_VEHICLE.equalsIgnoreCase(dto.getInspectionEntity())))) {
                return null;
            }
            fDto = transferToInspectionCheckListDto(dto, appId);

            if (!StringUtils.isEmpty(dto.getSvcName())) {
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

    public BsbAdCheckListShowDto getAdhocCheckListDto(String appId, List<OrgUserDto> orgUserDtoUsers) {
        BsbAdCheckListShowDto adCheckListShowDto = null;
        AdhocChecklistConfigDto confDto = inspectionClient.getAdhocChecklistConfigDaoByAppid(appId).getBody();
        if (confDto != null) {
            adCheckListShowDto = new BsbAdCheckListShowDto();
            List<BsbAdhocNcCheckItemDto> adItemList = IaisCommonUtils.genNewArrayList();
            if (confDto.getAdhocChecklistItemList() != null && !confDto.getAdhocChecklistItemList().isEmpty()) {
                for (AdhocChecklistItemDto temp : confDto.getAdhocChecklistItemList()) {
                    BsbAdhocNcCheckItemDto showDto = new BsbAdhocNcCheckItemDto(temp);
                    showDto.setSectionId(ChecklistConstants.ADHOC_SECTION_ID);
                    adItemList.add(showDto);
                    showDto.setAnswerForDifDtoMaps(IaisCommonUtils.genNewHashMap());
                    showDto.setAdhocAnswerForDifDtos(IaisCommonUtils.genNewArrayList());
                    for (OrgUserDto user : orgUserDtoUsers) {
                        BsbAnswerForDifDto anDto = new BsbAnswerForDifDto();
                        anDto.setSubmitId(user.getId());
                        anDto.setSubmitName(user.getDisplayName());
                        showDto.getAdhocAnswerForDifDtos().add(anDto);
                    }
                }
            }
            adCheckListShowDto.setAdItemList(adItemList);
        }
        return adCheckListShowDto;
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
        if(!checkList.isEmpty()){
            List<InspectionCheckQuestionDto> cqDtoList = IaisCommonUtils.genNewArrayList();
            for(InspectionCheckQuestionDto temp:checkList){
                InspectionCheckQuestionDto inspectionCheckQuestionDto = CopyUtil.copyMutableObject(temp);
                inspectionCheckQuestionDto.setAppPreCorreId(appPremCorrId);
                cqDtoList.add(inspectionCheckQuestionDto);
            }
            dto.setCheckList(checkList);
            fillInspectionFillCheckListDto(dto);
            return dto;
        }
        return dto;
    }

    public void fillInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto){
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
    public void itemDto(InspectionFillCheckListDto infillCheckListDto){
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
    }
    public void getItemCheckListDto(InspectionFillCheckListDto infillCheckListDto){
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
            List<BsbAnswerForDifDto> answerForDifDtos = new ArrayList<>(userNum);
            Map<String, BsbAnswerForDifDto> answerForDifDtoMaps = Maps.newHashMapWithExpectedSize(userNum);
            for (OrgUserDto entry : orgUserDtos) {
                BsbAnswerForDifDto answerForDifDto = new BsbAnswerForDifDto();
                answerForDifDto.setSubmitId(entry.getId());
                answerForDifDto.setSubmitName(entry.getUserId());
                answerForDifDtos.add(answerForDifDto);
                answerForDifDtoMaps.put(entry.getId(),answerForDifDto);
            }
            inspectionCheckQuestionDto.setAnswerForDifDtos(answerForDifDtos);
            inspectionCheckQuestionDto.setAnswerForDifDtoMaps(answerForDifDtoMaps);
        }


    }



    public void getInspectionCheckQuestionDtoByAnswerForDifDto(InspectionCheckQuestionDto inspectionCheckQuestionDto, BsbAnswerForDifDto answerForDifDto){
        inspectionCheckQuestionDto.setRemark(answerForDifDto.getRemark());
        inspectionCheckQuestionDto.setChkanswer(answerForDifDto.getAnswer());
        inspectionCheckQuestionDto.setRectified("1".equalsIgnoreCase(answerForDifDto.getIsRec()));
        inspectionCheckQuestionDto.setNcs(answerForDifDto.getNcs());
        inspectionCheckQuestionDto.setFollowupAction(answerForDifDto.getFollowupAction());
        inspectionCheckQuestionDto.setFollowupItem(answerForDifDto.getFollowupItem());
        inspectionCheckQuestionDto.setObserveFollowup(answerForDifDto.getObserveFollowup());
        inspectionCheckQuestionDto.setDueDate(answerForDifDto.getDueDate());
    }
    public void getRateOfCheckList(InspectionFDtosDto serListDto, BsbAdCheckListShowDto adchklDto) {
        if(serListDto == null) return;
        if(serListDto.getFdtoList()!=null){
            getServiceTotalAndNc(serListDto);
        }
        if(adchklDto!=null&&!IaisCommonUtils.isEmpty(adchklDto.getAdItemList())){
            getAdhocTotalAndNc(adchklDto,serListDto);
        }
        int totalNcNum = serListDto.getGeneralNc()+serListDto.getServiceNc()+serListDto.getAdhocNc();

        serListDto.setTotalNcNum(totalNcNum);
    }

    private void getAdhocTotalAndNc(BsbAdCheckListShowDto adchklDto, InspectionFDtosDto serListDto) {
        int totalNum = 0;
        int ncNum = 0;
        int doNum = 0;
        for(BsbAdhocNcCheckItemDto aditem : adchklDto.getAdItemList()){
            totalNum++;
            if(!StringUtils.isEmpty(aditem.getAdAnswer())){
                if( "NO".equalsIgnoreCase(aditem.getAdAnswer())){
                    if(StringUtil.isNotEmpty(aditem.getRemark()) && StringUtil.isNotEmpty(aditem.getNcs())){
                        ncNum++;
                    }
                }
                doNum++;
            }
        }
        serListDto.setAdhocTotal(totalNum);
        serListDto.setAdhocNc(ncNum);
        serListDto.setAdhocDo(doNum);
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
                    if(!StringUtils.isEmpty(cqDto.getChkanswer())){
                        if( "NO".equalsIgnoreCase(cqDto.getChkanswer())){
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

    public void getInspectionFillCheckListDtoForShow(InspectionFillCheckListDto inspectionFillCheckListDto) {
        if(inspectionFillCheckListDto != null){
            inspectionFillCheckListDto.setSvcNameShow( StringUtil.getFilterSpecialCharacter( inspectionFillCheckListDto.getSubName()));
            List<SectionDto> sectionDtos = inspectionFillCheckListDto.getSectionDtoList();
            if(!CollectionUtils.isEmpty(sectionDtos)){
                for(SectionDto sectionDto : sectionDtos){
                    if(sectionDto != null && !CollectionUtils.isEmpty(sectionDto.getItemDtoList())) {
                        List<ItemDto> itemDtoList = sectionDto.getItemDtoList();
                        for (ItemDto itemDto : itemDtoList) {
                            if (itemDto.getIncqDto() != null)
                                itemDto.getIncqDto().setSectionNameShow(StringUtil.getFilterSpecialCharacter(itemDto.getIncqDto().getSectionNameSub()));
                        }
                    }
                }
            }
        }
    }

    public AdhocChecklistConfigDto saveAdhocChecklistConfig(AdhocChecklistConfigDto currentAdhocChecklistConfig, AdhocChecklistConfigDto oldAdhocChecklistConfig) {
        if (isEditAdhocCheckListConfig(currentAdhocChecklistConfig, oldAdhocChecklistConfig)) {
            return inspectionClient.saveAdhocChecklistConfig(currentAdhocChecklistConfig).getBody();
        }
        return currentAdhocChecklistConfig;
    }

    /**
     * jude AdhocCheckListConfig is change
     *
     * @param adhocChecklistConfigDto    current AdhocChecklistConfig
     * @param adhocChecklistConfigDtoOld old AdhocChecklistConfig
     */
    private boolean isEditAdhocCheckListConfig(AdhocChecklistConfigDto adhocChecklistConfigDto, AdhocChecklistConfigDto adhocChecklistConfigDtoOld) {
        if (adhocChecklistConfigDto == null && adhocChecklistConfigDtoOld == null) {
            return false;
        }
        if (adhocChecklistConfigDto == null || adhocChecklistConfigDtoOld == null) {
            return true;
        }
        List<AdhocChecklistItemDto> allAdhocItem = adhocChecklistConfigDto.getAdhocChecklistItemList();
        List<AdhocChecklistItemDto> oldAdhocItems = adhocChecklistConfigDtoOld.getAdhocChecklistItemList();
        if (IaisCommonUtils.isEmpty(allAdhocItem) && IaisCommonUtils.isEmpty(oldAdhocItems)) {
            return false;
        }
        if (IaisCommonUtils.isEmpty(allAdhocItem) || IaisCommonUtils.isEmpty(oldAdhocItems)) {
            return true;
        }

        if (allAdhocItem.size() != oldAdhocItems.size()) {
            return true;
        }
        for (AdhocChecklistItemDto adhocChecklistItemDto : allAdhocItem) {
            boolean haveAhoc = false;
            for (AdhocChecklistItemDto adhocChecklistItemDtoOld : oldAdhocItems) {
                if (adhocChecklistItemDtoOld.getId().equalsIgnoreCase(adhocChecklistItemDto.getId())) {
                    haveAhoc = true;
                    break;
                }
            }
            if (!haveAhoc) {
                return true;
            }
        }
        return false;
    }
}
