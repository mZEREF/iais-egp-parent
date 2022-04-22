package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import sg.gov.moh.iais.egp.bsb.dto.file.*;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsRectificationDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;

/**
 * @author YiMing
 * 2022/2/11 13:10
 **/
@Slf4j
@Delegator("rectifiesNCsDelegator")
public class BsbRectifiesNonComplianceDelegator {
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;

    public BsbRectifiesNonComplianceDelegator(InspectionClient inspectionClient, InspectionService inspectionService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_APP_ID);
        request.getSession().removeAttribute(KEY_RECTIFY_SAVED_DATA_MAP);
        request.getSession().removeAttribute(KEY_NCS_RECTIFICATION_DISPLAY_DATA);
        request.getSession().removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);
        AuditTrailHelper.auditFunction("Applicant rectifies NCs", "Applicant rectifies NCs");
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //get application id
        //search NCs list info
        ParamUtil.setSessionAttr(request,KEY_APP_ID,"C945DF6C-99BC-EC11-BE76-000C298D317C");
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        InsRectificationDisplayDto displayDto = inspectionClient.getNonComplianceFindingFormDtoByAppId(appId).getEntity();
        //save basic info such as appId and config id
        if(displayDto != null){
            RectifyInsReportDto reportDto = inspectionService.getRectifyNcsSavedDocDto(request);
            if(!displayDto.getDocDtoList().isEmpty()){
                reportDto.setSavedDocMap(displayDto.getDocDtoList().stream().collect(Collectors.toMap(DocRecordInfo::getRepoId, Function.identity())));
                ParamUtil.setSessionAttr(request,KEY_RECTIFY_SAVED_DOC_DTO,reportDto);
            }

            //load origin icon status
            inspectionService.loadOriginIconStatus(displayDto.getItemDtoList(),reportDto,request);
            //data->session
            ParamUtil.setSessionAttr(request,KEY_NCS_RECTIFICATION_DISPLAY_DATA,displayDto);
            //pay attention to clear session
        }
    }


    public void prepareNCsData(BaseProcessClass bpc){
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
        ParamUtil.setSessionAttr(request,KEY_ALL_ITEM_RECTIFY,isAllRectified);
    }

    public void handleNCs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String maskedItemValue = ParamUtil.getString(request,KEY_ITEM_VALUE);
        String itemValue = MaskUtil.unMaskValue(KEY_MASKED_ITEM_VALUE,maskedItemValue);
        ParamUtil.setSessionAttr(request,KEY_ITEM_VALUE,itemValue);
        actionJumpHandler(request);
    }

    public void submitNCs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //do doc sync
        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        RectifyInsReportSaveDto savedDto = new RectifyInsReportSaveDto();

        //set application id and config id
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        InsRectificationDisplayDto displayDto = (InsRectificationDisplayDto) ParamUtil.getSessionAttr(request,KEY_NCS_RECTIFICATION_DISPLAY_DATA);
        savedDto.setConfigId(displayDto.getConfigId());
        savedDto.setAppId(appId);

        //get all map value as list
        Map<String, RectifyInsReportSaveDto.RectifyItemSaveDto>  saveDtoMap = inspectionService.getRectifyNCsSavedRemarkMap(request);
        savedDto.setItemSaveDtoList(new ArrayList<>(saveDtoMap.values()));

        //set doc
        List<NewFileSyncDto> newFilesToSync = inspectionService.saveNewUploadedDoc(docDto);
        if(!docDto.getSavedDocMap().isEmpty()){
            savedDto.setAttachmentList(new ArrayList<>(docDto.getSavedDocMap().values()));
            savedDto.setToBeDeletedDocIds(docDto.getToBeDeletedDocIds());
        }
        inspectionClient.saveInsNonComplianceReport(savedDto);
        //save all info into database
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = inspectionService.deleteUnwantedDoc(docDto);
            // sync docs
            inspectionService.syncNewDocsAndDeleteFiles(newFilesToSync,toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    public void prepareRectifyData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String itemValue = (String) ParamUtil.getSessionAttr(request,KEY_ITEM_VALUE);
        InsRectificationDisplayDto displayDto = (InsRectificationDisplayDto) ParamUtil.getSessionAttr(request,KEY_NCS_RECTIFICATION_DISPLAY_DATA);
        Map<String,RectifyInsReportSaveDto.RectifyItemSaveDto> saveMapDto = inspectionService.getRectifyNCsSavedRemarkMap(request);
        RectifyInsReportDto reportDto = inspectionService.getRectifyNcsSavedDocDto(request);
        //Prepare the data pre-displayed on the Rectify page
        //remarks
        if(!saveMapDto.isEmpty()){
            RectifyInsReportSaveDto.RectifyItemSaveDto saveDto = saveMapDto.get(itemValue);
            ParamUtil.setRequestAttr(request,KEY_RECTIFY_ITEM_SAVE_DTO,saveDto);
        }
        //new saved document
        if(!reportDto.getNewDocMap().isEmpty()){
            Map<String,List<NewDocInfo>> newDocSubTypeMap = reportDto.getNewDocSubTypeMap();
            List<NewDocInfo> newDocInfos = newDocSubTypeMap.get(itemValue);
            ParamUtil.setRequestAttr(request,KEY_NEW_SAVED_DOCUMENT,newDocInfos);
        }
        //document search from database
        if(!reportDto.getSavedDocMap().isEmpty()){
            Map<String,List<DocRecordInfo>> savedDocSubTypeMap = reportDto.getSavedDocSubTypeMap();
            List<DocRecordInfo> savedDocInfos = savedDocSubTypeMap.get(itemValue);
            ParamUtil.setRequestAttr(request,KEY_SAVED_DOCUMENT,savedDocInfos);
        }
        InsRectificationDisplayDto.RectificationItemDto itemDto = displayDto.getRectifyFindingItemDtoByItemValue(itemValue);
        ParamUtil.setRequestAttr(request,"rectifyItemDto",itemDto);
    }

    public void handleRectifyPage(BaseProcessClass bpc){
        //request mapping item doc and item remarks info
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
            String itemValue = (String) ParamUtil.getSessionAttr(request,KEY_ITEM_VALUE);
            Assert.hasLength(itemValue,"item value -sectionId +'--v--'+configId is null");
            docDto.reqObjMapping(request, DocConstants.DOC_TYPE_INSPECTION_NON_COMPLIANCE,itemValue);
            Map<String,RectifyInsReportSaveDto.RectifyItemSaveDto> savedDtoMap = inspectionService.getRectifyNCsSavedRemarkMap(request);
            inspectionService.putItemDataNeedSave(request,itemValue,savedDtoMap);
            log.info(LogUtil.escapeCrlf(docDto.toString()));
            log.info(LogUtil.escapeCrlf(savedDtoMap.toString()));
            inspectionService.turnCurrentIconStatus(request,docDto,itemValue);
            ParamUtil.setSessionAttr(request,KEY_RECTIFY_SAVED_DOC_DTO,docDto);
        }
        actionJumpHandler(request);
    }

    private void actionJumpHandler(HttpServletRequest request){
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        Assert.hasLength(actionType,"action_type is null");
        if(ModuleCommonConstants.KEY_NAV_RECTIFY.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_RECTIFY);
        }else if(ModuleCommonConstants.KEY_SUBMIT.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_SUBMIT);
        }else if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_PREPARE);
        }else if(ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_PREPARE);
        }
    }


}
