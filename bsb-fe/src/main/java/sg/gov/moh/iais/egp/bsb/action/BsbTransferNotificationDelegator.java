package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
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
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
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

/**
 * @author YiMing
 * @version 2021/11/2 19:46
 **/

@Slf4j
@Delegator("transferNotificationDelegator")
public class BsbTransferNotificationDelegator {
    public static final String KEY_TRANSFER_NOTIFICATION_DTO = "transferNotDto";
    public static final String KEY_FAC_LIST_DTO = "facListDto";
    public static final String KEY_FAC_ID = "facId";
    private static final String PARAM_SAVED_KEY_MAP = "savedKeyMap";
    private static final String PARAM_SAVED_OTHERS_DOC = "savedOthersDoc";
    public static final String KEY_DRAFT = "draft";

    private final TransferClient transferClient;
    private final FileRepoClient fileRepoClient;
    private final BsbSubmissionCommon subCommon;
    private final DataSubmissionClient submissionClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityRegistrationService facilityRegistrationService;


    public BsbTransferNotificationDelegator(TransferClient transferClient, FileRepoClient fileRepoClient, BsbSubmissionCommon subCommon, DataSubmissionClient submissionClient, BsbFileClient bsbFileClient, FacilityRegistrationService facilityRegistrationService) {
        this.transferClient = transferClient;
        this.fileRepoClient = fileRepoClient;
        this.subCommon = subCommon;
        this.submissionClient = submissionClient;
        this.bsbFileClient = bsbFileClient;
        this.facilityRegistrationService = facilityRegistrationService;
    }

