package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.IncidentFollowupClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.CommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.followup.*;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;

/**
 * @author YiMing
 * @version 2022/1/7 15:45
 **/

@Slf4j
@Delegator("followUPReportDelegator")
public class FollowUPReportDelegator{
    private static final String KEY_FOLLOW_UP_1A = "followup1A";
    private static final String KEY_FOLLOW_UP_1B = "followup1B";
    private static final String KEY_FOLLOW_UP_DOCUMENT = "followupDoc";
    private static final String KEY_MODULE_CHOOSE = "choose";
    private static final String PARAM_REFERENCE_NO = "refNo";
    private static final String KEY_MODULE_CHOOSE_REFERENCE_NO_1A = "referNo1A";
    private static final String KEY_MODULE_CHOOSE_REFERENCE_NO_1B = "referNo1B";
    private static final String KEY_FOLLOW_UP_1A_PREVIEW = "previewA";
    private static final String KEY_FOLLOW_UP_1B_PREVIEW = "previewB";
    private static final String KEY_NEW_FILE_MAP = "newFiles";
    private static final String KEY_SAVED_FILE_MAP = "savedFiles";
    private static final String PARAM_DOC_SETTINGS = "docSettings";
    private final IncidentFollowupClient followupClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public FollowUPReportDelegator(IncidentFollowupClient followupClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.followupClient = followupClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void startFollowup1A(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_FOLLOW_UP_1A);
        request.getSession().removeAttribute(KEY_FOLLOW_UP_DOCUMENT);
        request.getSession().removeAttribute(KEY_FOLLOW_UP_1A_PREVIEW);
        request.getSession().removeAttribute(KEY_MODULE_CHOOSE);
        ParamUtil.setSessionAttr(request,KEY_MODULE_CHOOSE,KEY_MODULE_CHOOSE_REFERENCE_NO_1A);
        AuditTrailHelper.auditFunction(ReportableEventConstants.MODULE_NAME_INCIDENT_FOLLOW_UP, "Incident Follow-up Report 1A");
    }

