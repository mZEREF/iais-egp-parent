package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author YiMing
 * @version 2021/11/2 14:21
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferNotificationDto implements Serializable {

    @Data
    @NoArgsConstructor
    public static class TransferNot implements Serializable{
        private String scheduleType;
        private String batCode;
        private String transferType;
        private String batQty;
        private String transferQty;
        private String mstUnit;
    }


    public TransferNotificationDto() {
        //Initialize the transferNotList
        transferNotList = new ArrayList<>();
        transferNotList.add(new TransferNot());
    }


    private List<TransferNot> transferNotList;
    private String facId;
    private String receiveFacility;
    private String expectedTfDate;
    private String expArrivalTime;
    private String providerName;
    private String remarks;
    private String ensure;



    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public String getExpectedTfDate() {
        return expectedTfDate;
    }

    public void setExpectedTfDate(String expectedTfDate) {
        this.expectedTfDate = expectedTfDate;
    }

    public String getExpArrivalTime() {
        return expArrivalTime;
    }

    public void setExpArrivalTime(String expArrivalTime) {
        this.expArrivalTime = expArrivalTime;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceiveFacility() {
        return receiveFacility;
    }

    public void setReceiveFacility(String receiveFacility) {
        this.receiveFacility = receiveFacility;
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getEnsure() {
        return ensure;
    }

    public void setEnsure(String ensure) {
        this.ensure = ensure;
    }

    //transferNotList getter setter
    public List<TransferNot> getTransferNotList() { return new ArrayList<>(transferNotList);
    }

    public void clearTransferNotList(){
        this.transferNotList.clear();
    }

    public void addTransferNotList(TransferNot transferNot){
        this.transferNotList.add(transferNot);
    }

    public void setTransferNotList(List<TransferNot> transferLists) {
        this.transferNotList = new ArrayList<>(transferLists);
    }



    //------------------------------------------Validation---------------------------------------------

    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("transferFeignClient", "validateTransferNot", new Object[]{this});
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
    private static final String KEY_SECTION_AMT             = "sectionAmt";
    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
    private static final String KEY_PREFIX_BAT_CODE         = "batCode";
    private static final String KEY_PREFIX_BAT_QTY          = "transferType";
    private static final String KEY_PREFIX_TRANSFER_TYPE    = "batQty";
    private static final String KEY_PREFIX_TRANSFER_QTY     = "transferQty";
    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "mstUnit";
    private static final String KEY_EXPECTED_TRANSFER_DATE  = "expectedTfDate";
    private static final String KEY_EXPECTED_ARRIVAL_TIME   = "expArrivalTime";
    private static final String KEY_PROVIDER_NAME           = "providerName";
    private static final String KEY_REMARK                  = "remarks";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearTransferNotList();

        for (int i = 0; i < amt; i++) {
            TransferNot transferNot = new TransferNot();
            transferNot.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            transferNot.setBatCode(ParamUtil.getString(request,KEY_PREFIX_BAT_CODE+SEPARATOR+i));
            transferNot.setTransferType(ParamUtil.getString(request,KEY_PREFIX_BAT_QTY+SEPARATOR+i));
            transferNot.setBatQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_TYPE +SEPARATOR+i));
            transferNot.setTransferQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_QTY+SEPARATOR+i));
            transferNot.setMstUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));
            addTransferNotList(transferNot);
        }
        this.setExpectedTfDate(ParamUtil.getString(request,KEY_EXPECTED_TRANSFER_DATE));
        this.setExpArrivalTime(ParamUtil.getString(request,KEY_EXPECTED_ARRIVAL_TIME));
        this.setProviderName(ParamUtil.getString(request,KEY_PROVIDER_NAME));
        this.setRemarks(ParamUtil.getString(request,KEY_REMARK));
    }

}
