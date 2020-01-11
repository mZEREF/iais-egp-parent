package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.submission.client.model.ServiceStatus;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.kafka.GlobalConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private SubmissionClient client;

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
        log.info(StringUtil.changeForLog("The submissionId is-->:"+submissionId));
        String token = ParamUtil.getString(request, "token");
        String serviceName = ParamUtil.getString(request, "service");
        log.info(StringUtil.changeForLog("The serviceName is-->:"+serviceName));
        boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
//        if (!isLeagal) {
//            throw new IaisRuntimeException("Visit without Token!!");
//        }
        String operation = ParamUtil.getString(request, "operation");
        log.info(StringUtil.changeForLog("The operation is-->:"+operation));
        Map<String, List<ServiceStatus>> map = client.getSubmissionStatus(AppConsts.REST_PROTOCOL_TYPE
                + RestApiUrlConsts.EVENT_BUS, submissionId, operation);
        log.info(StringUtil.changeForLog("The map.size() is-->:"+map.size()));
        if (map.size() > 0) {
            boolean completed = true;
            boolean success = true;
            for (Map.Entry<String, List<ServiceStatus>> ent : map.entrySet()) {
                for (ServiceStatus status : ent.getValue()) {
                    log.info(StringUtil.changeForLog("The status is-->:"+status.getStatus()));
                    log.info(StringUtil.changeForLog("The getErrorMsg is-->:"+status.getErrorMsg()));
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
            log.info(StringUtil.changeForLog("The success is-->:"+success));
            log.info(StringUtil.changeForLog("The completed is-->:"+completed));
            if (!success) {
                client.setCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                        submissionId, operation, "");
            }else{
                log.info(StringUtil.changeForLog("The BE licence save success "));
                if(EventBusConsts.OPERATION_LICENCE_SAVE.equals(operation)){
                    String data = ParamUtil.getString(request,"data");
                    ObjectMapper mapper = new ObjectMapper();
                    EventBusLicenceGroupDtos eventBusLicenceGroupDtos = mapper.readValue(data, EventBusLicenceGroupDtos.class);
                    //step2 save licence to Fe DB
                     licenceService.createFESuperLicDto(eventBusLicenceGroupDtos.getLicenceGroupDtos());
                }
            }
        }
        log.info(StringUtil.changeForLog("The LicenceService callBack end ..."));
    }
}
