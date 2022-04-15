package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.system.EventCallbackTrackDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.EventBusClient;
import com.ecquaria.cloud.submission.client.model.GetSubmissionStatusResp;
import com.ecquaria.cloud.submission.client.model.ServiceStatus;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.kafka.GlobalConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * EventbusCallBackDelegate
 *
 * @author Jinhua
 * @date 2020/3/3 17:19
 */
@Slf4j
@RestController
@RequestMapping(value = "halp-event-callback")
public class EventbusCallBackDelegate {
    @Autowired
    private SubmissionClient submissionClient;
    @Autowired
    private EventBusClient eventBusClient;

    @GetMapping
    public ResponseEntity<String> callback(@RequestParam(name = "submissionId") String submissionId,
                                           @RequestParam(name = "updateDate") String updateDate,
                                           @RequestParam(name = "submissionStatus") String submissionStatus,
                                           @RequestParam(name = "serviceStatus") String serviceStatus,
                                           @RequestParam(name = "callbackType") String callbackType,
                                           @RequestParam(name = "compensation") String compensation,
                                           @RequestParam(name = "operation") String operation, @RequestParam(name = "token") String token,
                                           @RequestParam(name = "service") String serviceName,
                                           @RequestParam(name = "eventRefNo") String eventRefNum) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("<=========== Eventbus Callback Start ===========>");
        log.info(StringUtil.changeForLog("Submission Id ===========> " + submissionId));
        log.info(StringUtil.changeForLog("service name ===========> " + serviceName));
        //handle callback tracking
        EventCallbackTrackDto dto = eventBusClient.getCallbackTracking(submissionId, operation).getEntity();
        try {
            boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
            log.info(StringUtil.changeForLog("event Ref number ===========> "+ eventRefNum));
            if (!isLeagal) {
                throw new IaisRuntimeException("Visit without Token!!");
            }

            log.info(StringUtil.changeForLog("Event bus operation ===========> "+ operation));
            GetSubmissionStatusResp statusResp = eventBusClient.getSubmissionStatus(submissionId, operation).getEntity();
            Map<String, List<ServiceStatus>> map = null;
            if (statusResp != null) {
                map = statusResp.getSubmissionStatusMap();
            }
            if (map != null && map.size() >= 1) {
                boolean success = true;
                boolean pending = false;
                for (Map.Entry<String, List<ServiceStatus>> ent : map.entrySet()) {
                    for (ServiceStatus status : ent.getValue()) {
                        log.info("Result status ===========> {}", status.getStatus());
                        if (status.getStatus().contains(GlobalConstants.STATE_PENDING)) {
                            pending = true;
                        } else if (!status.getServiceStatus().contains(GlobalConstants.STATUS_SUCCESS)) {
                            success = false;
                        }
                    }
                }
                if (!success && !pending) {
                    submissionClient.setCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                            submissionId, operation, "");
                    for (Map.Entry<String, List<ServiceStatus>> ent : map.entrySet()) {
                        for (ServiceStatus status : ent.getValue()) {
                            log.info("Come into Compensation ======>");
                            submissionClient.submitCompensation(AppConsts.REST_PROTOCOL_TYPE
                                            + RestApiUrlConsts.EVENT_BUS,
                                    submissionId, status.getServiceName(), operation);
                        }
                    }
                } else if (!pending) {
                    RedisCacheHelper cacheHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
                    String flagKey = submissionId + "_" + operation + "_CallbackFlag";
                    String setVal = UUID.randomUUID().toString();
                    String flag = cacheHelper.get("IaisEventbusCbCount", flagKey);
                    if (StringUtil.isEmpty(flag)) {
                        cacheHelper.set("IaisEventbusCbCount",
                                flagKey, setVal, 60L * 60L * 24L);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(),e);
                            Thread.currentThread().interrupt();
                            throw e;
                        }
                        flag = cacheHelper.get("IaisEventbusCbCount", flagKey);
                        if (setVal.equals(flag)) {
                            log.info("<======= Do callback =======>");
                            callbackMethod(submissionId, operation, eventRefNum);
                            if (dto != null) {
                                dto.setStatus("Completed");
                                eventBusClient.updateCallbackTracking(dto);
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            log.error("Error when eventbus callback ==> ", th);
            if (dto != null) {
                dto.setStatus("Failed");
                eventBusClient.updateCallbackTracking(dto);
            }
            if (th instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("<=========== Eventbus Callback Finish ===========>");

        return ResponseEntity.ok("Success");
    }

    private void callbackMethod(String submissionId, String operation, String eventRefNum)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Do something next step
        if (EventBusConsts.OPERATION_DEMO_CREATE_ORG.equals(operation)) {
            handleDemoNext(submissionId, eventRefNum, operation);
        } else if (EventBusConsts.OPERATION_APPLICATION_UPDATE_APPEAL.equals(operation)) {
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AppealApplicaionServiceImpl",
                    "updateFEAppealApplicationDto");
        } else if(EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION.equals(operation)) {
            log.info(StringUtil.changeForLog("eventRefNum ****"+eventRefNum));
            log.info("send task callback *****");
            invokeMethod(submissionId,eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.LicenceFileDownloadServiceImpl",
                    "sendTask");
            log.info("***send task callback end *****");
        } else if(EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC.equals(operation)) {
            log.info("send post inspection task  *****");
            log.info(StringUtil.changeForLog("send post inspection task eventRefNum *****"+eventRefNum));
            invokeMethod(submissionId,eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.InsRepServiceImpl",
                    "sendPostInsTaskFeData");
        } else if(  EventBusConsts.OPERATION_CREATE_AUDIT_TASK.equals(operation)) {
            log.info("send create audit inspection task  *****");
            invokeMethod(submissionId,eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AuditSystemListServiceImpl",
                    "createTaskCallBack");
        } else if (EventBusConsts.OPERATION_LICENCE_SAVE.equals(operation)) {
            log.info("Licence  save start");
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.LicenceServiceImpl",
                    "createFESuperLicDto");
        } else if(EventBusConsts.OPERATION_APPLICATION_UPDATE.equals(operation)) {
            log.info("Application update start");
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.ApplicationServiceImpl",
                    "updateFEApplicationStatus");
        } else if (EventBusConsts.OPERATION_LICENCE_SAVE_APPEAL.equals(operation)) {
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AppealServiceImpl",
                    "updateFEAppealLicenceDto");
        } else if(EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING.equals(operation)) {
            log.info("-------send task call back----");
            try {
                invokeMethod(submissionId, eventRefNum,
                        "com.ecquaria.cloud.moh.iais.service.impl.LicenceFileDownloadServiceImpl",
                        "removeFile");
            } catch (ClassNotFoundException e){
                log.error(e.getMessage(), e);
            }

        } else if(EventBusConsts.OPERATION__AUDIT_TASK_CANCELED.equalsIgnoreCase(operation)) {
            log.info("-------cancel audit task call back ----");
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AuditSystemListServiceImpl",
                    "releaseTimeForInsUserCallBack");
        } else if(EventBusConsts.OPERATION_REQUEST_INFORMATION.equalsIgnoreCase(operation)) {
            log.info("-------do request information  call back ----");
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AppSubmissionServiceImpl",
                    "updateInboxMsgStatus");
        } else if(EventBusConsts.OPERATION_REQUEST_RFC_RENEW_INFORMATION_SUBMIT.equals(operation)) {
            log.info("-------do rfc/renew request information call back ----");
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AppSubmissionServiceImpl",
                    "updateInboxMsgStatus");
        }
    }

    private void handleDemoNext(String submissionId, String eventRefNum, String operation)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        invokeMethod(submissionId, eventRefNum,
                "com.ecquaria.cloud.moh.iais.service.impl.OrgUserAccountSampleServiceImpl",
                "saveOrgUserEvent");
    }

    private void invokeMethod(String submissionId, String eventRefNum, String clsName, String methodName)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class cls = MiscUtil.getClassFromName(clsName);
        Object obj = SpringContextHelper.getContext().getBean(cls);
        Method med = cls.getMethod(methodName, new Class[]{String.class, String.class});
        med.invoke(obj, new String[] {eventRefNum, submissionId});
    }

}
