package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.AtEicClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicmEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OrgEicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

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
    private static final int RETRY_LIMIT = 5;

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
            boolean keepOn = true;
            int i = 0;
            boolean atCon = true;
            boolean appCon = true;
            boolean licCon = true;
            boolean licmCon = true;
            boolean orgCon = true;
            boolean sysCon = true;
            while (keepOn && i < 200) {
                keepOn = atCon || appCon || licCon || licmCon || orgCon || sysCon;
                List<EicRequestTrackingDto> atList = null;
                if (atCon) {
                    atList = atEicClient.getPendingRecords(moduleName).getEntity();
                    atCon = !IaisCommonUtils.isEmpty(atList);
                }
                List<EicRequestTrackingDto> appList = null;
                if (appCon) {
                    appList = appEicClient.getPendingRecords(moduleName).getEntity();
                    appCon = !IaisCommonUtils.isEmpty(appList);
                }
                List<EicRequestTrackingDto> licList = null;
                if (licCon) {
                    licList = licEicClient.getPendingRecords(moduleName).getEntity();
                    licCon = !IaisCommonUtils.isEmpty(licList);
                }
                List<EicRequestTrackingDto> licmList = null;
                if (licmCon) {
                    licmList = licmEicClient.getPendingRecords(moduleName).getEntity();
                    licmCon = !IaisCommonUtils.isEmpty(licmList);
                }
                List<EicRequestTrackingDto> orgList = null;
                if (orgCon) {
                    orgList = orgEicClient.getPendingRecords(moduleName).getEntity();
                    orgCon = !IaisCommonUtils.isEmpty(orgList);
                }
                List<EicRequestTrackingDto> sysList = null;
                if (sysCon) {
                    sysList = eicClient.getPendingRecords(moduleName).getEntity();
                    sysCon = !IaisCommonUtils.isEmpty(sysList);
                }
                AuditTrailHelper.setupBatchJobAuditTrail(this);
                AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
                AuditTrailDto.setThreadDto(auditTrailDto);
                if (!IaisCommonUtils.isEmpty(atList)) {
                    keepOn = true;
                    atList.forEach(ert -> reTrigger(ert, auditTrailDto));
                    atEicClient.updateStatus(atList);
                }

                if (!IaisCommonUtils.isEmpty(appList)) {
                    keepOn = true;
                    appList.forEach(ert -> reTrigger(ert, auditTrailDto));
                    appEicClient.updateStatus(appList);
                }

                if (!IaisCommonUtils.isEmpty(licList)) {
                    keepOn = true;
                    licList.forEach(ert -> reTrigger(ert, auditTrailDto));
                    licEicClient.updateStatus(licList);
                }

                if (licmList!=null&&!licmList.isEmpty()) {
                    keepOn = true;
                    licmList.forEach(ert -> reTrigger(ert, auditTrailDto));
                    licmEicClient.updateStatus(licmList);
                }

                if (orgList!=null&&!orgList.isEmpty()) {
                    keepOn = true;
                    orgList.forEach(ert -> reTrigger(ert, auditTrailDto));
                    orgEicClient.updateStatus(orgList);
                }

                if (sysList!=null&&!sysList.isEmpty()) {
                    keepOn = true;
                    sysList.forEach(ert -> reTrigger(ert, auditTrailDto));
                    eicClient.updateStatus(sysList);
                }
                i++;
            }
            log.info("<======== End EIC Self Recover Job =========>");
            JobLogger.log("<======== End EIC Self Recover Job =========>");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage());
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
            Class dtoCls = MiscUtil.getClassFromName(ert.getDtoClsName());
            Object obj = JsonUtil.parseToObject(ert.getDtoObject(), dtoCls);
            Class actCls = MiscUtil.getClassFromName(ert.getActionClsName());
            Object actObj = SpringContextHelper.getContext().getBean(actCls);
            Method method = actCls.getMethod(ert.getActionMethod(), dtoCls);
            method.invoke(actObj, obj);
            ert.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            if (ert.getProcessNum() > RETRY_LIMIT) {
                ert.setStatus(AppConsts.EIC_STATUS_PROCESSING_LOCKED);
            }
            log.error("Error for EIC tracking id " + StringUtil.changeForLog(ert.getId()), e);
            JobLogger.log(e);
        }
    }
}
