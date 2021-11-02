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
 * @author YiMing
 * @version 2021/11/1 17:42
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequestDto {
    @Data
    @NoArgsConstructor
    public static class TransferList implements Serializable {
        private String scheduleType;
        private String batCode;
        private String expectedBatQty;
        private String receivedQty;
        private String meaUnit;
    }

    private  List<TransferList> transferLists;

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


    //----------------------request-->object----------------------------------
    private static final String SEPARATOR                   = "--v--";
    private static final String KEY_SECTION_AMT             = "sectionAmt";
    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
    private static final String KEY_PREFIX_BAT_CODE         = "batCode";
    private static final String KEY_PREFIX_EXPECTED_BAT_QTY = "expectedBatQty";
    private static final String KEY_PREFIX_RECEIVE_QTY      = "receivedQty";
    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearTransferLists();

        for (int i = 0; i < amt; i++) {
            TransferList transferList = new TransferList();
            transferList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            transferList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_BAT_CODE+SEPARATOR+i));
            transferList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_EXPECTED_BAT_QTY+SEPARATOR+i));
            transferList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_RECEIVE_QTY +SEPARATOR+i));
            transferList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));
            addTransferLists(transferList);
        }

    }

}
