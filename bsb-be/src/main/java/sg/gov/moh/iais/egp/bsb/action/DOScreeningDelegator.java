package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator(value = "DOScreeningDelegator")
@Slf4j
public class DOScreeningDelegator {
    private final ProcessClient processClient;

    @Autowired
    public DOScreeningDelegator(ProcessClient processClient) {
        this.processClient = processClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ProcessContants.class);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Application application = processClient.getApplicationById("05EF1B40-E3E2-EB11-8B7D-000C293F0C88").getEntity();
        ParamUtil.setRequestAttr(request, ProcessContants.APPLICATION_INFO, application);

    }

    public void screenedByDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

    }

    public void reject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void requestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }


}
