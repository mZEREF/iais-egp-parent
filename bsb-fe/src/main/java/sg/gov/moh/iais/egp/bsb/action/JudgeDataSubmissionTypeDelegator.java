package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

/**
 * @author tangtang
 * @date 2022/1/14 15:41
 */
@Slf4j
@Delegator("judgeDataSubTypeDelegator")
public class JudgeDataSubmissionTypeDelegator {
    public static final String KEY_BACK = "back";
    public static final String KEY_DRAFT = "draft";

    private final DraftClient draftClient;

    public JudgeDataSubmissionTypeDelegator(DraftClient draftClient) {
        this.draftClient = draftClient;
    }

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ACTION_TYPE, null);
        ParamUtil.setSessionAttr(request,KEY_DRAFT,null);
        ParamUtil.setSessionAttr(request,KEY_BACK,null);
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,null);
        if(log.isInfoEnabled()){
            log.info("In the future this module will be used to initialize some data");
        }
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter("editId");
        String applicationId = MaskUtil.unMaskValue("editId", maskedAppId);
        if (maskedAppId == null || applicationId == null || maskedAppId.equals(applicationId)) {
            throw new IaisRuntimeException("Invalid Application ID");
        }
        DraftDto draftDto = draftClient.retrieveDraftByApplicationId(applicationId).getEntity();
        Assert.notNull(draftDto,"Queried draft by applicationId is null");
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> draftMap;
        String dataSubmissionType = null;
        try {
            draftMap = mapper.readValue(draftDto.getDraftData(), new TypeReference<Map<String, Object>>() {});
            dataSubmissionType = (String) draftMap.get("dataSubmissionType");
        } catch (JsonProcessingException e) {
            log.error("translate draftData to Map error");
        }
        Assert.notNull(dataSubmissionType,"dataSubmissionType is null");
        String actionType;
        switch (dataSubmissionType) {
            case KEY_DATA_SUBMISSION_TYPE_CONSUME:
                actionType = "consumeUrl";
                break;
            case KEY_DATA_SUBMISSION_TYPE_DISPOSAL:
                actionType = "disposalUrl";
                break;
            case KEY_DATA_SUBMISSION_TYPE_EXPORT:
                actionType = "exportUrl";
                break;
            case KEY_DATA_SUBMISSION_TYPE_RECEIPT:
                actionType = "receiptUrl";
                break;
            case KEY_DATA_SUBMISSION_TYPE_REQUEST_FOR_TRANSFER:
                actionType = "requestTransferUrl";
                break;
            case KEY_DATA_SUBMISSION_TYPE_TRANSFER:
                actionType = "transferUrl";
                break;
            case KEY_DATA_SUBMISSION_ACKNOWLEDGEMENT_OF_RECEIPT_OF_TRANSFER:
                actionType = "ackTransferUrl";
                break;
            case KEY_DATA_SUBMISSION_TYPE_BAT_INVENTORY:
                actionType = "batInventoryUrl";
                break;
            default:
                throw new IllegalStateException("Unexpected dataSubmissionType: " + dataSubmissionType);
        }
        ParamUtil.setSessionAttr(request, KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request,KEY_DRAFT,draftDto);
        ParamUtil.setSessionAttr(request,KEY_BACK,"app");
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,dataSubmissionType);
    }
}
