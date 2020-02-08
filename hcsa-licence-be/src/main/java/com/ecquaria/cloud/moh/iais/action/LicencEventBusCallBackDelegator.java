package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.submission.client.model.ServiceStatus;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.kafka.GlobalConstants;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * LicencEventBusCallBackDelegator
 *
 * @author suocheng
 * @date 12/25/2019
 */
@Delegator(value = "licencEventBusCallBackDelegator")
@Slf4j
public class LicencEventBusCallBackDelegator {
    @Autowired
    private SubmissionClient submissionClient;

    @Autowired
    private LicenceService licenceService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void callBack(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("The LicenceService callBack start ..."));
        HttpServletRequest request = bpc.request;
        String submissionId = ParamUtil.getString(request,"submissionId");
        log.info(StringUtil.changeForLog("Submission Id ==> " + submissionId));
        String token = ParamUtil.getString(request, "token");
        String serviceName = ParamUtil.getString(request, "service");
        log.info(StringUtil.changeForLog("serviceName is ==> " + serviceName));
        boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
        String eventRefNum = ParamUtil.getString(request, "eventRefNo");
        log.info(StringUtil.changeForLog("eventRefNum is ==> " + eventRefNum));
        if (!isLeagal) {
            throw new IaisRuntimeException("Visit without Token!!");
        }
        String operation = ParamUtil.getString(request, "operation");
        log.info(StringUtil.changeForLog("operation is ==> " + operation));
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
                            && !status.getServiceStatus().equals(GlobalConstants.STATUS_SUCCESS)) {
                        success = false;
                    }
                }
                log.info(StringUtil.changeForLog("completed is ==> " + completed));
                log.info(StringUtil.changeForLog("success is ==> " + success));
                if (!completed && !success) {
                    break;
                }
            }
            if (!success) {
                if(EventBusConsts.OPERATION_LICENCE_SAVE.equals(operation)){
                    submissionClient.setCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                            submissionId, EventBusConsts.OPERATION_APPLICATION_UPDATE, "");
                }else if(EventBusConsts.OPERATION_APPLICATION_UPDATE.equals(operation)){
                    submissionClient.setCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                            submissionId, EventBusConsts.OPERATION_LICENCE_SAVE, "");
                }
            } else {
                log.info(StringUtil.changeForLog("The BE licence save success "));
                if(EventBusConsts.OPERATION_LICENCE_SAVE.equals(operation)){
                    LicEicRequestTrackingDto licEicRequestTrackingDto = licenceService.getLicEicRequestTrackingDtoByRefNo(eventRefNum);
                    if(licEicRequestTrackingDto!=null){
                        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
                        licEicRequestTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        licEicRequestTrackingDto.setAuditTrailDto(auditTrailDto);
                        licenceService.updateLicEicRequestTrackingDto(licEicRequestTrackingDto);
                    }else{
                        log.error(StringUtil.changeForLog("This eventReo can not get the LicEicRequestTrackingDto -->:"+eventRefNum));
                    }
                }
            }

        }
        log.info(StringUtil.changeForLog("The LicenceService callBack end ..."));
    }
}
