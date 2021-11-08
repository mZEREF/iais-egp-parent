package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/11/2 19:46
 **/

@Slf4j
@Delegator(value = "transferNotificationDelegator")
public class BsbTransferNotificationDelegator {
    public static final String KEY_TRANSFER_NOTIFICATION_DTO = "transferNotDto";
    private final TransferClient transferClient;
    private final FileRepoClient fileRepoClient;

    public BsbTransferNotificationDelegator(TransferClient transferClient, FileRepoClient fileRepoClient) {
        this.transferClient = transferClient;
        this.fileRepoClient = fileRepoClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        if(log.isInfoEnabled()){
            log.info("In the future this module will be used to initialize some data");
        }
    }

    /**
     * prepareData
     * this module is used to prepare facility info and biological agent/toxin
     * */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        TransferNotificationDto transferNotificationDto = getTransferNotification(request);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,transferNotificationDto.retrieveValidationResult());
        }
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,"doSettings",settingMap);
        ParamUtil.setRequestAttr(request,KEY_TRANSFER_NOTIFICATION_DTO,transferNotificationDto);
    }

    public void saveAndPrepareConfirm(BaseProcessClass bpc){
         HttpServletRequest request = bpc.request;
         //get value from jsp and bind value to dto
         TransferNotificationDto notificationDto = getTransferNotification(request);
         notificationDto.reqObjectMapping(request);
         doValidation(notificationDto,request);
         //use to show file information
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
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
         List<TransferNotificationDto.TransferNot> transferNotList = notificationDto.getTransferNotList();
         if(!CollectionUtils.isEmpty(transferNotList)){
             for (TransferNotificationDto.TransferNot not : transferNotList) {
                 PrimaryDocDto primaryDocDto = not.getPrimaryDocDto();
                 if(primaryDocDto != null){
                     //complete simple save file to db and save data to dto for show in jsp
                     MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
                     List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
                     primaryDocDto.newFileSaved(repoIds);
                     //newFile change to saved File and save info to db
                     not.setSavedInfos(primaryDocDto.getExistDocTypeList());
                 }else{
                     log.info("please ensure your object has value");
                 }
             }
         }else{
             log.info("you have not key your transferList");
         }
         String ensure = ParamUtil.getString(request,"ensure");
         notificationDto.setEnsure(ensure);
         TransferNotificationDto.TransferNotNeedR transferNotNeedR = notificationDto.getTransferNotNeedR();
         transferClient.saveNewTransferNot(transferNotNeedR);
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
        settingMap.put("others",new DocSetting(DocConstants.DOC_TYPE_OTHERS,"others",true));
        return settingMap;
    }
}
