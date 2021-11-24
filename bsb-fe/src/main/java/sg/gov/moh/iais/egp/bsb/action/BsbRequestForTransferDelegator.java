package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.AckTransferReceiptDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferRequestDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_FAC_LISTS;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_FAC_SELECTION;

/**
 * @author YiMing
 * @version 2021/11/1 17:27
 **/

@Slf4j
@Delegator(value = "requestTransferDelegator")
public class BsbRequestForTransferDelegator {

    public static final String KEY_FAC_ID = "facId";
    private static final String KEY_FACILITY_INFO = "facilityInfo";
    private static final String KEY_TRANSFER_REQUEST_DTO = "transferRequestDto";
    private final TransferClient transferClient;
    private final BsbSubmissionCommon subCommon;
    private final DataSubmissionClient submissionClient;

    public BsbRequestForTransferDelegator(TransferClient transferClient, BsbSubmissionCommon subCommon, DataSubmissionClient submissionClient) {
        this.transferClient = transferClient;
        this.subCommon = subCommon;
        this.submissionClient = submissionClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request,KEY_TRANSFER_REQUEST_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void preFacSelect(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        selectOption(request);
    }

    public void preSwitch0(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        String facId = ParamUtil.getRequestString(request,KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id",facId);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,facId);

    }

    public void prepareSwitch1(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = request.getParameter("action_type");
        ParamUtil.setSessionAttr(bpc.request, "action_type", actionType);
    }

    /**
     * prepareData
     * this module is used to prepare facility info and biological agent/toxin
     * */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        TransferRequestDto dto = getTransferRequest(request);
        ParamUtil.setRequestAttr(request,"transferReq",dto);
        FacListDto.FacList facList = subCommon.getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
        subCommon.prepareSelectOption(request,"scheduleType",subCommon.getBiologicalById(request));
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
    }

    public void saveAndConfirm(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        TransferRequestDto dto = getTransferRequest(request);
        dto.reqObjectMapping(request,subCommon);
        ParamUtil.setSessionAttr(request,KEY_TRANSFER_REQUEST_DTO,dto);
        doValidation(dto,request);
        ParamUtil.setRequestAttr(request,"transferLists",dto.getTransferLists());
    }

    public void save(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        TransferRequestDto dto = getTransferRequest(request);
        transferClient.saveRequestTransfer(dto);
    }


    public Map<String,DocSetting> getDocSettingMap(){
        Map<String,DocSetting> settingMap = new HashMap<>();
        settingMap.put("ityBat",new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT,"Inventory: Biological Agents",true));
        settingMap.put("ityToxin",new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN,"Inventory: Toxins",true));
        settingMap.put("others",new DocSetting(DocConstants.DOC_TYPE_OTHERS,"others",true));
        return settingMap;
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doValidation(TransferRequestDto dto, HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
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
        List<SelectOption> selectModel = new ArrayList<>();
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(MaskUtil.maskValue("id",fac.getFacId()), fac.getFacName()));
        }
        ParamUtil.setRequestAttr(request, KEY_FAC_SELECTION, selectModel);
        //Put in session called for later operations
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,(Serializable) facLists);
    }

    private TransferRequestDto getTransferRequest(HttpServletRequest request){
        TransferRequestDto dto = (TransferRequestDto) ParamUtil.getSessionAttr(request,KEY_TRANSFER_REQUEST_DTO);
        return dto == null?getDefaultDto():dto;
    }

    private TransferRequestDto getDefaultDto() {
        return new TransferRequestDto();
    }

}
