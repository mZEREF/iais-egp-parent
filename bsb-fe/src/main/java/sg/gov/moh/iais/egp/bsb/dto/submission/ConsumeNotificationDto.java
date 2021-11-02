package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhu Tangtang
 * @date 2021/11/2 13:19
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumeNotificationDto {
    @Data
    @NoArgsConstructor
    public static class ConsumptionList implements Serializable {
        private String scheduleType;
        private String bat;
        private String consumeType;
        private String consumedQty;
        private String meaUnit;
        private String remarks;
    }

    private List<ConsumptionList> consumptionLists;

    public ConsumeNotificationDto() {
        consumptionLists = new ArrayList<>();
        consumptionLists.add(new ConsumptionList());
    }

    public List<ConsumptionList> getConsumptionLists() { return new ArrayList<>(consumptionLists);
    }

    public void clearConsumptionLists(){
        this.consumptionLists.clear();
    }

    public void addConsumptionLists(ConsumptionList consumptionLists){
        this.consumptionLists.add(consumptionLists);
    }

    public void setConsumptionLists(List<ConsumptionList> consumptionLists) {
        this.consumptionLists = new ArrayList<>(consumptionLists);
    }


    //----------------------request-->object----------------------------------
    private static final String SEPARATOR                   = "--v--";
    private static final String KEY_SECTION_AMT             = "sectionAmt";
    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
    private static final String KEY_PREFIX_BAT         = "bat";
    private static final String KEY_PREFIX_CONSUME_TYPE = "consumeType";
    private static final String KEY_PREFIX_CONSUME_QTY      = "consumedQty";
    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";
    private static final String KEY_PREFIX_REMARKS = "remarks";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearConsumptionLists();

        for (int i = 0; i < amt; i++) {
            ConsumptionList consumptionList = new ConsumptionList();
            consumptionList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            consumptionList.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            consumptionList.setConsumeType(ParamUtil.getString(request,KEY_PREFIX_CONSUME_TYPE+SEPARATOR+i));
            consumptionList.setConsumedQty(ParamUtil.getString(request,KEY_PREFIX_CONSUME_QTY +SEPARATOR+i));
            consumptionList.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));
            consumptionList.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS+SEPARATOR+i));
            addConsumptionLists(consumptionList);
        }

    }
}
