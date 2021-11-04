package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_PREFIX_REMARKS;


@Delegator("dataSubmissionDelegator")
@Slf4j
public class DataSubmissionDelegator {
    @Autowired
    private DataSubmissionClient dataSubmissionClient;

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
     * StartStep: PrepareFacilitySelect
     */
    public void doPrepareFacilitySelect(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
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
     * StartStep: prepareConfirm
     */
    public void prepareConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
//        ParamUtil.setSessionAttr(request,"consumeNotification", null);
        ConsumeNotificationDto dto = new ConsumeNotificationDto();
        dto.reqObjectMapping(request);
        dto.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS));
        ParamUtil.setRequestAttr(request,"notification", dto);
    }
    /**
     * StartStep: saveConsumeNot
     */
    public void saveConsumeNot(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ConsumeNotificationDto dto = (ConsumeNotificationDto) ParamUtil.getSessionAttr(request,"consumeNotification");


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

    public void selectOption(HttpServletRequest request) {
        FacListDto facListDto = dataSubmissionClient.queryAllApprovalFacList().getEntity();
        List<FacListDto.FacList> facLists = facListDto.getFacLists();
        facLists.remove(0);
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(fac.getFacId(), fac.getFacName()));
        }
        ParamUtil.setRequestAttr(request, "facList", selectModel);
    }
}
