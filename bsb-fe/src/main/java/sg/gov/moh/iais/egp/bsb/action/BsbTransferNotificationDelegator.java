package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
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
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
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

/**
 * @author YiMing
 * @version 2021/11/2 19:46
 **/

@Slf4j
@Delegator(value = "transferNotificationDelegator")
public class BsbTransferNotificationDelegator {
    public static final String KEY_TRANSFER_NOTIFICATION_DTO = "transferNotDto";
    public static final String KEY_FAC_LIST_DTO = "facListDto";
    public static final String KEY_FAC_ID = "facId";
    private final TransferClient transferClient;
    private final FileRepoClient fileRepoClient;
    private final BsbSubmissionCommon subCommon;
    private final DataSubmissionClient submissionClient;
    private final BsbFileClient bsbFileClient;


    public BsbTransferNotificationDelegator(TransferClient transferClient, FileRepoClient fileRepoClient, BsbSubmissionCommon subCommon, DataSubmissionClient submissionClient, BsbFileClient bsbFileClient) {
        this.transferClient = transferClient;
        this.fileRepoClient = fileRepoClient;
        this.subCommon = subCommon;
        this.submissionClient = submissionClient;
        this.bsbFileClient = bsbFileClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request,KEY_TRANSFER_NOTIFICATION_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        ParamUtil.setSessionAttr(request,KEY_OTHER_DOC,null);
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void preFacSelect(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        selectOption(request);
    }

    public void preSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        String facId = ParamUtil.getRequestString(request,KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id",facId);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,facId);
    }

    /**
     * prepareData
     * this module is used to prepare facility info and biological agent/toxin
     * */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        //prepare facility info
        String facId = (String) ParamUtil.getSessionAttr(request,KEY_FAC_ID);
        FacListDto facListDto = subCommon.getFacListDto(request);
        ParamUtil.setSessionAttr(request,KEY_FAC_LIST_DTO,facListDto);
        if(log.isInfoEnabled()){
            log.info("facListDto,facId value is {},{}", LogUtil.escapeCrlf(facListDto.toString()),LogUtil.escapeCrlf(facId));
        }
        if(StringUtils.hasLength(facId)){
            //this part is prepared data for facInfo show in jsp
            FacListDto.FacList facList = subCommon.getFacListById(request,facId);
            ParamUtil.setSessionAttr(request,"facilityInfo",facList);
            List<Biological> biological = facList.getBioMap().get(facId);
            subCommon.prepareSelectOption(request,"scheduleType",biological);
        }else{
            log.error("Your function has arguments with null values");
        }

        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        TransferNotificationDto transferNotificationDto = getTransferNotification(request);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,transferNotificationDto.retrieveValidationResult());
        }
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,"doSettings",settingMap);
        ParamUtil.setRequestAttr(request,KEY_TRANSFER_NOTIFICATION_DTO,transferNotificationDto);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_TRANSFER);
    }

    public void saveAndPrepareConfirm(BaseProcessClass bpc){
         HttpServletRequest request = bpc.request;
         //get value from jsp and bind value to dto
         TransferNotificationDto notificationDto = getTransferNotification(request);
         notificationDto.reqObjectMapping(request);
         //set facId into dto
         notificationDto.setFacId(subCommon.getFacInfo(request).getFacId());
         doValidation(notificationDto,request);
         //use to show file information
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
        ParamUtil.setSessionAttr(request,KEY_OTHER_DOC,(Serializable) notificationDto.getOtherNewInfos());
        ParamUtil.setRequestAttr(request,"docMeta",notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request,KEY_TRANSFER_NOTIFICATION_DTO,notificationDto);
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

    /**
     * save
     * save information to database finally
     * */
    public void save(BaseProcessClass bpc){
         HttpServletRequest request = bpc.request;
         TransferNotificationDto notificationDto = getTransferNotification(request);
        List<NewFileSyncDto> newFilesToSync = null;
         if(!notificationDto.getAllNewDocInfos().isEmpty()){
             //complete simple save file to db and save data to dto for show in jsp
             MultipartFile[] files = notificationDto.getAllNewDocInfos().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
             List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
             newFilesToSync = new ArrayList<>(notificationDto.newFileSaved(repoIds));
         }

         String ensure = ParamUtil.getString(request,"ensure");
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
     * @return TransferNotification
     * */
    private TransferNotificationDto getTransferNotification(HttpServletRequest request){
        TransferNotificationDto notificationDto = (TransferNotificationDto) ParamUtil.getSessionAttr(request,KEY_TRANSFER_NOTIFICATION_DTO);
        return notificationDto == null?getDefaultDto():notificationDto;
    }

    private TransferNotificationDto getDefaultDto() {
        return new TransferNotificationDto();
    }



    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doValidation(TransferNotificationDto dto,HttpServletRequest request){
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
        settingMap.put("ityBat",new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT,"Inventory: Biological Agents",true));
        settingMap.put("ityToxin",new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN,"Inventory: Toxins",true));
        settingMap.put("others",new DocSetting(DocConstants.DOC_TYPE_OTHERS,"Others",true));
        return settingMap;
    }

    /**
     * This method is used to query all Facility info
     */
    private void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,null);
        FacListDto facListDto = submissionClient.queryAllApprovalFacList().getEntity();
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

}
