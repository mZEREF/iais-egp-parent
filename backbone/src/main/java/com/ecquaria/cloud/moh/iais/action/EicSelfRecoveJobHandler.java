package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.AtEicClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicmEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OnlineApptEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OrgEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * EicSelfRecoveJobHandler
 *
 * @author Jinhua
 * @date 2020/7/8 17:42
 */
@JobHandler(value="eicSelfRecoveJobHandler")
@Component
@Slf4j
public class EicSelfRecoveJobHandler extends IJobHandler {
    @Autowired
    private AtEicClient atEicClient;
    @Autowired
    private AppEicClient appEicClient;
    @Autowired
    private LicEicClient licEicClient;
    @Autowired
    private LicmEicClient licmEicClient;
    @Autowired
    private EicClient eicClient;
    @Autowired
    private OrgEicClient orgEicClient;
    @Autowired
    private OnlineApptEicClient onlineApptEicClient;
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            log.info("<======== Start EIC Self Recover Job =========>");
            JobLogger.log("<======== Start EIC Self Recover Job =========>");
            String moduleName = currentApp + "-" + currentDomain;
            List<EicRequestTrackingDto> atList = atEicClient.getPendingRecords(moduleName).getEntity();
            List<EicRequestTrackingDto> appList = appEicClient.getPendingRecords(moduleName).getEntity();
            List<EicRequestTrackingDto> licList = licEicClient.getPendingRecords(moduleName).getEntity();
            List<EicRequestTrackingDto> licmList = licmEicClient.getPendingRecords(moduleName).getEntity();
            List<EicRequestTrackingDto> orgList = orgEicClient.getPendingRecords(moduleName).getEntity();
            List<EicRequestTrackingDto> sysList = eicClient.getPendingRecords(moduleName).getEntity();
            List<EicRequestTrackingDto> apptList = onlineApptEicClient.getPendingRecords(moduleName).getEntity();
            AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(currentDomain);
            if (!IaisCommonUtils.isEmpty(atList)) {
                atList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                atEicClient.updateStatus(atList);
            }
            if (!IaisCommonUtils.isEmpty(appList)) {
                appList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                appEicClient.updateStatus(appList);
            }
            if (!IaisCommonUtils.isEmpty(licList)) {
                licList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                licEicClient.updateStatus(licList);
            }
            if (!IaisCommonUtils.isEmpty(licmList)) {
                licmList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                licmEicClient.updateStatus(licmList);
            }
            if (!IaisCommonUtils.isEmpty(orgList)) {
                orgList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                orgEicClient.updateStatus(orgList);
            }
            if (!IaisCommonUtils.isEmpty(sysList)) {
                sysList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                eicClient.updateStatus(sysList);
            }
            if (!IaisCommonUtils.isEmpty(apptList)) {
                apptList.forEach(ert -> {
                    reTrigger(ert, auditTrailDto);
                });
                onlineApptEicClient.updateStatus(apptList);
            }
            log.info("<======== End EIC Self Recover Job =========>");
            JobLogger.log("<======== End EIC Self Recover Job =========>");
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void reTrigger(EicRequestTrackingDto ert, AuditTrailDto auditTrailDto) {
        Date now = new Date();
        ert.setLastActionAt(now);
        ert.setProcessNum(ert.getProcessNum() + 1);
        ert.setAuditTrailDto(auditTrailDto);
        if (ert.getFirstActionAt() == null) {
            ert.setFirstActionAt(now);
        }
        try {
            Class dtoCls = Class.forName(ert.getDtoClsName());
            Object obj = JsonUtil.parseToObject(ert.getDtoObject(), dtoCls);
            Class actCls = Class.forName(ert.getActionClsName());
            Object actObj = SpringContextHelper.getContext().getBean(actCls);
            Method method = actCls.getMethod(ert.getActionMethod(), dtoCls);
            method.invoke(actObj, obj);
            ert.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            log.error("Error for EIC tracking id " + StringUtil.changeForLog(ert.getId()), e);
            JobLogger.log(e);
        }
    }
}
