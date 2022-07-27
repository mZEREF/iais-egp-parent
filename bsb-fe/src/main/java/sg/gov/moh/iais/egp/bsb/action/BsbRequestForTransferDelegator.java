package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DATA_SUBMISSION_REQUEST_FOR_TRANSFER;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_DATA_SUBMISSION;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_DATA_SUBMISSION_TYPE_REQUEST_FOR_TRANSFER;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_FAC_LISTS;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_FAC_SELECTION;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_SUBMISSION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;

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
    public static final String KEY_DRAFT = "draft";
    private final BsbSubmissionCommon subCommon;
    private final DataSubmissionClient submissionClient;

    @Autowired
    public BsbRequestForTransferDelegator(BsbSubmissionCommon subCommon, DataSubmissionClient submissionClient) {
        this.subCommon = subCommon;
        this.submissionClient = submissionClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_FACILITY_INFO);
        session.removeAttribute(KEY_TRANSFER_REQUEST_DTO);
        session.removeAttribute(KEY_FAC_ID);
        AuditTrailHelper.auditFunction(MODULE_DATA_SUBMISSION, FUNCTION_DATA_SUBMISSION_REQUEST_FOR_TRANSFER);
    }

    public void preFacSelect(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        selectOption(request);
        DraftDto draft = (DraftDto) ParamUtil.getSessionAttr(request, KEY_DRAFT);
        String dataSubmissionType = (String) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_TYPE);
        if (draft != null && dataSubmissionType != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                TransferRequestDto dto = mapper.readValue(draft.getDraftData(), TransferRequestDto.class);
                ParamUtil.setSessionAttr(request, KEY_TRANSFER_REQUEST_DTO, dto);
                ParamUtil.setSessionAttr(request, KEY_FAC_ID, dto.getFacId());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
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
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_REQUEST_FOR_TRANSFER);
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
        submissionClient.saveRequestTransfer(dto);
    }


    public void saveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TransferRequestDto dto = getTransferRequest(request);
        dto.setDataSubmissionType(KEY_DATA_SUBMISSION_TYPE_REQUEST_FOR_TRANSFER);
        dto.reqObjectMapping(request,subCommon);

        //save draft
        String draftAppNo = submissionClient.saveDraftRequestTransfer(dto);
        dto.setDraftAppNo(draftAppNo);
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
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
        List<SelectOption> selectModel = new ArrayList<>(facLists.size());
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
