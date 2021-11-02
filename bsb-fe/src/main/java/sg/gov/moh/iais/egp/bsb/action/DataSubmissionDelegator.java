package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.submission.ConsumeNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.DisposalNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ExportNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReceiptNotificationDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

@Delegator("dataSubmissionDelegator")
@Slf4j
public class DataSubmissionDelegator {
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
     * StartStep: PrepareDataSubmissionSelect
     */
    public void doPrepareFacilitySelect(BaseProcessClass bpc) {
        // todo get facility info
    }
    /**
     * StartStep: PrepareSwitch
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
    }
    /**
     * StartStep: prepareConsume
     */
    public void prepareConsume(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"consumeNotification",new ConsumeNotificationDto());
    }
    /**
     * StartStep: prepareConsume
     */
    public void prepareDisposal(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"disposalNotification",new DisposalNotificationDto());
    }
    /**
     * StartStep: prepareConsume
     */
    public void prepareExport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"exportNotification",new ExportNotificationDto());
    }
    /**
     * StartStep: prepareConsume
     */
    public void prepareReceive(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"receiveNotification",new ReceiptNotificationDto());
    }
}
