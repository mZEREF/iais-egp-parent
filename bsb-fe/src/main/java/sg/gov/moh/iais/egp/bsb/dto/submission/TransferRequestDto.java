package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.action.BsbSubmissionCommon;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiMing
 * @version 2021/11/1 17:42
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequestDto implements Serializable {
    @Data
    @NoArgsConstructor
    public static class TransferList implements Serializable {
        private String scheduleType;
        private String batCode;
        private String expectedBatQty;
        private String expReceivedQty;
        private String meaUnit;
    }

    private  List<TransferList> transferLists;
    private String facId;
    private String receivingFacId;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public TransferRequestDto() {
        transferLists = new ArrayList<>();
        transferLists.add(new TransferList());
    }

    public List<TransferList> getTransferLists() { return new ArrayList<>(transferLists);
    }

    public void clearTransferLists(){
        this.transferLists.clear();
    }

    public void addTransferLists(TransferList transferList){
        this.transferLists.add(transferList);
    }

    public void setTransferLists(List<TransferList> transferLists) {
        this.transferLists = new ArrayList<>(transferLists);
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getReceivingFacId() {
        return receivingFacId;
    }

    public void setReceivingFacId(String receivingFacId) {
        this.receivingFacId = receivingFacId;
    }

    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("transferFeignClient", "validateRequestTransfer", new Object[]{this});
        return validationResultDto.isPass();
    }


    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    //----------------------request-->object----------------------------------
    private static final String SEPARATOR                   = "--v--";
    private static final String KEY_SECTION_IDXES           = "sectionIdx";
    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
    private static final String KEY_PREFIX_BAT_CODE         = "batCode";
    private static final String KEY_PREFIX_EXPECTED_BAT_QTY = "expectedBatQty";
    private static final String KEY_PREFIX_RECEIVE_QTY      = "expReceivedQty";
    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request, BsbSubmissionCommon common){
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearTransferLists();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            TransferList transferList = new TransferList();
            transferList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+idx));
            transferList.setBatCode(ParamUtil.getString(request,KEY_PREFIX_BAT_CODE+SEPARATOR+idx));
            transferList.setExpectedBatQty(ParamUtil.getString(request,KEY_PREFIX_EXPECTED_BAT_QTY+SEPARATOR+idx));
            transferList.setExpReceivedQty(ParamUtil.getString(request,KEY_PREFIX_RECEIVE_QTY +SEPARATOR+idx));
            transferList.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+idx));
            addTransferLists(transferList);
        }
        this.facId = common.getFacInfo(request).getFacId();
    }

}
