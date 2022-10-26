package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsRectificationDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPLICANT_RECTIFIES_NCS;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ALL_ITEM_RECTIFY;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ITEM_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_MASKED_ITEM_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_NCS_RECTIFICATION_DISPLAY_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_NEW_SAVED_DOCUMENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RECTIFY_ITEM_SAVE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RECTIFY_SAVED_DATA_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RECTIFY_SAVED_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SAVED_DOCUMENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_NAV_BACK;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_NAV_PREPARE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_NAV_RECTIFY;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SAVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;

/**
 * @author YiMing
 * 2022/2/11 13:10
 **/
@Slf4j
@RequiredArgsConstructor
@Delegator("rectifiesNCsDelegator")
public class BsbRectifiesNonComplianceDelegator {
    private static final String MASK_PARAM_APP_ID = "ncAppId";
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;
    private final DocSettingService docSettingService;
    private final RfiService rfiService;
    private final RfiClient rfiClient;


    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_RECTIFY_SAVED_DATA_MAP);
        session.removeAttribute(KEY_NCS_RECTIFICATION_DISPLAY_DATA);
        session.removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);

        //search NCs list info
        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        if (maskedAppId != null) {
            String appId = MaskUtil.unMaskValue(MASK_PARAM_APP_ID, maskedAppId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
            }
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
        }
        // if rfi module
        rfiService.clearAndSetAppIdInSession(request);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_APPLICANT_RECTIFIES_NCS);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //get application id
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
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
            if (NO.equals(entry.getValue())) {
                isAllRectified = false;
                break;
            }
        }
        //prepare docType
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
        //judge is rfi
        String confirmRfi = (String) ParamUtil.getSessionAttr(request, KEY_CONFIRM_RFI);
        if (confirmRfi != null && confirmRfi.equals(KEY_CONFIRM_RFI_Y)) {
            //save rfi data
            String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
            rfiClient.saveInspectionNC(savedDto, rfiDataId);
        } else {
            inspectionClient.saveInsNonComplianceReport(savedDto);
        }
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

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.customOptions(docSettingService.getAfcCertificationUploadDocTypeList().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        String docTypeOpsJson = JsonUtil.parseToJson(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
    }

    public void handleRectifyPage(BaseProcessClass bpc){
        //request mapping item doc and item remarks info
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if(KEY_SAVE.equals(actionType)){
            RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
            String itemValue = (String) ParamUtil.getSessionAttr(request,KEY_ITEM_VALUE);
            Assert.hasLength(itemValue,"item value -sectionId +'--v--'+configId is null");
            docDto.reqObjMapping(request, itemValue);
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
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        Assert.hasLength(actionType,"action_type is null");
        if(KEY_NAV_RECTIFY.equals(actionType)){
            ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, KEY_NAV_RECTIFY);
        }else if(KEY_SUBMIT.equals(actionType)){
            ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, KEY_SUBMIT);
        }else if(KEY_SAVE.equals(actionType)){
            ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, KEY_NAV_PREPARE);
        }else if(KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, KEY_NAV_PREPARE);
        }
    }


}
