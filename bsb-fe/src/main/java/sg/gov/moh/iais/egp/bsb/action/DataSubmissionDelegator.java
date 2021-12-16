package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

@Slf4j
@Delegator("dataSubmissionDelegator")
public class DataSubmissionDelegator {
    private static final String DATA_SYNC_ERROR_MSG = "Fail to sync files to BE";
    private static final String PARAM_KEY_MAP = "keyMap";


    private final DataSubmissionClient dataSubmissionClient;
    private final FileRepoClient fileRepoClient;
    private final BsbSubmissionCommon subCommon;
    private final BsbFileClient bsbFileClient;

    public DataSubmissionDelegator(DataSubmissionClient dataSubmissionClient,FileRepoClient fileRepoClient,BsbSubmissionCommon subCommon,BsbFileClient bsbFileClient){
        this.dataSubmissionClient = dataSubmissionClient;
        this.fileRepoClient = fileRepoClient;
        this.subCommon = subCommon;
        this.bsbFileClient = bsbFileClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        ParamUtil.setSessionAttr(request,KEY_OTHER_DOC,null);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        if(log.isInfoEnabled()){
            log.info("In the future this module will be used to initialize some data");
        }
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void prepareNotificationTypeSelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
    }

    /**
     * StartStep: PrepareFacilitySelect
     * prepare facility list
     */
    public void doPrepareFacilitySelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
    }

    /**
     * StartStep: PrepareSwitch0
     * put facility id in session
     */
    public void doPrepareSwitch0(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        String facId = ParamUtil.getRequestString(request,KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id",facId);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,facId);
    }

    /**
     * StartStep: prepareConsumeData
     * Prepare data for the callback and facility info
     */
    public void prepareConsumeData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        //
        ConsumeNotificationDto notificationDto = getConsumeNotification(request);
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,settingMap);
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, notificationDto);
        Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getKeyNewInfos();
        ParamUtil.setRequestAttr(request,PARAM_KEY_MAP,keyNewInfos);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_CONSUME);
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
        doConsumeValidation(notificationDto,request);
        //use to show file information
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,getDocSettingMap());
        ParamUtil.setRequestAttr(request,KEY_DOC_META,notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getAllDocMetaByDocType().get(KEY_DOC_TYPE_OF_OTHER));
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, notificationDto);
    }

    /**
     * StartStep: saveConsumeNot
     * save consume notification
     */
    public void saveConsumeNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ConsumeNotificationDto dto = getConsumeNotification(request);
        List<NewFileSyncDto> newFilesToSync = null;
        if(!dto.getAllNewDocInfos().isEmpty()){
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR = dto.getConsumeNotNeedR();
        dataSubmissionClient.saveConsumeNot(consumeNotNeedR);
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
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * StartStep: prepareDisposalData
     */
    public void prepareDisposalData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        //
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getKeyNewInfos();
        ParamUtil.setRequestAttr(request,PARAM_KEY_MAP,keyNewInfos);
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,settingMap);
        ParamUtil.setSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO, notificationDto);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_DISPOSAL);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareDisposalConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
        notificationDto.reqObjectMapping(request);
        doDisposalValidation(notificationDto,request);
        //use to show file information
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getAllDocMetaByDocType().get(KEY_DOC_TYPE_OF_OTHER));
        ParamUtil.setRequestAttr(request,KEY_DOC_META,notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO, notificationDto);
    }

    /**
     * StartStep: saveDisposalNot
     * save consume notification
     */
    public void saveDisposalNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto dto = getDisposalNotification(request);
        List<NewFileSyncDto> newFilesToSync = null;
        if(!dto.getAllNewDocInfos().isEmpty()){
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR = dto.getDisposalNotNeedR();
        dataSubmissionClient.saveDisposalNot(disposalNotNeedR);
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
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * StartStep: prepareExportData
     */
    public void prepareExportData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        //
        ExportNotificationDto notificationDto = getExportNotification(request);
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getKeyNewInfos();
        ParamUtil.setRequestAttr(request,PARAM_KEY_MAP,keyNewInfos);
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,settingMap);
        ParamUtil.setSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO, notificationDto);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_EXPORT);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareExportConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto notificationDto = getExportNotification(request);
        notificationDto.reqObjectMapping(request);
        doExportValidation(notificationDto,request);
        //use to show file information
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getAllDocMetaByDocType().get(KEY_DOC_TYPE_OF_OTHER));
        ParamUtil.setRequestAttr(request,KEY_DOC_META,notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO, notificationDto);
    }

    /**
     * StartStep: saveExportNot
     * save consume notification
     */
    public void saveExportNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto dto = getExportNotification(request);
        List<NewFileSyncDto> newFilesToSync = null;
        if(!dto.getAllNewDocInfos().isEmpty()){
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ExportNotificationDto.ExportNotNeedR exportNotNeedR = dto.getExportNotNeedR();
        dataSubmissionClient.saveExportNot(exportNotNeedR);
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
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * StartStep: prepareReceiptData
     */
    public void prepareReceiveData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        //
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
        //prepare facility info
        getFacInfoAndSchType(request);

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos = notificationDto.getKeyNewInfos();
        ParamUtil.setRequestAttr(request,PARAM_KEY_MAP,keyNewInfos);
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,settingMap);
        ParamUtil.setSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO, notificationDto);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_RECEIPT);
    }

    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareReceiptConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
        notificationDto.reqObjectMapping(request);
        doReceiptValidation(notificationDto,request);
        //use to show file information
        ParamUtil.setRequestAttr(request,KEY_DO_SETTINGS,getDocSettingMap());
        ParamUtil.setSessionAttr(request, KEY_OTHER_DOC, (Serializable) notificationDto.getAllDocMetaByDocType().get(KEY_DOC_TYPE_OF_OTHER));
        ParamUtil.setRequestAttr(request,KEY_DOC_META,notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO, notificationDto);
    }

    /**
     * StartStep: saveReceiptNot
     * save consume notification
     */
    public void saveReceiptNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto dto = getReceiptNotification(request);
        List<NewFileSyncDto> newFilesToSync = null;
        if(!dto.getAllNewDocInfos().isEmpty()){
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = new ArrayList<>(dto.newFileSaved(repoIds));
        }
        ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR = dto.getReceiptNotNeedR();
        dataSubmissionClient.saveReceiptNot(receiptNotNeedR);
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
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }

    /**
     * This method is used to query all Facility info
     */
    private void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,null);
        FacListDto facListDto = dataSubmissionClient.queryAllApprovalFacList().getEntity();
        List<FacListDto.FacList> facLists = facListDto.getFacLists();
        //Removes the newly created object where is null
        facLists.remove(0);
        List<SelectOption> selectModel = new ArrayList<>(facLists.size());
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(MaskUtil.maskValue("id",fac.getFacId()), fac.getFacName()));
        }
        ParamUtil.setRequestAttr(request, KEY_FAC_SELECTION, selectModel);
        //Put in session called for later operations
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,(Serializable) facLists);
    }

    /**
     * this method just used to charge if consume notification dto exist
     * */
    private ConsumeNotificationDto getConsumeNotification(HttpServletRequest request){
        ConsumeNotificationDto notificationDto = (ConsumeNotificationDto) ParamUtil.getSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultConsumeNotDto() : notificationDto;
    }

    private ConsumeNotificationDto getDefaultConsumeNotDto() {
        return new ConsumeNotificationDto();
    }

    /**
     * this method just used to charge if disposal notification dto exist
     * */
    private DisposalNotificationDto getDisposalNotification(HttpServletRequest request){
        DisposalNotificationDto notificationDto = (DisposalNotificationDto) ParamUtil.getSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultDisposalNotDto() : notificationDto;
    }

    private DisposalNotificationDto getDefaultDisposalNotDto() {
        return new DisposalNotificationDto();
    }

    /**
     * this method just used to charge if export notification dto exist
     * */
    private ExportNotificationDto getExportNotification(HttpServletRequest request){
        ExportNotificationDto notificationDto = (ExportNotificationDto) ParamUtil.getSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultExportNotDto() : notificationDto;
    }

    private ExportNotificationDto getDefaultExportNotDto() {
        return new ExportNotificationDto();
    }

    /**
     * this method just used to charge if receipt notification dto exist
     * */
    private ReceiptNotificationDto getReceiptNotification(HttpServletRequest request){
        ReceiptNotificationDto notificationDto = (ReceiptNotificationDto) ParamUtil.getSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultReceiptNotDto() : notificationDto;
    }

    private ReceiptNotificationDto getDefaultReceiptNotDto() {
        return new ReceiptNotificationDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doConsumeValidation(ConsumeNotificationDto dto,HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }

    private void doDisposalValidation(DisposalNotificationDto dto,HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }

    private void doExportValidation(ExportNotificationDto dto,HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }

    private void doReceiptValidation(ReceiptNotificationDto dto,HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }

    /**
     *a way to get default display in jsp
     * getDocSettingMap
     * @return Map<String,DocSetting>
     * */
    private Map<String, DocSetting> getDocSettingMap(){
        Map<String,DocSetting> settingMap = new HashMap<>();
        settingMap.put(KEY_DOC_TYPE_INVENTORY_BAT,new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT,"Inventory: Biological Agents",true));
        settingMap.put(KEY_DOC_TYPE_INVENTORY_TOXIN,new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN,"Inventory: Toxins",true));
        settingMap.put(KEY_DOC_TYPE_OTHERS,new DocSetting(DocConstants.DOC_TYPE_OTHERS,KEY_DOC_TYPE_OF_OTHER,true));
        return settingMap;
    }

    private void getFacInfoAndSchType(HttpServletRequest request){
        String facId = (String) ParamUtil.getSessionAttr(request,KEY_FAC_ID);
        if(StringUtils.hasLength(facId)){
            //this part is prepared data for facInfo show in jsp
            FacListDto.FacList facList = subCommon.getFacListById(request,facId);
            ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);

            List<Biological> biological = facList.getBioMap().get(facId);
            subCommon.prepareSelectOption(request,KEY_SCHEDULE_TYPE,biological);
        }else{
            log.error("Your function has arguments with null values");
        }
    }
}

