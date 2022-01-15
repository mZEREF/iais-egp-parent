package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jmapper.JMapper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.entity.Draft;
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

    private final DataSubmissionClient dataSubmissionClient;

    public JudgeDataSubmissionTypeDelegator(DataSubmissionClient dataSubmissionClient) {
        this.dataSubmissionClient = dataSubmissionClient;
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

    public void prepareData(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter("editId");
        String applicationId = MaskUtil.unMaskValue("editId", maskedAppId);
        if (maskedAppId == null || applicationId == null || maskedAppId.equals(applicationId)) {
            throw new IaisRuntimeException("Invalid Application ID");
        }
        DraftDto draftDto = dataSubmissionClient.getDraftDto(applicationId).getEntity();
        assert draftDto != null;
        JMapper<Draft, DraftDto> draftJMapper = new JMapper<>(Draft.class,DraftDto.class);
        Draft draft = draftJMapper.getDestinationWithoutControl(draftDto);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> draftMap = mapper.readValue(draft.getDraftData(), new TypeReference<Map<String, Object>>() {});
        String dataSubmissionType = (String) draftMap.get("dataSubmissionType");
        //
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
        ParamUtil.setSessionAttr(request,KEY_DRAFT,draft);
        ParamUtil.setSessionAttr(request,KEY_BACK,"app");
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,dataSubmissionType);
    }
}
