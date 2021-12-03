package sg.gov.moh.iais.egp.bsb.common.rfc;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;

import java.util.List;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2021/11/9
 */
@Slf4j
public class DecisionFlowTypeImpl implements DecisionFlowType{
    @Override
    public RfcFlowType facRegFlowType(List<DiffContent> list) {
        Map<String,RfcFlowType> map = RfcFakerInitConfig.getFacRegFlowConfig();
        //list is null, don't have change, return
        if (list.isEmpty()){
            return RfcFlowType.DONOTHING;
        }
        //config map is null, throw exception
        if (map.isEmpty()){
            log.error("============= config map is null =============");
            throw new IaisRuntimeException("config map is null");
        }
        for (DiffContent diffContent : list) {
            String field = diffContent.getModifyField();
            RfcFlowType flowTypeValue = map.get(field);
            if (flowTypeValue == null){
                //config map don't contains modify field, throw exception, remark field
                log.error("config map don't contains modify field {}", field);
                throw new IaisRuntimeException("config map don't contains modify field");
            }else if(flowTypeValue == RfcFlowType.AMENDMENT){
                //as long as there is a type of APPROVAL, return APPROVAL directly
                return RfcFlowType.AMENDMENT;
            }
        }
        //there has modify field, but not APPROVAL type
        return RfcFlowType.NOTIFICATION;
    }

    @Override
    public RfcFlowType approvalAppFlowType(List<DiffContent> list) {
        //all change info, return AMENDMENT
        if (!list.isEmpty()){
            return RfcFlowType.AMENDMENT;
        }
        return RfcFlowType.DONOTHING;
    }

    @Override
    public RfcFlowType facCerRegFlowType(List<DiffContent> list) {
        Map<String,RfcFlowType> map = RfcFakerInitConfig.getFacCerRegFlowConfig();
        //list is null, don't have change, return
        if (list.isEmpty()){
            return RfcFlowType.DONOTHING;
        }
        //config map is null, throw exception
        if (map.isEmpty()){
            log.error("============= config map is null =============");
            throw new IaisRuntimeException("config map is null");
        }
        for (DiffContent diffContent : list) {
            String field = diffContent.getModifyField();
            RfcFlowType flowTypeValue = map.get(field);
            if (flowTypeValue == null){
                //config map don't contains modify field, throw exception, remark field
                log.error("config map don't contains modify field {}", field);
                throw new IaisRuntimeException("config map don't contains modify field");
            }else if(flowTypeValue == RfcFlowType.AMENDMENT){
                //as long as there is a type of APPROVAL, return APPROVAL directly
                return RfcFlowType.AMENDMENT;
            }
        }
        //there has modify field, but not APPROVAL type
        return RfcFlowType.NOTIFICATION;
    }
}
