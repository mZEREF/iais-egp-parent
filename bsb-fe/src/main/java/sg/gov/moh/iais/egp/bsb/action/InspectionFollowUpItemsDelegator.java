package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
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

    public InspectionFollowUpItemsDelegator(InspectionClient inspectionClient, InspectionService inspectionService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute("");
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_RECTIFY_SAVED_DTO);
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
            RectifyInsReportSaveDto savedDto = inspectionService.getRectifyNCsSavedDto(request);
            RectifyInsReportDto reportDto = inspectionService.getRectifyNcsSavedDocDto(request);
            savedDto.setAppId(rectifyFindingFormDto.getAppId());
            savedDto.setConfigId(rectifyFindingFormDto.getConfigId());
            if(!rectifyFindingFormDto.getDocDtoList().isEmpty()){
                reportDto.setSavedDocMap(rectifyFindingFormDto.getDocDtoList().stream().collect(Collectors.toMap(DocRecordInfo::getRepoId, Function.identity())));
                ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, reportDto);
            }
            ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DTO, savedDto);

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
        RectifyFindingFormDto findingFormDto = (RectifyFindingFormDto) ParamUtil.getSessionAttr(request, KEY_RECTIFY_FINDING_FORM);
        String itemValue = (String) ParamUtil.getSessionAttr(request, KEY_ITEM_VALUE);
        RectifyFindingFormDto.RectifyFindingItemDto itemDto = findingFormDto.getRectifyFindingItemDtoByItemValue(itemValue);
        ParamUtil.setRequestAttr(request,"rectifyItemDto",itemDto);
    }

    public void doFollowUpItems(BaseProcessClass bpc) {
        //request mapping item doc and item remarks info
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
            RectifyInsReportSaveDto savedDto = inspectionService.getRectifyNCsSavedDto(request);
            String itemValue = (String) ParamUtil.getSessionAttr(request, KEY_ITEM_VALUE);
            Assert.hasLength(itemValue,"item value -sectionId +'--v--'+configId is null");
            docDto.reqObjMapping(request, DocConstants.DOC_TYPE_INSPECTION_NON_COMPLIANCE, itemValue);
            savedDto.reqObjMapping(request, itemValue);
            log.info(LogUtil.escapeCrlf(docDto.toString()));
            log.info(LogUtil.escapeCrlf(savedDto.toString()));
            Map<String,String> itemRectifyMap = inspectionService.getItemRectifyMap(request);
            inspectionService.turnCurrentIconStatus(request, docDto, itemValue, itemRectifyMap);
            ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DTO, savedDto);
            ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, docDto);
        }
        actionJumpHandler(request);
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //do doc sync
        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        RectifyInsReportSaveDto savedDto = inspectionService.getRectifyNCsSavedDto(request);
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
        Assert.hasLength(actionType,"action_type is null");
        if(ModuleCommonConstants.KEY_NAV_FOLLOW_UP_ITEMS.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_FOLLOW_UP_ITEMS);
        } else if(ModuleCommonConstants.KEY_SUBMIT.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_SUBMIT);
        } else if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_PREPARE);
        } else if(ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_PREPARE);
        }
    }
}