    public void startFollowup1B(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_FOLLOW_UP_1B);
        request.getSession().removeAttribute(KEY_FOLLOW_UP_DOCUMENT);
        request.getSession().removeAttribute(KEY_FOLLOW_UP_1B_PREVIEW);
        request.getSession().removeAttribute(KEY_MODULE_CHOOSE);
        ParamUtil.setSessionAttr(request,KEY_MODULE_CHOOSE,KEY_MODULE_CHOOSE_REFERENCE_NO_1B);
        AuditTrailHelper.auditFunction(ReportableEventConstants.MODULE_NAME_INCIDENT_FOLLOW_UP, "Incident Follow-up Report 1B");
    }

    public void preReferenceNo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String choose = (String) ParamUtil.getSessionAttr(request,KEY_MODULE_CHOOSE);
        List<String> referNoList = new ArrayList<>();
        Assert.hasLength(choose,"the key yo choose module is null");
        switch (choose){
            case KEY_MODULE_CHOOSE_REFERENCE_NO_1A:
                referNoList = followupClient.queryAll1ARefNo();
                break;
            case KEY_FOLLOW_UP_1B:
                referNoList = followupClient.queryAll1BRefNo();
                break;
            default:
                break;
        }
        ParamUtil.setRequestAttr(request,"referNoOps",tempReferenceNoOps(referNoList));
    }

    public void handleReferenceNo1A(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String maskedReferNo = ParamUtil.getString(request,PARAM_REFERENCE_NO);
        String referNo = MaskUtil.unMaskValue(PARAM_REFERENCE_NO,maskedReferNo);
        FollowupPreviewADto followupPreviewADto = followupClient.queryFollowupInfoAByRefNo(referNo).getEntity();
        retrieveFollowupInfoA(request,followupPreviewADto);
    }

    public void handleReferenceNo1B(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String maskedReferNo = ParamUtil.getString(request,PARAM_REFERENCE_NO);
        String referNo = MaskUtil.unMaskValue(PARAM_REFERENCE_NO,maskedReferNo);
        FollowupPreviewBDto followupPreviewBDto = followupClient.queryFollowupInfoBByRefNo(referNo).getEntity();
        retrieveFollowupInfoB(request,followupPreviewBDto.getIncidentId(),followupPreviewBDto.getIncidentInvestId());
    }

    public void preFollowUPReport1A(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        CommonDocDto commonDocDto = getFollowupDoc(request);
        ParamUtil.setRequestAttr(request,KEY_NEW_FILE_MAP,commonDocDto.getNewDocTypeMap());
        ParamUtil.setRequestAttr(request,KEY_SAVED_FILE_MAP,commonDocDto.getExistDocTypeMap());
        ParamUtil.setRequestAttr(request,PARAM_DOC_SETTINGS,getFollowupDocSettings());
    }

    public void handleFollowUPReport1A(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //retrieve page document data
        CommonDocDto followupDoc = getFollowupDoc(request);
        followupDoc.reqObjMapping(request);
        //retrieve report info A data
        FollowupInfoADto infoADto = getFollowupInfoADto(request);
        infoADto.reqObjMapping(request);
        //put info into validation dto
        Followup1AMetaDto followup1AMetaDto = Followup1AMetaDto.retrieve1AMetaData(infoADto,followupDoc);

        String actionType = ParamUtil.getString(request,ModuleCommonConstants.KEY_ACTION_TYPE);
        if(ModuleCommonConstants.KEY_NAV_NEXT.equals(actionType)){
            //do validation
            ValidationResultDto resultDto =  followupClient.validateFollowup1A(followup1AMetaDto);
            doValidation(resultDto,request);
        }else if(ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_BACK);
        }else if(ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)){
            //1.Set the action_type value to prepare
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_PREPARE);
            //2.save data into draft db
            saveFollow1ADraft(request,followupDoc,infoADto);
        }
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_DOCUMENT,followupDoc);
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_1A,infoADto);
    }

    public void preViewReport1A(BaseProcessClass bpc){
        log.info("may do something in future");
        HttpServletRequest request = bpc.request;
        CommonDocDto commonDocDto = getFollowupDoc(request);
        ParamUtil.setRequestAttr(request,KEY_NEW_FILE_MAP,commonDocDto.getNewDocTypeMap());
        ParamUtil.setRequestAttr(request,KEY_SAVED_FILE_MAP,commonDocDto.getSavedDocMap());
        ParamUtil.setRequestAttr(request,PARAM_DOC_SETTINGS,getFollowupDocSettings());
    }

    public void submitReport1A(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FollowupInfoADto followupInfoADto  = getFollowupInfoADto(request);
        CommonDocDto followupDoc = getFollowupDoc(request);
        //save doc
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(followupDoc);
        FollowupReport1ADto report1ADto = FollowupReport1ADto.from(followupInfoADto,followupDoc);
        followupClient.saveNewFollowupReport1A(report1ADto);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(followupDoc);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    public void preFollowUPReport1B(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_DOC_SETTINGS,getFollowupDocSettings());
    }

    public void handleFollowUPReport1B(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //retrieve page document data
        CommonDocDto followupDoc = getFollowupDoc(request);
        followupDoc.reqObjMapping(request);
        //retrieve report info A data
        FollowupInfoBDto infoBDto = getFollowupInfoBDto(request);
        infoBDto.reqObjMapping(request);
        //put info into validation dto
        Followup1BMetaDto followup1BMetaDto = Followup1BMetaDto.retrieve1BMetaData(infoBDto,followupDoc);

        String actionType = ParamUtil.getString(request,ModuleCommonConstants.KEY_ACTION_TYPE);
        if(ModuleCommonConstants.KEY_NAV_NEXT.equals(actionType)){
            //do validation
            ValidationResultDto resultDto =  followupClient.validateFollowup1B(followup1BMetaDto);
            doValidation(resultDto,request);
        }else if(ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE,"doBack");
        }else if(ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)){
            //1.Set the action_type value to prepare
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_PREPARE);
            //2.save data into draft db
            saveFollow1BDraft(request,followupDoc,infoBDto);
        }
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_DOCUMENT,followupDoc);
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_1B,infoBDto);

    }

    public void preViewReport1B(BaseProcessClass bpc){
        log.info("may do something in future");
        HttpServletRequest request = bpc.request;
        CommonDocDto commonDocDto = getFollowupDoc(request);
        ParamUtil.setRequestAttr(request,KEY_NEW_FILE_MAP,commonDocDto.getNewDocTypeMap());
        ParamUtil.setRequestAttr(request,KEY_SAVED_FILE_MAP,commonDocDto.getSavedDocMap());
        ParamUtil.setRequestAttr(request,PARAM_DOC_SETTINGS,getFollowupDocSettings());
    }

    public void submitReport1B(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FollowupInfoBDto followupInfoBDto  = getFollowupInfoBDto(request);
        CommonDocDto followupDoc = getFollowupDoc(request);
        //save doc
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(followupDoc);
        FollowupReport1BDto report1BDto = FollowupReport1BDto.from(followupInfoBDto,followupDoc);
        followupClient.saveNewFollowupReport1B(report1BDto);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(followupDoc);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    //The temporary option value is the reference no list obtained from the API
    public List<SelectOption> tempReferenceNoOps(List<String> referNoList){
        if(CollectionUtils.isEmpty(referNoList)){
            return originalOps();
        }
        List<SelectOption> referNoOps = new ArrayList<>(referNoList.size()+1);
        referNoOps.add(new SelectOption("","Please Select"));
        for (String refNo : referNoList) {
            referNoOps.add(new SelectOption(MaskUtil.maskValue(PARAM_REFERENCE_NO,refNo),refNo));
        }
        return referNoOps;
    }
    private static List<SelectOption> originalOps(){
        List<SelectOption> originalOps = new ArrayList<>(1);
        originalOps.add(new SelectOption("","Please Select"));
        return originalOps;
    }

    /**
     * retrieveFollowupInfoA
     * A method for populating the DTO with some of the necessary ids retrieved from the background
     * @param previewADto dto for storing the data you want to pre-store
     * */
    public void retrieveFollowupInfoA(HttpServletRequest request,FollowupPreviewADto previewADto){
        FollowupInfoADto infoADto = getFollowupInfoADto(request);
        String incidentId = previewADto.getIncidentId();
        String incidentInvestId = previewADto.getIncidentInvestId();
        List<IncidentInfoA> incidentInfoAList = previewADto.getIncidentInfoAList();
        Assert.hasLength(incidentId,"incident id key is null");
        Assert.hasLength(incidentInvestId,"incident investigation id key is null");
        Assert.notEmpty(incidentInfoAList,"list for storing follow-up1A preview data is empty ");
        infoADto.setIncidentId(incidentId);
        infoADto.setIncidentInvestId(incidentInvestId);

        //set preview data into infoADto for jsp review
        List<FollowupInfoADto.InfoADto> infoADtoList = new ArrayList<>(incidentInfoAList.size());
        for (IncidentInfoA infoA : incidentInfoAList) {
            FollowupInfoADto.InfoADto dto = new FollowupInfoADto.InfoADto();
            dto.setIncidentCause(infoA.getIncidentCause());
            dto.setExplainCause(infoA.getExplainCause());
            dto.setMeasure(infoA.getMeasure());
            dto.setImplementEntityDate(infoA.getImplementEntityDate());
            infoADtoList.add(dto);
        }
        infoADto.setInfoADtoList(infoADtoList);
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_1A,infoADto);
    }

    /**
     * retrieveFollowupInfoB
     * A method for populating the DTO with some of the necessary ids retrieved from the background
     * @param incidentId incident notification id
     * @param incidentInvestId incident investigation report id
     * */
    public void retrieveFollowupInfoB(HttpServletRequest request,String incidentId,String incidentInvestId){
        FollowupInfoBDto infoBDto = getFollowupInfoBDto(request);
        Assert.hasLength(incidentId,"incident id key is null");
        Assert.hasLength(incidentInvestId,"incident investigation id key is null");
        infoBDto.setIncidentId(incidentId);
        infoBDto.setIncidentInvestId(incidentInvestId);
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_1B,infoBDto);
    }

    public FollowupInfoADto getFollowupInfoADto(HttpServletRequest request){
        FollowupInfoADto followupInfoADto = (FollowupInfoADto) ParamUtil.getSessionAttr(request,KEY_FOLLOW_UP_1A);
        return followupInfoADto == null?getDefaultReport1A():followupInfoADto;
    }
    public FollowupInfoBDto getFollowupInfoBDto(HttpServletRequest request){
        FollowupInfoBDto followupInfoBDto = (FollowupInfoBDto) ParamUtil.getSessionAttr(request,KEY_FOLLOW_UP_1B);
        return followupInfoBDto == null?getDefaultReport1B():followupInfoBDto;
    }

    public CommonDocDto getFollowupDoc(HttpServletRequest request){
        CommonDocDto commonDocDto = (CommonDocDto) ParamUtil.getSessionAttr(request,KEY_FOLLOW_UP_DOCUMENT);
        return commonDocDto == null?new CommonDocDto():commonDocDto;
    }

    public FollowupInfoADto getDefaultReport1A(){
        return new FollowupInfoADto();
    }
    public FollowupInfoBDto getDefaultReport1B(){
        return new FollowupInfoBDto();
    }


    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getFollowupDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }


    /**
     * doValidation
     * Used to implement basic validation logic
     * @param resultDto The result DTO returned by the backend validation
     * */
    public void doValidation(ValidationResultDto resultDto,HttpServletRequest request){
        //validate pass -> 1.Set the action_type value to next 2.Set key needShowError to N
        if(resultDto.isPass()){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_NEXT);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH,ValidationConstants.NO);
        }else{
            //validate fail -> 1.Set key needShowError to Y
            //                 2.Set the action_type value to back
            //                 3.Set key error map
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH,ValidationConstants.YES);
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_PREPARE);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,resultDto.toErrorMsg());
        }
    }


    public void saveFollow1ADraft(HttpServletRequest request,CommonDocDto dto,FollowupInfoADto followupInfoADto){
        //save document draft
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(dto);

        //save draft
        FollowupReport1ADto report1ADto = FollowupReport1ADto.from(followupInfoADto,dto);
        String draftAppNo = followupClient.saveDraftFollowup1A(report1ADto);
        followupInfoADto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(dto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    public void saveFollow1BDraft(HttpServletRequest request,CommonDocDto dto,FollowupInfoBDto followupInfoBDto){
        //save document draft
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(dto);

        //save draft
        FollowupReport1BDto report1BDto = FollowupReport1BDto.from(followupInfoBDto,dto);
        String draftAppNo = followupClient.saveDraftFollowup1B(report1BDto);
        followupInfoBDto.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(dto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }


    /** Save new uploaded documents into FE file repo.
     * @param dto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(CommonDocDto dto) {
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = dto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = dto.newFileSaved(repoIds);
        }
        return newFilesToSync;
    }


    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param dto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(CommonDocDto dto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(dto.getToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            dto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }

    /** Sync new uploaded documents to BE; delete unwanted documents in BE too.
     * @param newFilesToSync a list of DTOs contains ID and data
     * @param toBeDeletedRepoIds a list of repo IDs to be deleted in BE
     */
    public void syncNewDocsAndDeleteFiles(List<NewFileSyncDto> newFilesToSync, List<String> toBeDeletedRepoIds) {
        // sync files to BE file-repo (save new added files, delete useless files)
        if (!CollectionUtils.isEmpty(newFilesToSync) || !CollectionUtils.isEmpty(toBeDeletedRepoIds)) {
            /* Ignore the failure of sync files currently.
             * We should add a mechanism to retry synchronization of files in the future */
            FileRepoSyncDto syncDto = new FileRepoSyncDto();
            syncDto.setNewFiles(newFilesToSync);
            syncDto.setToDeleteIds(toBeDeletedRepoIds);
            bsbFileClient.saveFiles(syncDto);
        }
    }
}
