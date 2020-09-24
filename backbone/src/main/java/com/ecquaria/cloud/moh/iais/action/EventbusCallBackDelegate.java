package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.submission.client.model.ServiceStatus;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.kafka.GlobalConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * EventbusCallBackDelegate
 *
 * @author Jinhua
 * @date 2020/3/3 17:19
 */
@Slf4j
@Delegator("eventbusCallBackDelegate")
public class EventbusCallBackDelegate {
    @Autowired
    private SubmissionClient submissionClient;


    public void callback(BaseProcessClass bpc) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("<=========== Eventbus Callback Start ===========>");
        HttpServletRequest request = bpc.request;
        String submissionId = ParamUtil.getString(request,"submissionId");
        String operation = ParamUtil.getString(request, "operation");
        log.info(StringUtil.changeForLog("Submission Id ===========> " + submissionId));
        String token = ParamUtil.getString(request, "token");
        String serviceName = ParamUtil.getString(request, "service");
        log.info(StringUtil.changeForLog("service name ===========> " + serviceName));
        boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
        String eventRefNum = ParamUtil.getString(request, "eventRefNo");
        log.info("event Ref number ===========> {}", eventRefNum);
        if (!isLeagal) {
            throw new IaisRuntimeException("Visit without Token!!");
        }

        log.info("Event bus operation ===========> {}", operation);
        Map<String, List<ServiceStatus>> map = submissionClient.getSubmissionStatus(
                AppConsts.REST_PROTOCOL_TYPE
                        + RestApiUrlConsts.EVENT_BUS, submissionId, operation);
        log.info("The status map size: ===========> {}", map.size());
        if (map.size() >= 1) {
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
                        submissionClient.submitCompensation(AppConsts.REST_PROTOCOL_TYPE
                                + RestApiUrlConsts.EVENT_BUS,
                                submissionId, status.getServiceName(), operation);
                    }
                }
            } else if (!pending) {
                String flag = SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                        .get("IaisEventbusCbCount",
                        submissionId + "_" + operation + "_CallbackFlag");
                if (StringUtil.isEmpty(flag)) {
                    log.info("<======= Do callback =======>");
                    SpringContextHelper.getContext().getBean(RedisCacheHelper.class).set("IaisEventbusCbCount",
                            submissionId + "_" + operation + "_CallbackFlag", "callback", 60L * 60L * 24L);
                    callbackMethod(submissionId, operation, eventRefNum);
                }
            }
        }
        log.info("<=========== Eventbus Callback Finish ===========>");
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
        } else if(EventBusConsts.OPERATION_POST_INSPECTION_TASK.equals(operation)) {
            log.info("send post inspection task  *****");
            log.info("send post inspection task eventRefNum *****"+eventRefNum);
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
        }else if(EventBusConsts.OPERATION_APPLICATION_UPDATE.equals(operation)){
            log.info("Application update start");
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.ApplicationServiceImpl",
                    "updateFEApplicationStatus");
        } else if (EventBusConsts.OPERATION_LICENCE_SAVE_APPEAL.equals(operation)) {
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.AppealServiceImpl",
                    "updateFEAppealLicenceDto");
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
        Class cls = Class.forName(clsName);
        Object obj = SpringContextHelper.getContext().getBean(cls);
        Method med = cls.getMethod(methodName, new Class[]{String.class, String.class});
        med.invoke(obj, new String[] {eventRefNum, submissionId});
    }

}
