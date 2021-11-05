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
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

/**
 * @author Zhu Tangtang
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumeNotificationDto implements Serializable {
    @Data
    @NoArgsConstructor
    public static class ConsumptionNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String consumeType;
        private String consumedQty;
        private String meaUnit;
    }

    private List<ConsumptionNot> consumptionNotList;
    private String facId;
    private String remarks;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ConsumeNotificationDto() {
        consumptionNotList = new ArrayList<>();
        consumptionNotList.add(new ConsumptionNot());
    }

    public List<ConsumptionNot> getConsumptionNotList() { return new ArrayList<>(consumptionNotList);
    }

    public void clearConsumptionNotList(){
        this.consumptionNotList.clear();
    }

    public void addConsumptionNotList(ConsumptionNot consumptionLists){
        this.consumptionNotList.add(consumptionLists);
    }

    public void setConsumptionNotList(List<ConsumptionNot> consumptionLists) {
        this.consumptionNotList = new ArrayList<>(consumptionLists);
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateConsumeNot", new Object[]{this});
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

    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        clearConsumptionNotList();
        for (int i = 0; i < amt; i++) {
            ConsumptionNot consumptionNot = new ConsumptionNot();
            consumptionNot.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            consumptionNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            consumptionNot.setConsumeType(ParamUtil.getString(request,KEY_PREFIX_CONSUME_TYPE+SEPARATOR+i));
            consumptionNot.setConsumedQty(ParamUtil.getString(request,KEY_PREFIX_CONSUME_QTY +SEPARATOR+i));
            consumptionNot.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));
            addConsumptionNotList(consumptionNot);
        }
        this.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS));
        this.setFacId((String) ParamUtil.getSessionAttr(request,"facId"));
    }
}
