package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferRequestDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YiMing
 * @version 2021/11/1 17:27
 **/

@Slf4j
@Delegator(value = "requestTransferDelegator")
public class BsbRequestForTransferDelegator {

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        if(log.isInfoEnabled()){
            log.info("In the future this module will be used to initialize some data");
        }
    }

    /**
     * prepareData
     * this module is used to prepare facility info and biological agent/toxin
     * */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"transferReq",new TransferRequestDto());
    }


}
