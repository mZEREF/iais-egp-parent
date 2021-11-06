package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Delegator("dataSubmissionDelegator")
public class DataSubmissionDelegator {
    public static final String KEY_CONSUME_NOTIFICATION_DTO = "consumeNotification";
    public static final String KEY_FACILITY_INFO = "facilityInfo";
    public static final String KEY_FAC_LISTS = "facLists";
    public static final String KEY_FAC_SELECTION = "facSelection";
    public static final String KEY_FAC_ID = "facId";

    private DataSubmissionClient dataSubmissionClient;

    public DataSubmissionDelegator(DataSubmissionClient dataSubmissionClient){
        this.dataSubmissionClient = dataSubmissionClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,KEY_CONSUME_NOTIFICATION_DTO,null);
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

    /**
     * StartStep: prepareConsume
     * Prepare data for the callback and facility info
     */
    public void prepareConsume(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,null);
        //
        ConsumeNotificationDto consumeNotification = getConsumeNotification(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,consumeNotification.retrieveValidationResult());
        }
        //prepare facility info
        String facId = (String) ParamUtil.getSessionAttr(request,KEY_FAC_ID);
        List<FacListDto.FacList> facLists = (List<FacListDto.FacList>)ParamUtil.getSessionAttr(request,KEY_FAC_LISTS);
        for (FacListDto.FacList facList : facLists) {
            if (facList.getFacId().equals(facId)){
                ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
            }
        }
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, consumeNotification);
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
     * StartStep: prepareConfirm
     * Put data into session for preview and save
     */
    public void prepareConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
//        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, null);
        ConsumeNotificationDto consumeNotification = getConsumeNotification(request);
        consumeNotification.reqObjectMapping(request);
        doValidation(consumeNotification,request);
        ParamUtil.setSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO, consumeNotification);
    }
    /**
     * StartStep: saveConsumeNot
     * save consume notification
     */
    public void saveConsumeNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ConsumeNotificationDto dto = getConsumeNotification(request);
        dataSubmissionClient.saveConsumeNot(dto);
    }


    /**
     * StartStep: prepareConsume
     */
    public void prepareDisposal(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"disposalNotification",new DisposalNotificationDto());
    }
    /**
     * StartStep: prepareConsume
     */
    public void prepareExport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"exportNotification",new ExportNotificationDto());
    }
    /**
     * StartStep: prepareConsume
     */
    public void prepareReceive(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"receiveNotification",new ReceiptNotificationDto());
    }

    /**
     * This method is used to query all Facility info
     */
    public void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,null);
        FacListDto facListDto = dataSubmissionClient.queryAllApprovalFacList().getEntity();
        List<FacListDto.FacList> facLists = facListDto.getFacLists();
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
     * this method just used to charge if dto exist
     * */
    public ConsumeNotificationDto getConsumeNotification(HttpServletRequest request){
        ConsumeNotificationDto notificationDto = (ConsumeNotificationDto) ParamUtil.getSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO);
        return notificationDto == null ? getDefaultConsumeNotDto() : notificationDto;
    }

    private ConsumeNotificationDto getDefaultConsumeNotDto() {
        return new ConsumeNotificationDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    public void doValidation(ConsumeNotificationDto dto,HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }
}

