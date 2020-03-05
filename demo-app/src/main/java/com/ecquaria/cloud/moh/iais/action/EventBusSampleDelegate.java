package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.client.SampleClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.sample.OrgSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
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
    private SubmissionClient submissionClient;
    @Autowired
    private SampleClient sampleClient;

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
        String submissionId = sampleClient.getSeqId().getEntity();
        String callbackUrl = "sample-web:8080/sample-web/eservice/INTERNET/ComEventBusCallback";
        SubmitReq req = EventBusHelper.getSubmitReq(orgDto, submissionId, EventBusConsts.SERVICE_NAME_DEMO,
                EventBusConsts.OPERATION_DEMO_CREATE_ORG, "", callbackUrl, "batchjob", true,
                "INTERNET", "EventBusSample", "start");
        req.addCallbackParam("eventRefNo", orgDto.getEventRefNo());
        SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                + RestApiUrlConsts.EVENT_BUS, req);
    }



}
