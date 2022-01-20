package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jmapper.JMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;

@Slf4j
@Delegator("dataSubmissionDelegator")
public class DataSubmissionDelegator {
    private static final String DATA_SYNC_ERROR_MSG = "Fail to sync files to BE";
    private static final String PARAM_KEY_MAP = "keyMap";
    private static final String PARAM_SAVED_KEY_MAP = "savedKeyMap";
    private static final String PARAM_SAVED_OTHERS_DOC = "savedOthersDoc";
    public static final String KEY_DRAFT = "draft";


    private final DataSubmissionClient dataSubmissionClient;
    private final FileRepoClient fileRepoClient;
    private final BsbSubmissionCommon subCommon;
    private final FacilityRegistrationService facilityRegistrationService;

    public DataSubmissionDelegator(DataSubmissionClient dataSubmissionClient, FileRepoClient fileRepoClient, BsbSubmissionCommon subCommon, FacilityRegistrationService facilityRegistrationService) {
        this.dataSubmissionClient = dataSubmissionClient;
        this.fileRepoClient = fileRepoClient;
        this.subCommon = subCommon;
        this.facilityRegistrationService = facilityRegistrationService;
    }

    /**
     * start
     * This module is used to initialize data
     */
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_CONSUME_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request, KEY_DISPOSAL_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request, KEY_EXPORT_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request, KEY_RECEIPT_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, null);
        ParamUtil.setSessionAttr(request, KEY_FAC_ID, null);
        if (log.isInfoEnabled()) {
            log.info("In the future this module will be used to initialize some data");
        }
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void prepareNotificationTypeSelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, null);
    }

    /**
     * StartStep: PrepareFacilitySelect
     * prepare facility list
     */
    public void doPrepareFacilitySelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
        DraftDto draft = (DraftDto) ParamUtil.getSessionAttr(request, KEY_DRAFT);
        String dataSubmissionType = (String) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_TYPE);
        if (draft != null && dataSubmissionType != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                switch (dataSubmissionType) {
                    case KEY_DATA_SUBMISSION_TYPE_CONSUME:
                        ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR = mapper.readValue(draft.getDraftData(), ConsumeNotificationDto.ConsumeNotNeedR.class);
                        JMapper<ConsumeNotificationDto, ConsumeNotificationDto.ConsumeNotNeedR> consumeDtoJMapper = new JMapper<>(ConsumeNotificationDto.class, ConsumeNotificationDto.ConsumeNotNeedR.class);
                        ConsumeNotificationDto consumeNotificationDto = consumeDtoJMapper.getDestinationWithoutControl(consumeNotNeedR);
                        //put saved doc to savedDocMap
                        if (!CollectionUtils.isEmpty(consumeNotNeedR.getDocInfos())) {
                            consumeNotificationDto.draftDocToMap(consumeNotNeedR.getDocInfos());
                        }
                        ParamUtil.setSessionAttr(request, KEY_CONSUME_NOTIFICATION_DTO, consumeNotificationDto);
                        ParamUtil.setSessionAttr(request, KEY_FAC_ID, consumeNotificationDto.getFacId());
                        break;
                    case KEY_DATA_SUBMISSION_TYPE_DISPOSAL:
                        DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR = mapper.readValue(draft.getDraftData(), DisposalNotificationDto.DisposalNotNeedR.class);
                        JMapper<DisposalNotificationDto, DisposalNotificationDto.DisposalNotNeedR> disposalDtoJMapper = new JMapper<>(DisposalNotificationDto.class, DisposalNotificationDto.DisposalNotNeedR.class);
                        DisposalNotificationDto disposalNotificationDto = disposalDtoJMapper.getDestinationWithoutControl(disposalNotNeedR);
                        //put saved doc to savedDocMap
                        if (!CollectionUtils.isEmpty(disposalNotNeedR.getDocInfos())) {
                            disposalNotificationDto.draftDocToMap(disposalNotNeedR.getDocInfos());
                        }
                        ParamUtil.setSessionAttr(request, KEY_DISPOSAL_NOTIFICATION_DTO, disposalNotificationDto);
                        ParamUtil.setSessionAttr(request, KEY_FAC_ID, disposalNotificationDto.getFacId());
                        break;
                    case KEY_DATA_SUBMISSION_TYPE_EXPORT:
                        ExportNotificationDto.ExportNotNeedR exportNotNeedR = mapper.readValue(draft.getDraftData(), ExportNotificationDto.ExportNotNeedR.class);
                        JMapper<ExportNotificationDto, ExportNotificationDto.ExportNotNeedR> exportDtoJMapper = new JMapper<>(ExportNotificationDto.class, ExportNotificationDto.ExportNotNeedR.class);
                        ExportNotificationDto exportNotificationDto = exportDtoJMapper.getDestinationWithoutControl(exportNotNeedR);
                        //put saved doc to savedDocMap
                        if (!CollectionUtils.isEmpty(exportNotNeedR.getDocInfos())) {
                            exportNotificationDto.draftDocToMap(exportNotNeedR.getDocInfos());
                        }
                        ParamUtil.setSessionAttr(request, KEY_EXPORT_NOTIFICATION_DTO, exportNotificationDto);
                        ParamUtil.setSessionAttr(request, KEY_FAC_ID, exportNotificationDto.getFacId());
                        break;
                    case KEY_DATA_SUBMISSION_TYPE_RECEIPT:
                        ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR = mapper.readValue(draft.getDraftData(), ReceiptNotificationDto.ReceiptNotNeedR.class);
                        JMapper<ReceiptNotificationDto, ReceiptNotificationDto.ReceiptNotNeedR> receiptDtoJMapper = new JMapper<>(ReceiptNotificationDto.class, ReceiptNotificationDto.ReceiptNotNeedR.class);
                        ReceiptNotificationDto receiptNotificationDto = receiptDtoJMapper.getDestinationWithoutControl(receiptNotNeedR);
                        //put saved doc to savedDocMap
                        if (!CollectionUtils.isEmpty(receiptNotNeedR.getDocInfos())) {
                            receiptNotificationDto.draftDocToMap(receiptNotNeedR.getDocInfos());
                        }
                        ParamUtil.setSessionAttr(request, KEY_RECEIPT_NOTIFICATION_DTO, receiptNotificationDto);
                        ParamUtil.setSessionAttr(request, KEY_FAC_ID, receiptNotificationDto.getFacId());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected dataSubmissionType: " + dataSubmissionType);
                }
            } catch (JsonProcessingException e) {
                log.error("Fail to retrieve data");
            }
        }
    }


    /**
     * StartStep: PrepareSwitch0
     * put facility id in session
     */
    public void doPrepareSwitch0(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FAC_ID, null);
        String facId = ParamUtil.getRequestString(request, KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id", facId);
        ParamUtil.setSessionAttr(request, KEY_FAC_ID, facId);
    }

    /**
     * StartStep: prepareConsumeData
     * Prepare data for the callback and facility info
     */
    public void prepareConsumeData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, null);
        //
        ConsumeNotificationDto notificationDto = getConsumeNotification(request);
        notificationDto.draftDocToMap(new ArrayList<>(notificationDto.getSavedDocInfos().values()));
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, notificationDto.retrieveValidationResult());
        }
        Map<String, DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, settingMap);
        ParamUtil.setSessionAttr(request, KEY_CONSUME_NOTIFICATION_DTO, notificationDto);
        Map<Integer, List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getNewKeyNewInfos();
        ParamUtil.setRequestAttr(request, PARAM_KEY_MAP, keyNewInfos);
        //
        Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos = notificationDto.getOldKeySavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_KEY_MAP, oldKeySavedInfos);

        //key is index in ConsumptionNot
        Map<String, ConsumeNotificationDto.ConsumptionNot> notificationMap = notificationDto.getConsumptionNotList().stream().collect(Collectors.toMap(ConsumeNotificationDto.ConsumptionNot::getIndex, Function.identity()));
        for (ConsumeNotificationDto.ConsumptionNot dto : notificationMap.values()) {
            if (StringUtils.hasLength(dto.getIndex())) {
                List<PrimaryDocDto.DocRecordInfo> docRecordInfos = oldKeySavedInfos.get(Integer.valueOf(dto.getIndex()));
                if (!CollectionUtils.isEmpty(docRecordInfos)) {
                    //Retrieves the ids of all saved files in the current section and stores them in the repoIdSavedString
                    String savedRepoIdString = docRecordInfos.stream().map(PrimaryDocDto.DocRecordInfo::getRepoId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                    dto.setRepoIdSavedString(savedRepoIdString);
                }
            }
        }
        //

        List<PrimaryDocDto.DocRecordInfo> otherSavedInfos = notificationDto.getOtherSavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_OTHERS_DOC, otherSavedInfos);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, KEY_DATA_SUBMISSION_TYPE_CONSUME);
    }

    /**
     * StartStep: PrepareSwitch
     * Maybe it will be useful in the future
     */
    public void prepareSwitch1(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionType = request.getParameter(KEY_ACTION_TYPE);
        ParamUtil.setSessionAttr(bpc.request, KEY_ACTION_TYPE, actionType);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareConsumeConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get value from jsp and bind value to dto
        ConsumeNotificationDto notificationDto = getConsumeNotification(request);
        notificationDto.reqObjectMapping(request);
        doConsumeValidation(notificationDto, request);
        //use to show file information
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getOtherNewInfos());
        ParamUtil.setRequestAttr(request, KEY_DOC_META, notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request, KEY_CONSUME_NOTIFICATION_DTO, notificationDto);
    }

    public void saveDraftConsume(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ConsumeNotificationDto dto = getConsumeNotification(request);
        dto.reqObjectMapping(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR = dto.getConsumeNotNeedR();
        //save draft
        String draftAppNo = dataSubmissionClient.saveDraftConsume(consumeNotNeedR);
        dto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    /**
     * StartStep: saveConsumeNot
     * save consume notification
     */
    public void saveConsumeNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ConsumeNotificationDto dto = getConsumeNotification(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR = dto.getConsumeNotNeedR();
        dataSubmissionClient.saveConsumeNot(consumeNotNeedR);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * StartStep: prepareDisposalData
     */
    public void prepareDisposalData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, null);
        //
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
        notificationDto.draftDocToMap(new ArrayList<>(notificationDto.getSavedDocInfos().values()));
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, notificationDto.retrieveValidationResult());
        }
        Map<Integer, List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getNewKeyNewInfos();
        ParamUtil.setRequestAttr(request, PARAM_KEY_MAP, keyNewInfos);
        Map<String, DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, settingMap);
        ParamUtil.setSessionAttr(request, KEY_DISPOSAL_NOTIFICATION_DTO, notificationDto);

        Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos = notificationDto.getOldKeySavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_KEY_MAP, oldKeySavedInfos);
        //key is index in ConsumptionNot
        Map<String, DisposalNotificationDto.DisposalNot> notificationMap = notificationDto.getDisposalNotList().stream().collect(Collectors.toMap(DisposalNotificationDto.DisposalNot::getIndex, Function.identity()));
        for (DisposalNotificationDto.DisposalNot dto : notificationMap.values()) {
            if (StringUtils.hasLength(dto.getIndex())) {
                List<PrimaryDocDto.DocRecordInfo> docRecordInfos = oldKeySavedInfos.get(Integer.valueOf(dto.getIndex()));
                if (!CollectionUtils.isEmpty(docRecordInfos)) {
                    //Retrieves the ids of all saved files in the current section and stores them in the repoIdSavedString
                    String savedRepoIdString = docRecordInfos.stream().map(PrimaryDocDto.DocRecordInfo::getRepoId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                    dto.setRepoIdSavedString(savedRepoIdString);
                }
            }
        }

        List<PrimaryDocDto.DocRecordInfo> otherSavedInfos = notificationDto.getOtherSavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_OTHERS_DOC, otherSavedInfos);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, KEY_DATA_SUBMISSION_TYPE_DISPOSAL);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareDisposalConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
        notificationDto.reqObjectMapping(request);
        doDisposalValidation(notificationDto, request);
        //use to show file information
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getOtherNewInfos());
        ParamUtil.setRequestAttr(request, KEY_DOC_META, notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request, KEY_DISPOSAL_NOTIFICATION_DTO, notificationDto);
    }

    public void saveDraftDisposal(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto dto = getDisposalNotification(request);
        dto.reqObjectMapping(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR = dto.getDisposalNotNeedR();
        //save draft
        String draftAppNo = dataSubmissionClient.saveDraftDisposal(disposalNotNeedR);
        dto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    /**
     * StartStep: saveDisposalNot
     * save consume notification
     */
    public void saveDisposalNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto dto = getDisposalNotification(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR = dto.getDisposalNotNeedR();
        dataSubmissionClient.saveDisposalNot(disposalNotNeedR);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * StartStep: prepareExportData
     */
    public void prepareExportData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, null);
        //
        ExportNotificationDto notificationDto = getExportNotification(request);
        notificationDto.draftDocToMap(new ArrayList<>(notificationDto.getSavedDocInfos().values()));
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, notificationDto.retrieveValidationResult());
        }
        Map<Integer, List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getNewKeyNewInfos();
        ParamUtil.setRequestAttr(request, PARAM_KEY_MAP, keyNewInfos);
        Map<String, DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, settingMap);
        ParamUtil.setSessionAttr(request, KEY_EXPORT_NOTIFICATION_DTO, notificationDto);

        Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos = notificationDto.getOldKeySavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_KEY_MAP, oldKeySavedInfos);
        //key is index in ConsumptionNot
        Map<String, ExportNotificationDto.ExportNot> notificationMap = notificationDto.getExportNotList().stream().collect(Collectors.toMap(ExportNotificationDto.ExportNot::getIndex, Function.identity()));
        for (ExportNotificationDto.ExportNot dto : notificationMap.values()) {
            if (StringUtils.hasLength(dto.getIndex())) {
                List<PrimaryDocDto.DocRecordInfo> docRecordInfos = oldKeySavedInfos.get(Integer.valueOf(dto.getIndex()));
                if (!CollectionUtils.isEmpty(docRecordInfos)) {
                    //Retrieves the ids of all saved files in the current section and stores them in the repoIdSavedString
                    String savedRepoIdString = docRecordInfos.stream().map(PrimaryDocDto.DocRecordInfo::getRepoId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                    dto.setRepoIdSavedString(savedRepoIdString);
                }
            }
        }
        List<PrimaryDocDto.DocRecordInfo> otherSavedInfos = notificationDto.getOtherSavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_OTHERS_DOC, otherSavedInfos);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, KEY_DATA_SUBMISSION_TYPE_EXPORT);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareExportConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto notificationDto = getExportNotification(request);
        notificationDto.reqObjectMapping(request);
        doExportValidation(notificationDto, request);
        //use to show file information
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getOtherNewInfos());
        ParamUtil.setRequestAttr(request, KEY_DOC_META, notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request, KEY_EXPORT_NOTIFICATION_DTO, notificationDto);
    }

    public void saveDraftExport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto dto = getExportNotification(request);
        dto.reqObjectMapping(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ExportNotificationDto.ExportNotNeedR exportNotNeedR = dto.getExportNotNeedR();
        //save draft
        String draftAppNo = dataSubmissionClient.saveDraftExport(exportNotNeedR);
        dto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    /**
     * StartStep: saveExportNot
     * save consume notification
     */
    public void saveExportNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto dto = getExportNotification(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ExportNotificationDto.ExportNotNeedR exportNotNeedR = dto.getExportNotNeedR();
        dataSubmissionClient.saveExportNot(exportNotNeedR);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * StartStep: prepareReceiptData
     */
    public void prepareReceiveData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, null);
        //
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
        notificationDto.draftDocToMap(new ArrayList<>(notificationDto.getSavedDocInfos().values()));
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, notificationDto.retrieveValidationResult());
        }
        Map<Integer, List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getNewKeyNewInfos();
        ParamUtil.setRequestAttr(request, PARAM_KEY_MAP, keyNewInfos);
        Map<String, DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, settingMap);
        ParamUtil.setSessionAttr(request, KEY_RECEIPT_NOTIFICATION_DTO, notificationDto);

        Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos = notificationDto.getOldKeySavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_KEY_MAP, oldKeySavedInfos);
        //key is index in ConsumptionNot
        Map<String, ReceiptNotificationDto.ReceiptNot> notificationMap = notificationDto.getReceiptNotList().stream().collect(Collectors.toMap(ReceiptNotificationDto.ReceiptNot::getIndex, Function.identity()));
        for (ReceiptNotificationDto.ReceiptNot dto : notificationMap.values()) {
            if (StringUtils.hasLength(dto.getIndex())) {
                List<PrimaryDocDto.DocRecordInfo> docRecordInfos = oldKeySavedInfos.get(Integer.valueOf(dto.getIndex()));
                if (!CollectionUtils.isEmpty(docRecordInfos)) {
                    //Retrieves the ids of all saved files in the current section and stores them in the repoIdSavedString
                    String savedRepoIdString = docRecordInfos.stream().map(PrimaryDocDto.DocRecordInfo::getRepoId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                    dto.setRepoIdSavedString(savedRepoIdString);
                }
            }
        }
        List<PrimaryDocDto.DocRecordInfo> otherSavedInfos = notificationDto.getOtherSavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_OTHERS_DOC, otherSavedInfos);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, KEY_DATA_SUBMISSION_TYPE_RECEIPT);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareReceiptConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
        notificationDto.reqObjectMapping(request);
        doReceiptValidation(notificationDto, request);
        //use to show file information
        ParamUtil.setRequestAttr(request, KEY_DO_SETTINGS, getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getOtherNewInfos());
        ParamUtil.setRequestAttr(request, KEY_DOC_META, notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request, KEY_RECEIPT_NOTIFICATION_DTO, notificationDto);
    }

    public void saveDraftReceipt(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto dto = getReceiptNotification(request);
        dto.reqObjectMapping(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR = dto.getReceiptNotNeedR();
        //save draft
        String draftAppNo = dataSubmissionClient.saveDraftReceipt(receiptNotNeedR);
        dto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    /**
     * StartStep: saveReceiptNot
     * save consume notification
     */
    public void saveReceiptNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto dto = getReceiptNotification(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR = dto.getReceiptNotNeedR();
        dataSubmissionClient.saveReceiptNot(receiptNotNeedR);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * This method is used to query all Facility info
     */
    private void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, KEY_FAC_LISTS, null);
        FacListDto facListDto = dataSubmissionClient.queryAllApprovalFacList().getEntity();
        List<FacListDto.FacList> facLists = facListDto.getFacLists();
        //Removes the newly created object where is null
        facLists.remove(0);
        List<SelectOption> selectModel = new ArrayList<>(facLists.size());
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(MaskUtil.maskValue("id", fac.getFacId()), fac.getFacName()));
        }
        ParamUtil.setRequestAttr(request, KEY_FAC_SELECTION, selectModel);
        //Put in session called for later operations
        ParamUtil.setSessionAttr(request, KEY_FAC_LISTS, (Serializable) facLists);
    }

    /**
     * this method just used to charge if consume notification dto exist
     */
    private ConsumeNotificationDto getConsumeNotification(HttpServletRequest request) {
        ConsumeNotificationDto notificationDto = (ConsumeNotificationDto) ParamUtil.getSessionAttr(request, KEY_CONSUME_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultConsumeNotDto() : notificationDto;
    }

    private ConsumeNotificationDto getDefaultConsumeNotDto() {
        return new ConsumeNotificationDto();
    }

    /**
     * this method just used to charge if disposal notification dto exist
     */
    private DisposalNotificationDto getDisposalNotification(HttpServletRequest request) {
        DisposalNotificationDto notificationDto = (DisposalNotificationDto) ParamUtil.getSessionAttr(request, KEY_DISPOSAL_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultDisposalNotDto() : notificationDto;
    }

    private DisposalNotificationDto getDefaultDisposalNotDto() {
        return new DisposalNotificationDto();
    }

    /**
     * this method just used to charge if export notification dto exist
     */
    private ExportNotificationDto getExportNotification(HttpServletRequest request) {
        ExportNotificationDto notificationDto = (ExportNotificationDto) ParamUtil.getSessionAttr(request, KEY_EXPORT_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultExportNotDto() : notificationDto;
    }

    private ExportNotificationDto getDefaultExportNotDto() {
        return new ExportNotificationDto();
    }

    /**
     * this method just used to charge if receipt notification dto exist
     */
    private ReceiptNotificationDto getReceiptNotification(HttpServletRequest request) {
        ReceiptNotificationDto notificationDto = (ReceiptNotificationDto) ParamUtil.getSessionAttr(request, KEY_RECEIPT_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultReceiptNotDto() : notificationDto;
    }

    private ReceiptNotificationDto getDefaultReceiptNotDto() {
        return new ReceiptNotificationDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     */
    private void doConsumeValidation(ConsumeNotificationDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }

    private void doDisposalValidation(DisposalNotificationDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }

    private void doExportValidation(ExportNotificationDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }

    private void doReceiptValidation(ReceiptNotificationDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }

    /**
     * a way to get default display in jsp
     * getDocSettingMap
     *
     * @return Map<String, DocSetting>
     */
    private Map<String, DocSetting> getDocSettingMap() {
        Map<String, DocSetting> settingMap = new HashMap<>();
        settingMap.put(KEY_DOC_TYPE_INVENTORY_BAT, new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT, "Inventory: Biological Agents", true));
        settingMap.put(KEY_DOC_TYPE_INVENTORY_TOXIN, new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN, "Inventory: Toxins", true));
        settingMap.put(KEY_DOC_TYPE_OTHERS, new DocSetting(DocConstants.DOC_TYPE_OTHERS, KEY_DOC_TYPE_OF_OTHER, true));
        return settingMap;
    }

    private void getFacInfoAndSchType(HttpServletRequest request) {
        String facId = (String) ParamUtil.getSessionAttr(request, KEY_FAC_ID);
        if (StringUtils.hasLength(facId)) {
            //this part is prepared data for facInfo show in jsp
            FacListDto.FacList facList = subCommon.getFacListById(request, facId);
            ParamUtil.setSessionAttr(request, KEY_FACILITY_INFO, facList);

            List<Biological> biological = facList.getBioMap().get(facId);
            subCommon.prepareSelectOption(request, KEY_SCHEDULE_TYPE, biological);
        } else {
            log.error("Your function has arguments with null values");
        }
    }

    /**
     * Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     *
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
        for (String id : toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }
}

