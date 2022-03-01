package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;

/**
 * @author : LiRan
 * @date : 2022/2/21
 */
@Slf4j
@Delegator("followUpItemsDelegator")
public class InspectionFollowUpItemsDelegator {
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;

    @Autowired
    public InspectionFollowUpItemsDelegator(InspectionClient inspectionClient, InspectionService inspectionService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_RECTIFY_SAVED_REMARK_MAP);
        session.removeAttribute(KEY_RECTIFY_FINDING_FORM);
        session.removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "Applicant send follow-up items");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //TODO get mask application id from menu
        //search inspection follow-up items finding list
        RectifyFindingFormDto rectifyFindingFormDto = inspectionClient.getFollowUpItemsFindingFormDtoByAppId("2BAF80B5-5F61-EC11-BE74-000C298D317C").getEntity();
        //save basic info such as appId and config id
        if(rectifyFindingFormDto != null){
            RectifyInsReportDto reportDto = inspectionService.getRectifyNcsSavedDocDto(request);

            if(!rectifyFindingFormDto.getDocDtoList().isEmpty()){
                reportDto.setSavedDocMap(rectifyFindingFormDto.getDocDtoList().stream().collect(Collectors.toMap(DocRecordInfo::getRepoId, Function.identity())));
                ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, reportDto);
            }

            //load origin icon status
            inspectionService.loadOriginIconStatus(rectifyFindingFormDto.getItemDtoList(), reportDto, request);
            //data->session
            ParamUtil.setSessionAttr(request, KEY_RECTIFY_FINDING_FORM, rectifyFindingFormDto);
            //pay attention to clear session
        }
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //judge if all items is rectified
        boolean isAllRectified = true;
        Map<String,String> itemRectifyMap = inspectionService.getItemRectifyMap(request);
        for (Map.Entry<String, String> entry : itemRectifyMap.entrySet()) {
            if (MasterCodeConstants.NO.equals(entry.getValue())) {
                isAllRectified = false;
                break;
            }
        }
        ParamUtil.setSessionAttr(request, KEY_ALL_ITEM_RECTIFY, isAllRectified);
    }

    public void doPrepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedItemValue = ParamUtil.getString(request, KEY_ITEM_VALUE);
        String itemValue = MaskUtil.unMaskValue(KEY_MASKED_ITEM_VALUE, maskedItemValue);
        ParamUtil.setSessionAttr(request, KEY_ITEM_VALUE, itemValue);
        actionJumpHandler(request);
    }

    public void prepareFollowUpItems(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String itemValue = (String) ParamUtil.getSessionAttr(request, KEY_ITEM_VALUE);
        RectifyFindingFormDto findingFormDto = (RectifyFindingFormDto) ParamUtil.getSessionAttr(request,KEY_RECTIFY_FINDING_FORM);
        Map<String,RectifyInsReportSaveDto.RectifyItemSaveDto> saveMapDto = inspectionService.getRectifyNCsSavedRemarkMap(request);
        RectifyInsReportDto reportDto = inspectionService.getRectifyNcsSavedDocDto(request);
        //Prepare the data pre-displayed on the Rectify page
        if(!saveMapDto.isEmpty()){
            //show info save before
            RectifyInsReportSaveDto.RectifyItemSaveDto saveDto = saveMapDto.get(itemValue);
            ParamUtil.setRequestAttr(request,KEY_RECTIFY_ITEM_SAVE_DTO,saveDto);
        }
        //new saved document
        if(!reportDto.getNewDocMap().isEmpty()){
            Map<String,List<NewDocInfo>> newDocSubTypeMap = reportDto.getNewDocSubTypeMap();
            List<NewDocInfo> newDocInfos = newDocSubTypeMap.get(itemValue);
            ParamUtil.setRequestAttr(request, KEY_NEW_SAVED_DOCUMENT, newDocInfos);
        }
        //document search from database
        if(!reportDto.getSavedDocMap().isEmpty()){
            Map<String,List<DocRecordInfo>> savedDocSubTypeMap = reportDto.getSavedDocSubTypeMap();
            List<DocRecordInfo> savedDocInfos = savedDocSubTypeMap.get(itemValue);
            ParamUtil.setRequestAttr(request, KEY_SAVED_DOCUMENT, savedDocInfos);
        }
        //show info search from database
        RectifyFindingFormDto.RectifyFindingItemDto itemDto = findingFormDto.getRectifyFindingItemDtoByItemValue(itemValue);
        ParamUtil.setRequestAttr(request,"rectifyItemDto", itemDto);
    }

    public void doFollowUpItems(BaseProcessClass bpc) {
        //request mapping item doc and item remarks info
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
            Map<String,RectifyInsReportSaveDto.RectifyItemSaveDto> savedDtoMap = inspectionService.getRectifyNCsSavedRemarkMap(request);
            String itemValue = (String) ParamUtil.getSessionAttr(request, KEY_ITEM_VALUE);
            Assert.hasLength(itemValue,"item value -sectionId +'--v--'+configId is null");
            docDto.reqObjMapping(request, DocConstants.DOC_TYPE_INSPECTION_NON_COMPLIANCE, itemValue);
            log.info(LogUtil.escapeCrlf(docDto.toString()));
            log.info(LogUtil.escapeCrlf(savedDtoMap.toString()));
            inspectionService.putItemRemarkValue(request,itemValue,savedDtoMap);
            //icon status
            Map<String, String> itemRectifyMap = inspectionService.getItemRectifyMap(request);
            inspectionService.turnCurrentIconStatus(request, docDto, itemValue, itemRectifyMap);
            ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, docDto);
        }
        actionJumpHandler(request);
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //do doc sync
        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        RectifyInsReportSaveDto savedDto = new RectifyInsReportSaveDto();

        RectifyFindingFormDto rectifyFindingFormDto = (RectifyFindingFormDto) ParamUtil.getSessionAttr(request,KEY_RECTIFY_FINDING_FORM);
        //set application id and config id
        savedDto.setConfigId(rectifyFindingFormDto.getConfigId());
        savedDto.setAppId(rectifyFindingFormDto.getAppId());

        //set remarks or other info
        Map<String, RectifyInsReportSaveDto.RectifyItemSaveDto>  saveDtoMap = inspectionService.getRectifyNCsSavedRemarkMap(request);
        savedDto.setItemSaveDtoList(new ArrayList<>(saveDtoMap.values()));

        //set doc
        List<NewFileSyncDto> newFilesToSync = inspectionService.saveNewUploadedDoc(docDto);
        if(!docDto.getSavedDocMap().isEmpty()){
            savedDto.setAttachmentList(new ArrayList<>(docDto.getSavedDocMap().values()));
            savedDto.setToBeDeletedDocIds(docDto.getToBeDeletedDocIds());
        }
        inspectionClient.saveFollowUpItemsData(savedDto);
        //save all info into database
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = inspectionService.deleteUnwantedDoc(docDto);
            // sync docs
            inspectionService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    private void actionJumpHandler(HttpServletRequest request){
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
        if(ModuleCommonConstants.KEY_NAV_FOLLOW_UP_ITEMS.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_FOLLOW_UP_ITEMS);
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_VALUE, actionValue);
        } else if(ModuleCommonConstants.KEY_SUBMIT.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_SUBMIT);
        } else if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_PREPARE);
        } else if(ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_PREPARE);
        }
    }
}
