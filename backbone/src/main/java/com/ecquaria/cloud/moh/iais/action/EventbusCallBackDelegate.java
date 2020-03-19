package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.ApplicationClientFallback;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
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
    @Autowired
    private ApplicationClientFallback applicationClientFallback;


    public void callback(BaseProcessClass bpc) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("<=========== Eventbus Callback Start ===========>");
        HttpServletRequest request = bpc.request;
        String submissionId = ParamUtil.getString(request,"submissionId");
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
        String operation = ParamUtil.getString(request, "operation");
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
                // Do something next step
                if (EventBusConsts.SERVICE_NAME_DEMO.equals(serviceName)) {
                    handleDemoNext(submissionId, eventRefNum, operation);
                }else if (EventBusConsts.SERVICE_NAME_APPSUBMIT.equals(serviceName)) {
                    if(EventBusConsts.OPERATION_APPLICATION_UPDATE_APPEAL.equals(operation)){
                         invokeMethod(submissionId, eventRefNum,
                                 "com.ecquaria.cloud.moh.iais.service.impl.AppealApplicaionServiceImpl",
                                 "updateFEAppealApplicationDto");
                    }
                }else if (EventBusConsts.SERVICE_NAME_ROUNTINGTASK.equals(serviceName)) {

                }else if (EventBusConsts.SERVICE_NAME_LICENCESAVE.equals(serviceName)) {
                    if(EventBusConsts.OPERATION_LICENCE_SAVE.equals(operation)){
                        invokeMethod(submissionId, eventRefNum,
                                "com.ecquaria.cloud.moh.iais.service.impl.LicenceServiceImpl",
                                "createFESuperLicDto");
                    }else if(EventBusConsts.OPERATION_LICENCE_SAVE_APPEAL.equals(operation)){
                        invokeMethod(submissionId, eventRefNum,
                                "com.ecquaria.cloud.moh.iais.service.impl.AppealServiceImpl",
                                "updateFEAppealLicenceDto");
                    }
                }else if(EventBusConsts.SERVICE_NAME_APPSUBMIT.equals(serviceName)){

                    if(EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION.equals(operation)){
                        log.info("eventRefNum ****"+eventRefNum);
                        AppEicRequestTrackingDto entity = applicationClientFallback.getAppEicRequestTracking(eventRefNum).getEntity();
                        String dtoObj = entity.getDtoObj();
                        Map<String,List<ApplicationDto>> map1 = JsonUtil.parseToObject(dtoObj, Map.class);
                        log.info("send task callback *****");
                        List<ApplicationDto> applicationDtoList = map1.get("listNewApplicationDto");
                        List<ApplicationDto> list = map1.get("requestForInfList");
                        invokeMethod(applicationDtoList,list,submissionId,
                                "com.ecquaria.cloud.moh.iais.service.impl.LicenceFileDownloadServiceImpl",
                                "sendTask");

                        log.info("***send task callback end *****");
                    }

                }
            }
        }
        log.info("<=========== Eventbus Callback Finish ===========>");
    }

    private void handleDemoNext(String submissionId, String eventRefNum, String operation)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (EventBusConsts.OPERATION_DEMO_CREATE_ORG.equals(operation)) {
            invokeMethod(submissionId, eventRefNum,
                    "com.ecquaria.cloud.moh.iais.service.impl.OrgUserAccountSampleServiceImpl",
                    "saveOrgUserEvent");
        }
    }

    private void invokeMethod(String submissionId, String eventRefNum, String clsName, String methodName)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class cls = Class.forName(clsName);
        Object obj = SpringContextHelper.getContext().getBean(cls);
        Method med = cls.getMethod(methodName, new Class[]{String.class, String.class});
        med.invoke(obj, new String[] {eventRefNum, submissionId});
    }

    private void invokeMethod(List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String submissionId,
                              String clsName, String methodName) throws ClassNotFoundException,NoSuchMethodException,InvocationTargetException,
    IllegalAccessException{
        Class cls = Class.forName(clsName);

        Object obj =    SpringContextHelper.getContext().getBean(cls);
        Method med = cls.getMethod(methodName, new Class[]{List.class, List.class,String.class});
        med.invoke(obj,listApplicationDto,requestForInfList,submissionId);
    }
}
