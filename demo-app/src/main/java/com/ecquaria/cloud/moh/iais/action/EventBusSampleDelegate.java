package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.client.SysAdmDemoClient;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.sample.OrgSampleDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.submission.client.model.ServiceStatus;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.kafka.GlobalConstants;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * Process: EventBusSample
 *
 * @author Jinhua
 * @date 2020/1/6 17:53
 */
@Delegator("eventBusSampleDelegate")
@Slf4j
public class EventBusSampleDelegate {
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private SubmissionClient submissionClient;
    @Autowired
    private SysAdmDemoClient sysAdmDemoClient;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("<==== Start to test Event bus =====>"));
        OrgSampleDto orgDto = new OrgSampleDto();
        orgDto.setOrgType("Any");
        orgDto.setUenNo(String.valueOf(System.currentTimeMillis()));
        orgDto.setStatus("Active");
        orgDto.setEventRefNo(orgDto.getUenNo());
        orgDto.setAuditTrailDto(AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET));
        String submissionId = sysAdmDemoClient.getSeqId().getEntity();
        String callbackUrl = systemParamConfig.getInterServerName()
                + "/sample-web/eservice/INTERNET/EventBusSample/1/CallbackStep";
        SubmitReq req = EventBusHelper.getSubmitReq(orgDto, submissionId, "sampleTest",
                "createOrg", "", callbackUrl, "batchjob", false,
                "INTERNET", "EventBusSample", "start");
        SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                + RestApiUrlConsts.EVENT_BUS, req);
    }

    /**
     * AutoStep: CallbackStep
     *
     * @param bpc
     * @throws
     */
    public void callback(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String submissionId = ParamUtil.getString(request,"submissionId");
        log.info("Submission Id ==> " + submissionId);
        String token = ParamUtil.getString(request, "token");
        String serviceName = ParamUtil.getString(request, "service");
        boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
        if (!isLeagal) {
            throw new IaisRuntimeException("Visit without Token!!");
        }
        String operation = ParamUtil.getString(request, "operation");
        Map<String, List<ServiceStatus>> map = submissionClient.getSubmissionStatus(
                AppConsts.REST_PROTOCOL_TYPE
                + RestApiUrlConsts.EVENT_BUS, submissionId, operation);
        if (map.size() >= 1) {
            log.info("Got records from DB");
            boolean completed = true;
            boolean success = true;
            for (Map.Entry<String, List<ServiceStatus>> ent : map.entrySet()) {
                for (ServiceStatus status : ent.getValue()) {
                    if (!status.getStatus().equals(GlobalConstants.STATE_COMPLETED)) {
                        completed = false;
                    }
                    if (!status.getStatus().equals(GlobalConstants.STATE_COMPLETED)
                            && !status.getStatus().equals(GlobalConstants.STATUS_SUCCESS)) {
                        success = false;
                    }
                }
                if (!completed && !success) {
                    break;
                }
            }
            if (!success) {
                submissionClient.setCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                        submissionId, operation, "");
            }
        }
        log.info("Complete");
    }

    /**
     * AutoStep: FinalStep
     *
     * @param bpc
     * @throws
     */
    public void finalStep(BaseProcessClass bpc) {

    }
}
