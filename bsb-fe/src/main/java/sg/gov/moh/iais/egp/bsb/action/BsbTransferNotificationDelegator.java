package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YiMing
 * @version 2021/11/2 19:46
 **/

@Slf4j
@Delegator(value = "transferNotificationDelegator")
public class BsbTransferNotificationDelegator {
    public static final String KEY_TRANSFER_NOTIFICATION_DTO = "transferNotDto";
    private final TransferClient transferClient;

    public BsbTransferNotificationDelegator(TransferClient transferClient) {
        this.transferClient = transferClient;
    }

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
        TransferNotificationDto transferNotificationDto = getTransferNotification(request);
        ParamUtil.setRequestAttr(request,"transferNot",transferNotificationDto);
    }

    public void saveAndPrepareConfirm(BaseProcessClass bpc){
         HttpServletRequest request = bpc.request;
         //get value from jsp and bind value to dto
         TransferNotificationDto notificationDto = getTransferNotification(request);
         notificationDto.reqObjectMapping(request);
         ParamUtil.setSessionAttr(request,"transferNot",notificationDto);
    }

    public void save(BaseProcessClass bpc){
         HttpServletRequest request = bpc.request;
         TransferNotificationDto notificationDto = getTransferNotification(request);
         transferClient.saveNewTransferNot(notificationDto);
    }

    /**
     * this method just used to charge if dto exist
     * */
    public TransferNotificationDto getTransferNotification(HttpServletRequest request){
        TransferNotificationDto notificationDto = (TransferNotificationDto) ParamUtil.getSessionAttr(request,KEY_TRANSFER_NOTIFICATION_DTO);
        return notificationDto == null?getDefaultDto():notificationDto;
    }

    private TransferNotificationDto getDefaultDto() {
        return new TransferNotificationDto();
    }
}
