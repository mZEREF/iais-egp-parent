package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_FAC_ID;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_PREFIX_ENSURE;

@Slf4j
@Delegator("dataSubmissionDelegator")
public class DataSubmissionDelegator {
    public static final String KEY_CONSUME_NOTIFICATION_DTO = "consumeNotification";
    public static final String KEY_DISPOSAL_NOTIFICATION_DTO = "disposalNotification";
    public static final String KEY_EXPORT_NOTIFICATION_DTO = "exportNotification";
    public static final String KEY_RECEIPT_NOTIFICATION_DTO = "receiveNotification";
    public static final String KEY_FACILITY_INFO = "facilityInfo";
    public static final String KEY_FAC_LISTS = "facLists";
    public static final String KEY_FAC_SELECTION = "facSelection";
    public static final String KEY_ACTION_TYPE = "action_type";

    private final DataSubmissionClient dataSubmissionClient;
    private final FileRepoClient fileRepoClient;

    public DataSubmissionDelegator(DataSubmissionClient dataSubmissionClient,FileRepoClient fileRepoClient){
        this.dataSubmissionClient = dataSubmissionClient;
        this.fileRepoClient = fileRepoClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,KEY_CONSUME_NOTIFICATION_DTO,null);
        ParamUtil.setSessionAttr(bpc.request,KEY_DISPOSAL_NOTIFICATION_DTO,null);
        ParamUtil.setSessionAttr(bpc.request,KEY_EXPORT_NOTIFICATION_DTO,null);
        ParamUtil.setSessionAttr(bpc.request,KEY_RECEIPT_NOTIFICATION_DTO,null);
        if(log.isInfoEnabled()){
            log.info("In the future this module will be used to initialize some data");
        }
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
     * StartStep: PrepareFacilitySelect
     * get the selected facility id
     */
    public void doPrepareDataSubmissionSelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        String facId = ParamUtil.getRequestString(request,KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id",facId);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,facId);
    }

    //consume notification code
    /**
     * StartStep: prepareConsumeData
     * Prepare data for the callback and facility info
     */
    public void prepareConsumeData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        //
        ConsumeNotificationDto notificationDto = getConsumeNotification(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        Map<String,DocSetting> settingMap = getDocSettingMap();
        ParamUtil.setRequestAttr(request,"doSettings",settingMap);
        //prepare facility info
        getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, notificationDto);
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
        notificationDto.setEnsure(ParamUtil.getString(request,KEY_PREFIX_ENSURE));
        doConsumeValidation(notificationDto,request);
        //use to show file information
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
        ParamUtil.setRequestAttr(request,"docMeta",notificationDto.getAllDocMetaByDocType());
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: saveConsumeNot
     * save consume notification
     */
    public void saveConsumeNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ConsumeNotificationDto dto = getConsumeNotification(request);
        List<ConsumeNotificationDto.ConsumptionNot> consumeNotList = dto.getConsumptionNotList();
        if(!CollectionUtils.isEmpty(consumeNotList)){
            for (ConsumeNotificationDto.ConsumptionNot not : consumeNotList) {
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
            log.info("you have not key your consumeList");
        }
        String ensure = ParamUtil.getString(request,"ensure");
        dto.setEnsure(ensure);
        ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR = dto.getConsumeNotNeedR();
        dataSubmissionClient.saveConsumeNot(consumeNotNeedR);
    }

    //disposal notification code
    /**
     * StartStep: prepareDisposalData
     */
    public void prepareDisposalData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        //
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        //prepare facility info
        getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareDisposalConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
        notificationDto.reqObjectMapping(request);
        notificationDto.setEnsure(ParamUtil.getString(request,KEY_PREFIX_ENSURE));
        doDisposalValidation(notificationDto,request);
        ParamUtil.setSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: saveDisposalNot
     * save consume notification
     */
    public void saveDisposalNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DisposalNotificationDto notificationDto = getDisposalNotification(request);
//        dataSubmissionClient.saveConsumeNot(dto);
    }

    //export notification code
    /**
     * StartStep: prepareExportData
     */
    public void prepareExportData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        //
        ExportNotificationDto notificationDto = getExportNotification(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        //prepare facility info
        getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareExportConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto notificationDto = getExportNotification(request);
        notificationDto.reqObjectMapping(request);
        notificationDto.setEnsure(ParamUtil.getString(request,KEY_PREFIX_ENSURE));
        doExportValidation(notificationDto,request);
        ParamUtil.setSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: saveExportNot
     * save consume notification
     */
    public void saveExportNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ExportNotificationDto notificationDto = getExportNotification(request);
//        dataSubmissionClient.saveConsumeNot(dto);
    }

    //receive notification code
    /**
     * StartStep: prepareReceiptData
     */
    public void prepareReceiveData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        //
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,notificationDto.retrieveValidationResult());
        }
        //prepare facility info
        getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareReceiptConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
        notificationDto.reqObjectMapping(request);
        notificationDto.setEnsure(ParamUtil.getString(request,KEY_PREFIX_ENSURE));
        doReceiptValidation(notificationDto,request);
        ParamUtil.setSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO, notificationDto);
    }
    /**
     * StartStep: saveReceiptNot
     * save consume notification
     */
    public void saveReceiptNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReceiptNotificationDto notificationDto = getReceiptNotification(request);
//        dataSubmissionClient.saveConsumeNot(dto);
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
        List<SelectOption> selectModel = new ArrayList<>();
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(fac.getFacId(), fac.getFacName()));
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
     * prepare facility info
     */
    private void getFacInfo(HttpServletRequest request){
        String facId = (String) ParamUtil.getSessionAttr(request,KEY_FAC_ID);
        List<FacListDto.FacList> facLists = (List<FacListDto.FacList>)ParamUtil.getSessionAttr(request,KEY_FAC_LISTS);
        for (FacListDto.FacList facList : facLists) {
            if (facList.getFacId().equals(facId)){
                ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
            }
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

