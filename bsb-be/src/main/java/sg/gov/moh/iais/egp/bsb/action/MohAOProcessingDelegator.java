package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;


/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "aoProcessingDelegator")
@Slf4j
public class MohAOProcessingDelegator {

    public void start(BaseProcessClass bpc) {
        // do nothing now
    }

    public void prepareData(BaseProcessClass bpc) {
        // do nothing now
    }

    public void prepareSwitch(BaseProcessClass bpc){
        // do nothing now
    }

    public void process(BaseProcessClass bpc){
        // do nothing now
    }
}