    /**
     * start
     * This module is used to initialize data
     */
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request, KEY_TRANSFER_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request, KEY_FAC_ID, null);
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, null);
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void preFacSelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
        DraftDto draft = (DraftDto) ParamUtil.getSessionAttr(request, KEY_DRAFT);
        String dataSubmissionType = (String) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_TYPE);
        if (draft != null && dataSubmissionType != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (KEY_DATA_SUBMISSION_TYPE_TRANSFER.equals(dataSubmissionType)) {
                    TransferNotificationDto.TransferNotNeedR transferNotNeedR = mapper.readValue(draft.getDraftData(), TransferNotificationDto.TransferNotNeedR.class);
                    JMapper<TransferNotificationDto, TransferNotificationDto.TransferNotNeedR> transferDtoJMapper = new JMapper<>(TransferNotificationDto.class, TransferNotificationDto.TransferNotNeedR.class);
                    TransferNotificationDto transferNotificationDto = transferDtoJMapper.getDestinationWithoutControl(transferNotNeedR);
                    //put saved doc to savedDocMap
                    if (!CollectionUtils.isEmpty(transferNotNeedR.getDocInfos())) {
                        transferNotificationDto.draftDocToMap(transferNotNeedR.getDocInfos());
                    }
                    ParamUtil.setSessionAttr(request, KEY_CONSUME_NOTIFICATION_DTO, transferNotificationDto);
                    ParamUtil.setSessionAttr(request, KEY_FAC_ID, transferNotificationDto.getFacId());
                } else {
                    throw new IllegalStateException("Unexpected dataSubmissionType: " + dataSubmissionType);
                }
            } catch (JsonProcessingException e) {
                log.error("Fail to retrieve data");
            }
        }
    }

    public void preSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_FAC_ID, null);
        String facId = ParamUtil.getRequestString(request, KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id", facId);
        ParamUtil.setSessionAttr(request, KEY_FAC_ID, facId);
    }

    /**
     * prepareData
     * this module is used to prepare facility info and biological agent/toxin
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, null);
        //prepare facility info
        String facId = (String) ParamUtil.getSessionAttr(request, KEY_FAC_ID);
        FacListDto facListDto = subCommon.getFacListDto(request);
        ParamUtil.setSessionAttr(request, KEY_FAC_LIST_DTO, facListDto);
        if (log.isInfoEnabled()) {
            log.info("facListDto,facId value is {},{}", LogUtil.escapeCrlf(facListDto.toString()), LogUtil.escapeCrlf(facId));
        }
        if (StringUtils.hasLength(facId)) {
            //this part is prepared data for facInfo show in jsp
            FacListDto.FacList facList = subCommon.getFacListById(request, facId);
            ParamUtil.setSessionAttr(request, "facilityInfo", facList);
            List<Biological> biological = facList.getBioMap().get(facId);
            subCommon.prepareSelectOption(request, "scheduleType", biological);
        } else {
            log.error("Your function has arguments with null values");
        }

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        TransferNotificationDto transferNotificationDto = getTransferNotification(request);
        transferNotificationDto.draftDocToMap(new ArrayList<>(transferNotificationDto.getSavedDocInfos().values()));
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, transferNotificationDto.retrieveValidationResult());
        }
        Map<String, DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request, "doSettings", settingMap);
        ParamUtil.setRequestAttr(request, KEY_TRANSFER_NOTIFICATION_DTO, transferNotificationDto);
        Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos = transferNotificationDto.getNewKeyNewInfos();
        ParamUtil.setRequestAttr(request,"keyMap",keyNewInfos);
        //
        Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos = transferNotificationDto.getOldKeySavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_KEY_MAP, oldKeySavedInfos);
        //key is index in ConsumptionNot
        Map<String, TransferNotificationDto.TransferNot> notificationMap = transferNotificationDto.getTransferNotList().stream().collect(Collectors.toMap(TransferNotificationDto.TransferNot::getIndex, Function.identity()));
        for (TransferNotificationDto.TransferNot dto : notificationMap.values()) {
            if (StringUtils.hasLength(dto.getIndex())) {
                List<PrimaryDocDto.DocRecordInfo> docRecordInfos = oldKeySavedInfos.get(Integer.valueOf(dto.getIndex()));
                if (!CollectionUtils.isEmpty(docRecordInfos)) {
                    //Retrieves the ids of all saved files in the current section and stores them in the repoIdSavedString
                    String savedRepoIdString = docRecordInfos.stream().map(PrimaryDocDto.DocRecordInfo::getRepoId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                    dto.setRepoIdSavedString(savedRepoIdString);
                }
            }
        }
        List<PrimaryDocDto.DocRecordInfo> otherSavedInfos = transferNotificationDto.getOtherSavedInfos();
        ParamUtil.setRequestAttr(request, PARAM_SAVED_OTHERS_DOC, otherSavedInfos);
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_TYPE, KEY_DATA_SUBMISSION_TYPE_TRANSFER);
    }

    public void saveAndPrepareConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get value from jsp and bind value to dto
        TransferNotificationDto notificationDto = getTransferNotification(request);
        notificationDto.reqObjectMapping(request);
        //set facId into dto
        notificationDto.setFacId(subCommon.getFacInfo(request).getFacId());
        doValidation(notificationDto, request);
        //use to show file information
        ParamUtil.setRequestAttr(request, "doSettings", getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getOtherNewInfos());
        ParamUtil.setRequestAttr(request, "docMeta", notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request, KEY_TRANSFER_NOTIFICATION_DTO, notificationDto);
    }

    /**
     * StartStep: PrepareSwitch
     * Maybe it will be useful in the future
     */
    public void prepareSwitch1(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionType = request.getParameter("action_type");
        ParamUtil.setSessionAttr(bpc.request, "action_type", actionType);
    }

    public void saveDraftTransfer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TransferNotificationDto dto = getTransferNotification(request);
        dto.reqObjectMapping(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        TransferNotificationDto.TransferNotNeedR transferNotNeedR = dto.getTransferNotNeedR();
        //save draft
        String draftAppNo = transferClient.saveDraftTransfer(transferNotNeedR);
        dto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
        ParamUtil.setSessionAttr(request,KEY_TRANSFER_NOTIFICATION_DTO,dto);
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    /**
     * save
     * save information to database finally
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TransferNotificationDto notificationDto = getTransferNotification(request);
        List<NewFileSyncDto> newFilesToSync = null;
        if (!notificationDto.getAllNewDocInfos().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = notificationDto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(notificationDto.newFileSaved(repoIds));
        }

        String ensure = ParamUtil.getString(request, "ensure");
        notificationDto.setEnsure(ensure);
        TransferNotificationDto.TransferNotNeedR transferNotNeedR = notificationDto.getTransferNotNeedR();
        transferClient.saveNewTransferNot(transferNotNeedR);
        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (newFilesToSync != null && !newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    /**
     * this method just used to charge if dto exist
     *
     * @return TransferNotification
     */
    private TransferNotificationDto getTransferNotification(HttpServletRequest request) {
        TransferNotificationDto notificationDto = (TransferNotificationDto) ParamUtil.getSessionAttr(request, KEY_TRANSFER_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultDto() : notificationDto;
    }

    private TransferNotificationDto getDefaultDto() {
        return new TransferNotificationDto();
    }


    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     */
    private void doValidation(TransferNotificationDto dto, HttpServletRequest request) {
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
        settingMap.put("ityBat", new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT, "Inventory: Biological Agents", true));
        settingMap.put("ityToxin", new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN, "Inventory: Toxins", true));
        settingMap.put("others", new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", true));
        return settingMap;
    }

    /**
     * This method is used to query all Facility info
     */
    private void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, KEY_FAC_LISTS, null);
        FacListDto facListDto = submissionClient.queryAllApprovalFacList().getEntity();
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
